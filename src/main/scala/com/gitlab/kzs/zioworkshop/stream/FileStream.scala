package com.gitlab.kzs.zioworkshop.stream

import java.nio.file.Paths

import cats.effect.Resource
import com.gitlab.kzs.zioworkshop.STask
import com.gitlab.kzs.zioworkshop.model.Stock
import fs2.{Stream, io, text}
import zio.Task
import zio.interop.catz._

import scala.concurrent.ExecutionContextExecutorService

trait FileStream {
  def stocksFromFile: Stream[STask, Stock]
}

// We could also stream content from an external webservice here, using a file to keep the exercice simple
class FileStreamLive(path: String, blockingExecutionContext: Resource[Task, ExecutionContextExecutorService]) extends FileStream {

  def stocksFromFile: Stream[STask, Stock] = ???

}

