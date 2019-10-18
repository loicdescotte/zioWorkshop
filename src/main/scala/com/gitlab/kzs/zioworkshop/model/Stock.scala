package com.gitlab.kzs.zioworkshop.model

case class Stock(id:Int, value: Int)

sealed abstract class StockError(cause: Throwable) extends Exception(cause)
case object EmptyStock extends StockError(new Exception)
case object StockNotFound extends StockError(new Exception)
case class StockDBAccessError(cause: Throwable) extends StockError(cause)

/**
  * Stock business logic
  */
object Stock {
  def validate(stock: Stock): Either[StockError, Stock] = if (stock.value > 0) Right(stock) else Left(EmptyStock)
}
