package com.gitlab.kzs

import doobie.util.transactor.Transactor.Aux
import zio.{Task, TaskR, ZIO}

package object zioworkshop {

  type IOTransactor = Aux[Task, Unit]

  type SIO[E, A] = ZIO[ExtServices, E, A]

  type STask[A] = TaskR[ExtServices, A]

}
