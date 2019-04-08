# SCR1 in Chisel

## What is this ?
This is a folk project for porting SCR1 code. Original SCR1 code is written in System Verilog.
And target language is Chisel3.

 - Chisel3 : https://github.com/freechipsproject/chisel3

## Porting Policy
To compare with System Verilog and Chisel, I'll have 2-stages to convert it.

- 1st stage : Correspond all System Verilog code and Chisel code at one by one.
  - This stage is simply porting System Verilog to Chisel3.
  - The purpose is follows:
    - How to convert System Verilog to chisel3.
    - Can find out a reason easily, when I face a bug.
    - To compare the amount of code between System Verilog and some restricted Chisel3.
  - 1st stage's target is SCR1 with AXI4 I/F.
  - I have some restriction for Chisel3:
    - Don't use `Bundle` to aggregate I/O
    - Don't use `Seq` or other Scala Collection class.
- 2nd stage : Use all Scala and Chisel grammar to get a good quality.
  - "Good quality" means:
    - Less code quantity
    - Maintain code easily
    - More configurable
  - This stage is optimizing stage for Chisel3.
  - No restriction to design Chisel-SCR1
    - I can use all Scala and Chisel3 functions

Both 1st stage and 2nd stage, whole variable name is named by Camel-case to follow Scala Stayles Guide.

## Getting Started
T.B.D

### Prerequisites
- sbt 1.2.1

###  
