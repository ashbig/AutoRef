/**
 * $Id $
 *
 * File: ExportFlexToFasta.java
 * Author: Dongmei Zuo
 * Date: 10-19-2001
 *
 * This program exports the sequences from FLEXGene database to FASTA format
 * files by calling a specific URL. 
 */

import edu.harvard.med.hip.flex.export.*;
import java.net.*;
import java.io.*;

public class ExportFlexToFasta {
    public static final String urlString = "http://kotel.med.harvard.edu/FLEX/ExportFLEXtoFASTA.do";

    public static void main(String [] args) {
	try {
	    URL url = new URL(urlString);
	    url.openStream();
	} catch (MalformedURLException ex) {
	    System.out.println(ex);
	} catch (IOException ex) {
	    System.out.println(ex);
	}
    }
}
	

