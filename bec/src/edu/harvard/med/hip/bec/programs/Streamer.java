/*
 * Streamer.java
 *
 * Created on December 4, 2002, 10:28 AM
 */

package edu.harvard.med.hip.bec.programs;

/**
 *
 * @author  htaycher
 */
import java.io.*;

public class Streamer implements Runnable
{
     private InputStream m_is = null;
     private OutputStream m_os = null;
     private final static int   BUFFER_SIZE = 8192;
     
     
     public Streamer(InputStream input_stream, OutputStream output_stream)
     {
            m_is = input_stream;
            m_os = output_stream;
     }

     public void run()
     {
         int numBytesRead = 0;
         byte[] buffer = null;
          try
          {
//write from input stream into output stream
             
              buffer = new byte[BUFFER_SIZE];
              while ( ( numBytesRead = m_is.read(buffer, 0, BUFFER_SIZE)) > 0)
              {
                m_os.write(buffer, 0, numBytesRead);
              }
              m_os.flush(); 
          }
          catch (IOException ie)
          {
              ie.printStackTrace();
          }
     } // run
} // Streamer

