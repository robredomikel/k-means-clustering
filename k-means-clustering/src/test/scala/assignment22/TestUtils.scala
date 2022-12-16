package assignment22

object TestUtils {
  val DefaultTolerance: Double = 0.1  // 0.1 means that values must be within 10% of each other to pass isClose test

  // helper function for checking are two doubles close in value
  def isClose[ValType <: AnyVal](a: ValType, b: ValType, tolerance: Double): Boolean = {
    a match {
      case intA: Int => b match {
        case intB: Int => intA == intB
        case _ => false  // the types of a and b did not match
      }
      case doubleA: Double => b match {
        case doubleB: Double => if (doubleA == 0.0) {
          Math.abs(doubleB) < tolerance
        } else {
          Math.abs(doubleB - doubleA) / Math.abs(doubleA) < tolerance
        }
        case _ => false  // the types of a and b did not match
      }
      case _ => false  // other number types are not supported
    }
  }

  // helper function to sort the given array using the first element of the tuples
  def sortByFirstElement[ValType <: AnyVal, T](array: Array[(ValType, T)])(implicit ordering: Ordering[ValType]): Array[(ValType, T)] = {
    array.sortBy({
      case (a: Int, _) => a
      case (a: Double, _) => a
    })
  }

  // helper function to sort the given array using the first element of the tuples
  def sortByFirstElement[ValType <: AnyVal, T, U](array: Array[(ValType, T, U)])(implicit ordering: Ordering[ValType]): Array[(ValType, T, U)] = {
    array.sortBy({
      case (a: Int, _, _) => a
      case (a: Double, _, _) => a
    })
  }

  // helper function for checking if the given arrays contain similar elements
  def checkArray[ValType1 <: AnyVal, ValType2 <: AnyVal](
    array1: Array[(ValType1, ValType2)],
    array2: Array[(ValType1, ValType2)],
    tolerance: Double
  )(implicit ordering: Ordering[ValType1]): Boolean = {
    if (array1.length == array2.length) {
      sortByFirstElement(array1).zip(sortByFirstElement(array2)).forall({
        case ((a, b), (refA, refB)) => isClose(a, refA, tolerance) && isClose(b, refB, tolerance)
      })
    } else {
      false
    }
  }

  // helper function for checking if the given arrays contain similar elements
  def checkArray(
    array1: Array[(Double, Double, Double)],
    array2: Array[(Double, Double, Double)],
    tolerance: Double
  ): Boolean = {
    if (array1.length == array2.length) {
      sortByFirstElement(array1).zip(sortByFirstElement(array2)).forall({
        case ((a, b, c), (refA, refB, refC)) =>
          isClose(a, refA, tolerance) && isClose(b, refB, tolerance) && isClose(c, refC, tolerance)
      })
    } else {
      false
    }
  }

  // helper function for checking if the given inputArray contain similar elements when compared to one of the referenceArrays
  def checkArrays[ValType1 <: AnyVal, ValType2 <: AnyVal](
    inputArray: Array[(ValType1, ValType2)],
    referenceArray1: Array[(ValType1, ValType2)],
    referenceArray2: Array[(ValType1, ValType2)],
    tolerance: Double
  )(implicit ordering: Ordering[ValType1]): Boolean = {
    checkArray(inputArray, referenceArray1, tolerance) || checkArray(inputArray, referenceArray2, tolerance)
  }

  // helper function for checking if the given inputArray contain similar elements when compared to one of the referenceArrays
  def checkArrays(
    inputArray: Array[(Double, Double, Double)],
    referenceArray1: Array[(Double, Double, Double)],
    referenceArray2: Array[(Double, Double, Double)],
    tolerance: Double
  ): Boolean = {
    checkArray(inputArray, referenceArray1, tolerance) || checkArray(inputArray, referenceArray2, tolerance)
  }

  // Helper function for creating an error message when the given arrays do not match
  def getErrorMessage[ValType1 <: AnyVal, ValType2 <: AnyVal](inputArray: Array[(ValType1, ValType2)]): String = {
    s": ${toString(inputArray)} did not match the reference values"
  }

  // Helper function for creating an error message when the given arrays do not match
  def getErrorMessage[ValType1 <: AnyVal, ValType2 <: AnyVal, ValType3 <: AnyVal](
    inputArray: Array[(ValType1, ValType2, ValType3)]
  ): String = {
    s": ${toString(inputArray)} did not match the reference values"
  }

  def getArraySizeErrorMessage(expectedSize: Int): String = {
    s": expected an array with $expectedSize elements"
  }

  // Helper function to round numbers to the specified decimal level
  def round[ValType <: AnyVal](value: ValType, decimals: Int): Double = {
    val pow10: Double = Math.pow(10, decimals)
    val valueInDouble: Double = value match {
      case intValue: Int => intValue.toDouble
      case doubleValue: Double => doubleValue
      case _ => 0.0
    }
    Math.round(pow10 * valueInDouble) / pow10
  }

  // Helper function to transform an array of tuples to a string
  def toString[ValType1 <: AnyVal, ValType2 <: AnyVal](array: Array[(ValType1, ValType2)]): String = {
    s"(${array.map({case (a, b) => (round(a, 3), round(b, 3))}).mkString(",")})"
  }

  // Helper function to transform an array to a string
  def toString[ValType1 <: AnyVal, ValType2 <: AnyVal, ValType3 <: AnyVal](
    array: Array[(ValType1, ValType2, ValType3)]
  ): String = {
    s"(${array.map({case (a, b, c) => (round(a, 3), round(b, 3), round(c, 3))}).mkString(",")})"
  }
}
