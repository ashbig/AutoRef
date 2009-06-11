/*
 * Plate384ToSlideGalFileParser.java
 *
 * Created on March 2, 2007, 4:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.Block;
import transfer.ProgrammappingTO;
import core.Well;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import transfer.ProgramcontainerTO;
import util.Constants;

/**
 *
 * @author DZuo
 */
public class Plate384ToSlideGalFileParser extends AbstractProgramMappingFileParser {
    public static final String EMPTY = "empty";
    
    /** Creates a new instance of Plate384ToSlideGalFileParser */
    public Plate384ToSlideGalFileParser() {
        super();
    }
    
    public String getSrccontainertype() {
        return ProgramcontainerTO.PLATE384;
    }
    
    public String getDestcontainertype() {
        return ProgramcontainerTO.SLIDE;
    }
    
   /** 
    public ContainerheadermapTO parseMappingFile(InputStream input, boolean isNumber) throws ProgramMappingFileParserException {
        ContainerheadermapTO slide = new ContainerheadermapTO("slide",ContainerheadermapTO.CAT_PROGRAM,null,false,new ContainertypeTO(ContainertypeTO.TYPE_SLIDE));
        
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            List<Block> blocks = new ArrayList<Block>();
            Set<Integer> scaleX = new TreeSet<Integer>();
            Set<Integer> scaleY = new TreeSet<Integer>();
            String line = null;
            boolean startMap = false;
            while((line = in.readLine()) != null && line.trim().length()>0) {
                if(line.startsWith("\"Block") && line.indexOf("=")!=-1) {
                    System.out.println("Reading blocks");
                    //start reading block positions
                    try {
                        StringTokenizer tokenizer = new StringTokenizer(line, "=");
                        String blocknumline = tokenizer.nextToken();
                        int blocknum = Integer.parseInt(blocknumline.substring(6));
                        
                        String blockline = tokenizer.nextToken();
                        tokenizer = new StringTokenizer(blockline, ",");
                        int blockposx = Integer.parseInt(tokenizer.nextToken().trim());
                        int blockposy = Integer.parseInt(tokenizer.nextToken().trim());
                        System.out.println("blocknum="+blocknum+"\t"+"blockx="+blockposx+"\t"+"blocky="+blockposy);
                        Block block = new Block(blocknum, blockposx, blockposy);
                        blocks.add(block);
                        scaleX.add(new Integer(blockposx));
                        scaleY.add(new Integer(blockposy));
                        System.out.println("Add block "+block.getNum());
                    } catch (Exception ex) {
                        throw new ProgramMappingFileParserException("Error parsing block information.\n"+ex.getMessage());
                    }
                }
                
                if(line.startsWith("\"Block\"")) {
                    startMap = true;
                    System.out.println("Converting blocks");
                    convertBlocks(blocks, scaleX, scaleY);
                    continue;
                }
                
                if(startMap) {
                    //start reading mapping
                    System.out.println("Reading mapping");
                    System.out.println(line);
                    try {
                        StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                        int blocknum = Integer.parseInt(tokenizer.nextToken().trim());
                        int blockrow = Integer.parseInt(tokenizer.nextToken().trim());
                        int blockcol = Integer.parseInt(tokenizer.nextToken().trim());
                        String platewell = tokenizer.nextToken().trim();
                        tokenizer = new StringTokenizer(platewell, "-");
                        //int srcplatenum = Integer.parseInt(tokenizer.nextToken().trim());
                        String srcplate = tokenizer.nextToken().trim();
                        String srcwell = tokenizer.nextToken().trim();
                        int srcposition = Well.convertWellToPos(srcwell, Plate96To384ProgramMappingFileParser.DESTCOLNUM);
                        Block b = findBlock(blocks, blocknum);
                        if(b == null) {
                            throw new ProgramMappingFileParserException("Cannot find block mapping for block number: "+blocknum);
                        }
                        
                        Well w = new Well(""+blockrow, ""+blockcol);
                        b.addWell(w);
                        
                        ContainercellmapTO src = new ContainercellmapTO(srcposition,srcwell.substring(0,1),srcwell.substring(1),null,null,null,null);
                        srcContainers.add(srcplate);
                        
                        SlidecellmapTO map = new SlidecellmapTO(0,null,null,null,null,slide,src,blocknum,b.getX(),b.getY(),b.getPosx(),b.getPosy(),blockrow,blockcol);
                        
                        slide.addContainercellmap(map);
                    } catch (Exception ex) {
                        throw new ProgramMappingFileParserException("Error parsing mapping information.\n"+ex.getMessage());
                    }
                }
            }
            in.close();
            
            int blockrownum = Block.getBlockRow(blocks);
            List<SlidecellmapTO> cells = (ArrayList)slide.getContainercellmapCollection();
            for(SlidecellmapTO m:cells) {
                Block b = findBlock(blocks, m.getBlocknum());
                m.calculateDestAbsolutePositions(b.getRowNumInBlock(),b.getColNumInBlock(),blockrownum);
            }
            
            //slide.setContainercellmapCollection(srccontainers);
        } catch (Exception ex) {
            throw new ProgramMappingFileParserException(ex.getMessage());
        }
        
        return slide;
    }
    */
  
