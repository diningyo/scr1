// See LICENSE for license details.
package scr1.top

import chisel3._
import scr1.core.SCR1Config

class MemAxi(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {

    val axiReinit = Input(Bool())

    // Core Interface
    val coreIdle = Output(Bool())
    val coreReqAck = Output(Bool())
    val coreReq = Input(Bool())
    val coreCmd = Input(UInt(MemCmd.width.W))
    val coreWidth = Input(UInt(MemWidth.width.W))
    val coreAddr = Input(UInt(cfg.dmemAWidth.W))
    val coreWdata = Input(UInt(cfg.dmemDWidth.W))
    val coreRdata = Output(UInt(cfg.dmemDWidth.W))
    val coreResp = Output(UInt(MemResp.width.W))

    // AXI
    val awid = Output(UInt(4.W))
    val awaddr = Output(UInt(cfg.dmemAWidth.W))
    val awlen = Output(UInt(8.W))
    val awsize = Output(UInt(3.W))
    val awburst = Output(UInt(2.W))
    val awlock = Output(UInt(1.W))
    val awcache = Output(UInt(4.W))
    val awprot = Output(UInt(3.W))
    val awregion = Output(UInt(4.W))
    val awuser = Output(UInt(4.W))
    val awqos = Output(UInt(4.W))
    val awvalid = Output(Bool())
    val awready = Input(Bool())
    val wdata = Output(UInt(cfg.dmemDWidth.W))
    val wstrb = Output(UInt((cfg.dmemDWidth / 8).W))
    val wlast = Output(Bool())
    val wuser = Output(UInt(4.W))
    val wvalid = Output(Bool())
    val wready = Input(Bool())
    val bid = Input(UInt(4.W))
    val bresp = Input(UInt(2.W))
    val bvalid = Input(Bool())
    val buser = Input(UInt(4.W))
    val bready = Output(Bool())

    val arid = Output(UInt(4.W))
    val araddr = Output(UInt(cfg.dmemDWidth.W))
    val arlen = Output(UInt(8.W))
    val arsize = Output(UInt(3.W))
    val arburst = Output(UInt(2.W))
    val arlock = Output(UInt(1.W))
    val arcache = Output(UInt(4.W))
    val arprot = Output(UInt(3.W))
    val arregion = Output(UInt(4.W))
    val aruser = Output(UInt(4.W))
    val arqos = Output(UInt(4.W))
    val arvalid = Output(Bool())
    val arready = Input(Bool())
    val rdata = Input(UInt(cfg.dmemDWidth.W))
    val rresp = Input(UInt(2.W))
    val rid = Input(UInt(4.W))
    val rlast = Input(Bool())
    val ruser = Input(UInt(4.W))
    val rvalid = Input(Bool())
    val rready = Output(Bool())
  })

  io := DontCare
}
