//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ProcessRunner.java
 *
 * Created on July 11, 2005, 10:05 AM
 */

package edu.harvard.med.hip.bec.programs;


import java.io.*;
import java.util.*;
/**
 *
 * @author  htaycher
 */
public class SystemProcessRunner
{
    
    /** Creates a new instance of ProcessRunner */
     public static boolean runOSCall( String cmd)
    {
         try
        {
            Runtime r = Runtime.getRuntime();
            r.traceMethodCalls(true);
            Process p = r.exec(cmd);
             BufferedInputStream berr = new BufferedInputStream(p.getErrorStream());
            BufferedInputStream binput = new BufferedInputStream(p.getInputStream());
            int x = 0;int y = 0;
            
            boolean    isFinished = false;
            boolean    isErrDone = false;
            boolean    isOutDone = false;
            byte[]      buff = new byte[255];
            
            while (!isFinished)
            {
                if (berr.available() == 0 && binput.available() == 0)
                {
                    try
                    {
                        p.exitValue();
                        isFinished = true;
                        break;
                    }
                    catch (IllegalThreadStateException e)
                    {
                        Thread.currentThread().sleep(100);
                    }
                    catch(Exception e)
                    {
                        throw new Exception("Cannot run program");
                    }
                }
                else
                {
                    berr.read(buff, 0, Math.min(255, berr.available()));
                    binput.read(buff, 0, Math.min(255, binput.available()));
                   }
            }
            p.waitFor();
            if (p.exitValue() != 0)
            {
                 return false;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            return false;
        } 
        return true;
    }
    
    
}
