/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import java.io.*;
/**
 *
 * @author htaycher
 */
public class BaseCDSSequence extends BaseSequence
{
    private int    m_cds_stop =-1;
    private int     m_cds_start = -1;
            
            public int    getCdsStart(){ return m_cds_start;}
    public int    getCdsStop(){ return m_cds_stop;}
      public void     setCdsStart(int v){  m_cds_start = v;}
    public void     setCdsStop(int v){  m_cds_stop= v;}
    public String getCodingSequence()    {        return getText().substring(m_cds_start-1,m_cds_stop);    }
    
  public BaseCDSSequence( )
    {        super( "",BASE_CDS_SEQUENCE); }
    public BaseCDSSequence( String t)
    {        super( t,BASE_CDS_SEQUENCE);  }
   public BaseCDSSequence( String t, int id, int cdsst, int cdsstop)
    {        
       super( t,BASE_CDS_SEQUENCE);
       m_id=id;
       m_cds_stop=cdsstop;
       m_cds_start= cdsst;
   }
   
    public void   writeToFile(BufferedWriter fout , boolean isFlush) throws Exception
     {
         String seq=null;
         try
         {
            fout.write("\n>"+m_id +"\n");
            fout.write(this.getCodingSequence() );
            if (isFlush) fout.flush();
         }
         catch(Exception e)
         {
             System.out.println("Cannot write sequence "+m_id+" "+m_cds_start +" "+m_cds_stop+" "+m_text.length());
             throw new Exception ("Cannot write sequence "+m_id+" "+m_cds_start +" "+m_cds_stop+" "+m_text.length());
         }
           
        
     }
  
}
