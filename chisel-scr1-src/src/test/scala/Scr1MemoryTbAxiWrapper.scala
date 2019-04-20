// See LICENSE for license details.

import chisel3._
import chisel3.core.IntParam
import chisel3.util.HasBlackBoxResource
import scr1.SCR1Config

class Scr1MemoryTbAxiWrapper(
  memSize: Int,
  //numIf: Int,
  addrWidth: Int,
  dataWidth: Int,
  idWidth: Int,
  irqLinesNum: Int
  )(implicit cfg: SCR1Config) extends BlackBox(
  Map(
    "SIZE" -> IntParam(memSize),
    //"N_IF" -> IntParam(numIf),
    "W_ID" -> IntParam(idWidth),
    "W_ADR" -> IntParam(addrWidth),
    "W_DATA" -> IntParam(dataWidth),
    "SCR1_IRQ_LINES_NUM" -> IntParam(irqLinesNum)
  )) with HasBlackBoxResource {
  val io = IO(new Bundle {
    // System
    val rst_n = Input(Bool())
    val clk = Input(Clock())
    val irq_lines = Output(UInt(cfg.irqNum.W))

    // Write address channel
    val awid_0 = Input(UInt(idWidth.W))
    val awaddr_0 = Input(UInt(addrWidth.W))
    val awlen_0 = Input(UInt(8.W))
    val awsize_0 = Input(UInt(3.W))
    val awburst_0 = Input(UInt(2.W))
    val awlock_0 = Input(UInt(1.W))
    val awcache_0 = Input(UInt(4.W))
    val awprot_0 = Input(UInt(3.W))
    val awregion_0 = Input(UInt(4.W))
    val awuser_0 = Input(UInt(4.W))
    val awqos_0 = Input(UInt(4.W))
    val awvalid_0 = Input(Bool())
    val awready_0 = Input(Bool())
    val wdata_0 = Input(UInt(dataWidth.W))
    val wstrb_0 = Input(UInt((dataWidth / 8).W))
    val wlast_0 = Input(Bool())
    val wuser_0 = Input(UInt(4.W))
    val wvalid_0 = Input(Bool())
    val wready_0 = Input(Bool())
    val bid_0 = Input(UInt(idWidth.W))
    val bresp_0 = Input(UInt(2.W))
    val bvalid_0 = Input(Bool())
    val buser_0 = Input(UInt(4.W))
    val bready_0 = Input(Bool())
    val arid_0 = Input(UInt(idWidth.W))
    val araddr_0 = Input(UInt(addrWidth.W))
    val arlen_0 = Input(UInt(8.W))
    val arsize_0 = Input(UInt(3.W))
    val arburst_0 = Input(UInt(2.W))
    val arlock_0 = Input(UInt(1.W))
    val arcache_0 = Input(UInt(4.W))
    val arprot_0 = Input(UInt(3.W))
    val arregion_0 = Input(UInt(4.W))
    val aruser_0 = Input(UInt(4.W))
    val arqos_0 = Input(UInt(4.W))
    val arvalid_0 = Input(Bool())
    val arready_0 = Input(Bool())
    val rid_0 = Input(UInt(idWidth.W))
    val rdata_0 = Input(UInt(dataWidth.W))
    val rresp_0 = Input(UInt(2.W))
    val rlast_0 = Input(Bool())
    val ruser_0 = Input(UInt(4.W))
    val rvalid_0 = Input(Bool())
    val rready_0 = Input(Bool())

    // Data Memory
    val awid_1 = Input(UInt(idWidth.W))
    val awaddr_1 = Input(UInt(addrWidth.W))
    val awlen_1 = Input(UInt(8.W))
    val awsize_1 = Input(UInt(3.W))
    val awburst_1 = Input(UInt(2.W))
    val awlock_1 = Input(UInt(1.W))
    val awcache_1 = Input(UInt(4.W))
    val awprot_1 = Input(UInt(3.W))
    val awregion_1 = Input(UInt(4.W))
    val awuser_1 = Input(UInt(4.W))
    val awqos_1 = Input(UInt(4.W))
    val awvalid_1 = Input(Bool())
    val awready_1 = Input(Bool())
    val wdata_1 = Input(UInt(dataWidth.W))
    val wstrb_1 = Input(UInt((dataWidth / 8).W))
    val wlast_1 = Input(Bool())
    val wuser_1 = Input(UInt(4.W))
    val wvalid_1 = Input(Bool())
    val wready_1 = Input(Bool())
    val bid_1 = Input(UInt(idWidth.W))
    val bresp_1 = Input(UInt(2.W))
    val bvalid_1 = Input(Bool())
    val buser_1 = Input(UInt(4.W))
    val bready_1 = Input(Bool())
    val arid_1 = Input(UInt(idWidth.W))
    val araddr_1 = Input(UInt(addrWidth.W))
    val arlen_1 = Input(UInt(8.W))
    val arsize_1 = Input(UInt(3.W))
    val arburst_1 = Input(UInt(2.W))
    val arlock_1 = Input(UInt(1.W))
    val arcache_1 = Input(UInt(4.W))
    val arprot_1 = Input(UInt(3.W))
    val arregion_1 = Input(UInt(4.W))
    val aruser_1 = Input(UInt(4.W))
    val arqos_1 = Input(UInt(4.W))
    val arvalid_1 = Input(Bool())
    val arready_1 = Input(Bool())
    val rid_1 = Input(UInt(idWidth.W))
    val rdata_1 = Input(UInt(dataWidth.W))
    val rresp_1 = Input(UInt(2.W))
    val rlast_1 = Input(Bool())
    val ruser_1 = Input(UInt(4.W))
    val rvalid_1 = Input(Bool())
    val rready_1 = Input(Bool())
  })

  setResource("/scr1_memory_tb_axi.sv")
}
