package com.gitlab.kzs

import doobie.util.transactor.Transactor.Aux
import zio.Task

package object zioworkshop {

  type IOTransactor = Aux[Task, Unit]

}
