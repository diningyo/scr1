// See LICENSE for license details.

package scr1.top

import Chisel.Reset
import chisel3._
import scr1.core._
import scr1.core.primitives.{ResetBufCell, ResetSyncCell}


class Scr1Top(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {
    val pwripRst = Input(Bool()) // Power-up Reset
    val cpuRrst = Input(Bool()) // CPU Reset (Core Reset)
    val testMode = Input(Bool()) // Test mode
    val testRst = Input(Bool()) // Test mode's reset
    val rtcClk = Input(Clock()) // Real-time clock
    val ndmRstOut = if (cfg.dbgcEn) Some(Output(Bool())) else None // Non-DM Reset from the Debug Module (DM)

    // Fuses
    val fuseMhartId = Input(UInt(cfg.xlen.W)) // Hart ID
    val fuseIdcode = if (cfg.dbgcEn) Some(Input(UInt(32.W))) else None // TAPC IDCODE

    // IRQ
    val irqLines = cfg.irqType match {
      case IPIC => Input(UInt(cfg.irqNum.W)) // IRQ lines to IPIC
      case NoIPIC => Input(Bool())           // External IRQ input
    }
    val softIrq = Input(Bool()) // Software IRQ input

    // JTAG I/F
    val trst = if (cfg.dbgcEn) Some(Input(Bool())) else None // change polarity(negedge -> posedge)
    val tcl = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tms = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tdi = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tdo = if (cfg.dbgcEn) Some(Output(Bool())) else None
    val tdoEn = if (cfg.dbgcEn) Some(Output(Bool())) else None

    // Instruction Memory Interface
    val imemAwId = Output(UInt(4.W))
    val imemAwAddr = Output(UInt(cfg.imemAWidth.W))
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
    val imemWData = Output(UInt(cfg.imemDWidth.W))
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
    val imemArAddr = Output(UInt(cfg.imemAWidth.W))
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
    val imemRData = Input(UInt(cfg.imemDWidth.W))
    val imemRId = Input(UInt(4.W))
    val imemRLast = Input(Bool())
    val imemRUser = Input(UInt(4.W))
    val imemRValid = Output(Bool())
    val imemRReady = Input(Bool())

    // Data Memory Interface
    val dmemAwId = Output(UInt(4.W))
    val dmemAwAddr = Output(UInt(cfg.dmemAWidth.W))
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
    val dmemWData = Output(UInt(cfg.dmemDWidth.W))
    val dmemWStrb = Output(UInt((cfg.dmemDWidth / 8).W))
    val dmemWLast = Output(Bool())
    val dmemWUser = Output(UInt(4.W))
    val dmemWValid = Output(Bool())
    val dmemWReady = Input(Bool())
    val dmemBId = Input(UInt(4.W))
    val dmemBResp = Input(UInt(2.W))
    val dmemBValid = Input(Bool())
    val dmemBReady = Output(Bool())

    val dmemArId = Output(UInt(4.W))
    val dmemArAddr = Output(UInt(cfg.dmemDWidth.W))
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
    val dmemRData = Input(UInt(cfg.dmemDWidth.W))
    val dmemRId = Input(UInt(4.W))
    val dmemRLast = Input(Bool())
    val dmemRUser = Input(UInt(4.W))
    val dmemRValid = Output(Bool())
    val dmemRReady = Input(Bool())
  })

  io := DontCare
  //-------------------------------------------------------------------------------
  // Reset logic
  //-------------------------------------------------------------------------------
  // Power-Up Reset synchronizer
  val modPwrupRstSync = Module(new ResetSyncCell())
  modPwrupRstSync.reset := io.pwripRst
  modPwrupRstSync.io.testRst := io.testRst
  modPwrupRstSync.io.testMode := io.testMode
  val pwrupRstSync = modPwrupRstSync.io.rstOut

  // Regular Reset synchronizer
  val modRstResetSync = Module(new ResetSyncCell())
  modRstResetSync.io.testRst := io.testRst
  modRstResetSync.io.testMode := io.testMode
  val rstSync = modRstResetSync.io.rstOut

  // CPU Reset synchronizer
  val modCpuRstSync = Module(new ResetSyncCell)
  modCpuRstSync.io.testRst := io.testRst
  modCpuRstSync.io.testMode := io.testMode
  val cpuRstSync = modCpuRstSync.io.rstOut

  // Combo Reset (Power-Up and Regular Resets): reset_n
  val resetSync = rstSync && pwrupRstSync
  val modResetCell = Module(new ResetBufCell)
  modResetCell.reset := resetSync
  modResetCell.io.testMode := io.testMode
  modResetCell.io.testRst := io.testRst
  modResetCell.io.resetIn := false.B


}
