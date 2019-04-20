// See LICENSE for license details.

package scr1.core.primitives

import chisel3._
import chisel3.core.withReset

class ResetBufCell extends Module {
  val io = IO(new Bundle {
    val testMode = Input(Bool())
    val testRst = Input(Bool())
    val resetIn = Input(Bool())
    val resetOut = Output(Bool())
    val resetStatus = Output(Bool())
  })

  val resetFF = RegInit(false.B)
  val resetStatusFF = RegInit(false.B)
  val rstMux = Mux(io.testMode, io.testRst, reset)

  withReset(rstMux) {
    resetFF := io.resetIn
    resetStatusFF := io.resetIn
  }

  io.resetOut := Mux(io.testMode, io.testRst, resetFF)
  io.resetStatus := Mux(io.testMode, io.testRst, resetStatusFF)
}

class ResetSyncCell extends Module {
  val io = IO(new Bundle {
    val testRst = Input(Bool())
    val testMode = Input(Bool())
    val rstOut = Output(Bool())
  })

  val rstDff = RegInit(VecInit(Seq.fill(2)(true.B)))
  val localRstIn = Mux(io.testMode, io.testRst, reset)

  withReset(localRstIn) {
    rstDff(0) := 1.U
    rstDff(1) := rstDff(0)
  }

  io.rstOut := Mux(io.testMode, io.testRst, rstDff(1))
}

class ResetBufQlfyCell extends Module {
  val io = IO(new Bundle{
    val testRst = Input(Bool())
    val testMode = Input(Bool())
    val resetIn = Input(Bool())
    val resetOutQlfy = Output(Bool())
    val resetOut = Output(Bool())
    val resetStatus = Output(Bool())
  })

  // Front async stage
  val resetInMux = Mux(io.testMode, io.testRst, reset.toBool() && io.resetIn)
  val resetFrontFF = RegInit(true.B)
  val resetVictimFF = RegInit(true.B)
  val resetQualifierFF = RegInit(true.B)
  val resetLuckyFF = RegInit(true.B)
  val resetStatusFF = RegInit(true.B)

  withReset(resetInMux) {
    resetFrontFF := false.B
  }

  // Core sync stages
  val rstMux = Mux(io.testMode, io.testRst, reset)

  withReset(rstMux) {
    resetVictimFF := resetFrontFF
    resetQualifierFF := resetVictimFF
    resetLuckyFF := resetQualifierFF

    // Reset status stage
    resetStatusFF := resetQualifierFF
  }

  io.resetOutQlfy := resetQualifierFF
  io.resetOut := Mux(io.testMode, io.testRst, resetLuckyFF)
}

class ResetAnd2Cell extends Module {
  val io = IO(new Bundle {
    val rstIn = Input(UInt(2.W))
    val testRst = Input(Bool())
    val testMode = Input(Bool())
    val rstOut = Output(Bool())
  })

  val rstOut = Mux(io.testMode, io.testRst, io.rstIn.andR)
}

class ResetAnd3Cell extends Module {
  val io = IO(new Bundle {
    val rstIn = Input(UInt(3.W))
    val testRst = Input(Bool())
    val testMode = Input(Bool())
    val rstOut = Output(Bool())
  })

  val rstOut = Mux(io.testMode, io.testRst, io.rstIn.andR)
}

class ResetMux2Cell extends Module {
  val io = IO(new Bundle {
    val rstIn = Input(UInt(2.W))
    val select = Input(Bool())
    val testRst = Input(Bool())
    val testMode = Input(Bool())
    val rstOut = Output(Bool())
  })

  io.rstOut := Mux(io.testMode, io.testRst, io.rstIn(io.select))
}
