/*
 * Gene.java
 *
 * Created on January 18, 2002, 2:27 PM
 */

package edu.harvard.med.hip.metagene.core;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Gene {
    private int hipGeneId;
    private String symbol;
    private int qualityidSymbol;
    private String name;
    private int qualityidName;
    private String date;
    private int locusid;
    private Vector nicknames;
    private Vector information;
    
    /** Creates new Gene */
    public Gene(int id, String symbol, int qualSymbol, String name, int qualName, String date, int locusid) {
        this.hipGeneId = id;
        this.symbol = symbol;
        this.qualityidSymbol = qualSymbol;
        this.name = name;
        this.qualityidName = qualName;
        this.date = date;
        this.locusid = locusid;
    } 
 
    public int getHipGeneId() {
        return hipGeneId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    public void setNicknames(Vector nicknames) {
        this.nicknames = nicknames;
    }
    
    public Vector getNicknames() {
        return nicknames;
    }
    
    public String getNicknamesString() {
        String rt = "";
        
        if(nicknames == null)
            return rt;
        
        for(int i=0; i<nicknames.size(); i++) {
            String nickname = (String)nicknames.elementAt(i);
            
            if(i == 0)
                rt = nickname;
            else 
                rt = rt+","+nickname;
        }
        
        return rt;
    }
    
    public void setInformation(Vector information) {
        this.information = information;
    }
    
    public String getGosString() {
        if(information == null)
            return "";
        
        String go = null;
        for(int i=0; i<information.size(); i++) {
            Geneinfo info = (Geneinfo)information.elementAt(i);
            if(Geneinfo.GO.equals(info.getType())) {
                if(go == null)
                    go = info.getValue();
                else
                    go = go+","+info.getValue();
            }
        }
        
        return go;
    }
    
    public String getProteomesString() {
        if(information == null)
            return "";

        String proteome = null;
        for(int i=0; i<information.size(); i++) {
            Geneinfo info = (Geneinfo)information.elementAt(i);
            if(Geneinfo.GO.equals(info.getType())) {
                if(proteome == null)
                    proteome = info.getValue();
                else
                    proteome = proteome+","+info.getValue();
            }
        }
        
        return proteome;
    }
    
    public int getLocusid() {
        return locusid;
    }
}
