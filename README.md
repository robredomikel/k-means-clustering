
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
This task implements the k means clustering for two dimensional data. It uses
### Basic 3D K-means
Point class will define the attributes and methods implemented for Point objects, such as, their coordinates, height or name. Mostly, all methods implemented in this class are __getter__ functions, so that point class works to define and store __all the information__ of each point.

### K-means using labels

### Silhouette Method
## Further operations
Once all the points and routes are built in the map, the program also computes the **length of the specified route** and **the highest distance rise from a given point**.
![orienteering](https://github.com/robredomikel/orienteering/blob/main/screenshots/rise.PNG)

## Further comments
This project should be seen from a **Modularity** backed perspective. Indeed, all the implemented code has been based on this part of the project.

Rather than efficiency, the implementation of this project is more oriented to the **connection between different objects**, so there may be surely some operations that could be more efficient. 

## References
- [Data-Intensive Programming, Lectures | Tampere universities (tuni.fi)](https://www.tuni.fi/en/study-with-us/data-intensive-programming-lectures)
- [Scala Tutorial (tutorialspoint.com)](https://www.tutorialspoint.com/scala/index.htm)
- 
