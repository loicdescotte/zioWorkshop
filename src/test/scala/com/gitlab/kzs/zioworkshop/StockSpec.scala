package com.gitlab.kzs.zioworkshop

import com.gitlab.kzs.zioworkshop.dao.StockDAO
import com.gitlab.kzs.zioworkshop.model.{Stock, StockDBAccessError, StockNotFound}
import org.http4s._
import org.http4s.syntax.kleisli._
import org.specs2.mutable.Specification
import zio.interop.catz._
import zio.{DefaultRuntime, IO, Task}

class StockSpec extends Specification {

  val fakeStockDAO: StockDAO = new StockDAO {

    //you could also use a mocking framework here
    override def currentStock(stockId: Int): Task[Stock] = {
      stockId match {
        case 1 => IO.succeed(Stock(1, 10))
        case 2 => IO.succeed(Stock(2, 15))
        case 3 => IO.succeed(Stock(3, 0))
        case 99 => IO.fromEither(Left(StockDBAccessError(new Exception("BOOM!"))))
        case _ => IO.fromEither(Left(StockNotFound))
      }
    }

    override def updateStock(stockId: Int, updateValue: Int): Task[Stock] = ???
  }
  

  val testRuntime = new DefaultRuntime {}

  "Stock HTTP Service" should {
    "return 200 and current stock" in {
      val request = Request[Task](Method.GET, uri"""/stock/1""")
      val stockResponse = testRuntime.unsafeRun(new HTTPService(fakeStockDAO).routes.orNotFound.run(request))
      stockResponse.status must beEqualTo(Status.Ok)
      testRuntime.unsafeRun(stockResponse.as[String]) must beEqualTo(???)
    }

    "return 200 and updated stock" in {
      ???
    }
  
    "return empty stock error" in {
      ???
    }

    "return stock not found error" in {
      ???
    }

    "return internal server error" in {
      val request = ???
      val stockResponse = ???
      stockResponse.status must beEqualTo(Status.InternalServerError)
      testRuntime.unsafeRun(stockResponse.as[String]) must contain(???)
    }
  }
}
