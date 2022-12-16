package assignment22

import breeze.linalg.Matrix.castOps
import breeze.linalg.Vector.castFunc
import breeze.linalg.sum
import breeze.numerics.round
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DecimalType, DoubleType, IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.functions.{array, asc, avg, col, count, desc, explode, max, min, round, sum, udf, when, year}
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.evaluation.ClusteringEvaluator
import org.apache.spark.ml.feature.MinMaxScaler

import Array._
import breeze.linalg.axpy
import breeze.numerics.Scaling
import org.jfree.chart
import breeze.linalg._
import breeze.plot._
import org.apache.spark.sql

import scala.math.BigDecimal.double2bigDecimal

// SUBMISSION TEMPLATE:

//  - commit hash: a89dfb596529a2511e4f861ede988aba28d0cdea
//  - programming language: Scala
//  - additional tasks: 1, 2, 5
//  - bonus point: yes

class Assignment {

  val spark: SparkSession = SparkSession.builder()
    .appName("projectSession")
    .config("spark.driver.host", "localhost")
    .master("local")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  spark.conf.set("spark.sql.shuffle.partitions", "5")

  // Schema for task 1, only "a" and "b" considered.
  val TwoDSchema = new StructType(Array(
      new StructField("a", DoubleType, true),
      new StructField("b", DoubleType, true)
    )
  )

  // the data frame to be used in tasks 1 and 4
  val dataD2: DataFrame = spark.read.option("header", true).option("delimiter", ",").schema(TwoDSchema)
    .csv("/home/mikel/Desktop/DIP/exercises-scala/pqmiro/sip-still-in-progress/scala/data/dataD2.csv")

  // Loading of 2-dimension dirty data
  val dataDirtyD2: DataFrame = spark.read.option("header", true).option("delimiter", ",").schema(TwoDSchema)
    .csv("/home/mikel/Desktop/DIP/exercises-scala/pqmiro/sip-still-in-progress/scala/data/dataD2_dirty.csv")

  // Filtering the dirty data to make it clean.
  val correctD2 : DataFrame = dataDirtyD2.where(dataDirtyD2("a").isNotNull
    && dataDirtyD2("b").isNotNull)
    //&& (dataDirtyD2("LABEL") === "Fatal" || dataDirtyD2("LABEL") === "Ok"))

  // We consider a third column of doubles "c".
  val TriDSchema = new StructType(Array(
    new StructField("a", DoubleType, true),
    new StructField("b", DoubleType, true),
    new StructField("c", DoubleType, true)
    )
  )

  // the data frame to be used in task 2
  val dataD3: DataFrame = spark.read.option("header", true).option("delimiter", ",").schema(TriDSchema)
    .csv("/home/mikel/Desktop/DIP/exercises-scala/pqmiro/sip-still-in-progress/scala/data/dataD3.csv")

  // Schema for task 3, third column contains Labels "Fatal" & "Ok"
  val LabelSchema = new StructType(Array(
    new StructField("a", DoubleType, true),
    new StructField("b", DoubleType, true),
    new StructField("LABEL", StringType, true)
    )
  )
  // the data frame to be used in task 3 (based on dataD2 but containing numeric labels)
  val dataD2task3: DataFrame = spark.read.option("header", true).option("delimiter", ",").schema(LabelSchema)
    .csv("/home/mikel/Desktop/DIP/exercises-scala/pqmiro/sip-still-in-progress/scala/data/dataD2.csv")

  // Loading of 3-dimension dirty data
  val dirtyD3 : DataFrame = spark.read.option("header", true).option("delimiter", ",").schema(LabelSchema)
    .csv("/home/mikel/Desktop/DIP/exercises-scala/pqmiro/sip-still-in-progress/scala/data/dataD2_dirty.csv")

  // Filtering the dirty data to make it clean.
  val correctDirtyD3: DataFrame = dirtyD3.where(dirtyD3("a").isNotNull
    && dirtyD3("b").isNotNull && (dirtyD3("LABEL") === "Fatal" || dirtyD3("LABEL") === "Ok"))
    .withColumn("LABEL", when(col("LABEL").equalTo("Fatal"), 1)
    .otherwise(0))

