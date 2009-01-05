/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.blast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author DZuo
 */
public class BlastParser {

    public static final int DEFAULT_ALENGTH = 100;
    public static final double DEFAULT_PID = 90.0;
    private String file;
    private List<BlastHit> infos;
    private int alength;
    private double pid;

    public BlastParser() {
        infos = new ArrayList();
        setAlength(DEFAULT_ALENGTH);
        setPid(DEFAULT_PID);
    }

    public BlastParser(String file) {
        setFile(file);
        infos = new ArrayList();
        setAlength(DEFAULT_ALENGTH);
        setPid(DEFAULT_PID);
    }

    /** Parse the output file generated using -m 9
     */
    public void parseTabularOutput() throws Exception {
        infos = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(getFile()));
            String line = null;
            String lastQueryid = null;
            String lastSubid = null;
            BlastHit lastHit = null;
            while ((line = in.readLine()) != null) {
                if (line.trim().indexOf("#") == 0) {
                    continue;
                }

                try {
                    StringTokenizer st = new StringTokenizer(line, "\t");
                    String queryid = st.nextToken();
                    String subid = st.nextToken();
                    double percentid = Double.parseDouble(st.nextToken());
                    int alignlength = Integer.parseInt(st.nextToken());
                    int mismatch = Integer.parseInt(st.nextToken());
                    int gap = Integer.parseInt(st.nextToken());
                    int qstart = Integer.parseInt(st.nextToken());
                    int qend = Integer.parseInt(st.nextToken());
                    int sstart = Integer.parseInt(st.nextToken());
                    int send = Integer.parseInt(st.nextToken());
                    double evalue = Double.parseDouble(st.nextToken());
                    double score = Double.parseDouble(st.nextToken());

                    if (alignlength >= alength && percentid >= pid) {
                        BlastInfo info = new BlastInfo(queryid, subid, percentid, alignlength, mismatch, gap, qstart, qend, sstart, send, evalue, score);
                        if(!subid.equals(lastSubid) || !queryid.equals(lastQueryid)) {
                            if(lastHit != null) {
                                lastHit.calculateSummary();
                            }
                            lastHit = new BlastHit(queryid,subid);
                            infos.add(lastHit);
                            lastQueryid = queryid;
                            lastSubid = subid;
                        }
                        lastHit.addBlastinfo(info);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new Exception("Error parsing line: " + line);
                }
            }
            if(lastHit != null) {
                lastHit.calculateSummary();
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new Exception("Error reading output file.\n" + ex.getMessage());
        }
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public List<BlastHit> getInfos() {
        return infos;
    }

    public void setInfos(List<BlastHit> infos) {
        this.infos = infos;
    }

    public int getAlength() {
        return alength;
    }

    public void setAlength(int alength) {
        this.alength = alength;
    }

    public double getPid() {
        return pid;
    }

    public void setPid(double pid) {
        this.pid = pid;
    }
}
