import scala.collection.mutable.ListBuffer
import scala.util.Try

case class SensorData(sensor: String, min: Int, avg: Int, max: Int)

object Main {
  def replaceNaN(number: Int): Any = {
    if (number == Int.MinValue)
      "NaN"
    else
      number
  }

  def humidityCalculation(pathName: String): (ListBuffer[SensorData], Int, Int, Int) = {
    val sensorDataMap = scala.collection.mutable.Map[String, List[Int]]()
    val files = new java.io.File(pathName).listFiles.filter(_.getName.endsWith(".csv"))
    var countProcess = 0
    var countFailed = 0
    for (file <- files) {
      val bufferedSource = io.Source.fromFile(file)
      for (line <- bufferedSource.getLines) {
        val cols = line.split(",").map(_.trim)
        if (!(cols(0).equalsIgnoreCase("sensor-id") || cols(1).equalsIgnoreCase("humidity"))) {
          countProcess += 1
          val currList = sensorDataMap.getOrElse(cols(0), List())
          if (!cols(1).equalsIgnoreCase("NaN")) {
            sensorDataMap.update(cols(0), currList ++ List(cols(1).toInt))
          } else {
            countFailed += 1
            sensorDataMap.update(cols(0), currList)
          }
        }
      }
      bufferedSource.close
    }

    val finalMap = ListBuffer[SensorData]()
    sensorDataMap.foreach(f => {
      val min = Try(f._2.min).getOrElse(Int.MinValue)
      val max = Try(f._2.max).getOrElse(Int.MinValue)
      val avg = Try(f._2.sum / f._2.length).getOrElse(Int.MinValue)
      finalMap.+=(SensorData(f._1, min, avg, max))
    })
    val outputOrder = finalMap.sortWith(_.avg > _.avg)
    (outputOrder, files.length, countProcess, countFailed)
  }

  def printOutput(response: (ListBuffer[SensorData], Int, Int, Int)): Unit = {
    println(s"Num of processed files: ${response._2}")
    println(s"Num of processed measurements: ${response._3}")
    println(s"Num of failed measurements: ${response._4}\n")
    println("Sensors with highest avg humidity:\n")
    println("sensor-id,min,avg,max")
    response._1.foreach(f => {
      println(f.sensor + "," + replaceNaN(f.min) + "," + replaceNaN(f.avg) + "," + replaceNaN(f.max))
    })
  }

  def main(args: Array[String]): Unit = {
    val response = humidityCalculation(args(0))
    printOutput(response)
  }
}