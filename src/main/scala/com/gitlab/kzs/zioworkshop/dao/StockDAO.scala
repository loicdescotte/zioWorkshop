package com.gitlab.kzs.zioworkshop.dao

import com.gitlab.kzs.zioworkshop.IOTransactor
import com.gitlab.kzs.zioworkshop.model.{Stock, StockDBAccessError, StockError, StockNotFound}
import doobie.implicits._
import zio.{IO, Task}
import zio.interop.catz._
import fs2.Stream

trait StockDAO {
  def currentStock(stockId: Int): IO[StockError, Stock]
  def updateStock(stockId: Int, updateValue: Int): IO[StockError, Stock]
  def allStocks: Stream[Task, Stock]
}

/**
  * The methods in this class are pure functions
  * They can describe how to interact with the database (select, insert, ...)
  * But as IO is lazy, no side effect will be executed here
  *
  * @param xa
  */
class StockDAOLive(val xa: IOTransactor) extends StockDAO{

  override def currentStock(stockId: Int): IO[StockError, Stock] = ???

  override  def updateStock(stockId: Int, updateValue: Int): IO[StockError, Stock] = ???

  override def allStocks: Stream[Task, Stock] = ??? //TIP use Doobie .stream method
}
