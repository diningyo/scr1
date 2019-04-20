// See LICENSE for license details.

import scala.util.control.Breaks
import org.scalatest.{BeforeAndAfterAllConfigMap, ConfigMap}
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}

import scr1._

/**
  * Test environment for Scr1
  * @param c testbench object which has Scr1 and memory model.
  */
class Scr1UnitTester(c: SimDtm) extends PeekPokeTester(c) {

  val timeoutCycle = 1000
  val b = Breaks

  b.breakable {
    reset()
    step(1)

    poke(c.io.halt, true)
    step(10)
    poke(c.io.halt, false)

    for (_ <- 0 until timeoutCycle) {
      if (peek(c.io.success) == 0x1) {
        println("c.io.success becomes high. test is success")
        b.break
      }
      step(1)
    }
  }

  expect(c.io.success, true)
  step(5)
}

/**
  * Object for riscv-tests's parameters
  */
object RiscvTestsParams {

  /**
    * riscv-tests/isa/rv32ui
    */
  val rv32uiTestList: Seq[((String, String), Int)] = Map(
    "add"        -> "rv32ui-p-add.hex",
    "addi"       -> "rv32ui-p-addi.hex",
    "and"        -> "rv32ui-p-and.hex",
    "andi"       -> "rv32ui-p-andi.hex",
    "auipc"      -> "rv32ui-p-auipc.hex",
    "beq"        -> "rv32ui-p-beq.hex",
    "bge"        -> "rv32ui-p-bge.hex",
    "bgeu"       -> "rv32ui-p-bgeu.hex",
    "blt"        -> "rv32ui-p-blt.hex",
    "bltu"       -> "rv32ui-p-bltu.hex",
    "bne"        -> "rv32ui-p-bne.hex",
    "fence_i"    -> "rv32ui-p-fence_i.hex",
    "jal"        -> "rv32ui-p-jal.hex",
    "jalr"       -> "rv32ui-p-jalr.hex",
    "lb"         -> "rv32ui-p-lb.hex",
    "lbu"        -> "rv32ui-p-lbu.hex",
    "lh"         -> "rv32ui-p-lh.hex",
    "lhu"        -> "rv32ui-p-lhu.hex",
    "lui"        -> "rv32ui-p-lui.hex",
    "lw"         -> "rv32ui-p-lw.hex",
    "or"         -> "rv32ui-p-or.hex",
    "ori"        -> "rv32ui-p-ori.hex",
    "sb"         -> "rv32ui-p-sb.hex",
    "sh"         -> "rv32ui-p-sh.hex",
    "simple"     -> "rv32ui-p-simple.hex",
    "sll"        -> "rv32ui-p-sll.hex",
    "slli"       -> "rv32ui-p-slli.hex",
    "slt"        -> "rv32ui-p-slt.hex",
    "slti"       -> "rv32ui-p-slti.hex",
    "sltiu"      -> "rv32ui-p-sltiu.hex",
    "sltu"       -> "rv32ui-p-sltu.hex",
    "sra"        -> "rv32ui-p-sra.hex",
    "srai"       -> "rv32ui-p-srai.hex",
    "srl"        -> "rv32ui-p-srl.hex",
    "srli"       -> "rv32ui-p-srli.hex",
    "sub"        -> "rv32ui-p-sub.hex",
    "sw"         -> "rv32ui-p-sw.hex",
    "xor"        -> "rv32ui-p-xor.hex",
    "xori"       -> "rv32ui-p-xori.hex"
  ).toSeq.sortBy(_._1).zipWithIndex

  /**
    * riscv-tests/isa/rv32mi
    */
  val rv32miTestList: Seq[((String, String), Int)] = Map(
    "breakpoint" -> "rv32mi-p-breakpoint.hex",
    "csr"        -> "rv32mi-p-csr.hex",
    "illegal"    -> "rv32mi-p-illegal.hex",
    "ma_addr"    -> "rv32mi-p-ma_addr.hex",
    "ma_fetch"   -> "rv32mi-p-ma_fetch.hex",
    "mcsr"       -> "rv32mi-p-mcsr.hex",
    "sbreak"     -> "rv32mi-p-sbreak.hex",
    "scall"      -> "rv32mi-p-scall.hex",
    "shamt"      -> "rv32mi-p-shamt.hex"
  ).toSeq.sortBy(_._1).zipWithIndex

  /**
    * riscv-tests/isa/rv32uc
    */
  val rv32ucTestList: Seq[((String, String), Int)] = Map(
    "rvc"        -> "rv32uc-p-rvc.hex"
  ).toSeq.sortBy(_._1).zipWithIndex

  /**
    * riscv-tests/isa/rv32um
    */
  val rv32umTestList: Seq[((String, String), Int)] = Map(
    "div"        -> "rv32um-p-div.hex",
    "divu"       -> "rv32um-p-divu.hex",
    "mul"        -> "rv32um-p-mul.hex",
    "mulh"       -> "rv32um-p-mulh.hex",
    "mulhsu"     -> "rv32um-p-mulhsu.hex",
    "mulhu"      -> "rv32um-p-mulhu.hex",
    "rem"        -> "rv32um-p-rem.hex",
    "remu"       -> "rv32um-p-remu.hex"
  ).toSeq.sortBy(_._1).zipWithIndex
}

/**
  * riscv-tests test environment
  */
abstract class Scr1BaseTester extends ChiselFlatSpec with BeforeAndAfterAllConfigMap  {

  val defaultArgs = scala.collection.mutable.Map(
    "--generate-vcd-output" -> "off",
    "--backend-name" -> "verilator",
    "--is-verbose" -> false
  )

  override def beforeAll(configMap: ConfigMap): Unit = {
    if (configMap.get("--backend-name").isDefined) {
      defaultArgs("--backend-name") = configMap.get("--backend-name").fold("")(_.toString)
    }
    if (configMap.get("--generate-vcd-output").isDefined) {
      defaultArgs("--generate-vcd-output") = configMap.get("--generate-vcd-output").fold("")(_.toString)
    }
    if (configMap.get("--is-verbose").isDefined) {
      defaultArgs("--is-verbose") = true
    }
  }

  def getArgs(optArgs: Map[String, Any]): Array[String] = {
    val argsMap = defaultArgs ++ optArgs
    argsMap.map {
      case (key: String, value: String) => s"$key=$value"
      case (key: String, value: Boolean) => if (value) key else ""
    }.toArray
  }

  def dutName: String = "Scr1"

  def runRiscvTests(group: String, test: ((String, String), Int))
                   (implicit cfg: SCR1Config): Unit = {

    val ((instruction, testFile), seqNo) = test

    val testFilePath = isaTestDir + testFile

    it should f"support RISC-V instruction $instruction%-10s - [riscv-tests:$group%s-$seqNo%03d]" in {

      val args = getArgs(Map(
        "--top-name" -> instruction,
        "--target-dir" -> s"test_run_dir/isa/$instruction"
      ))

      Driver.execute(args, () => new SimDtm(testFilePath)) {
        c => new Scr1UnitTester(c)
      } should be (true)
    }
  }

  val isaTestDir = "src/test/resources/isa/"
}

/**
  * Test module for riscv-tests RV32I
  */
class Scr1RV32ITester extends Scr1BaseTester {

  behavior of dutName

  implicit val cfg: SCR1Config = SCR1Config()

  val testList = Map(
    "rv32ui" -> RiscvTestsParams.rv32uiTestList,
    "rv32mi" -> RiscvTestsParams.rv32miTestList
  )

  for ((subTestGroup, subTestList) <- testList; test <- subTestList) {
    runRiscvTests(subTestGroup, test)
  }
}
