/** 
 * $Id: ProcessProtocol.java,v 1.2 2001-07-09 16:02:39 jmunoz Exp $
 * @File: ProcessProcessProtocol.java
 * @Date: 5/24/01  
 * @author Wendy Mar
 */

package edu.harvard.med.hip.flex.process;
import java.util.*;

public class ProcessProtocol
{
   public static final ProcessProtocol IDENTIFY_SEQUENCE = new ProcessProtocol();
   public static final ProcessProtocol APPROVE_SEQUENCES = new ProcessProtocol();
   public static final ProcessProtocol DESIGN_CONSTRUCT = new ProcessProtocol();
   public static final ProcessProtocol ORDER_OLIGO_PLATES = new ProcessProtocol();
   public static final ProcessProtocol RECEIVE_OLIGO_PLATES = new ProcessProtocol();
   public static final ProcessProtocol CREATE_PCR_PLATES = new ProcessProtocol();
   public static final ProcessProtocol CREATE_GEL_PLATES = new ProcessProtocol();
   public static final ProcessProtocol GENERATE_FILTER_PLATES = new ProcessProtocol();
   public static final ProcessProtocol CREATE_BP = new ProcessProtocol();
   public static final ProcessProtocol CREATE_TRANSFORMATION = new ProcessProtocol();
   public static final ProcessProtocol CREATE_CULTURE = new ProcessProtocol();
   public static final ProcessProtocol CREATE_DNA = new ProcessProtocol();

   public void check()
   {}

   private ProcessProtocol()
   {}

   	/**
	 * Get the ProcessProtocol ID number of a certain ProcessProtocol enumeration.
	 *
	 * @param ProcessProtocol The ProcessProtocol object.
	 * @return A String represents the ProcessProtocolId
	 */

	public int getId(ProcessProtocol ProcessProtocol)
	{
		int result;
		ProcessProtocol.check();
		if (ProcessProtocol == ProcessProtocol.IDENTIFY_SEQUENCE)
		{
 			result = 1;
		}
		else if (ProcessProtocol == ProcessProtocol.APPROVE_SEQUENCES)
		{
 			result = 2;
		}
		else if (ProcessProtocol == ProcessProtocol.DESIGN_CONSTRUCT)
		{
 			result = 3;
		}
		else if (ProcessProtocol == ProcessProtocol.ORDER_OLIGO_PLATES)
		{
 			result = 4;
		}
		else if (ProcessProtocol == ProcessProtocol.RECEIVE_OLIGO_PLATES)
		{
 			result = 5;
		}
		else if (ProcessProtocol == ProcessProtocol.CREATE_PCR_PLATES)
		{
 			result = 6;
		}
		else if (ProcessProtocol == ProcessProtocol.CREATE_GEL_PLATES)
		{
 			result = 7;
		}
		else if (ProcessProtocol == ProcessProtocol.GENERATE_FILTER_PLATES)
		{
 			result = 8;
		}
		


  

		else
		{
 			throw (new IllegalArgumentException("The given ProcessProtocol was unrecognized."));
		}
		return result;
	} // getProcessProtocolId


	public int getId()
	{
		int result = getId(this);
		return result;
	} // getProcessProtocolId
	
} // ProcessProtocol
