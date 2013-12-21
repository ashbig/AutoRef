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
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.Invoice;

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
    
    public static Paragraph makeSenderAddress() {
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10); 
        String s = "Harvard Medical School\nDep.BCMP Room C- 214\n240 Longwood Ave.\nBoston, MA 02115\n(617)432-1210\nAttn: Lola Yao";
        Paragraph par = new Paragraph(12, new Chunk(s, font));
        return par;
    }
    
    public static Paragraph makeRecieveAddress(Invoice invoice) {
        Paragraph par = new Paragraph();
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10); 
        par.add(new Chunk("PI:\t" + invoice.getPiname()+"\n", font));
        par.add(new Chunk("PI Email:\t"+invoice.getPiemail()+"\n", font));
        par.add(new Chunk("Institution:\t" + invoice.getInstitution()+"\n", font));
        par.add(new Chunk("Grant or PO Number:\t" + invoice.getAccountnum(), font));
        return par;
    }
    
    public static Paragraph makePlasmidRecieveAddress(CloneOrder order) {
        Paragraph par = new Paragraph();
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 10); 
        par.add(new Chunk("Order ID:\t" + order.getOrderid()+"\n", font));
        par.add(new Chunk("Order Date:\t" + order.getOrderDate()+"\n", font));
        par.add(new Chunk("Order By:\t" + order.getName()+"\n", font));
        par.add(new Chunk("PI:\t" + order.getPiname()+"\n", font));
        par.add(new Chunk("PI email:\t" + order.getPiemail()+"\n", font));
        par.add(new Chunk("Grant or PO Number:\t" + order.getPonumber()+"\n", font));
        return par;
    }
}
