// See LICENSE for license details.
package scr1.top

import chisel3._
import chisel3.util._
import scr1.core.SCR1Config

//-------------------------------------------------------------------------------
// Local types declaration
//-------------------------------------------------------------------------------
object Fsm {
  val width = 1
  val addr = 0
  val data = 1
}

/**
  * Routing module for Instruction access
  * @param cfg Scr1 parameter object
  */
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

  //-------------------------------------------------------------------------------
  // Local signal declaration
  //-------------------------------------------------------------------------------
  val fsm = RegInit(Fsm.data.U(Fsm.width.W))
  val portSel = Wire(Bool())
  val portSelReg = RegInit(false.B)
  val selRdata = Wire(UInt(cfg.imemDWidth.W))
  val selResp = Wire(UInt(MemResp.width.W))
  val selReqAck = Wire(Bool())

  //-------------------------------------------------------------------------------
  // FSM
  //-------------------------------------------------------------------------------
  portSel := (io.imemAddr & cfg.tcmAddrMask.U) === cfg.tcmAddrPattern.U

  switch (fsm) {
    is (Fsm.addr.U) {
      when (io.imemReq && selReqAck) {
        fsm := Fsm.data.U
        portSelReg := portSel
      }
    }
    is (Fsm.data.U) {
      switch (selResp) {
        is (MemResp.rdyOk.U) {
          when (io.imemReq && selReqAck) {
            fsm := Fsm.data.U
            portSelReg := portSel
          } .otherwise {
            fsm := Fsm.addr.U
          }
        }
        is (MemResp.rdyEr.U) {
          fsm := Fsm.addr.U
        }
      }
    }
  }

  when ((fsm === Fsm.addr.U) ||
    ((fsm === Fsm.data.U) && (selResp === MemResp.rdyOk.U))) {
      selReqAck := Mux(portSel, io.port1ReqAck, io.port0ReqAck)
  } .otherwise {
    selReqAck := false.B
  }

  selRdata := Mux(portSelReg, io.port1Rdata, io.port0Rdata)
  selResp := Mux(portSelReg, io.port1Resp, io.port0Resp)

  //-------------------------------------------------------------------------------
  // Interface to core
  //-------------------------------------------------------------------------------
  io.imemReqAck := selReqAck
  io.imemRdata := selRdata
  io.imemResp := selResp

  //-------------------------------------------------------------------------------
  // Interface to PORT0
  //-------------------------------------------------------------------------------
  switch (fsm) {
    is (Fsm.addr.U) {
      io.port0Req := io.imemReq && (!portSel)
    }
    is (Fsm.data.U) {
      when (selResp === MemResp.rdyOk.U) {
        io.port0Req := io.imemReq & (!portSel)
      }
    }
  }

  io.port0Cmd := Mux(!portSel, io.imemCmd, MemCmd.error.U)
  io.port0Addr := Mux(!portSel, io.imemAddr, 0.U)

  //-------------------------------------------------------------------------------
  // Interface to PORT1
  //-------------------------------------------------------------------------------
  switch (fsm) {
    is (Fsm.addr.U) {
      io.port0Req := io.imemReq && portSel
    }
    is (Fsm.data.U) {
      when (selResp === MemResp.rdyOk.U) {
        io.port0Req := io.imemReq & portSel
      }
    }
  }

  io.port0Cmd := Mux(portSel, io.imemCmd, MemCmd.error.U)
  io.port0Addr := Mux(portSel, io.imemAddr, 0.U)

  //-------------------------------------------------------------------------------
  // Assertion
  //-------------------------------------------------------------------------------

  /* delete */
}
