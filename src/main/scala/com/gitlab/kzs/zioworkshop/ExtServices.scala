package com.gitlab.kzs.zioworkshop

import com.gitlab.kzs.zioworkshop.dao.{StockDAO, StockDAOLive}
import doobie.util.transactor.Transactor
import com.gitlab.kzs.zioworkshop.dao.StockDAOLive
import zio.Task
import zio.clock.Clock
import zio.interop.catz._


/**
  * External services
  */
trait ExtServices extends Clock {
  val stockDao: StockDAO
}

object ExtServicesLive extends ExtServices with Clock.Live  {

  val xa = Transactor.fromDriverManager[Task](
    "org.h2.Driver",
    "jdbc:h2:file:./localdb;INIT=RUNSCRIPT FROM 'src/main/resources/sql/create.sql'"
    , "sa", ""
  )
  override val stockDao: StockDAO = new StockDAOLive(xa)
}