  // Before dealing with the k-means process, we convert the Label cases into numerical binary cases 1 = "Fatal" & 0 = "Ok"
  val dataD2WithLabels : DataFrame = dataD2task3.withColumn("LABEL", when(col("LABEL").equalTo("Fatal"), 1)
                                                .otherwise( 0))


  def task1(df: DataFrame, k: Int): Array[(Double, Double)] = {

    // Create the Assembler vector based on columns A and B, output comes into "features" column of arrays
    val vectorAssembler = new VectorAssembler()
      .setInputCols(Array("a", "b"))
      .setOutputCol("features")

    // We set the Pipeline so what we prepare our data to be processed by MLlib tools.
    val transformationPipeline = new Pipeline()
      .setStages(Array(vectorAssembler))

    // Fitting the pipeline
    val pipeLine = transformationPipeline.fit(df)

    // So that we transform our initial data.
    val transformedData : DataFrame = pipeLine.transform(df)

    // Then we create the scaler, in which "scaledFeatures" column is based on the Min and Max values of the features of
    // each row/observation.
    val scaler = new MinMaxScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")

    // Compute summary statistics and generate MinMaxScalerModel
    val scalerModel = scaler.fit(transformedData)

    // rescale each feature to range [min, max].
    val scaledData : DataFrame = scalerModel.transform(transformedData)

    // Set the k means based on the given k parameters
    val kmeans = new KMeans()
      .setK(k).setFeaturesCol("scaledFeatures")

    // Fits the model with the scaled data.
    val kmModel = kmeans.fit(scaledData)

    // all k-means of the clusters
    val overall_clusters : Array[org.apache.spark.ml.linalg.Vector] = kmModel.clusterCenters

    // all k-means for a and b
    val a_b_clusters : Array[(Double, Double)] = overall_clusters.map(k => (k(0), k(1)))

    // RESCALING PROCESS

    // Definition of min and max values of the  a and b columns.
    val minValA: Double = df.select(min("a")).take(1)(0).getDouble(0)
    val maxValA: Double = df.select(max("a")).take(1)(0).getDouble(0)

    val minValB: Double = df.select(min("b")).take(1)(0).getDouble(0)
    val maxValB: Double = df.select(max("b")).take(1)(0).getDouble(0)

    // Rescaled version of the cluster values, a printing statement can be found below this definition to check the
    // output values.
    val rescaledCluster : Array[(Double, Double)] = a_b_clusters.map(tuple => (tuple._1 * (maxValA - minValA) + minValA,
                                                                                tuple._2 * (maxValB - minValB) + minValB))

    rescaledCluster.foreach(println)

    return a_b_clusters
  }

