// See LICENSE for license details.

package scr1.top

import chisel3._

/**
  * Memory command
  */
object MemCmd {
  val width = 2
  val rd = 0
  val wr = 1
  val error = 2
}

/**
  * Memory data width
  */
object MemWidth {
  val width = 2
  val byte = 0
  val hword = 1
  val word = 2
}

/**
  * Memory response
  */
object MemResp {
  val width = 2
  val notRdy = 0
  val rdyOk = 1
  val rdyEr = 2
}
