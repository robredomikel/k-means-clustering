

# k-means-clustering
This project is based on the final programming project required in the course Data Intensive Programming at Tampere University. For that, Scala was used as the **functional programming language** to work with the Apache Spark Session used.
  
- **Note:** In order to check the code the following path must be followed within the content uploaded in GitHub; [path](https://github.com/robredomikel/k-means-clustering/blob/main/k-means-clustering/src/main/scala/assignment22/Assignment.scala).
- **Used datasets:**
	- [2D data](https://github.com/robredomikel/k-means-clustering/blob/main/k-means-clustering/data/dataD2.csv)
	- [2D dirty data](https://github.com/robredomikel/k-means-clustering/blob/main/k-means-clustering/data/dataD2_dirty.csv)
	- [3D data](https://github.com/robredomikel/k-means-clustering/blob/main/k-means-clustering/data/dataD3.csv)

## Table of contents
- [Description](https://github.com/robredomikel/k-means-clustering#description)
- [How to run](https://github.com/robredomikel/k-means-clustering#how-to-run)
- [Main tasks](https://github.com/robredomikel/k-means-clustering#main-features)
	1. [Basic 2D K-means](https://github.com/robredomikel/k-means-clustering#main-features)
	2. [Basic 3D K-means](https://github.com/robredomikel/k-means-clustering#main-features)
	3. [K-means using labels](https://github.com/robredomikel/k-means-clustering#main-features)
	4. [Silhouette Method](https://github.com/robredomikel/k-means-clustering#main-features)
- [Further details](https://github.com/robredomikel/k-means-clustering#further-details) 
- [Further comments](https://github.com/robredomikel/k-means-clustering#further-comments)
- [References](https://github.com/robredomikel/k-means-clustering#references)

## Description
In this project I implemented the necessary functions based on different scenarios to apply the _k-means-clustering_ **Machine Learning** pipeline which will be further discussed below.

The goal of this project was to understand the importance of **cleaning the data**, and **adapting it** to fit it in the pipeline that the **MLlib** library in Spark uses. 
## How to run
The user would need to download the project directory and set the IntelliJ IDEA in order to use Scala with Apache spark. Once the IDE is set to run a scala project,  **open** the main initial directory appearing in this project and wait until the Spark Session analyzes all the code. Then it would be just running the program to check the results.
 
## Main tasks 
Explanation of the main tasks implemented. **It is also worth to note** that the Spark Session is well initialized so that it computes the implemented code in Scala.
```scala
val spark: SparkSession = SparkSession.builder()
								      .appName("projectSession")
                                      .config("spark.driver.host", "localhost")
                                      .master("local")
                                      .getOrCreate()
spark.sparkContext.setLogLevel("ERROR")
spark.conf.set("spark.sql.shuffle.partitions", "5")
```
### Basic 2D K-means
This task implements the k means clustering for two dimensional data. It uses the 2D dataset and computes the two columns converted in dataframes to use the MLlib.

### Basic 3D K-means
Same topic as in the previous task but with a third extra column of data in the 3D dataset.

### K-means using labels
Given the 2D dataset, this time the LABEL column is used to divide the observations between "Ok" and "Fatal". Then the clustering should be done in three dimensions. 

### Silhouette Method
The Silhouette method is used to find the optimal number of clusters in the data. It is implemented for the previous cases so that in both cases the optimal number of clusters is calculated.

## Further operations
- The program is able to run the 2D dirty dataset; it cleans all the imperfections of the data in the dataset so that it processes only the observations that match the required format in the ML pipeline.
- It uses a standarized ML pipeline in all the tasks.
-  The cluster centers shown in the results of all the tasks are scaled back to the original scale of the dara after this being processed in the ML pipeline.

## Further comments
The importance of this project is based on the different usage of **Dataframes**, **RDDs**, **Scala vectors** and how to build a standarized **ML pipeline**. Also, how to make use of data managed in clusters with Apache Spark is a hidden but important point of this project.

## References
- [Data-Intensive Programming, Lectures | Tampere universities (tuni.fi)](https://www.tuni.fi/en/study-with-us/data-intensive-programming-lectures)
- [Scala Tutorial (tutorialspoint.com)](https://www.tutorialspoint.com/scala/index.htm)
