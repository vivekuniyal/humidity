import Main.{humidityCalculation, printOutput, replaceNaN}
import org.scalatest.FunSuite

class TestClass extends FunSuite{

  test("3 sensors should be present in the output") {
    val response = humidityCalculation("files")
    println(response._1)
    assert(response._1.size === 3) // for 3 sensors data present in files
  }

  test("processed files count should be 2") {
    val response = humidityCalculation("files")
    println(response._1)
    assert(response._2 === 2) // for 2 files
  }

  test("valid humidity values should be 7") {
    val response = humidityCalculation("files")
    println(response)
    assert(response._3 === 7) // for 7 valid values in files
  }

  test("NaN encountered in the files should be 2") {
    val response = humidityCalculation("files")
    assert(response._4 === 2) // for 2 NaN values in files
  }

  test("Output Data should be printed") {
    val response = humidityCalculation("files")
    printOutput(response)
  }

  test("Output number should be 97") {
    val ouputNumer = replaceNaN(97)
    assert(ouputNumer === 97)
  }

  test("Output number should be NaN") {
    val ouputNumer = replaceNaN(Int.MinValue)
    assert(ouputNumer === "NaN")
  }
}
