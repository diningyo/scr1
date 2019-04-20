// See LICENSE for license details.
package scr1.top

import chisel3._

import scr1.core.SCR1Config

class Tcm(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle{
    // Core instruction interface
    val imemReqAck = Output(Bool())
    val imemReq = Input(Bool())
    val imemCmd = Input(UInt(2.W))
    val imemAddr = Input(UInt(cfg.imemAWidth.W))
    val imemRdata = Output(UInt(cfg.imemDWidth.W))
    val imemResp = Output(UInt(2.W))

    // Core data interface
    val dmemReqAck = Output(Bool())
    val dmemReq = Input(Bool())
    val dmemCmd = Input(UInt(2.W))
    val dmemWidth = Input(UInt(2.W))
    val dmemAddr = Input(UInt(cfg.dmemAWidth.W))
    val dmemWdata = Input(UInt(cfg.dmemDWidth.W))
    val dmemRdata = Output(UInt(cfg.dmemDWidth.W))
    val dmemResp = Output(UInt(2.W))
  })

  io := DontCare
}
