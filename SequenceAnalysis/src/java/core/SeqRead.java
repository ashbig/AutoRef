/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Lab User
 */
public class SeqRead implements Serializable {

    private String readname;
    private int phred;
    private String sequence;
    private SeqValidation currentValidation;
    private List<SeqValidation> validations;
    private String read;
    private String plate;
    private int pos;
    private String well;

    public boolean isValidationExist() {
        if (currentValidation == null) {
            return false;
        }
        return true;
    }

    public boolean isBlastExist() {
        if (!isValidationExist()) {
            return false;
        }
        if (currentValidation.getResult().equals(SeqValidation.RESULT_LOWSCORE)) {
            return false;
        }

        return true;
    }
    
    public boolean isBlastNotExist() {
        return !isBlastExist();
    }
    
    /**
     * @return the readname
     */
    public String getReadname() {
        return readname;
    }

    /**
     * @param readname the readname to set
     */
    public void setReadname(String readname) {
        this.readname = readname;
    }

    /**
     * @return the phred
     */
    public int getPhred() {
        return phred;
    }

    /**
     * @param phred the phred to set
     */
    public void setPhred(int phred) {
        this.phred = phred;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the currentValidation
     */
    public SeqValidation getCurrentValidation() {
        return currentValidation;
    }

    /**
     * @param currentValidation the currentValidation to set
     */
    public void setCurrentValidation(SeqValidation currentValidation) {
        this.currentValidation = currentValidation;
    }

    /**
     * @return the read
     */
    public String getRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(String read) {
        this.read = read;
    }

    /**
     * @return the validations
     */
    public List<SeqValidation> getValidations() {
        return validations;
    }

    /**
     * @param validations the validations to set
     */
    public void setValidations(List<SeqValidation> validations) {
        this.validations = validations;
    }

    /**
     * @return the plate
     */
    public String getPlate() {
        return plate;
    }

    /**
     * @param plate the plate to set
     */
    public void setPlate(String plate) {
        this.plate = plate;
    }

    /**
     * @return the pos
     */
    public int getPos() {
        return pos;
    }

    /**
     * @param pos the pos to set
     */
    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * @return the well
     */
    public String getWell() {
        return well;
    }

    /**
     * @param well the well to set
     */
    public void setWell(String well) {
        this.well = well;
    }
}
