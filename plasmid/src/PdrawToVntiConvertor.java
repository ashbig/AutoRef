/*
 * PdrawToVntiConvertor.java
 *
 * Created on March 14, 2006, 2:50 PM
 */

/**
 *
 * @author  DZuo
 */

import java.io.*;
import java.util.*;

public class PdrawToVntiConvertor {
    public static final String INPUTDIR = "G:\\vector\\input\\John\\Syn_Let\\";
    public static final String OUTPUTDIR = "G:\\vector\\output\\John\\Syn_Let\\";
    
    public static final String DNANAME = "DNAname";
    public static final String ISCIRCULAR = "IScircular";
    public static final String ELEMENT = "Element";
    public static final String SEQUENCE = "Sequence";
    
    /** Creates a new instance of PdrawToVntiConvertor */
    public PdrawToVntiConvertor() {
    }
    
    public VntiEmbl convertToEmbl(String input) throws Exception {
        BufferedReader in = new BufferedReader(new FileReader(input));
        VntiEmbl v = new VntiEmbl();
        List features = new ArrayList();
        
        String line;
        boolean seqStart = false;
        String sequence = "";
        while((line = in.readLine()) != null && line.trim().length()>0) {
            System.out.println(line);
            if(line.indexOf(DNANAME)>=0) {
                String dnaname = line.substring(line.indexOf(DNANAME)+7).trim();
                v.setVectorname(dnaname.replace(' ', '\\'));
                continue;
            }
            if(line.indexOf(ISCIRCULAR)>=0) {
                System.out.println(line.indexOf(ISCIRCULAR));
                System.out.println(line.length());
                String isCircular = line.substring(line.indexOf(ISCIRCULAR)+10).trim();
                if(isCircular.equals("YES")) {
                    v.setVectortype(VntiEmbl.CIRCULAR_DNA);
                } else {
                    v.setVectortype(VntiEmbl.LINEAR_DNA);
                }
                continue;
            }
            if(line.indexOf(ELEMENT)>=0) {
                VntiVectorFeature f = new VntiVectorFeature();
                /**
                 * StringTokenizer st = new StringTokenizer(line);
                 * String ignore = st.nextToken();
                 * f.setLabel(st.nextToken().trim());
                 * f.setStart(Integer.parseInt(st.nextToken()));
                 * f.setEnd(Integer.parseInt(st.nextToken()));
                 * int i = Integer.parseInt(st.nextToken());
                 * String name = VntiVectorFeature.getName(i);
                 * if(name == null)
                 * throw new Exception("Cannot get feature name by "+i);
                 * f.setName(name);
                 * int j = Integer.parseInt(st.nextToken());
                 * if(j == 0)
                 * f.setIsComplement(true);
                 **/
                String label = line.substring(10, 40).trim();
                if(label.indexOf(", rev")>=0) {
                    label = label.substring(0, label.indexOf(", rev"));
                    label = label + "\\seq";
                }
                f.setLabel(label.replace(' ', '\\'));
                f.setStart(Integer.parseInt(line.substring(40, 50).trim()));
                f.setEnd(Integer.parseInt(line.substring(50, 60).trim()));
                int i = Integer.parseInt(line.substring(60, 65).trim());
                String name = VntiVectorFeature.getName(i);
                if(name == null)
                    throw new Exception("Cannot get feature name by "+i);
                f.setName(name);
                int j = Integer.parseInt(line.substring(65).trim());
                if(j == 0)
                    f.setIsComplement(true);
                
                features.add(f);
                continue;
            }
            if(line.indexOf(SEQUENCE)>=0) {
                seqStart = true;
                continue;
            }
            if(seqStart) {
                StringTokenizer st = new StringTokenizer(line);
                String ignore = st.nextToken();
                while(st.hasMoreTokens()) {
                    String s = st.nextToken().trim();
                    sequence += s;
                }
            }
        }
        
        in.close();
        v.setSequence(sequence);
        v.setFeatures(features);
        
        return v;
    }
    
