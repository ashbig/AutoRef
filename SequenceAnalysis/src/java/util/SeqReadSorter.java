/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import core.SeqRead;
import java.util.Comparator;

/**
 *
 * @author Lab User
 */
public class SeqReadSorter implements Comparator {

    public int compare(Object o1, Object o2) {
        try {
            SeqRead read1 = (SeqRead) o1;
            SeqRead read2 = (SeqRead) o2;
            int pos1 = read1.getPos();
            int pos2 = read2.getPos();
            String plate1 = read1.getPlate();
            String plate2 = read2.getPlate();

            if (plate1.compareTo(plate2) < 0) {
                return -1;
            }
            if (plate1.compareTo(plate2) > 0) {
                return 1;
            }
            if (pos1 < pos2) {
                return -1;
            }
            if (pos1 > pos2) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
