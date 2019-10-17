package com.gitlab.kzs.zioworkshop

import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import org.slf4j.LoggerFactory._
import zio._
import zio.interop.catz._
/**
  * HTTP routes definition
  */
object HTTPService extends Http4sDsl[Task] {

  val logger = getLogger(this.getClass)

  val routes: HttpRoutes[Task] = HttpRoutes.of[Task] {

    case GET -> Root / "hello" =>
      // retrieve stock in database
      Ok("hello")
  }
}

object Server extends CatsApp {

  import zio.interop.catz.implicits._

  //Runtime will execute IO unsafe callslmll (i.e. all the side effects) and manage threading
  val program = ZIO.runtime[Environment].flatMap { implicit runtime =>
    //Start the server
    BlazeServerBuilder[Task]
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(HTTPService.routes.orNotFound)
      .serve
      .compile.drain
  }

  //plug the real service
  override def run(args: List[String]) = program.fold(_ => 1, _ => 0)


}

