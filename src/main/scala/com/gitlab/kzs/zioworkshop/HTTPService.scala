package com.gitlab.kzs.zioworkshop

import com.gitlab.kzs.zioworkshop.model.{EmptyStock, Stock, StockError, StockNotFound}
import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import org.slf4j.LoggerFactory._
import zio.interop.catz._
import zio.{TaskR, ZIO}

/**
  * HTTP routes definition
  */
object HTTPService extends Http4sDsl[STask] {

  val logger = getLogger(this.getClass)

  //dependency injection using ZIO environment
  val stockDao = ??? //TIP : use ZIO.access

  val routes: HttpRoutes[STask] = HttpRoutes.of[STask] {

    case GET -> Root / "stock" / IntVar(stockId) =>
      // retrieve stock in database
      val stockDbResult: ZIO[ExtServices, StockError, Stock] = ???

      stockOrErrorResponse(stockDbResult)

    case PUT -> Root / "stock" / IntVar(stockId) / IntVar(updateValue) => ???
  }

  def stockOrErrorResponse(stockResponse: ZIO[ExtServices, StockError, Stock]): TaskR[ExtServices, Response[STask]] = {
    ???
  }

}

object Server extends CatsApp {

  //Runtime will execute IO unsafe calls (i.e. all the side effects) and manage threading
  val program = ZIO.runtime[ExtServices].flatMap { implicit runtime =>
    //Start the server
    BlazeServerBuilder[STask]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(HTTPService.routes.orNotFound)
      .serve
      .compile.drain
  }

  //plug the real service
  override def run(args: List[String]) = program.provide(ExtServicesLive).fold(_ => 1, _ => 0)
}


