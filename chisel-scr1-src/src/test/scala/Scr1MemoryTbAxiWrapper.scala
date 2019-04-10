// See LICENSE for license details.

import chisel3._
import chisel3.core.IntParam
import chisel3.util.HasBlackBoxResource
import scr1.SCR1Config

class Scr1MemoryTbAxiWrapper(
  memSize: Int,
  numIf: Int,
  addrWidth: Int,
  dataWidth: Int,
  idWidth: Int)(implicit cfg: SCR1Config) extends BlackBox(
  Map(
    "SIZE" -> IntParam(memSize),
    "N_IF" -> IntParam(numIf),
    "W_ID" -> IntParam(idWidth),
    "W_ADR" -> IntParam(addrWidth),
    "W_DATA" -> IntParam(dataWidth)
  )) with HasBlackBoxResource {
  val io = IO(new Bundle {
    // System
    val rst_n = Input(Bool())
    val clk = Input(Clock())
    val irq_lines = Output(UInt(cfg.irqNum.W))

    // Write address channel
    val awvalid = Input(Vec(numIf, Bool()))
    val awid = Input(Vec(numIf, UInt(idWidth.W)))
    val awaddr = Input(Vec(numIf, UInt(addrWidth.W)))
    val awsize = Input(Vec(numIf, UInt(3.W)))
    val awlen = Input(Vec(numIf, UInt(8.W)))
    val awready = Input(Vec(numIf, Bool()))

    // Write data channel
    val wvalid = Input(Vec(numIf, Bool()))
    val wdata = Input(Vec(numIf, UInt(dataWidth.W)))
    val wstrb = Input(Vec(numIf, UInt((dataWidth / 8).W)))
    val wlast = Input(Vec(numIf, Bool()))
    val wready = Input(Vec(numIf, Bool()))

    // Write response channel
    val bready = Input(Vec(numIf, Bool()))
    val bvalid = Input(Vec(numIf, Bool()))
    val bid = Input(Vec(numIf, UInt(idWidth.W)))
    val bresp = Input(Vec(numIf, UInt(2.W)))

    // Read address channel
    val arvalid = Input(Vec(numIf, Bool()))
    val arid = Input(Vec(numIf, UInt(idWidth.W)))
    val araddr = Input(Vec(numIf, UInt(addrWidth.W)))
    val arburst = Input(Vec(numIf, UInt(2.W)))
    val arsize = Input(Vec(numIf, UInt(3.W)))
    val arlen = Input(Vec(numIf, UInt(8.W)))
    val arready = Input(Vec(numIf, Bool()))

    // Read data channel
    val rready = Input(Vec(numIf, Bool()))
    val rvalid = Input(Vec(numIf, Bool()))
    val rid = Input(Vec(numIf, UInt(idWidth.W)))
    val rdata = Input(Vec(numIf, UInt(dataWidth.W)))
    val rlast = Input(Vec(numIf, Bool()))
    val rresp = Input(Vec(numIf, UInt(2.W)))
  })

  setResource("/scr1_memory_tb_axi.sv")
}