    public void printVntiEmblFile(VntiEmbl v, String filename) throws Exception {
        OutputStreamWriter out = new FileWriter(filename);
        out.write("ID   "+v.getVectorname()+" standard; "+v.getVectortype()+";    ; "+v.getSequenceLength()+" BP.\n");
        out.write("CC   This file is created by Vector NTI\n");
        out.write("CC   http://www.invitrogen.com/\n");
        out.write("CC   VNTDATE|404557096|\n");
        out.write("CC   VNTDBDATE|404557797|\n");
        out.write("CC   LSOWNER|\n");
        out.write("CC   VNTAUTHORNAME|"+v.getAuthor()+"|\n");
        out.write("FH   Key             Location/Qualifiers\n");
        out.write("FH\n");
        
        List features = v.getFeatures();
        for(int i=0; i<features.size(); i++) {
            VntiVectorFeature f = (VntiVectorFeature)features.get(i);
            String name = f.getName();
            out.write("FT   "+TextFormat.format(name,16,true,' '));
            if(f.getIsComplement()) {
                out.write("complement("+f.getStart()+".."+f.getEnd()+")\n");
            } else {
                out.write(f.getStart()+".."+f.getEnd()+"\n");
            }
            out.write("FT                   /vntifkey="+f.getKey()+"\n");
            out.write("FT                   /label="+f.getLabel()+"\n");
        }
        out.write("SQ   Sequence "+v.getSequenceLength()+" BP; "+v.getBaseNumber("A")+" A; "+v.getBaseNumber("C")+" C; "+v.getBaseNumber("G")+" G; "+v.getBaseNumber("T")+" t;\n");
        
        String seq = v.getSequence().toLowerCase();
        int i=0;
        while(i<seq.length()) {
            if(i%60 == 0) {
                out.write("     ");
            }
            
            if((i+10)>seq.length()) {
                out.write(TextFormat.format(seq.substring(i),10,true,' ')+" ");
                
                if((i+10) % 60 != 0) {
                    while((i+10) % 60 != 0) {
                        out.write("           ");
                        i = i+10;
                    }
                    
                    out.write(TextFormat.format((new Integer(seq.length())).toString(), 10, false, ' ')+"\n");
                }
            } else {
                out.write(seq.substring(i, i+10)+" ");
            }
            if((i+10)%60 == 0) {
                if(i == seq.length()-1) {
                    out.write(TextFormat.format((new Integer(seq.length())).toString(), 10, false, ' ')+"\n");
                } else {
                    out.write(TextFormat.format((new Integer(i+10)).toString(),10,false,' ')+"\n");
                }
            }
            i = i+10;
        }
        out.write("//\n");
        out.close();
    }
    
    public static void main(String args[]) {
        String author = "John Doench";
        String outputextension = ".dat";
        PdrawToVntiConvertor convertor = new PdrawToVntiConvertor();
        
        try {
            File dir = new File(INPUTDIR);
            File[] files = dir.listFiles();
            for(int i=0; i<files.length; i++) {
                File f = files[i];
                String filename = f.getName();
                String outputfilename = filename.substring(0, filename.indexOf(".PDW"))+outputextension;
                System.out.println("Converting file "+filename);
                VntiEmbl v = convertor.convertToEmbl(INPUTDIR+filename);
                v.setAuthor(author);
                System.out.println("Converting file finished.");
                
                System.out.println(v.getVectorname());
                System.out.println(v.getVectortype());
                System.out.println(v.getSequence());
                System.out.println(v.getAuthor());
                System.out.println(v.getSequenceLength());
                
                List features = v.getFeatures();
                for(int n=0; n<features.size(); n++) {
                    VntiVectorFeature feature = (VntiVectorFeature)features.get(n);
                    System.out.println("\t"+feature.getLabel());
                    System.out.println("\t"+feature.getKey());
                    System.out.println("\t"+feature.getIsComplement());
                    System.out.println("\t"+feature.getName());
                    System.out.println("\t"+feature.getStart());
                    System.out.println("\t"+feature.getEnd());
                }
                
                System.out.println("Printing file "+outputfilename);
                convertor.printVntiEmblFile(v, OUTPUTDIR+outputfilename);
                System.out.println("Printing file finished.");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}