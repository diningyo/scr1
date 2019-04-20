// See LICENSE for license details.
package scr1.top

import chisel3._

import scr1.core.SCR1Config

class DmemRouter(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle{
    // Core interface
    val dmemReqAck = Output(Bool())
    val dmemReq = Input(Bool())
    val dmemCmd = Input(UInt(2.W))
    val dmemWidth = Input(UInt(2.W))
    val dmemAddr = Input(UInt(cfg.dmemAWidth.W))
    val dmemWdata = Input(UInt(cfg.dmemDWidth.W))
    val dmemRdata = Output(UInt(cfg.dmemDWidth.W))
    val dmemResp = Output(UInt(2.W))

    // PORT0 interface
    val port0ReqAck = Input(Bool())
    val port0Req = Output(Bool())
    val port0Cmd = Output(UInt(2.W))
    val port0Width = Output(UInt(2.W))
    val port0Addr = Output(UInt(cfg.dmemAWidth.W))
    val port0Wdata = Output(UInt(cfg.dmemDWidth.W))
    val port0Rdata = Input(UInt(cfg.dmemDWidth.W))
    val port0Resp = Input(UInt(2.W))

    // PORT1 interface
    val port1ReqAck = if (cfg.tcmEn) Some(Input(Bool())) else None
    val port1Req = if (cfg.tcmEn) Some(Output(Bool())) else None
    val port1Cmd = if (cfg.tcmEn) Some(Output(UInt(2.W))) else None
    val port1Width = if (cfg.tcmEn) Some(Output(UInt(2.W))) else None
    val port1Addr = if (cfg.tcmEn) Some(Output(UInt(cfg.dmemAWidth.W))) else None
    val port1Wdata = if (cfg.tcmEn) Some(Output(UInt(cfg.dmemDWidth.W))) else None
    val port1Rdata = if (cfg.tcmEn) Some(Input(UInt(cfg.dmemDWidth.W))) else None
    val port1Resp = if (cfg.tcmEn) Some(Input(UInt(2.W))) else None

    // PORT2 interface
    val port2ReqAck = Input(Bool())
    val port2Req = Output(Bool())
    val port2Cmd = Output(UInt(2.W))
    val port2Width = Output(UInt(2.W))
    val port2Addr = Output(UInt(cfg.dmemAWidth.W))
    val port2Wdata = Output(UInt(cfg.dmemDWidth.W))
    val port2Rdata = Input(UInt(cfg.dmemDWidth.W))
    val port2Resp = Input(UInt(2.W))
  })

}
