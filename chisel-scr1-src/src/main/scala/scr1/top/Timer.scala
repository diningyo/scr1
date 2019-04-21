// See LICENSE for license details.
package scr1.top

import chisel3._
import chisel3.core.{Mux, withClockAndReset}
import chisel3.util.{Cat, MuxCase}
import scr1.core._

class Timer(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {
    // Common
    val rtcClk = Input(Clock())

    // Memory interface
    val dmemReq = Input(Bool())
    val dmemCmd = Input(UInt(2.W))
    val dmemWidth = Input(UInt(2.W))
    val dmemAddr = Input(UInt(cfg.dmemAWidth.W))
    val dmemWdata = Input(UInt(cfg.dmemDWidth.W))
    val dmemReqAck = Output(Bool())
    val dmemRdata = Output(UInt(cfg.dmemDWidth.W))
    val dmemResp = Output(UInt(2.W))

    // Timer interface
    val timerVal = Output(UInt(64.W))
    val timerIrq = Output(Bool())
  })

  io := DontCare
}