/*
 * StatAnalysis.java
 *
 * Created on January 30, 2002, 6:01 PM
 */

package edu.harvard.med.hip.metagene.core;

/**
 *
 * @author  dzuo
 * @version 
 */
public class StatAnalysis {
    private Statistics stat;
    private double score;
    
    /** Creates new StatAnalysis */
    public StatAnalysis() {
    }

    public StatAnalysis(Statistics stat, double score) {
        this.stat = stat;
        this.score = score;
    }
  
    public Statistics getStat() {
        return stat;
    }
    
    public double getScore() {
        return score;
    }
    
    public int compareTo(StatAnalysis a) {
        double aScore = a.getScore();
        int rt = 0;
        
        if(Statistics.FISCHER.equals(a.getStat().getType())) {
            if(score < aScore) {
                rt = -1;
            } else if(score > aScore) {
                rt = 1;
            } else {
                rt = 0;
            }
        } else {
            if(score < aScore) {
                rt = 1;
            } else if(score > aScore) {
                rt = -1;
            } else {
                rt = 0;
            }
        }
        
        return rt;
    }
}
