/*
 * Well.java
 *
 * Created on February 27, 2007, 3:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

import java.io.Serializable;

/**
 *
 * @author DZuo
 */
public class Well implements Serializable {
    private String x;
    private String y;
    
    /** Creates a new instance of Well */
    public Well() {
    }
    
    public Well(String x, String y) {
        this.setX(x);
        this.setY(y);
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
    
    public String getFormatWell(){
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(2);
        fmt.setMinimumIntegerDigits(2);
        fmt.setGroupingUsed(false);
        return getX()+fmt.format(Integer.parseInt(getY()));
    }
    
    public static int convertWellToHPos( String well, int colnum) {
        int position = -1;
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
        
        
        row_value = row - a_value + 1;
        
        return (row_value - 1) * colnum +  column ;
    }
    
    public static int convertWellToVPos( String well, int rownum) {
        int position = -1;
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
        
        
        row_value = row - a_value + 1;
        
        return (column - 1) * rownum +  row_value ;
    }
    
    public static int convertWellToHPos(int row, int column, int colnum) {
        return (row - 1) * colnum +  column;
    }
    
    public static int convertWellToVPos(int row, int column, int rownum) {
        return (column - 1) * rownum +  row;
    }
    
    //convert well nomenculature from A10 to int
    public static Well convertHPosToWell( int well, int colnum) {
        String position = null;
        
        int a_value = (int) 'A';
        int column = well % colnum;
        int row_value = ((int) well / colnum  +1) ;
        String rowname = ""+((char) (a_value + row_value - 1));
        
        if (row_value == 0 ) {
            rowname=""+(char)(a_value+colnum-1);
            column = column-1;
        }
       return new Well(rowname, ""+column);
    }
    
    //convert well nomenculature from A10 to int
    public static Well convertVPosToWell( int well, int rownum) {
        String position = null;
        
        int a_value = (int) 'A';
        int row_value = well % rownum;
        int column = ((int) well / rownum  +1) ;
        String rowname = ""+((char) (a_value + row_value - 1));
        
        if (row_value == 0 ) {
            rowname=""+(char)(a_value+rownum-1);
            column = column-1;
        }
       return new Well(rowname, ""+column);
    }
    
    public static void main(String args[]) {
        int row = 3;
        int col = 5;
        int rownum = 8;
        int colnum = 12;
        int pos = Well.convertWellToHPos(row, col, colnum);
        System.out.println("pos="+pos+",["+row+","+col+"]");
        
        pos = Well.convertWellToVPos(row, col, rownum);
        System.out.println("pos="+pos+",["+row+","+col+"]");
        
        String well = "C5";
        pos = Well.convertWellToHPos(well, colnum);
        System.out.println("pos="+pos+",["+well+"]");
        
        pos = Well.convertWellToVPos(well, rownum);
        System.out.println("pos="+pos+",["+well+"]");
        
        Well w = Well.convertHPosToWell(29, colnum);
        System.out.println("well=["+w.getX()+","+w.getY()+"]");
        
        w = Well.convertVPosToWell(35, rownum);
        System.out.println("well=["+w.getX()+","+w.getY()+"]");
    }
}