    public List<ProgrammappingTO> parseMappingFile(InputStream input, boolean isNumber) throws ProgramMappingFileParserException {
        List<ProgrammappingTO> mappings = new ArrayList<ProgrammappingTO>();
   
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            List<Block> blocks = new ArrayList<Block>();
            Set<Integer> scaleX = new TreeSet<Integer>();
            Set<Integer> scaleY = new TreeSet<Integer>();
            String line = null;
            boolean startMap = false;
            while((line = in.readLine()) != null && line.trim().length()>0) {
                if(line.startsWith("\"Block") && line.indexOf("=")!=-1) {
                    //start reading block positions
                    try {
                        StringTokenizer tokenizer = new StringTokenizer(line, "=");
                        String blocknumline = tokenizer.nextToken();
                        int blocknum = Integer.parseInt(blocknumline.substring(6));
   
                        String blockline = tokenizer.nextToken();
                        tokenizer = new StringTokenizer(blockline, ",");
                        int blockposy = Integer.parseInt(tokenizer.nextToken().trim());
                        int blockposx = Integer.parseInt(tokenizer.nextToken().trim());
                        Block block = new Block(blocknum, blockposx, blockposy);
                        blocks.add(block);
                        scaleX.add(new Integer(blockposx));
                        scaleY.add(new Integer(blockposy));
                    } catch (Exception ex) {
                        throw new ProgramMappingFileParserException("Error parsing block information.\n"+ex.getMessage());
                    }
                }
   
                if(line.startsWith("\"Block\"")) {
                    startMap = true;
                    convertBlocks(blocks, scaleX, scaleY);
                    continue;
                }
   
                if(startMap) {
                    //start reading mapping
                    try {
                        StringTokenizer tokenizer = new StringTokenizer(line, "\t");
                        int blocknum = Integer.parseInt(tokenizer.nextToken().trim());
                        int blockrow = Integer.parseInt(tokenizer.nextToken().trim());
                        int blockcol = Integer.parseInt(tokenizer.nextToken().trim());
                        String platewell = tokenizer.nextToken().trim();
                        
                        String srcplate = Constants.NA;
                        String srcwell = Constants.NA;
                        int srcposition = 0;
                        
                        if(!EMPTY.equals(platewell)) {
                            tokenizer = new StringTokenizer(platewell, "-");
                            //int srcplatenum = Integer.parseInt(tokenizer.nextToken().trim());
                            srcplate = tokenizer.nextToken().trim();
                            srcwell = tokenizer.nextToken().trim();
                            srcposition = Well.convertWellToVPos(srcwell, Plate96To384ProgramMappingFileParser.DESTROWNUM);
                        }
                        Block b = findBlock(blocks, blocknum);
                        if(b == null) {
                            throw new ProgramMappingFileParserException("Cannot find block mapping for block number: "+blocknum);
                        }
   
                        Well w = new Well(""+blockrow, ""+blockcol);
                        b.addWell(w);
   
                        ProgrammappingTO p = new ProgrammappingTO();
                        String dlabel = "slide";
                        p.setDestplate(dlabel);
                        p.setDestpos(0);
                        p.setDestblockwellx(blockrow);
                        p.setDestblockwelly(blockcol);
                        p.setDestblockrow(b.getX());
                        p.setDestblockcol(b.getY());
                        p.setDestblocknum(blocknum);
                        p.setDestblockposx(b.getPosx());
                        p.setDestblockposy(b.getPosy());
                        p.setSrcplate(srcplate);
                        p.setSrcpos(srcposition);
                        if(srcwell.equals(Constants.NA)) {
                            p.setSrcwellx(Constants.NA);
                            p.setSrcwelly(Constants.NA);
                        } else {
                            p.setSrcwellx(srcwell.substring(0,1));
                            p.setSrcwelly(srcwell.substring(1));
                        }
                        mappings.add(p);
                        if(!Constants.NA.equals(srcplate)) 
                            addToContainers(getSrcContainers(), srcplate);
                        addToContainers(getDestContainers(), dlabel);
                    } catch (Exception ex) {
                        throw new ProgramMappingFileParserException("Error parsing mapping information.\n"+ex.getMessage());
                    }
                }
            }
            in.close();
   
            int blockcolnum = Block.getBlockCol(blocks);
            
            for(ProgrammappingTO m:mappings) {
                Block b = findBlock(blocks, m.getDestblocknum());
                m.calculateDestAbsolutePositions(b.getRowNumInBlock(),b.getColNumInBlock(),blockcolnum);
            }
        } catch (Exception ex) {
            throw new ProgramMappingFileParserException(ex.getMessage());
        }
        return mappings;
    }
   
    private void convertBlocks(List<Block> blocks, Set scaleX, Set scaleY) {
        for(Block b:blocks) {
            b.convertPosx(scaleX);
            b.convertPosy(scaleY);
        }
    }
    
    private Block findBlock(List<Block> blocks, long num) {
        for(Block b:blocks) {
            if(b.getNum() == num)
                return b;
        }
        
        return null;
    }
    
    protected void finalize() throws Throwable {
    }
}
