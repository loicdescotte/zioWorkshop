package com.gitlab.kzs.zioworkshop

import com.gitlab.kzs.zioworkshop.dao.StockDAO
import com.gitlab.kzs.zioworkshop.model.{Stock, StockDBAccessError, StockError, StockNotFound}
import com.gitlab.kzs.zioworkshop.stream.FileStream
import org.http4s._
import org.http4s.syntax.kleisli._
import org.specs2.mutable.Specification
import zio.clock.Clock
import zio.internal.PlatformLive
import zio.interop.catz._
import zio.{IO, Runtime, Task}
import fs2.Stream

class StockSpec extends Specification {

  object ExtServicesTest extends ExtServices with Clock.Live {
    override val stockDao: StockDAO = new StockDAO {

      //you could also use a mocking framework here
      override def currentStock(stockId: Int): IO[StockError, Stock] = {
        stockId match {
          case 1 => IO.succeed(Stock(1, 10))
          case 2 => IO.succeed(Stock(2, 15))
          case 3 => IO.succeed(Stock(3, 0))
          case 99 => IO.fromEither(Left(StockDBAccessError(new Exception("BOOM!"))))
          case _ => IO.fromEither(Left(StockNotFound))
        }
      }

      override def updateStock(stockId: Int, updateValue: Int): IO[StockError, Stock] = {
        currentStock(stockId).map(stock => stock.copy(value = stock.value + updateValue))
      }

      override def allStocks: Stream[Task, Stock] = Stream(
        Stock(1, 10),
        Stock(2, 15),
        Stock(3, 0)
      )
    }
    override val fileStream: FileStream = new FileStream {
      override def stocksFromFile: Stream[STask, Stock] = Stream(
        Stock(4, 20),
        Stock(5, 25)
      )
    }
  }

  val testRuntime = Runtime(ExtServicesTest, PlatformLive.Default)

  "Stock HTTP Service" should {

    "return 200 and all stocks" in {
      val request = Request[STask](Method.GET, uri"""/stocks""")
      val stockResponse = testRuntime.unsafeRun(HTTPService.routes.orNotFound.run(request))
      stockResponse.status must beEqualTo(Status.Ok)
      val result = testRuntime.unsafeRun(stockResponse.as[String])
      result must contain("""data: {"id":1,"value":10}""")
      result.split("""\n\n""").length must beEqualTo(5)
    }

  }
}
