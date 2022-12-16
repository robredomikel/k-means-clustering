package assignment22

import org.scalatest.funsuite.AnyFunSuite

abstract class DIPTestSuite extends AnyFunSuite {
  private val assignmentOption: Option[Assignment] =
    try {
      Some(new Assignment())
    }
    catch {
      case _: Error => None
    }

  def getAssignment: Assignment = {
    try {
      assignmentOption.get
    }
    catch {
      case _: Throwable => throw new NotImplementedError
    }
  }
}
