// See LICENSE for license details.
package scr1.top

import chisel3._

import scr1.core.SCR1Config

class ImemRouter(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle{
    // Core interface
    val imemReqAck = Output(Bool())
    val imemReq = Input(Bool())
    val imemCmd = Input(UInt(2.W))
    val imemAddr = Input(UInt(cfg.imemAWidth.W))
    val imemRdata = Output(UInt(cfg.imemDWidth.W))
    val imemResp = Output(UInt(2.W))

    // PORT0 interface
    val port0ReqAck = Input(Bool())
    val port0Req = Output(Bool())
    val port0Cmd = Output(UInt(2.W))
    val port0Addr = Output(UInt(cfg.imemAWidth.W))
    val port0Rdata = Input(UInt(cfg.imemDWidth.W))
    val port0Resp = Input(UInt(2.W))

    // PORT1 interface
    val port1ReqAck = Input(Bool())
    val port1Req = Output(Bool())
    val port1Cmd = Output(UInt(2.W))
    val port1Addr = Output(UInt(cfg.imemAWidth.W))
    val port1Rdata = Input(UInt(cfg.imemDWidth.W))
    val port1Resp = Input(UInt(2.W))
  })

  io := DontCare

}
