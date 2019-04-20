/// Copyright by Syntacore LLC © 2016-2018. See LICENSE for details
/// @file       <scr1_memory_tb_axi.sv>
/// @brief      AXI memory testbench
///

//`include "scr1_ipic.svh"

module Scr1MemoryTbAxiWrapper
    #(
      parameter SIZE   = 1*1024*1024,
      parameter W_ID   = 4,
      parameter W_ADR  = 32,
      parameter W_DATA = 32,
      parameter SCR1_IRQ_LINES_NUM = 1,
      parameter HEX_BIN_FILE = ""
      )
    (
     // System
     input logic  rst_n,
     input logic  clk,
     output logic [SCR1_IRQ_LINES_NUM-1:0] irq_lines,

     // Instruction Memory
     logic [3:0]  awid_0,
     logic [31:0] awaddr_0,
     logic [7:0]  awlen_0,
     logic [2:0]  awsize_0,
     logic [1:0]  awburst_0,
     logic        awlock_0,
     logic [3:0]  awcache_0,
     logic [2:0]  awprot_0,
     logic [3:0]  awregion_0,
     logic [3:0]  awuser_0,
     logic [3:0]  awqos_0,
     logic        awvalid_0,
     logic        awready_0,
     logic [31:0] wdata_0,
     logic [3:0]  wstrb_0,
     logic        wlast_0,
     logic [3:0]  wuser_0,
     logic        wvalid_0,
     logic        wready_0,
     logic [3:0]  bid_0,
     logic [1:0]  bresp_0,
     logic        bvalid_0,
     logic [3:0]  buser_0,
     logic        bready_0,
     logic [3:0]  arid_0,
     logic [31:0] araddr_0,
     logic [7:0]  arlen_0,
     logic [2:0]  arsize_0,
     logic [1:0]  arburst_0,
     logic        arlock_0,
     logic [3:0]  arcache_0,
     logic [2:0]  arprot_0,
     logic [3:0]  arregion_0,
     logic [3:0]  aruser_0,
     logic [3:0]  arqos_0,
     logic        arvalid_0,
     logic        arready_0,
     logic [3:0]  rid_0,
     logic [31:0] rdata_0,
     logic [1:0]  rresp_0,
     logic        rlast_0,
     logic [3:0]  ruser_0,
     logic        rvalid_0,
     logic        rready_0,

     // Data Memory
     logic [3:0]  awid_1,
     logic [31:0] awaddr_1,
     logic [7:0]  awlen_1,
     logic [2:0]  awsize_1,
     logic [1:0]  awburst_1,
     logic        awlock_1,
     logic [3:0]  awcache_1,
     logic [2:0]  awprot_1,
     logic [3:0]  awregion_1,
     logic [3:0]  awuser_1,
     logic [3:0]  awqos_1,
     logic        awvalid_1,
     logic        awready_1,
     logic [31:0] wdata_1,
     logic [3:0]  wstrb_1,
     logic        wlast_1,
     logic [3:0]  wuser_1,
     logic        wvalid_1,
     logic        wready_1,
     logic [3:0]  bid_1,
     logic [1:0]  bresp_1,
     logic        bvalid_1,
     logic [3:0]  buser_1,
     logic        bready_1,
     logic [3:0]  arid_1,
     logic [31:0] araddr_1,
     logic [7:0]  arlen_1,
     logic [2:0]  arsize_1,
     logic [1:0]  arburst_1,
     logic        arlock_1,
     logic [3:0]  arcache_1,
     logic [2:0]  arprot_1,
     logic [3:0]  arregion_1,
     logic [3:0]  aruser_1,
     logic [3:0]  arqos_1,
     logic        arvalid_1,
     logic        arready_1,
     logic [3:0]  rid_1,
     logic [31:0] rdata_1,
     logic [1:0]  rresp_1,
     logic        rlast_1,
     logic [3:0]  ruser_1,
     logic        rvalid_1,
     logic        rready_1
     );

    scr1_memory_tb_axi
        #(
          .SIZE               (SIZE               ),
          .N_IF               (2                  ),
          .W_ADR              (W_ADR              ),
          .W_DATA             (W_DATA             ),
          .SCR1_IRQ_LINES_NUM (SCR1_IRQ_LINES_NUM ),
          .HEX_BIN_FILE       (HEX_BIN_FILE       )
          )
    i_memory_tb
        (
         // Common
         .rst_n     (rst_n                   ),
         .clk       (clk                     ),

         .irq_lines (irq_lines               ),

         // Write address channel
         .awid      ( {awid_0,    awid_1}    ),
         .awaddr    ( {awaddr_0,  awaddr_1}  ),
         .awsize    ( {awsize_0,  awsize_1}  ),
         .awlen     ( {awlen_0,   awlen_1}   ),
         .awvalid   ( {awvalid_0, awvalid_1} ),
         .awready   ( {awready_0, awready_1} ),

         // Write data channel
         .wdata     ( {wdata_0,   wdata_1}   ),
         .wstrb     ( {wstrb_0,   wstrb_1}   ),
         .wvalid    ( {wvalid_0,  wvalid_1}  ),
         .wlast     ( {wlast_0,   wlast_1}   ),
         .wready    ( {wready_0,  wready_1}  ),

         // Write response channel
         .bready    ( {bready_0,  bready_1}  ),
         .bvalid    ( {bvalid_0,  bvalid_1}  ),
         .bid       ( {bid_0,     bid_1}     ),
         .bresp     ( {bresp_0,   bresp_1}   ),

         // Read address channel
         .arid      ( {arid_0,    arid_1}    ),
         .araddr    ( {araddr_0,  araddr_1}  ),
         .arburst   ( {arburst_0, arburst_1} ),
         .arsize    ( {arsize_0,  arsize_1}  ),
         .arlen     ( {arlen_0,   arlen_1}   ),
         .arvalid   ( {arvalid_0, arvalid_1} ),
         .arready   ( {arready_0, arready_1} ),

         // Read data channel
         .rvalid    ( {rvalid_0,  rvalid_1}  ),
         .rready    ( {rready_0,  rready_1}  ),
         .rid       ( {rid_0,     rid_1}     ),
         .rdata     ( {rdata_0,   rdata_1}   ),
         .rlast     ( {rlast_0,   rlast_1}   ),
         .rresp     ( {rresp_0,   rresp_1}   )
         );


