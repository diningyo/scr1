// See LICENSE for license details.

package scr1.top

import chisel3._
import scr1.core._
import scr1.core.primitives.{ResetBufCell, ResetSyncCell}


class Scr1Top(implicit cfg: SCR1Config) extends Module {
  val io = IO(new Bundle {
    val pwripRst = Input(Bool()) // Power-up Reset
    val cpuRst = Input(Bool()) // CPU Reset (Core Reset)
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
    val tck = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tms = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tdi = if (cfg.dbgcEn) Some(Input(Bool())) else None
    val tdo = if (cfg.dbgcEn) Some(Output(Bool())) else None
    val tdoEn = if (cfg.dbgcEn) Some(Output(Bool())) else None

    // Instruction Memory Interface
    val imemAwid = Output(UInt(4.W))
    val imemAwaddr = Output(UInt(cfg.imemAWidth.W))
    val imemAwlen = Output(UInt(8.W))
    val imemAwsize = Output(UInt(3.W))
    val imemAwburst = Output(UInt(2.W))
    val imemAwlock = Output(UInt(1.W))
    val imemAwcache = Output(UInt(4.W))
    val imemAwprot = Output(UInt(3.W))
    val imemAwregion = Output(UInt(4.W))
    val imemAwuser = Output(UInt(4.W))
    val imemAwqos = Output(UInt(4.W))
    val imemAwvalid = Output(Bool())
    val imemAwready = Input(Bool())
    val imemWdata = Output(UInt(cfg.imemDWidth.W))
    val imemWstrb = Output(UInt(4.W))
    val imemWlast = Output(Bool())
    val imemWuser = Output(UInt(4.W))
    val imemWvalid = Output(Bool())
    val imemWready = Input(Bool())
    val imemBid = Input(UInt(4.W))
    val imemBuser = Input(UInt(4.W))
    val imemBresp = Input(UInt(2.W))
    val imemBvalid = Input(Bool())
    val imemBready = Output(Bool())

    val imemArid = Output(UInt(4.W))
    val imemAraddr = Output(UInt(cfg.imemAWidth.W))
    val imemArlen = Output(UInt(8.W))
    val imemArsize = Output(UInt(3.W))
    val imemArburst = Output(UInt(2.W))
    val imemArlock = Output(UInt(1.W))
    val imemArcache = Output(UInt(4.W))
    val imemArprot = Output(UInt(3.W))
    val imemArregion = Output(UInt(4.W))
    val imemAruser = Output(UInt(4.W))
    val imemArqos = Output(UInt(4.W))
    val imemArvalid = Output(Bool())
    val imemArready = Input(Bool())
    val imemRdata = Input(UInt(cfg.imemDWidth.W))
    val imemRResp = Input(UInt(2.W))
    val imemRid = Input(UInt(4.W))
    val imemRlast = Input(Bool())
    val imemRuser = Input(UInt(4.W))
    val imemRvalid = Input(Bool())
    val imemRready = Output(Bool())

    // Data Memory Interface
    val dmemAwid = Output(UInt(4.W))
    val dmemAwaddr = Output(UInt(cfg.dmemAWidth.W))
    val dmemAwlen = Output(UInt(8.W))
    val dmemAwsize = Output(UInt(3.W))
    val dmemAwburst = Output(UInt(2.W))
    val dmemAwlock = Output(UInt(1.W))
    val dmemAwcache = Output(UInt(4.W))
    val dmemAwprot = Output(UInt(3.W))
    val dmemAwregion = Output(UInt(4.W))
    val dmemAwuser = Output(UInt(4.W))
    val dmemAwqos = Output(UInt(4.W))
    val dmemAwvalid = Output(Bool())
    val dmemAwready = Input(Bool())
    val dmemWdata = Output(UInt(cfg.dmemDWidth.W))
    val dmemWstrb = Output(UInt((cfg.dmemDWidth / 8).W))
    val dmemWlast = Output(Bool())
    val dmemWuser = Output(UInt(4.W))
    val dmemWvalid = Output(Bool())
    val dmemWready = Input(Bool())
    val dmemBid = Input(UInt(4.W))
    val dmemBuser = Input(UInt(4.W))
    val dmemBresp = Input(UInt(2.W))
    val dmemBvalid = Input(Bool())
    val dmemBready = Output(Bool())

    val dmemArid = Output(UInt(4.W))
    val dmemAraddr = Output(UInt(cfg.dmemDWidth.W))
    val dmemArlen = Output(UInt(8.W))
    val dmemArsize = Output(UInt(3.W))
    val dmemArburst = Output(UInt(2.W))
    val dmemArlock = Output(UInt(1.W))
    val dmemArcache = Output(UInt(4.W))
    val dmemArprot = Output(UInt(3.W))
    val dmemArregion = Output(UInt(4.W))
    val dmemAruser = Output(UInt(4.W))
    val dmemArqos = Output(UInt(4.W))
    val dmemArvalid = Output(Bool())
    val dmemArready = Input(Bool())
    val dmemRdata = Input(UInt(cfg.dmemDWidth.W))
    val dmemRid = Input(UInt(4.W))
    val dmemRResp = Input(UInt(2.W))
    val dmemRlast = Input(Bool())
    val dmemRuser = Input(UInt(4.W))
    val dmemRvalid = Input(Bool())
    val dmemRready = Output(Bool())
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
  val resetOut = modResetCell.io.resetOut

  //-------------------------------------------------------------------------------
  // SCR1 core instance
  //-------------------------------------------------------------------------------
  val coreTop = Module(new scr1.core.Top)

  val coreImemReqAck = Wire(Bool())
  val coreImemReq = Wire(Bool())
  val coreImemCmd = Wire(UInt(MemCmd.width.W))
  val coreImemAddr = Wire(UInt(cfg.imemAWidth.W))
  val coreImemRdata = Wire(UInt(cfg.imemDWidth.W))
  val coreImemResp = Wire(UInt(MemResp.width.W))

  // Data Memory Interface
  val coreDmemReqAck = Wire(Bool())
  val coreDmemReq = Wire(Bool())
  val coreDmemCmd = Wire(UInt(MemCmd.width.W))
  val coreDmemWidth = Wire(UInt(MemWidth.width.W))
  val coreDmemAddr = Wire(UInt(cfg.dmemAWidth.W))
  val coreDmemWdata = Wire(UInt(cfg.dmemDWidth.W))
  val coreDmemRdata = Wire(UInt(cfg.dmemDWidth.W))
  val coreDmemResp = Wire(UInt(MemResp.width.W))

  coreTop.io.pwrupRst := pwrupRstSync
  coreTop.reset := rstSync
  coreTop.io.cpuRst := cpuRstSync
  coreTop.io.testMode := io.testMode
  coreTop.io.testRst := io.testRst
  val coreRstLocal = coreTop.io.coreRstOut
  io.ndmRstOut.get := coreTop.io.ndmRstOut.get
  coreTop.io.fuseMhartId := io.fuseMhartId
  coreTop.io.fuseIdcode.get := io.fuseIdcode.get

  // IRQ
  val timerIrq = Wire(Bool())
  val timerVal = Wire(UInt(64.W))
  coreTop.io.irqLines := io.irqLines
  coreTop.io.softIrq := io.softIrq
  coreTop.io.timerIrq := timerIrq
  coreTop.io.mtimeExt := timerVal
  coreTop.io.trst.get := io.trst.get
  coreTop.io.tck.get := io.tck.get
  coreTop.io.tms.get :=  io.tms.get
  coreTop.io.tdi.get := io.tdi.get
  io.tdo.get := coreTop.io.tdo.get
  io.tdoEn.get :=  coreTop.io.tdoEn.get
  coreTop.io.imemReqAck := coreImemReqAck
  coreImemReq  := coreTop.io.imemReq
  coreImemCmd  := coreTop.io.imemCmd
  coreImemAddr := coreTop.io.imemAddr
  coreTop.io.imemRdata     := coreImemRdata
  coreTop.io.imemResp      := coreImemResp
  // Data memory interface
  coreTop.io.dmemReqAck   := coreDmemReqAck
  coreDmemReq       := coreTop.io.dmemReq
  coreDmemCmd       := coreTop.io.dmemCmd
  coreDmemWidth     := coreTop.io.dmemWidth
  coreDmemAddr      := coreTop.io.dmemAddr
  coreDmemWdata     := coreTop.io.dmemWdata
  coreTop.io.dmemRdata     := coreDmemRdata
  coreTop.io.dmemResp      := coreDmemResp

  //-------------------------------------------------------------------------------
  // TCM instance
  //-------------------------------------------------------------------------------
  val modTcm = if (cfg.tcmEn) Some(Module(new Tcm)) else None
  val tcmImemReqAck = Wire(Bool())
  val tcmImemReq = Wire(Bool())
  val tcmImemCmd = Wire(UInt(2.W))
  val tcmImemAddr = Wire(UInt(cfg.imemAWidth.W))
  val tcmImemRdata = Wire(UInt(cfg.imemDWidth.W))
  val tcmImemResp = Wire(UInt(2.W))

  // Core data interface
  val tcmDmemReqAck = Wire(Bool())
  val tcmDmemReq = Wire(Bool())
  val tcmDmemCmd = Wire(UInt(2.W))
  val tcmDmemWidth = Wire(UInt(2.W))
  val tcmDmemAddr = Wire(UInt(cfg.dmemAWidth.W))
  val tcmDmemWdata = Wire(UInt(cfg.dmemDWidth.W))
  val tcmDmemRdata = Wire(UInt(cfg.dmemDWidth.W))
  val tcmDmemResp = Wire(UInt(2.W))

  if (cfg.tcmEn) {
    val tcm = modTcm.get
    val tcmIo = tcm.io

    tcm.reset := coreRstLocal
    // Instruction interface to TCM
    tcmImemReqAck := tcmIo.imemReqAck
    tcmIo.imemReq := tcmImemReq
    tcmIo.imemCmd := tcmImemCmd
    tcmIo.imemAddr := tcmImemAddr
    tcmImemRdata := tcmIo.imemRdata
    tcmImemResp := tcmIo.imemResp
    // Data interface to TCM
    tcmDmemReqAck := tcmIo.dmemReqAck
    tcmIo.dmemReq := tcmDmemReq
    tcmIo.dmemCmd := tcmDmemCmd
    tcmIo.dmemWidth := tcmDmemWidth
    tcmIo.dmemAddr := tcmDmemAddr
    tcmIo.dmemWdata := tcmDmemWdata
    tcmDmemRdata := tcmIo.dmemRdata
    tcmDmemResp := tcmIo.dmemResp
  }

  //-------------------------------------------------------------------------------
  // Memory-mapped timer instance
  //-------------------------------------------------------------------------------
  // Core data interface
  val timer = Module(new Timer())
  val timerDmemReqAck = Wire(Bool())
  val timerDmemReq = Wire(Bool())
  val timerDmemCmd = Wire(UInt(2.W))
  val timerDmemWidth = Wire(UInt(2.W))
  val timerDmemAddr = Wire(UInt(cfg.dmemAWidth.W))
  val timerDmemWdata = Wire(UInt(cfg.dmemDWidth.W))
  val timerDmemRdata = Wire(UInt(cfg.dmemDWidth.W))
  val timerDmemResp = Wire(UInt(2.W))

  timer.reset := coreRstLocal
  timer.io.rtcClk := io.rtcClk
  timerDmemReqAck := timer.io.dmemReqAck
  timer.io.dmemReq := timerDmemReq
  timer.io.dmemCmd := timerDmemCmd
  timer.io.dmemWidth := timerDmemWidth
  timer.io.dmemAddr := timerDmemAddr
  timer.io.dmemWdata := timerDmemWdata
  timerDmemRdata := timer.io.dmemRdata
  timerDmemResp :=  timer.io.dmemResp
  timerVal := timer.io.timerVal
  timerIrq := timer.io.timerIrq

  //-------------------------------------------------------------------------------
  // Instruction memory router
  //-------------------------------------------------------------------------------
  val axiImemReqAck = Wire(Bool())
  val axiImemReq = Wire(Bool())
  val axiImemCmd = Wire(UInt(2.W))
  val axiImemAddr = Wire(UInt(cfg.imemAWidth.W))
  val axiImemRdata = Wire(UInt(cfg.imemDWidth.W))
  val axiImemResp = Wire(UInt(2.W))

  val imemRouter = if (cfg.imemRouterEn) Some(Module(new ImemRouter)) else None

  if (cfg.imemRouterEn) {
    val ir = imemRouter.get
    coreImemReqAck := ir.io.imemReqAck
    ir.io.imemReq := coreImemReq
    ir.io.imemCmd := coreImemCmd
    ir.io.imemAddr := coreImemAddr
    coreImemRdata := ir.io.imemRdata
    coreImemResp := ir.io.imemResp

    // PORT0 interface
    ir.io.port0ReqAck := axiImemReqAck
    axiImemReq := ir.io.port0Req
    axiImemCmd := ir.io.port0Cmd
    axiImemAddr := ir.io.port0Addr
    ir.io.port0Rdata := axiImemRdata
    ir.io.port0Resp := axiImemResp

    // PORT1 interface
    ir.io.port1ReqAck := tcmImemReqAck
    tcmImemReq := ir.io.port1Req
    tcmImemCmd := ir.io.port1Cmd
    tcmImemAddr := ir.io.port1Addr
    ir.io.port1Rdata := tcmImemRdata
    ir.io.port1Resp := tcmImemResp
  }
  else {
    axiImemReq := coreImemReq
    axiImemCmd := coreImemCmd
    axiImemAddr := coreImemAddr
    coreImemReqAck := axiImemReqAck
    coreImemRdata := axiImemRdata
    coreImemResp := axiImemResp
  }

  //-------------------------------------------------------------------------------
  // Data memory router
  //-------------------------------------------------------------------------------
  val axiDmemReqAck = Wire(Bool())
  val axiDmemReq = Wire(Bool())
  val axiDmemCmd = Wire(UInt(2.W))
  val axiDmemAddr = Wire(UInt(cfg.imemAWidth.W))
  val axiDmemRdata = Wire(UInt(cfg.imemDWidth.W))
  val axiDmemResp = Wire(UInt(2.W))

  val dmemRouter = Module(new DmemRouter)

  coreDmemReqAck := dmemRouter.io.dmemReqAck
  dmemRouter.io.dmemReq := coreDmemReq
  dmemRouter.io.dmemCmd := coreDmemCmd
  dmemRouter.io.dmemWidth := coreDmemWidth
  dmemRouter.io.dmemAddr := coreDmemAddr
  coreDmemRdata := dmemRouter.io.dmemRdata
  coreDmemResp := dmemRouter.io.dmemResp
  dmemRouter.io.dmemWdata := coreDmemWdata

  // PORT0 interface
  dmemRouter.io.port0ReqAck := axiDmemReqAck
  axiDmemReq := dmemRouter.io.port0Req
  axiDmemCmd := dmemRouter.io.port0Cmd
  axiDmemAddr := dmemRouter.io.port0Addr
  dmemRouter.io.port0Rdata := axiDmemRdata
  dmemRouter.io.port0Resp := axiDmemResp

  // PORT1 interface
  if (cfg.tcmEn) {
    dmemRouter.io.port1ReqAck.get := tcmDmemReqAck
    tcmDmemReq := dmemRouter.io.port1Req.get
    tcmDmemCmd := dmemRouter.io.port1Cmd.get
    tcmDmemAddr := dmemRouter.io.port1Addr.get
    dmemRouter.io.port1Rdata.get := tcmDmemRdata
    dmemRouter.io.port1Resp.get := tcmDmemResp
    tcmDmemWdata := dmemRouter.io.port1Wdata.get
    tcmDmemWidth := dmemRouter.io.port1Width.get
  }

  // PORT2 interface
  dmemRouter.io.port2ReqAck := timerDmemReqAck
  timerDmemReq := dmemRouter.io.port2Req
  timerDmemCmd := dmemRouter.io.port2Cmd
  timerDmemWidth := dmemRouter.io.port2Width
  timerDmemAddr := dmemRouter.io.port2Addr
  dmemRouter.io.port2Rdata := timerDmemRdata
  dmemRouter.io.port2Resp := timerDmemResp
  timerDmemWdata := dmemRouter.io.port2Wdata

  //-------------------------------------------------------------------------------
  // Instruction memory AXI bridge
  //-------------------------------------------------------------------------------
  val axiReinit = RegInit(false.B)

  val imemAxi = Module(new MemAxi)
  imemAxi.reset := resetOut

  imemAxi.io.axiReinit := axiReinit

  // Core Interface
  val axiImemIdle = imemAxi.io.coreIdle
  axiImemReqAck := imemAxi.io.coreReqAck
  imemAxi.io.coreReq    := axiImemReq
  imemAxi.io.coreCmd    := axiImemCmd
  imemAxi.io.coreWidth  := MemWidth.word
  imemAxi.io.coreAddr   := axiImemAddr
  imemAxi.io.coreWdata  := 0.U
  axiImemRdata := imemAxi.io.coreRdata
  axiImemResp := imemAxi.io.coreResp

  io.imemArid     := imemAxi.io.arid
  io.imemAraddr   := imemAxi.io.araddr
  io.imemArlen    := imemAxi.io.arlen
  io.imemArsize   := imemAxi.io.arsize
  io.imemArburst  := imemAxi.io.arburst
  io.imemArlock   := imemAxi.io.arlock
  io.imemArcache  := imemAxi.io.arcache
  io.imemArprot   := imemAxi.io.arprot
  io.imemArregion := imemAxi.io.arregion
  io.imemAruser   := imemAxi.io.aruser
  io.imemArqos    := imemAxi.io.arqos
  io.imemArvalid  := imemAxi.io.arvalid
  imemAxi.io.arready := io.imemArready
  imemAxi.io.rdata := io.imemRdata
  imemAxi.io.rid := io.imemRid
  imemAxi.io.rresp := io.imemRResp
  imemAxi.io.rlast := io.imemRlast
  imemAxi.io.ruser := io.imemRuser
  imemAxi.io.rvalid := io.imemRvalid
  io.imemRready   := imemAxi.io.rready

  io.imemAwid     := imemAxi.io.awid
  io.imemAwaddr   := imemAxi.io.awaddr
  io.imemAwlen    := imemAxi.io.awlen
  io.imemAwsize   := imemAxi.io.awsize
  io.imemAwburst  := imemAxi.io.awburst
  io.imemAwlock   := imemAxi.io.awlock
  io.imemAwcache  := imemAxi.io.awcache
  io.imemAwprot   := imemAxi.io.awprot
  io.imemAwregion := imemAxi.io.awregion
  io.imemAwuser   := imemAxi.io.awuser
  io.imemAwqos    := imemAxi.io.awqos
  io.imemAwvalid  := imemAxi.io.awvalid
  imemAxi.io.awready := io.imemAwready
  io.imemWdata := imemAxi.io.wdata
  io.imemWstrb := imemAxi.io.wstrb
  io.imemWuser := imemAxi.io.wuser
  io.imemWlast := imemAxi.io.wlast
  io.imemWvalid := imemAxi.io.wvalid
  imemAxi.io.wready := io.imemWready
  io.imemWuser := imemAxi.io.wuser
  io.imemWlast := imemAxi.io.wlast
  io.imemBready := imemAxi.io.bready
  imemAxi.io.bvalid := io.imemBvalid
  imemAxi.io.bresp := io.imemBresp
  imemAxi.io.bid := io.imemBid
  imemAxi.io.buser := io.imemBuser

  //-------------------------------------------------------------------------------
  // Data memory AXI bridge
  //-------------------------------------------------------------------------------
  val dmemAxi = Module(new MemAxi)
  dmemAxi.reset := resetOut

  dmemAxi.io.axiReinit := axiReinit

  // Core Interface
  val axiDmemIdle = dmemAxi.io.coreIdle
  axiDmemReqAck := dmemAxi.io.coreReqAck
  dmemAxi.io.coreReq    := axiDmemReq
  dmemAxi.io.coreCmd    := axiDmemCmd
  dmemAxi.io.coreWidth  := MemWidth.word
  dmemAxi.io.coreAddr   := axiDmemAddr
  dmemAxi.io.coreWdata  := 0.U
  axiDmemRdata := dmemAxi.io.coreRdata
  axiDmemResp := dmemAxi.io.coreResp

  io.dmemArid     := dmemAxi.io.arid
  io.dmemAraddr   := dmemAxi.io.araddr
  io.dmemArlen    := dmemAxi.io.arlen
  io.dmemArsize   := dmemAxi.io.arsize
  io.dmemArburst  := dmemAxi.io.arburst
  io.dmemArlock   := dmemAxi.io.arlock
  io.dmemArcache  := dmemAxi.io.arcache
  io.dmemArprot   := dmemAxi.io.arprot
  io.dmemArregion := dmemAxi.io.arregion
  io.dmemAruser   := dmemAxi.io.aruser
  io.dmemArqos    := dmemAxi.io.arqos
  io.dmemArvalid  := dmemAxi.io.arvalid
  dmemAxi.io.arready := io.dmemArready
  dmemAxi.io.rdata := io.dmemRdata
  dmemAxi.io.rid := io.dmemRid
  dmemAxi.io.rresp := io.dmemRResp
  dmemAxi.io.rlast := io.dmemRlast
  dmemAxi.io.ruser := io.dmemRuser
  dmemAxi.io.rvalid := io.dmemRvalid
  io.dmemRready := dmemAxi.io.rready

  io.dmemAwid     := dmemAxi.io.awid
  io.dmemAwaddr   := dmemAxi.io.awaddr
  io.dmemAwlen    := dmemAxi.io.awlen
  io.dmemAwsize   := dmemAxi.io.awsize
  io.dmemAwburst  := dmemAxi.io.awburst
  io.dmemAwlock   := dmemAxi.io.awlock
  io.dmemAwcache  := dmemAxi.io.awcache
  io.dmemAwprot   := dmemAxi.io.awprot
  io.dmemAwregion := dmemAxi.io.awregion
  io.dmemAwuser   := dmemAxi.io.awuser
  io.dmemAwqos    := dmemAxi.io.awqos
  io.dmemAwvalid  := dmemAxi.io.awvalid
  dmemAxi.io.awready := io.dmemAwready
  io.dmemWdata := dmemAxi.io.wdata
  io.dmemWstrb := dmemAxi.io.wstrb
  io.dmemWuser := dmemAxi.io.wuser
  io.dmemWlast := dmemAxi.io.wlast
  io.dmemWvalid := dmemAxi.io.wvalid
  dmemAxi.io.wready := io.dmemWready
  io.dmemWuser := dmemAxi.io.wuser
  io.dmemWlast := dmemAxi.io.wlast
  io.dmemBready := dmemAxi.io.bready
  dmemAxi.io.bvalid := io.dmemBvalid
  dmemAxi.io.bvalid := io.dmemBvalid
  dmemAxi.io.bresp := io.dmemBresp
  dmemAxi.io.bid := io.dmemBid
  dmemAxi.io.buser := io.dmemBuser

  //-------------------------------------------------------------------------------
  // AXI reinit logic
  //-------------------------------------------------------------------------------
  when (axiImemIdle && axiDmemIdle) {
    axiReinit := true.B
  }
}
