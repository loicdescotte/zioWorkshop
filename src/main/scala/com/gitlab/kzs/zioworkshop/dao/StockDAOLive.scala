package com.gitlab.kzs.zioworkshop.dao

import com.gitlab.kzs.zioworkshop.IOTransactor
import com.gitlab.kzs.zioworkshop.model.{Stock, StockDBAccessError, StockNotFound}
import doobie.implicits._
import zio.Task
import zio.interop.catz._

trait StockDAO {
  def currentStock(stockId: Int): Task[Stock]
  def updateStock(stockId: Int, increment: Int): Task[Stock]
}

/**
  * The methods in this class are pure functions
  * They can describe how to interact with the database (select, insert, ...)
  * But as IO is lazy, no side effect will be executed here
  *
  * @param xa
  */
class StockDAOLive(val xa: IOTransactor) extends StockDAO{

  override def currentStock(stockId: Int): Task[Stock] = {
    val stockDatabaseResult = sql"""
      SELECT * FROM stock where id=$stockId
     """.query[Stock].option

    val dbResult = stockDatabaseResult.transact(xa)
    ???
  }

  override def updateStock(stockId: Int, increment: Int): Task[Stock] = {    
    ???
  }
}
