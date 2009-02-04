/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io;

import transfer.MicrovigeneresultTO;
import transfer.MicrovigeneslideTO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author DZuo
 */
public class MicrovigeneFileParser {
    public static final String MAINROW = "Main Row";
    public static final String MAINCOL = "Main Col";
    public static final String SUBROW = "Sub Row";
    public static final String SUBCOL = "Sub Col";
    public static final String GENEID = "GeneID";
    public static final String MEANNET = "mean_net";
    public static final String MEANTOTAL = "mean_total";
    public static final String MEANBKG = "mean_bkg";
    public static final String BKGUSED = "bkg_used";
    public static final String MEANDUST = "mean_dust";
    public static final String MEDIANNET = "median_net";
    public static final String MEDIANTOTAL = "median_total";
    public static final String MEDIANBKG = "median_bkg";
    public static final String VOLNET = "vol_net";
    public static final String VOLTOTAL = "vol_total";
    public static final String VOLBKG = "vol_bkg";
    public static final String VOLDUST = "vol_dust";
    public static final String CVSPOT = "cv_spot";
    public static final String CVBKG = "cv_bkg";
    public static final String DUSTINESS = "dustiness";
    public static final String SPOTSKEW = "spot_skew";
    public static final String BKGSKEW = "bkg_skew";
    public static final String KURTOSIS = "kurtosis";
    public static final String RANK = "rank";
    public static final String TYPE = "type";
    public static final String XCENTER = "xcenter";
    public static final String YCENTER = "ycenter";
    public static final String AREASIGNAL = "area_signal";
    public static final String AREASPOT = "area_spot";
    public static final String AREABKG = "area_bkg";
    public static final String SOLIDITY = "solidity";
    public static final String CIRCULARITY = "circularity";
    public static final String ROUNDNESS = "roundness";
    public static final String ASPECT = "aspect";	 

    protected InputStream input;
    
    public MicrovigeneFileParser() {}
    
    public MicrovigeneFileParser(InputStream input) {
        setInput(input);
    }
    
    public MicrovigeneslideTO parseFile(InputStream input) throws NappaIOException {
        setInput(input);
        return parseFile();
    }
    
    public MicrovigeneslideTO parseFile() throws NappaIOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;

