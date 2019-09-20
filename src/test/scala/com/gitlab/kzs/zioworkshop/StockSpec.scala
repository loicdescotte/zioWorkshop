package com.gitlab.kzs.zioworkshop

import com.gitlab.kzs.zioworkshop.dao.StockDAO
import com.gitlab.kzs.zioworkshop.model.{Stock, StockDBAccessError, StockError, StockNotFound}
import org.http4s._
import org.http4s.syntax.kleisli._
import org.specs2.mutable.Specification
import zio.clock.Clock
import zio.internal.PlatformLive
import zio.interop.catz._
import zio.{IO, Runtime}

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
    }
  }

  val testRuntime = Runtime(ExtServicesTest, PlatformLive.Default)

  "Stock HTTP Service" should {
    "return 200 and current stock" in {
      val request = Request[STask](Method.GET, uri"""/stock/1""")
      val stockResponse = testRuntime.unsafeRun(HTTPService.routes.orNotFound.run(request))
      stockResponse.status must beEqualTo(???)
      testRuntime.unsafeRun(stockResponse.as[String]) must beEqualTo(???)
    }

    /* TODO
    "return 200 and updated stock" in {

    }

    "return empty stock error" in {

    }

    "return stock not found error" in {

    }

    "return database error" in {

    }

     */
  }
}
