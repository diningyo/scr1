// See LICENSE for license details.

package scr1.top

import chisel3._

/**
  * Memory command
  */
object MemCmd {
  val width = 1
  val rd = 0.U(width.W)
  val wr = 1.U(width.W)
}

/**
  * Memory data width
  */
object MemWidth {
  val width = 2
  val byte = 0.U(width.W)
  val hword = 1.U(width.W)
  val word = 2.U(width.W)
}

/**
  * Memory response
  */
object MemResp {
  val width = 2
  val notRdy = 0.U(width.W)
  val rdyOk = 1.U(width.W)
  val rdyEr = 2.U(width.W)
}