        try {
            line = in.readLine();
            StringTokenizer tokenizer = new StringTokenizer(line, "\t");
            String version = null;
            while(tokenizer.hasMoreTokens()) {
                version = tokenizer.nextToken().trim();
            }
                
            in.readLine();

            String date = in.readLine().trim();

            in.readLine();

            line = in.readLine().trim();
            tokenizer = new StringTokenizer(line, "\t");
            tokenizer.nextToken();
            int roirow = Integer.parseInt(tokenizer.nextToken());
            tokenizer.nextToken();
            int roicol = Integer.parseInt(tokenizer.nextToken());
            tokenizer.nextToken();
            int mainrow = Integer.parseInt(tokenizer.nextToken());
            tokenizer.nextToken();
            int maincol = Integer.parseInt(tokenizer.nextToken());
            tokenizer.nextToken();
            int subrow = Integer.parseInt(tokenizer.nextToken());
            tokenizer.nextToken();
            int subcol = Integer.parseInt(tokenizer.nextToken());
            
            MicrovigeneslideTO slide = new MicrovigeneslideTO(version,date,roirow,roicol,mainrow,maincol,subrow,subcol);
            
            List<String> titles = new ArrayList<String>();
            line = in.readLine().trim();
            tokenizer = new StringTokenizer(line, "\t");
            while(tokenizer.hasMoreTokens()) {
                String s = tokenizer.nextToken().trim();
                titles.add(s);
            }
            
            while ((line = in.readLine()) != null) {
                //System.out.print(line);
                tokenizer = new StringTokenizer(line.trim(), "\t");
                int index = 0;
                MicrovigeneresultTO spot = new MicrovigeneresultTO();
                while(tokenizer.hasMoreTokens()) {
                    String s = tokenizer.nextToken();
                    String title = titles.get(index);
                    //System.out.println("s="+s+",title="+title);
                    if(MAINROW.equals(title)) 
                        spot.setMainRow(Integer.parseInt(s));
                    if(MAINCOL.equals(title))
                        spot.setMainCol(Integer.parseInt(s));
                    if(SUBROW.equals(title))
                        spot.setSubRow(Integer.parseInt(s));
                    if(SUBCOL.equals(title))
                        spot.setSubCol(Integer.parseInt(s));
                    if(GENEID.equals(title))
                        spot.setGeneID(s);
                    if(MEANNET.equals(title))
                        spot.setMeannet(Double.parseDouble(s));
                    if(MEANTOTAL.equals(title))
                        spot.setMeantotal(Double.parseDouble(s));
                    if(MEANBKG.equals(title))
                        spot.setMeanbkg(Integer.parseInt(s));
                    if(BKGUSED.equals(title))
                        spot.setBkgused(Integer.parseInt(s));
                    if(MEANDUST.equals(title))
                        spot.setMeandust(Integer.parseInt(s));
                    if(MEDIANNET.equals(title))
                        spot.setMediannet(Integer.parseInt(s));
                    if(MEDIANTOTAL.equals(title))
                        spot.setMediantotal(Integer.parseInt(s));
                    if(MEDIANBKG.equals(title))
                        spot.setMedianbkg(Integer.parseInt(s));
                    if(VOLNET.equals(title))
                        spot.setVolnet(Integer.parseInt(s));
                    if(VOLTOTAL.equals(title))
                        spot.setVoltotal(Integer.parseInt(s));
                    if(VOLBKG.equals(title))
                        spot.setVolbkg(Integer.parseInt(s));
                    if(VOLDUST.equals(title))
                        spot.setVoldust(Double.parseDouble(s));
                    if(CVSPOT.equals(title))
                        spot.setCvspot(Double.parseDouble(s));
                    if(CVBKG.equals(title))
                        spot.setCvbkg(Double.parseDouble(s));
                    if(DUSTINESS.equals(title))
                        spot.setDustiness(Integer.parseInt(s));
                    if(SPOTSKEW.equals(title))
                        spot.setSpotskew(Double.parseDouble(s));
                    if(BKGSKEW.equals(title))
                        spot.setBkgskew(Double.parseDouble(s));
                    if(KURTOSIS.equals(title))
                        spot.setKurtosis(Double.parseDouble(s));
                    if(RANK.equals(title))
                        spot.setRank(Integer.parseInt(s));
                    if(TYPE.equals(title))
                        spot.setSpottype(Integer.parseInt(s));
                    if(XCENTER.equals(title))
                        spot.setXcenter(Integer.parseInt(s));
                    if(YCENTER.equals(title))
                        spot.setYcenter(Integer.parseInt(s));
                    if(AREASIGNAL.equals(title))
                        spot.setAreasignal(Integer.parseInt(s));
                    if(AREASPOT.equals(title))
                        spot.setAreaspot(Integer.parseInt(s));
                    if(AREABKG.equals(title))
                        spot.setAreabkg(Integer.parseInt(s));
                    if(SOLIDITY.equals(title))
                        spot.setSolidity(Integer.parseInt(s));
                    if(CIRCULARITY.equals(title))
                        spot.setCircularity(Double.parseDouble(s));
                    if(ROUNDNESS.equals(title))
                        spot.setRoundness(Double.parseDouble(s));
                    if(ASPECT.equals(title))
                        spot.setAspect(Double.parseDouble(s));
                    
                    index++;
                }
                int pos = slide.calculatePosition(mainrow,maincol,subrow,subcol);
                slide.addSpotinfo(spot);
            }
            return slide;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new NappaIOException("Error parsing file.\n"+ex.getMessage());
        }
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }
    
    public static final void main(String args[]) {
        String filename = "D:\\dev\\Test\\59_Nov7_441_122308_1306_3580 20um_allcolumns.txt";
        try {
            InputStream input = new FileInputStream(new File(filename));
            MicrovigeneFileParser parser = new MicrovigeneFileParser(input);
            MicrovigeneslideTO slide = parser.parseFile();
            System.out.println("Date="+slide.getDate());
            System.out.println("Version="+slide.getVersion());
            System.out.println("Main row="+slide.getMainRow());
            System.out.println("Main col="+slide.getMainCol());
            System.out.println("Sub row="+slide.getSubRow());
            System.out.println("Sub col="+slide.getSubCol());
            
            Collection<MicrovigeneresultTO> spots = slide.getSpots();
            for(MicrovigeneresultTO spot:spots) {
                System.out.println("Vol="+spot.getVoltotal());
                System.out.println("pos="+slide.calculatePosition(spot.getMainRow(), spot.getMainCol(), spot.getSubRow(), spot.getSubCol()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
