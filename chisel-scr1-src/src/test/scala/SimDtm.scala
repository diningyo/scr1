// See LICENSE for license details.
import chisel3._

import scr1._

class SimDtm(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {

  })

  val memSize = 32 * 1024
  val numIf = 2
  val addrWidth = 32
  val dataWidth = 32
  val idWidth = 4

  val dut = Module(new Scr1Top)
  val mem = Module(new Scr1MemoryTbAxiWrapper(memSize, numIf, addrWidth, dataWidth, idWidth))

  io := DontCare
  dut.io := DontCare
}