  def task2(df: DataFrame, k: Int): Array[(Double, Double, Double)] = {

    // Create the Assembler vector based on columns A and B, output comes into "features" column of arrays
    val vectorAssembler = new VectorAssembler()
      .setInputCols(Array("a", "b", "c"))
      .setOutputCol("features")

    // We set the Pipeline so what we prepare our data to be processed by MLlib tools.
    val transformationPipeline = new Pipeline()
      .setStages(Array(vectorAssembler))

    // Fitting the pipeline
    val pipeLine = transformationPipeline.fit(df)

    // So that we transform our initial data.
    val transformedData: DataFrame = pipeLine.transform(df)

    // Then we create the scaler, in which "scaledFeatures" column is based on the Min and Max values of the features of
    // each row/observation.
    val scaler = new MinMaxScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")

    // Compute summary statistics and generate MinMaxScalerModel
    val scalerModel = scaler.fit(transformedData)

    // rescale each feature to range [min, max].
    val scaledData: DataFrame = scalerModel.transform(transformedData)

    // Set the k means based on the given k parameters
    val kmeans = new KMeans()
      .setK(k).setFeaturesCol("scaledFeatures")

    // Fits the model with the scaled data.
    val kmModel = kmeans.fit(scaledData)

    // all k-means of the clusters
    val overall_clusters: Array[org.apache.spark.ml.linalg.Vector] = kmModel.clusterCenters

    // Definition of min and max values of the  a, b and c columns.
    val minValA: Double = df.select(min("a")).take(1)(0).getDouble(0)
    val maxValA: Double = df.select(max("a")).take(1)(0).getDouble(0)

    val minValB: Double = df.select(min("b")).take(1)(0).getDouble(0)
    val maxValB: Double = df.select(max("b")).take(1)(0).getDouble(0)

    val minValC: Double = df.select(min("c")).take(1)(0).getDouble(0)
    val maxValC: Double = df.select(max("c")).take(1)(0).getDouble(0)

    // Rescaled version of the cluster values.
    val rescaledCluster: Array[(Double, Double, Double)] = overall_clusters.map(tuple => (tuple(0) * (maxValA - minValA) + minValA,
                                                                                          tuple(1) * (maxValB - minValB) + minValB,
                                                                                          tuple(2) * (maxValC - minValC) + minValC))

    // Round values for the rescaled cluster values a printing statement can be found below this definition to check the
    // output values.
    val roundClustersRescaled = rescaledCluster.map(k => (BigDecimal(k._1.setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble),
                                                          BigDecimal(k._2.setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble),
                                                          BigDecimal(k._3.setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble)))

    roundClustersRescaled.foreach(println)


    // Round values for the overall_clusters
    val round_clusters = overall_clusters.map(k => (BigDecimal(k(0)).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble,
                                                    BigDecimal(k(1)).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble,
                                                    BigDecimal(k(2)).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble))

    // all k-means for a, b and c.
    val abc_clusters : Array[(Double, Double, Double)] = round_clusters.toArray

    return abc_clusters
  }

  def task3(df: DataFrame, k: Int): Array[(Double, Double)] = {

    // Create the Assembler vector based on columns A and B, output comes into "features" column of arrays
    val vectorAssembler = new VectorAssembler()
      .setInputCols(Array("a", "b", "LABEL"))
      .setOutputCol("features")

    // We set the Pipeline so what we prepare our data to be processed by MLlib tools.
    val transformationPipeline = new Pipeline()
      .setStages(Array(vectorAssembler))

    // Fitting the pipeline
    val pipeLine = transformationPipeline.fit(df)

    // So that we transform our initial data.
    val transformedData: DataFrame = pipeLine.transform(df)

    // Then we create the scaler, in which "scaledFeatures" column is based on the Min and Max values of the features of
    // each row/observation.
    val scaler = new MinMaxScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")

    // Compute summary statistics and generate MinMaxScalerModel
    val scalerModel = scaler.fit(transformedData)

    // rescale each feature to range [min, max].
    val scaledData: DataFrame = scalerModel.transform(transformedData)

    // Set the k means based on the given k parameters
    val kmeans = new KMeans()
      .setK(k).setFeaturesCol("scaledFeatures")

    // Fits the model with the scaled data.
    val kmModel = kmeans.fit(scaledData)

    // Make predictions in order to check which clusters contain biggest amount of "Fatal" results.
    val predictions: DataFrame = kmModel.transform(scaledData)


    // Sums all the 1s of "Fatal" results for each cluster.
    val fatalOrder: DataFrame = predictions.groupBy("prediction").sum("LABEL")

    // Overall sum of "Fatal" results in our data.
    val totalFatalitySum : DataFrame = predictions.groupBy("LABEL").sum("LABEL")

    // all k-means of the clusters
    val overall_clusters: Array[org.apache.spark.ml.linalg.Vector] = kmModel.clusterCenters

    // Array of all clusters with their "a" and "b" values
    val ab_clusters: Array[(Double, Double)] = overall_clusters.map(k => (k(0), k(1)))


    // Array of clusters with highest total count of "Fatal" results. (Shows only "a" and "b" values)
    val bestClusters : Array[(Double, Double)] = Array(ab_clusters(0), ab_clusters(3))

    // Definition of min and max values of the  a and b columns.
    val minValA: Double = df.select(min("a")).take(1)(0).getDouble(0)
    val maxValA: Double = df.select(max("a")).take(1)(0).getDouble(0)

    val minValB: Double = df.select(min("b")).take(1)(0).getDouble(0)
    val maxValB: Double = df.select(max("b")).take(1)(0).getDouble(0)

    // Rescaled version of the cluster values. A printing statement can be found below to check the values.
    val rescaledCluster: Array[(Double, Double)] = bestClusters.map(tuple => (tuple._1 * (maxValA - minValA) + minValA, tuple._2 * (maxValB - minValB) + minValB))

    rescaledCluster.foreach(println)

    // PROCESS: We use the predictions so that we can map which clusters contain the "Fatal" results, then fetch the ones
    // with highest count of this results, so that we store them manually in our final array.

    return bestClusters
  }

