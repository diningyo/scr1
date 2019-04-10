// See README.md for license details.

import java.io.File

import scala.collection.mutable.Map
import org.scalatest.{BeforeAndAfterAllConfigMap, ConfigMap}
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scr1.SCR1Config


class Scr1UnitTester(c: SimDtm, testBin: String) extends PeekPokeTester(c) {

  private val dtm = c

  println(s"test name - $testBin")

  fail
}

/*
add.S      \
addi.S         \
and.S          \
andi.S         \
auipc.S        \
isa/rv32ui/beq.S          \
isa/rv32ui/bge.S          \
isa/rv32ui/bgeu.S         \
isa/rv32ui/blt.S          \
isa/rv32ui/bltu.S         \
isa/rv32ui/bne.S          \
isa/rv32mi/csr.S          \
isa/rv32um/div.S          \
isa/rv32um/divu.S         \
isa/rv32ui/fence_i.S      \
isa/rv32mi/illegal.S      \
isa/rv32ui/jal.S          \
isa/rv32ui/jalr.S         \
isa/rv32ui/lb.S           \
isa/rv32ui/lbu.S          \
isa/rv32ui/lh.S           \
isa/rv32ui/lhu.S          \
isa/rv32ui/lui.S          \
isa/rv32ui/lw.S           \
isa/rv32mi/ma_addr.S      \
isa/rv32mi/ma_fetch.S     \
isa/rv32mi/mcsr.S         \
isa/rv32um/mul.S          \
isa/rv32um/mulh.S         \
isa/rv32um/mulhsu.S       \
isa/rv32um/mulhu.S        \
isa/rv32ui/or.S           \
isa/rv32ui/ori.S          \
isa/rv32um/rem.S          \
isa/rv32um/remu.S         \
isa/rv32uc/rvc.S          \
isa/rv32ui/sb.S           \
isa/rv32mi/sbreak.S       \
isa/rv32mi/scall.S        \
isa/rv32ui/sh.S           \
isa/rv32mi/shamt.S        \
isa/rv32ui/simple.S       \
isa/rv32ui/sll.S          \
isa/rv32ui/slli.S         \
isa/rv32ui/slt.S          \
isa/rv32ui/slti.S         \
isa/rv32ui/sltiu.S        \
isa/rv32ui/sltu.S         \
isa/rv32ui/sra.S          \
isa/rv32ui/srai.S         \
isa/rv32ui/srl.S          \
isa/rv32ui/srli.S         \
isa/rv32ui/sub.S          \
isa/rv32ui/sw.S           \
isa/rv32ui/xor.S          \
isa/rv32ui/xori.S
*/

/**
  *
  */
class Scr1Tester extends ChiselFlatSpec with BeforeAndAfterAllConfigMap {

  behavior of "Scr1"

  // Driver.execute's default argument Map
  val args = Map[String, Any](
    "--backend-name" -> "verilator",
    "--generate-vcd-output" -> "off"/*,
    "--is-gen-harness" -> None,
    "--is-gen-verilog" -> None,
    "--is-compiling" -> None*/
  )

  override def beforeAll(configMap: ConfigMap) = {
    if (configMap.get("--backend-name").isDefined) {
      args("--backend-name") = configMap.get("--backend-name").fold("")(_.toString)
    }
  }

  def getArgs(): Array[String] = {
    args.map {
      case (key: String, value: String) => s"$key=$value"
      case (key: String, value: Boolean) => if (value) key else ""
    }.toArray
  }

  val riscvTestsList = List("add", "ldi")

  for (instruction <- riscvTestsList) {
    it should s"pass a one of riscv-tests - $instruction" in {
      implicit val cfgs = SCR1Config()
      Driver.execute(getArgs(), () => new SimDtm) {
        c => new Scr1UnitTester(c, "") {

        }
      } should be (true)
    }
  }
}