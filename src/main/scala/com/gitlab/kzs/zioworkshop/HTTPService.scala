package com.gitlab.kzs.zioworkshop

import com.gitlab.kzs.zioworkshop.dao.{StockDAO, StockDAOLive}
import com.gitlab.kzs.zioworkshop.model.{EmptyStock, Stock, StockNotFound}
import doobie.util.transactor.Transactor
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
import zio.{IO, Task, ZIO}

/**
  * HTTP routes definition
  */
class HTTPService(dao: StockDAO) extends Http4sDsl[Task] {

  val logger = getLogger(this.getClass)

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {

    case GET -> Root / "stock" / IntVar(stockId) =>
      // retrieve stock in database
      val stockDbResult: Task[Stock] = ???

      stockOrErrorResponse(stockDbResult)

  }

  def stockOrErrorResponse(stockResponse: Task[Stock]): Task[Response[Task]] = {
    ???
  }

}

object Server extends CatsApp {

  import zio.interop.catz.implicits._

  val xa = Transactor.fromDriverManager[Task](
    "org.h2.Driver",
    "jdbc:h2:file:./localdb;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'"
    , "sa", ""
  )

  //Runtime will execute IO unsafe calls (i.e. all the side effects) and manage threading
  val program = ZIO.runtime[Environment].flatMap { implicit runtime =>
    //Start the server
    BlazeServerBuilder[Task]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(new HTTPService(new StockDAOLive(xa)).routes.orNotFound)
      .serve
      .compile.drain
  }

  //plug the real service
  override def run(args: List[String]) = program.fold(_ => 1, _ => 0)
}


