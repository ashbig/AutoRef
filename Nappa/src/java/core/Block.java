/*
 * Block.java
 *
 * Created on March 2, 2007, 4:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import transfer.SampleTO;

/**
 *
 * @author DZuo
 */
public class Block implements Serializable {
    private int num;
    private int posx;
    private int posy;
    private int x;
    private int y;
    private List<Well> wells; 
    private List<SampleTO> samples;
    
    /** Creates a new instance of Block */
    public Block() {
        this.wells = new ArrayList<Well>();
        this.samples = new ArrayList<SampleTO>();
    }
    
    public Block(int num, int x, int y) {
        this.setNum(num);
        this.setPosx(x);
        this.setPosy(y);
        this.wells = new ArrayList<Well>();
        this.samples = new ArrayList<SampleTO>();
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
    
    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public void convertPosx(Collection<Integer> scale) {
        int i=0; 
        for(Integer s:scale) {
            i++;
            
            int position = s.intValue();
            if(position == posx) {
                setX(i);
            }
        }
    }
    
    public void convertPosy(Collection<Integer> scale) {
        int i=0; 
        for(Integer s:scale) {
            i++;
            
            int position = s.intValue();
            if(position == posy) {
                setY(i);
            }
        }
    }
    
    public static int getBlockRow(List<Block> blocks) {
        Set<Integer> s = new HashSet<Integer>();
        for(Block b:blocks) {
            s.add(new Integer(b.getX()));
        }
        
        return s.size();
    }
    
    public static int getBlockCol(List<Block> blocks) {
        Set<Integer> s = new HashSet<Integer>();
        for(Block b:blocks) {
            s.add(new Integer(b.getY()));
        }
        
        return s.size();
    }
    
    public void addWell(Well w) {
        this.wells.add(w);
    }
    
    public int getRowNumInBlock() {
        Set<String> s = new HashSet<String>();
        for(Well w:wells) {
            s.add(w.getX());
        }
        return s.size();
    }
    
    public int getColNumInBlock() {
        Set<String> s = new HashSet<String>();
        for(Well w:wells) {
            s.add(w.getY());
        }
        return s.size();
    }

    public List<SampleTO> getSamples() {
        return samples;
    }

    public void setSamples(List<SampleTO> samples) {
        this.samples = samples;
    }
}
