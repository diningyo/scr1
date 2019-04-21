// See LICENSE for license details.
package scr1.top

import chisel3._
import chisel3.util._
import scr1.core.SCR1Config

//-------------------------------------------------------------------------------
// Local types declaration
//-------------------------------------------------------------------------------
private object Sel {
  val width = 2
  val selPort0 = 0
  val selPort1 = 1
  val selPort2 = 2
}

/**
  * Routing module for Dmem
  * @param cfg Scr1 parameter object
  */
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

  //-------------------------------------------------------------------------------
  // Local signal declaration
  //-------------------------------------------------------------------------------
  val fsm = RegInit(Fsm.data.U(Fsm.width.W))
  val portSel = Wire(UInt(Sel.width.W))
  val portSelReg = RegInit(Sel.selPort0.U(Sel.width.W))
  val selRdata = Wire(UInt(cfg.dmemDWidth.W))
  val selResp = Wire(UInt(MemResp.width.W))
  val selReqAck = Wire(Bool())

  //-------------------------------------------------------------------------------
  // FSM
  //-------------------------------------------------------------------------------
  when ((io.dmemAddr & cfg.tcmAddrMask.U) === cfg.tcmAddrPattern.U) {
    portSel := Sel.selPort1.U
  } .elsewhen ((io.dmemReqAck & cfg.timerAddrMask.U) === cfg.timerAddrPattern.U) {
    portSel := Sel.selPort2.U
  } .otherwise {
    portSel := Sel.selPort0.U
  }

  switch (fsm) {
    is (Fsm.addr.U) {
      when (io.dmemReq && selReqAck) {
        fsm := Fsm.data.U
        portSelReg := portSel
      }
    }
    is (Fsm.data.U) {
      switch (selResp) {
        is (MemResp.rdyOk.U) {
          when (io.dmemReq && selReqAck) {
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

    switch (portSel) {
      is (Sel.selPort0.U) {
        selReqAck := io.port0ReqAck
      }
      is (Sel.selPort1.U) {
        selReqAck := io.port1ReqAck.getOrElse(false.B)
      }
      is (Sel.selPort2.U) {
        selReqAck := io.port2ReqAck
      }
    }
  } .otherwise {
    selReqAck := false.B
  }

  switch (portSelReg) {
    is (Sel.selPort0.U) {
      selRdata := io.port0Rdata
      selResp := io.port0Resp
    }
    is (Sel.selPort1.U) {
      selRdata := io.port1Rdata.getOrElse(0.U)
      selResp := io.port1Resp.getOrElse(MemResp.notRdy.U)
    }
    is (Sel.selPort2.U) {
      selRdata := io.port2Rdata
      selResp := io.port2Resp
    }
  }

  //-------------------------------------------------------------------------------
  // Interface to core
  //-------------------------------------------------------------------------------
  io.dmemReqAck := selReqAck
  io.dmemRdata := selRdata
  io.dmemResp := selResp

  //-------------------------------------------------------------------------------
  // Interface to PORT0
  //-------------------------------------------------------------------------------
  switch (fsm) {
    is (Fsm.addr.U) {
      io.port0Req := io.dmemReq && (portSel === Sel.selPort0.U)
    }
    is (Fsm.data.U) {
      when (selResp === MemResp.rdyOk.U) {
        io.port0Req := io.dmemReq & (portSel === Sel.selPort0.U)
      }
    }
  }

  io.port0Cmd := Mux(portSel === Sel.selPort0.U, io.dmemCmd, MemCmd.error.U)
  io.port0Width := Mux(portSel === Sel.selPort0.U, io.dmemWidth, MemWidth.error.U)
  io.port0Addr := Mux(portSel === Sel.selPort0.U, io.dmemAddr, 0.U)
  io.port0Wdata := Mux(portSel === Sel.selPort0.U, io.dmemWdata, 0.U)

  //-------------------------------------------------------------------------------
  // Interface to PORT1
  //-------------------------------------------------------------------------------
  switch (fsm) {
    is (Fsm.addr.U) {
      io.port0Req := io.dmemReq && (portSel === Sel.selPort1.U)
    }
    is (Fsm.data.U) {
      when (selResp === MemResp.rdyOk.U) {
        io.port0Req := io.dmemReq && (portSel === Sel.selPort1.U)
      }
    }
  }

  io.port1Cmd.get := Mux(portSel === Sel.selPort1.U, io.dmemCmd, MemCmd.error.U)
  io.port1Width.get := Mux(portSel === Sel.selPort1.U, io.dmemWidth, MemWidth.error.U)
  io.port1Addr.get := Mux(portSel === Sel.selPort1.U, io.dmemAddr, 0.U)
  io.port1Wdata.get := Mux(portSel === Sel.selPort1.U, io.dmemWdata, 0.U)

  //-------------------------------------------------------------------------------
  // Interface to PORT2
  //-------------------------------------------------------------------------------
  switch (fsm) {
    is (Fsm.addr.U) {
      io.port2Req := io.dmemReq && (portSel === Sel.selPort2.U)
    }
    is (Fsm.data.U) {
      when (selResp === MemResp.rdyOk.U) {
        io.port2Req := io.dmemReq && (portSel === Sel.selPort2.U)
      }
    }
  }

  io.port2Cmd := Mux(portSel === Sel.selPort2.U, io.dmemCmd, MemCmd.error.U)
  io.port2Width := Mux(portSel === Sel.selPort2.U, io.dmemWidth, MemWidth.error.U)
  io.port2Addr := Mux(portSel === Sel.selPort2.U, io.dmemAddr, 0.U)
  io.port2Wdata := Mux(portSel === Sel.selPort2.U, io.dmemWdata, 0.U)

  //-------------------------------------------------------------------------------
  // Assertion
  //-------------------------------------------------------------------------------

  /* delete */
}
