package assignment22

import assignment22.TestUtils.DefaultTolerance
import assignment22.TestUtils.checkArray
import assignment22.TestUtils.checkArrays
import assignment22.TestUtils.getErrorMessage

class ResultTestSuite extends DIPTestSuite {

  test("Result test for task 1") {
    val K = 5
    // reference values when K-means seed is 1 and the centers are in the original scale
    val ReferenceCenters = Array(
      (-0.312, 5.874),
      (0.916, 1.320),
      (-0.119, 1.601),
      (0.875, 4.599),
      (0.228, 7.030)
    )
    // reference values when K-means seed is 1 and the coordinates are in [0, 1] scale
    val ReferenceCentersUnscaled = Array(
      (0.328, 0.596),
      (0.925, 0.129),
      (0.421, 0.158),
      (0.906, 0.465),
      (0.590, 0.715)
    )

    try {
      val centers = getAssignment.task1(getAssignment.dataD2, K)
      val testResult = checkArrays(
        inputArray = centers,
        referenceArray1 = ReferenceCenters,
        referenceArray2 = ReferenceCentersUnscaled,
        tolerance = DefaultTolerance
      )
      assert(testResult === true, getErrorMessage(centers))
    }
    catch {
      case error: Error => fail(error.getMessage)
    }
  }

  test("Result test for task 2") {
    val K = 5
    // reference values when K-means seed is 1 and the centers are in the original scale
    val ReferenceCenters = Array(
      (0.105, 2.126, 1921.846),
      (0.146, 8.993, 3600.213),
      (-0.983, 1.996, 835.407),
      (0.740, 3.523, 3327.593),
      (0.771, 7.916, -775.895)
    )
    // reference values when K-means seed is 1 and the coordinates are in [0, 1] scale
    val ReferenceCentersUnscaled = Array(
      (0.584, 0.210, 0.518),
      (0.603, 0.902, 0.791),
      (0.066, 0.197, 0.342),
      (0.885, 0.350, 0.746),
      (0.900 ,0.793, 0.081)
    )

    try {
      val centers = getAssignment.task2(getAssignment.dataD3, K)
      val testResult = checkArrays(
        inputArray = centers,
        referenceArray1 = ReferenceCenters,
        referenceArray2 = ReferenceCentersUnscaled,
        tolerance = DefaultTolerance
      )
      assert(testResult === true, getErrorMessage(centers))
    }
    catch {
      case error: Error => fail(error.getMessage)
    }
  }

  test("Result test for task 3") {
    val K = 5
    // reference values when K-means seed is 1 and the centers are in the original scale
    val ReferenceCenters = Array(
      (-0.292, 5.890),
      (0.872, 4.598)
    )
    // reference values when K-means seed is 1 and the coordinates are in [0, 1] scale
    val ReferenceCentersUnscaled = Array(
      (0.337, 0.595),
      (0.904, 0.465)
    )

    try {
      val centers = getAssignment.task3(getAssignment.dataD2WithLabels, K)
      val testResult = checkArrays(
        inputArray = centers,
        referenceArray1 = ReferenceCenters,
        referenceArray2 = ReferenceCentersUnscaled,
        tolerance = DefaultTolerance
      )
      assert(testResult === true, getErrorMessage(centers))
    }
    catch {
      case error: Error => fail(error.getMessage)
    }
  }

  test("Result test for task 4") {
    val lowK: Int = 2
    val highK: Int = 13
    // reference values when K-means seed is 1
    val ReferenceMeasures = Array(
      (2, 0.637),
      (3, 0.827),
      (4, 0.882),
      (5, 0.961),
      (6, 0.835),
      (7, 0.728),
      (8, 0.739),
      (9, 0.623),
      (10, 0.736),
      (11, 0.505),
      (12, 0.641),
      (13, 0.524)
    )

    try {
      val measures = getAssignment.task4(getAssignment.dataD2, lowK, highK)

      // since random factors like the K-means seed can have a huge impact on the resulting centers
      // 25% closeness test is used instead of the default 10% test
      val testResult = checkArray(
        array1 = measures,
        array2 = ReferenceMeasures,
        tolerance = 2.5 * DefaultTolerance
      )
      assert(testResult === true, getErrorMessage(measures))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  /*
  test("Result test for task 5") {
    val K = 5
    // reference values when K-means seed is 1 and the centers are in the original scale
    val ReferenceCenters = Array(
      (-0.312, 5.874),
      (0.916, 1.320),
      (-0.119, 1.601),
      (0.875, 4.599),
      (0.228, 7.030)
    )
    // reference values when K-means seed is 1 and the coordinates are in [0, 1] scale
    val ReferenceCentersUnscaled = Array(
      (0.328, 0.596),
      (0.925, 0.129),
      (0.421, 0.158),
      (0.906, 0.465),
      (0.590, 0.715)
    )

    try {
      val centers = getAssignment.task1(getAssignment.correctD2, K)
      val testResult = checkArrays(
        inputArray = centers,
        referenceArray1 = ReferenceCenters,
        referenceArray2 = ReferenceCentersUnscaled,
        tolerance = DefaultTolerance
      )
      assert(testResult === true, getErrorMessage(centers))
    }
    catch {
      case error: Error => fail(error.getMessage)
    }
  }

  test("Result test for task 6") {
    val K = 5
    // reference values when K-means seed is 1 and the centers are in the original scale
    val ReferenceCenters = Array(
      (-0.292, 5.890),
      (0.872, 4.598)
    )
    // reference values when K-means seed is 1 and the coordinates are in [0, 1] scale
    val ReferenceCentersUnscaled = Array(
      (0.337, 0.595),
      (0.904, 0.465)
    )

    try {
      val centers = getAssignment.task3(getAssignment.correctDirtyD3, K)
      val testResult = checkArrays(
        inputArray = centers,
        referenceArray1 = ReferenceCenters,
        referenceArray2 = ReferenceCentersUnscaled,
        tolerance = DefaultTolerance
      )
      assert(testResult === true, getErrorMessage(centers))
    }
    catch {
      case error: Error => fail(error.getMessage)
    }
  }

   */

}


