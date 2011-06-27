/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.util;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

/**
 *
 * @author Dongmei
 */
public class PdfEditor {
    public static Paragraph makeTitle(String s) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD); 
        Paragraph title = new Paragraph(new Phrase(20, new Chunk(s, font)));
        title.setAlignment(Element.ALIGN_CENTER);
        return title;
    }
    
    public static Paragraph makeRegBold(String s) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD); 
        Paragraph p = new Paragraph(12, new Chunk(s, font));
        return p;
    }
    
    public static Paragraph makeRegBoldUnderline(String s) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD); 
        font.setStyle(Font.UNDERLINE);
        Paragraph p = new Paragraph(12, new Chunk(s, font));
        return p;
    }
    
    public static Paragraph makeSmallBold(String s) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD); 
        Paragraph p = new Paragraph(12, new Chunk(s, font));
        return p;
    }
    
    public static Paragraph makeSmall(String s) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10); 
        Paragraph p = new Paragraph(12, new Chunk(s, font));
        return p;
    }
    
    public static Paragraph makeSmallItalic(String s) {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.ITALIC); 
        Paragraph p = new Paragraph(12, new Chunk(s, font));
        return p;
    }
}