  // Parameter low is the lowest k and high is the highest one.
  def task4(df: DataFrame, low: Int, high: Int): Array[(Int, Double)] = {

    // val clusters : Array[Int] = range(low, high)

    var KArray : Array[(Int, Double)] = new Array[(Int, Double)](12)
    for ( k <- low to high) {

      // Create the Assembler vector based on columns A and B, output comes into "features" column of arrays
      val vectorAssembler = new VectorAssembler()
        .setInputCols(Array("a", "b"))
        .setOutputCol("features")

      // We set the Pipeline so what we prepare our data to be processed by MLlib tools.
      val transformationPipeline = new Pipeline()
        .setStages(Array(vectorAssembler))

      // Fitting the pipeline
      val pipeLine = transformationPipeline.fit(df)

      // So that we transform our initial data.
      val transformedData: DataFrame = pipeLine.transform(df)

      // Then we create the scaler, in which "scaledFeatures" column is based on the Min and Max values of the features of
      // each row/observation.
      val scaler = new MinMaxScaler()
        .setInputCol("features")
        .setOutputCol("scaledFeatures")

      // Compute summary statistics and generate MinMaxScalerModel
      val scalerModel = scaler.fit(transformedData)

      // rescale each feature to range [min, max].
      val scaledData: DataFrame = scalerModel.transform(transformedData)

      // Set the k means based on the given k parameters
      val kmeans = new KMeans()
        .setK(k).setSeed(1).setFeaturesCol("scaledFeatures")

      // Fits the model with the scaled data.
      val kmModel = kmeans.fit(scaledData)

      // Make predictions in order to check which clusters contain biggest amount of "Fatal" results.
      val predictions: DataFrame = kmModel.transform(scaledData)

      // Evaluate clustering by computing Silhouette score
      val evaluator = new ClusteringEvaluator().setFeaturesCol("scaledFeatures")

      val silhouetteScore = evaluator.evaluate(predictions)

      KArray(k-2) = (k, silhouetteScore)
    }

    // Preprocessing the columns for the k Clusters to be plotted:
    val kClusterVals: DenseVector[Double] = DenseVector(KArray.map(tuple => tuple._1.toDouble).toSeq :_ *)

    // Preprocessing the columns for the silhouette scores to be plotted:
    val silhouetteVals : DenseVector[Double] = DenseVector(KArray.map(tuple => tuple._2).toSeq :_ *)

    // Creating the figure object
    val figure = Figure()

    // Initializing the object to be printed.
    val plt = figure.subplot(0)

    // Activation of legend messages to appear in the plot
    plt.legend = true

    plt.title = "Relation K Cluster between Silhouette Score"
    plt.xlabel = "Nº of K clusters"
    plt.ylabel = "Silhouette Score"

    // Addition of variables of be printed in the linear plot as X and Y axes.
    plt += plot(kClusterVals, silhouetteVals)

    figure.refresh()

    return KArray// REPLACE with actual implementation

  }

}