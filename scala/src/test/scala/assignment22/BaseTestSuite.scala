package assignment22

import assignment22.TestUtils.getArraySizeErrorMessage

class BaseTestSuite extends DIPTestSuite {

  test("Simple test for task 1") {
    val k: Int = 5

    try {
      val centers = getAssignment.task1(getAssignment.dataD2, k)
      assert(centers.length === k, getArraySizeErrorMessage(k))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  test("Simple test for task 2") {
    val k: Int = 5

    try {
      val centers = getAssignment.task2(getAssignment.dataD3, k)
      assert(centers.length === k, getArraySizeErrorMessage(k))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  test("Simple test for task 3") {
    val k: Int = 5

    try {
      val centers = getAssignment.task3(getAssignment.dataD2WithLabels, k)
      assert(centers.length === 2, getArraySizeErrorMessage(2))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  test("Simple test for task 4") {
    val lowK: Int = 2
    val highK: Int = 13
    val arrayLength: Int = highK - lowK + 1

    try {
      val measures = getAssignment.task4(getAssignment.dataD2, lowK, highK)
      assert(measures.length === arrayLength, getArraySizeErrorMessage(arrayLength))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  test("Simple test for task 5") {
    val k: Int = 5

    try {
      val centers = getAssignment.task1(getAssignment.correctD2, k)
      assert(centers.length === k, getArraySizeErrorMessage(k))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  test("Simple test for task 6") {
    val k: Int = 5

    try {
      val centers = getAssignment.task3(getAssignment.correctDirtyD3, k)
      assert(centers.length === 2, getArraySizeErrorMessage(2))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }

  test("Simple test for task 7") {
    val lowK: Int = 2
    val highK: Int = 13
    val arrayLength: Int = highK - lowK + 1

    try {
      val measures = getAssignment.task4(getAssignment.correctD2, lowK, highK)
      assert(measures.length === arrayLength, getArraySizeErrorMessage(arrayLength))
    }
    catch {
      case error: Throwable => fail(error.getMessage)
    }
  }
}
