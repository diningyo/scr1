// See LICENSE for license details.
import chisel3._

import scr1._

class SimDtm(hexBinFile: String)(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {
    val halt = Input(Bool())
    val success = Output(Bool())
  })

  val memSize = 32 * 1024
  val addrWidth = 32
  val dataWidth = 32
  val idWidth = 4

  val dut = Module(new Scr1Top)
  val mem = Module(new Scr1MemoryTbAxiWrapper(memSize, addrWidth, dataWidth, idWidth, cfg.irqNum))

  io := DontCare
  dut.io := DontCare
}
