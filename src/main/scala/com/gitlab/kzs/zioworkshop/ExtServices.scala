package com.gitlab.kzs.zioworkshop

import java.util.concurrent.Executors

import cats.effect.Resource
import com.gitlab.kzs.zioworkshop.dao.{StockDAO, StockDAOLive}
import com.gitlab.kzs.zioworkshop.stream.{FileStream, FileStreamLive}
import doobie.util.transactor.Transactor
import zio.Task
import zio.clock.Clock
import zio.interop.catz._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}


/**
  * External services
  */
trait ExtServices extends Clock {
  val stockDao: StockDAO
  val fileStream: FileStream
}

object ExtServicesLive extends ExtServices with Clock.Live  {

  val xa = Transactor.fromDriverManager[Task](
    "org.h2.Driver",
    "jdbc:h2:mem:poc;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'"
    , "sa", ""
  )

  override val stockDao: StockDAO = new StockDAOLive(xa)

  //streaming
  val blockingExecutionContext: Resource[Task, ExecutionContextExecutorService] = Resource.make(Task(ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(2))))(ec => Task(ec.shutdown()))
  override val fileStream: FileStream = new FileStreamLive("src/main/resources/specialCollection.txt", blockingExecutionContext)
}