endmodule : Scr1MemoryTbAxiWrapper


module scr1_memory_tb_axi
    #(
      parameter SIZE               = 1*1024*1024,
      parameter N_IF               = 2,
      parameter W_ID               = 4,
      parameter W_ADR              = 32,
      parameter W_DATA             = 32,
      parameter SCR1_IRQ_LINES_NUM = 1,
      parameter HEX_BIN_FILE       = ""
      )
    (
    // System
     input   logic                          rst_n,
     input   logic                          clk,
//`ifdef SCR1_IPIC_EN
     output  logic [SCR1_IRQ_LINES_NUM-1:0] irq_lines,
//`endif // SCR1_IPIC_EN

     // Write address channel
     input  logic [N_IF-1:0]                awvalid,
     input  logic [N_IF-1:0] [W_ID-1:0]     awid,
     input  logic [N_IF-1:0] [W_ADR-1:0]    awaddr,
     input  logic [N_IF-1:0] [2:0]          awsize,
     input  logic [N_IF-1:0] [7:0]          awlen,
     output logic [N_IF-1:0]                awready,

     // Write data channel
     input  logic [N_IF-1:0]                wvalid,
     input  logic [N_IF-1:0] [W_DATA-1:0]   wdata,
     input  logic [N_IF-1:0] [W_DATA/8-1:0] wstrb,
     input  logic [N_IF-1:0]                wlast,
     output logic [N_IF-1:0]                wready,

     // Write response channel
     input  logic [N_IF-1:0]                bready,
     output logic [N_IF-1:0]                bvalid,
     output logic [N_IF-1:0] [W_ID-1:0]     bid,
     output logic [N_IF-1:0] [1:0]          bresp,

     // Read address channel
     input  logic [N_IF-1:0]                arvalid,
     input  logic [N_IF-1:0] [W_ID-1:0]     arid,
     input  logic [N_IF-1:0] [W_ADR-1:0]    araddr,
     input  logic [N_IF-1:0] [1:0]          arburst,
     input  logic [N_IF-1:0] [2:0]          arsize,
     input  logic [N_IF-1:0] [7:0]          arlen,
     output logic [N_IF-1:0]                arready,

     // Read data channel
     input  logic [N_IF-1:0]                rready,
     output logic [N_IF-1:0]                rvalid,
     output logic [N_IF-1:0] [W_ID-1:0]     rid,
     output logic [N_IF-1:0] [W_DATA-1:0]   rdata,
     output logic [N_IF-1:0]                rlast,
     output logic [N_IF-1:0] [1:0]          rresp
     );

    //-------------------------------------------------------------------------------
    // Local parameters
    //-------------------------------------------------------------------------------
    localparam [W_ADR-1:0]                      PRINT_ADDR     = 32'hF000_0000;
    localparam [W_ADR-1:0]                      IRQ_ADDR       = 32'hF000_0100;

    //-------------------------------------------------------------------------------
    // Local signal declaration
    //-------------------------------------------------------------------------------
    logic  [7:0]                                memory [0:SIZE-1];
    logic  [N_IF-1:0] [W_ADR-1:0]               awaddr_hold;
    logic  [N_IF-1:0] [2:0]                     awsize_hold;
    genvar                                      gi;
    genvar                                      gj;

    `ifdef VERILATOR
    logic [255:0]                               test_file;
    `else // VERILATOR
    string                                      test_file;
    `endif // VERILATOR
    bit                                         test_file_init;

    //-------------------------------------------------------------------------------
    // Local functions
    //-------------------------------------------------------------------------------

    function automatic logic [W_DATA-1:0] mem_read (
        logic [W_ADR:0] adr,
        int             bytes_num,
        int             bytes_max
        );

        logic [W_ADR:0] byte_lane;

        mem_read  = 'x;
        byte_lane = 0;

        for(int i=0; i<$clog2(bytes_max); ++i) begin
            byte_lane[i] = adr[i];
        end

        for(int i=byte_lane; i<bytes_max & bytes_num!=0; ++i) begin
    `ifdef SCR1_IPIC_EN
            if (adr[W_ADR-1:1]==IRQ_ADDR[W_ADR-1:1]) begin
                if( i*8 < SCR1_IRQ_LINES_NUM ) begin
                    if( SCR1_IRQ_LINES_NUM < 8 ) begin
                        mem_read[(i*8)+:8] = irq_lines;
                    end else begin
                        mem_read[(i*8)+:8] = irq_lines[(i*8)+:8];
                    end
                end
            end else begin
                mem_read[(i*8)+:8] = memory[adr];
            end
    `else // SCR1_IPIC_EN
            mem_read[(i*8)+:8] = memory[adr];
    `endif // SCR1_IPIC_EN
            adr = adr+1'b1;
            bytes_num = bytes_num - 1'b1;
        end
    endfunction : mem_read

    function automatic void mem_write (
        logic [W_ADR-1:0]      adr,
        logic [W_DATA-1:0]     data,
        logic [(W_DATA/8)-1:0] bytes_en,
        int                    bytes_num,
        int                    bytes_max
        );

        logic[W_ADR:0]         byte_lane;

        byte_lane = 0;

        for(int i=0; i<$clog2(bytes_max); ++i) begin
            byte_lane[i] = adr[i];
        end

        for(int i=byte_lane; i<bytes_max & bytes_num!=0; ++i) begin
            if(bytes_en[i] & adr==PRINT_ADDR) begin
                $write("%c",data[(i*8)+:8]);
    `ifdef SCR1_IPIC_EN
            end else if(bytes_en[i] & adr[W_ADR-1:1]==IRQ_ADDR[W_ADR-1:1]) begin
                if( i*8 < SCR1_IRQ_LINES_NUM ) begin
                    if( SCR1_IRQ_LINES_NUM < 8 ) begin
                        irq_lines = data[SCR1_IRQ_LINES_NUM-1:0];
                    end else begin
                        irq_lines[(i*8)+:8] = data[(i*8)+:8];
                    end
                end
    `endif // SCR1_IPIC_EN
            end else if(bytes_en[i]) begin
                memory[adr] = data[(i*8)+:8];
            end
            adr       = adr+1'b1;
            bytes_num = bytes_num-1'b1;
        end
    endfunction : mem_write

    generate for(gi=0; gi<N_IF; ++gi) begin : rw_if

    //-------------------------------------------------------------------------------
    // Read operation
    //-------------------------------------------------------------------------------
    always @(posedge clk, negedge rst_n) begin
        if(~rst_n) begin
            arready[gi] <= 1'b1;
            rvalid[gi]  <= 1'b0;
            rresp[gi]   <= 2'd3;
            rdata[gi]   <= 'x;
            rlast[gi]   <= 1'b0;
            rid[gi]     <= '0;
        end else begin

            // Read data: acked
            if( rvalid[gi] & rready[gi] ) begin
                arready[gi] <= 1'b1;
                rvalid[gi]  <= 1'b0;
            end else if( rvalid[gi] & !rready[gi] ) begin
                arready[gi] <= 1'b0;
            end

            // Read data: valid
            if( arvalid[gi] & arready[gi] & ~(rvalid[gi] & !rready[gi]) ) begin

                rvalid[gi] <= 1'b1;
                rresp[gi]  <= '0;
                rlast[gi]  <= 1'b1;
                rid[gi]    <= arid[gi];

                rdata[gi]  <= mem_read( araddr[gi],
                                        2**arsize[gi],
                                        W_DATA/8 );
            end
        end
    end

    //-------------------------------------------------------------------------------
    // Write operation
    //-------------------------------------------------------------------------------
    always @(posedge clk, negedge rst_n) begin
        if(~rst_n) begin
            bvalid[gi]  <= '0;
            bresp[gi]   <= 2'd3;
            awready[gi] <= 1'b1;
            wready[gi]  <= 1'b1;
            if (test_file_init) $readmemh(HEX_BIN_FILE, memory);
        end else begin

            // Write data: response
            if( bvalid[gi] & bready[gi] ) begin
                bvalid[gi]  <= 1'b0;
                awready[gi] <= 1'b1;
                wready[gi]  <= 1'b1;
            end else if( bvalid[gi] & !bready[gi] ) begin
                awready[gi] <= 1'b0;
                wready[gi]  <= 1'b0;
            end

            // Write data: get address
            if( awvalid[gi] & awready[gi] & ~(bvalid[gi] & !bready[gi]) ) begin
                bid <= awid[gi];
                if( ~wvalid[gi] ) begin
                    awaddr_hold[gi] <= awaddr[gi];
                    awsize_hold[gi] <= awsize[gi];
                    awready[gi] <= 1'b0;
                end
            end

            // Write data: get data
            if( wvalid[gi] & wready[gi] & wlast[gi] ) begin
                bvalid[gi] <= 1'b1;
                bresp[gi]  <= '0;

                mem_write( awready[gi] ? awaddr[gi] : awaddr_hold[gi],
                           wdata[gi],
                           wstrb[gi],
                           2**(awready[gi] ? awsize[gi] : awsize_hold[gi]),
                           W_DATA/8 );
            end
        end
    end

    `ifndef VERILATOR
    //-------------------------------------------------------------------------------
    // Assertions
    //-------------------------------------------------------------------------------
    SVA_TBMEM_AWADDR_404 :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            awvalid[gi] |-> awaddr[gi]<SIZE | awaddr[gi]==PRINT_ADDR |
            awaddr[gi]==IRQ_ADDR
        )
        else $error("TBMEM: awaddr[%0d] >= SIZE",gi);

    SVA_TBMEM_X_AWVALID :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            !$isunknown(awvalid[gi])
        )
        else $error("TBMEM: X state on awvalid[%0d]",gi);

    SVA_TBMEM_X_AWCHANNEL :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            awvalid[gi] |-> !$isunknown({awid[gi],awaddr[gi],awsize[gi],awlen[gi]})
        )
        else $error("TBMEM: X state on aw channel[%0d]",gi);

    SVA_TBMEM_AWLEN :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            awvalid[gi] |-> awlen[gi]==0
        )
        else $error("TBMEM: awlen[%0d] = %0d is not supported",gi,awlen[gi]);

    SVA_TBMEM_X_WVALID :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            !$isunknown(wvalid[gi])
        )
        else $error("TBMEM: X state on wvalid[%0d]",gi);

    SVA_TBMEM_X_WCHANNEL :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            wvalid[gi] |-> !$isunknown({wstrb[gi],wlast[gi]})
        )
        else $error("TBMEM: X state on w channel[%0d]",gi);

    for(gj=0; gj<W_DATA/8; ++gj) begin : SVA_TBMEM_X_WSTRB
    WDATA :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            (wvalid[gi] & wstrb[gi][gj]) |-> !$isunknown(wdata[gi][(gj*8)+:8])
        )
        else $error("TBMEM: X state on wdata with wstrb[%0d][%0d]",gi,gj);
    end

    SVA_TBMEM_X_BREADY :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            bvalid[gi] |-> !$isunknown(bready[gi])
        )
        else $error("TBMEM: X state on bready[%0d]",gi);

    SVA_TBMEM_ARADDR_404 :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            arvalid[gi] |-> araddr[gi]<SIZE | araddr[gi]==PRINT_ADDR |
            awaddr[gi]==IRQ_ADDR
        )
        else $error("TBMEM: awaddr[%0d] >= SIZE",gi);

    SVA_TBMEM_X_ARVALID :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            !$isunknown(arvalid[gi])
        )
        else $error("TBMEM: X state on arvalid[%0d]",gi);

    SVA_TBMEM_X_ARCHANNEL :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            arvalid[gi] |-> !$isunknown({arid[gi],araddr[gi],arsize[gi],arlen[gi]})
        )
        else $error("TBMEM: X state on ar channel[%0d]",gi);

    SVA_TBMEM_ARLEN :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            arvalid[gi] |-> arlen[gi]==0
        )
        else $error("TBMEM: arlen[%0d] = %0d is not supported",gi,arlen[gi]);

    SVA_TBMEM_X_RREADY :
        assert property (
            @(negedge clk) disable iff (~rst_n)
            rvalid[gi] |-> !$isunknown(rready[gi])
        )
        else $error("TBMEM: X state on rready[%0d]",gi);

    `endif // VERILATOR

    end endgenerate

endmodule : scr1_memory_tb_axi
