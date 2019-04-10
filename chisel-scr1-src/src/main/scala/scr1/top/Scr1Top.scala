// See LICENSE for license details.
package scr1.top

import chisel3._
import scr1.core._


class Scr1Top(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {
    val testMode = Input(Bool()) // Test mode
    val rtcClk = Input(Clock()) // Real-time clock
    val rstnOut = Output(Bool()) // Core reset from DBGC

    // Fuses
    val fuseMhartId = Input(UInt(cfg.xLen.W)) // Hart ID

    // IRQ
    val irqLines = cfg.irqType match {
      case IPIC => Input(UInt(cfg.irqNum.W)) // IRQ lines to IPIC
      case NoIPIC => Input(Bool())           // External IRQ input
    }
    val softIrq = Input(Bool()) // Software IRQ input

    // JTAG I/F
    val trstn = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tcl = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tms = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tdi = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tdo = if (cfg.dbgcEn) Some(Output(Bool())) else None
    val tdoEn = if (cfg.dbgcEn) Some(Output(Bool())) else None

    // Instruction Memory Interface
    val imemAwId = Output(UInt(4.W))
    val imemAwAddr = Output(UInt(32.W))
    val imemAwLen = Output(UInt(8.W))
    val imemAwSize = Output(UInt(3.W))
    val imemAwBurst = Output(UInt(2.W))
    val imemAwLock = Output(UInt(1.W))
    val imemAwCache = Output(UInt(4.W))
    val imemAwProt = Output(UInt(3.W))
    val imemAwRegion = Output(UInt(4.W))
    val imemAwUser = Output(UInt(4.W))
    val imemAwQos = Output(UInt(4.W))
    val imemAwValid = Output(Bool())
    val imemAwReady = Input(Bool())
    val imemWData = Output(UInt(32.W))
    val imemWStrb = Output(UInt(4.W))
    val imemWLast = Output(Bool())
    val imemWUser = Output(UInt(4.W))
    val imemWValid = Output(Bool())
    val imemWReady = Input(Bool())
    val imemBId = Input(UInt(4.W))
    val imemBResp = Input(UInt(2.W))
    val imemBValid = Input(Bool())
    val imemBReady = Output(Bool())

    val imemArId = Output(UInt(4.W))
    val imemArAddr = Output(UInt(32.W))
    val imemArLen = Output(UInt(8.W))
    val imemArSize = Output(UInt(3.W))
    val imemArBurst = Output(UInt(2.W))
    val imemArLock = Output(UInt(1.W))
    val imemArCache = Output(UInt(4.W))
    val imemArProt = Output(UInt(3.W))
    val imemArRegion = Output(UInt(4.W))
    val imemArUser = Output(UInt(4.W))
    val imemArQos = Output(UInt(4.W))
    val imemArValid = Output(Bool())
    val imemArReady = Input(Bool())
    val imemRData = Input(UInt(32.W))
    val imemRId = Input(UInt(4.W))
    val imemRLast = Input(Bool())
    val imemRUser = Input(UInt(4.W))
    val imemRValid = Output(Bool())
    val imemRReady = Input(Bool())

    // Data Memory Interface
    val dmemAwId = Output(UInt(4.W))
    val dmemAwAddr = Output(UInt(32.W))
    val dmemAwLen = Output(UInt(8.W))
    val dmemAwSize = Output(UInt(3.W))
    val dmemAwBurst = Output(UInt(2.W))
    val dmemAwLock = Output(UInt(1.W))
    val dmemAwCache = Output(UInt(4.W))
    val dmemAwProt = Output(UInt(3.W))
    val dmemAwRegion = Output(UInt(4.W))
    val dmemAwUser = Output(UInt(4.W))
    val dmemAwQos = Output(UInt(4.W))
    val dmemAwValid = Output(Bool())
    val dmemAwReady = Input(Bool())
    val dmemWData = Output(UInt(32.W))
    val dmemWStrb = Output(UInt(4.W))
    val dmemWLast = Output(Bool())
    val dmemWUser = Output(UInt(4.W))
    val dmemWValid = Output(Bool())
    val dmemWReady = Input(Bool())
    val dmemBId = Input(UInt(4.W))
    val dmemBResp = Input(UInt(2.W))
    val dmemBValid = Input(Bool())
    val dmemBReady = Output(Bool())

    val dmemArId = Output(UInt(4.W))
    val dmemArAddr = Output(UInt(32.W))
    val dmemArLen = Output(UInt(8.W))
    val dmemArSize = Output(UInt(3.W))
    val dmemArBurst = Output(UInt(2.W))
    val dmemArLock = Output(UInt(1.W))
    val dmemArCache = Output(UInt(4.W))
    val dmemArProt = Output(UInt(3.W))
    val dmemArRegion = Output(UInt(4.W))
    val dmemArUser = Output(UInt(4.W))
    val dmemArQos = Output(UInt(4.W))
    val dmemArValid = Output(Bool())
    val dmemArReady = Input(Bool())
    val dmemRData = Input(UInt(32.W))
    val dmemRId = Input(UInt(4.W))
    val dmemRLast = Input(Bool())
    val dmemRUser = Input(UInt(4.W))
    val dmemRValid = Output(Bool())
    val dmemRReady = Input(Bool())
  })

  io := DontCare
}
