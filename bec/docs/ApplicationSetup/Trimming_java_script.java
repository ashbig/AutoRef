
//package docs.ApplicationSetup;

import java.io.*;
import java.util.*;
/**
 *
 * @author  HTaycher
 */
public class Trimming_java_script {

    /** Creates a new instance of PhdFilesTrimmer */

    public static String       LINE_SEPARATOR = System.getProperty("line.separator") ;
    //quality constants
    public static final int             MIN_SEQ_LEN	=20;
    public static final int             MAX_ENZ_POS	=100;
    public static final double             MIN_PRB_VAL=0.05;
    public static final double             MIN_AVG_QUAL	=10.0;

   // private double
   // private double                          m_score = MIN_AVG_QUAL;

/* script call parameters
 "$clone_path
 $trim_score $trim_first_base $trim_last_base
  $lqr_is_use_lqr $lqr_is_delete_lqr //--- value 1 - true, 0 - false
 $lqr_pass_score $lqr_first_base $lqr_last_base $lqr_min_length
*/
    public static void main(String arg[])
    {
        boolean isDebug = true;
        String directory_name = null;
        String os = System.getProperty("os.name");
        boolean isWind = ( os.indexOf("Win") > -1);
        double min_pbd_val = MIN_PRB_VAL;
        double score =20;int first_base = 20; int last_base = 800;
        int lqr_is_use_lqr =  0;int lqr_is_delete_lqr =  0;
            int quality_check_min_read_length = 150;
            int quality_check_score = 20;
            int quality_check_first_base = 100;
            int quality_check_last_base =  700;

        if ( arg.length > 0)
        {
            if ( arg.length != 10 )return;
           score = Double.parseDouble( arg[1]);
             first_base = Integer.parseInt( arg[2]);
             last_base = Integer.parseInt( arg[3] );
            lqr_is_use_lqr =  Integer.parseInt( arg[4] );
             lqr_is_delete_lqr =  Integer.parseInt( arg[5] );
             quality_check_score =  Integer.parseInt( arg[6] );
             quality_check_first_base =  Integer.parseInt( arg[7] );
             quality_check_last_base =  Integer.parseInt( arg[8] );
             quality_check_min_read_length =  Integer.parseInt( arg[9] );

        }
 System.out.println("score "+score);
            System.out.println( "first_base "+first_base);
System.out.println(          "   last_base "+last_base);
System.out.println(           " lqr_is_use_lqr "+lqr_is_use_lqr);
System.out.println(        "     lqr_is_delete_lqr"+lqr_is_delete_lqr );
System.out.println(        "     quality_check_score "+quality_check_score);
 System.out.println(       "     quality_check_first_base "+quality_check_first_base);
 System.out.println(       "     quality_check_last_base "+quality_check_last_base );
 System.out.println(       "     quality_check_min_read_length "+quality_check_min_read_length );

        if (arg.length > 0)// production
        {
                directory_name = arg[0];
                if ( isWind )
                 directory_name = convertUnixFileNameIntoWindowsBasedOnOneLetterApproach( directory_name);
        }
        else
        {

            directory_name= "/C/bio/plate_analysis/clone_samples/39133/133229";//\\phd_dir\\6142_E05_3277_4426_F5.ab1.phd.1";
            directory_name = convertUnixFileNameIntoWindowsBasedOnOneLetterApproach( directory_name);
        }
        Trimming_java_script runner = new Trimming_java_script();
        runner.runTrimming(directory_name, score, first_base, last_base ,
                         lqr_is_use_lqr ,  lqr_is_delete_lqr ,
                         quality_check_min_read_length ,
                         quality_check_score ,
                         quality_check_first_base ,
                         quality_check_last_base  );
        System.exit(0);
    }
    //---------------------


    public  void  runTrimming(String directory_name, double score, int first_base, int last_base,
                int lqr_is_use_lqr ,  int lqr_is_delete_lqr ,
                int quality_check_min_read_length ,int quality_check_score ,
               int quality_check_first_base, int quality_check_last_base )
    {
        try
        {
                double min_pbd_val = (float)Math.pow( (double)10.0, (double)( -score / 10.0 ) );
                File phd_dir = new File(directory_name + File.separator +"phd_dir");
                File [] phdFiles = phd_dir.listFiles();
                System.out.println(phdFiles.length+" files");
                boolean isHighQualityRead = false;
                ArrayList top_elements = null;
                ArrayList botom_elements= null;ArrayList scored_elements = null;
                ArrayList trimmed_scored_elements = null;
                for (int count = 0 ; count< phdFiles.length; count++)
                {
                    top_elements = new ArrayList();
                    botom_elements=  new ArrayList();
                    scored_elements =  new ArrayList();
                    try
                    {
                        System.out.println(phdFiles[count].getName());
                        readPhdFile(phdFiles[count] ,  top_elements,           botom_elements,  scored_elements);
                        System.out.println("read "+phdFiles[count].getName()+" "+scored_elements.size());

                        isHighQualityRead = checkReadQuality(scored_elements , quality_check_min_read_length , quality_check_score ,
                                            quality_check_first_base,  quality_check_last_base );
                        System.out.println("read "+phdFiles[count].getName()+" "+ isHighQualityRead);
                        if ( lqr_is_use_lqr == 1 && !isHighQualityRead)
                        {
                            phdFiles[count].delete();
                            System.out.println("read "+phdFiles[count].getName()+" deleted");
                            continue;
                        }
                        if ( lqr_is_delete_lqr == 1 && !isHighQualityRead)
                        {
                          //  File chromat_file = new File(directory_name + File.separator +"chromat_dir"+ File.separator +phdFiles[count].getName());
                          //  chromat_file.delete();
                            phdFiles[count].delete();
                            continue;
                        }
                        trimmed_scored_elements = trimScoredArray(scored_elements, score,   first_base,  last_base, min_pbd_val);
                        System.out.println("trimmed elm "+phdFiles[count].getName()+" "+trimmed_scored_elements.size());

                        if (trimmed_scored_elements.size() > 50 )
                        {
                            writeTrimedPhdFile( top_elements,  trimmed_scored_elements, botom_elements, phdFiles[count].getAbsolutePath());

                        }
                        else
                            phdFiles[count].delete();

                        System.out.println("phd files :" +phdFiles[count].getName());
                    }
                    catch(Exception e){}
                }
        }
        catch(Exception e){}

    }




    private  void readPhdFile(File phdFile, ArrayList top_elements,
                ArrayList botom_elements, ArrayList scored_elements) throws Exception
    {

        BufferedReader input = null;
       // BufferedWriter output = null;
        boolean isInsideRead = false;
        String line = null; int element_count = 0;
        ScoredElement element = null;
        String phdFile_name = null;
        ArrayList element_entities = null;
        boolean isReadInTopElement = true;
        boolean isReadInScoredElement = false;
        boolean isReadInBottomElement = false;
        try
        {
            phdFile_name = phdFile.getAbsolutePath();
            input = new BufferedReader(new FileReader(phdFile));
            while ((line = input.readLine()) != null)
            {
                if (  line.indexOf("END_DNA" ) != -1)
                {
                    isReadInBottomElement = true;
                    isReadInScoredElement = false;
                 }

                if ( isReadInTopElement  )
                {
                    top_elements.add(line + LINE_SEPARATOR ) ;
                }
                else if ( isReadInScoredElement )
                {
                    element_entities = splitString(line);
                    if ( element_entities.size () == 3 )
                    {
                        element = new ScoredElement( element_entities ) ;
                        scored_elements.add(element );
                    }
                }
                else if   (  isReadInBottomElement)
                {
                    botom_elements.add(line + LINE_SEPARATOR);
                }
                if ( isReadInTopElement && line.indexOf("BEGIN_DNA" ) != -1)
                {
                    isReadInScoredElement = true;
                    isReadInTopElement = false;
                }

            }
            input.close();
      }
        catch (Exception e)
        {

         }
    }

    private boolean checkReadQuality(ArrayList scored_elements ,
    int quality_check_min_read_length , int quality_check_score ,
    int quality_check_first_base,  int quality_check_last_base )
    {
        int total_score = 0; int base_number = 0;ScoredElement element = null;
        if ( scored_elements.size() < quality_check_min_read_length) return false;
        quality_check_last_base = (quality_check_last_base <= scored_elements.size())? quality_check_last_base : scored_elements.size() ;
 System.out.println(quality_check_first_base +" "+ quality_check_last_base+" "+scored_elements.size());
        for (int count = quality_check_first_base; count < quality_check_last_base; count++)
        {

            element = ( ScoredElement ) scored_elements.get( count);
            if ( element.getChar() != 'N' || element.getChar() != 'n')
            {
                total_score += element.getScore(); base_number++;
                // System.out.println(total_score +" "+ base_number);
            }

        }
         System.out.println(total_score +" "+ base_number);
        return ( (int) total_score / base_number > quality_check_score);
    }


    /*
    private  void trimPhdFile(File phdFile, int first_base, int last_base, double score, double min_pbd_val) throws Exception
    {

        BufferedReader input = null;
       // BufferedWriter output = null;
        boolean isInsideRead = false;
        String line = null; int element_count = 0;
        ArrayList scored_elements = new ArrayList ();
        ArrayList trimmed_scored_elements = new ArrayList ();
        ArrayList elements = new ArrayList(); ArrayList element_entities = null;
        ScoredElement element = null;
        String phdFile_name = null;
        first_base = ( first_base > 0) ? first_base : 0 ;
        last_base = ( last_base > 0) ? last_base : 0 ;
        boolean isBeforeDNAEnd = true; boolean isBeforeDNAStart = true;
        try
        {
            phdFile_name = phdFile.getAbsolutePath();
            input = new BufferedReader(new FileReader(phdFile));
        //    File output_file = new File(phdFile_name + "_new");
        //    output = new BufferedWriter(new FileWriter(output_file));
            while ((line = input.readLine()) != null)
            {

                if ( isBeforeDNAStart || ( !isBeforeDNAEnd && !isBeforeDNAStart ) )
                {
                    elements.add(line + LINE_SEPARATOR ) ;
                  //  output.write( line + LINE_SEPARATOR ) ;
                }
                else
                {
                    element_entities = splitString(line);
                    if ( element_entities.size () == 3 )
                    {
                        element = new ScoredElement( element_entities ) ;
                        scored_elements.add(element );
                    }

                }
                if ( isBeforeDNAStart && line.indexOf("BEGIN_DNA" ) != -1)
                    isBeforeDNAStart = false ;
                if (isBeforeDNAEnd && line.indexOf("END_DNA" ) != -1)
                {
                     if (  first_base > 0 ) first_base = (first_base < scored_elements.size() ) ? first_base :  scored_elements.size();
                    if (   last_base== 0 ) last_base = scored_elements.size();
                    if ( last_base > 0) last_base = (last_base > scored_elements.size() ) ? scored_elements.size() : last_base;

                    trimmed_scored_elements = trimScoredArray(scored_elements, score,   first_base,  last_base, min_pbd_val);
                    if (trimmed_scored_elements.size() > 50 )
                    {
                        for (int count = 0; count < trimmed_scored_elements.size(); count++)
                        {
                            elements.add( ((ScoredElement)trimmed_scored_elements.get(count)).toString());
                        }
                    }
                    else
                    {
                        for (int count = 0; count < scored_elements.size(); count++)
                        {
                            elements.add( ((ScoredElement)scored_elements.get(count)).toString());
                        }
                    }
                        //writeScoredArray(scored_elements, output);
                    isBeforeDNAEnd = false;
                    //output.write( line + LINE_SEPARATOR ) ;
                    elements.add(line + LINE_SEPARATOR);
                }
            }
            //output.flush();            output.close();
            input.close();

            FileWriter output = new FileWriter(phdFile_name);
            for (int count = 0; count< elements.size(); count++)
            {
                output.write((String)elements.get(count));
            }
            output.flush();output.close();
         //   saveAsFile( phdFile_name, output_file.getAbsolutePath() );
           // output_file.delete();
        }
        catch (Exception e)
        {

         }
    }
*/
    private      ArrayList  trimScoredArray(ArrayList scored_elements, double score,  int first_base, int last_base, double min_pbd_val)
    {
        ArrayList trimmed_scored_elements = new ArrayList();
        int[] trimmed_coordinates = new int[2];
        if (score > 5)
           trimmed_coordinates =  trimSequencePhredAlgorithm(scored_elements, score,   first_base,  last_base,  min_pbd_val );
        else
        {
            trimmed_coordinates[0] = first_base;
            if ( last_base <= first_base) trimmed_coordinates[1] = first_base;
            else
            {
                trimmed_coordinates[1] = (last_base >= scored_elements.size() ) ? last_base : scored_elements.size() ;
            }
        }
        for(int count = trimmed_coordinates[0]; count < trimmed_coordinates[1]; count++)
        {
            trimmed_scored_elements.add( scored_elements.get(count) );
        }
        return trimmed_scored_elements ;
    }

    private   void          writeScoredArray(ArrayList scored_elements,
                            BufferedWriter output) throws Exception
    {
        ScoredElement element = null;
        for (int count = 0; count < scored_elements.size() ; count++)
        {
            element = (ScoredElement) scored_elements.get(count);
            output.write( element.toString() );
        }
        output.flush();
    }

     private   void          writeTrimedPhdFile(ArrayList top_elements,
                ArrayList scored_elements, ArrayList botom_elements,
                String phdFile_name )throws Exception
     {
            File phdFile = new File(phdFile_name );
            BufferedWriter output = new BufferedWriter(new FileWriter(phdFile));
            for (int count = 0; count < top_elements.size() ; count++)
            {
                  output.write( (String)top_elements.get(count) );
            }
            writeScoredArray( scored_elements,  output);
            for (int count = 0; count < botom_elements.size() ; count++)
            {
               output.write( (String)botom_elements.get(count) );
            }
            output.flush();
            output.close();
     }



    private     int[]     trimSequencePhredAlgorithm  (ArrayList scored_elements, double score,  int first_base, int last_base, double min_pbd_val )
    {
        //** Find the maximum scoring subsequence.
          int start       = 0;          int end       = 0;
          int tbg       = 0;
          float probScore = 0.0F;          float maxScore  = 0.0F;
          float qualScore = 0.0F;          float maxQualScore = 0.0F;
          int count = 0;
          ScoredElement element = null;
          int[] trimmed_coordinates = new int[2];
          last_base = ( last_base == 0)? scored_elements.size() : last_base;
          for(count = first_base; count < last_base; ++count )
          {
            element = (ScoredElement) scored_elements.get(count);
            probScore +=  min_pbd_val - (float)Math.pow( (double)10.0, (double)( -element.getScore() / 10.0 ) );
            qualScore += (float)element.getScore();
            if( probScore <= 0.0 )
            {
              probScore = 0.0F;
              qualScore = 0.0F;
              tbg = count + 1;
            }
            if( probScore > maxScore )
            {
              start          = tbg;
              end          = count;
              maxScore     = probScore;
              maxQualScore = qualScore;
            }
          }
           /*
          ** Filter out very short sequences and sequences
          ** with low overall quality.
          */
          if( end - start + 1 < MIN_SEQ_LEN ||
              ( maxQualScore / (float)( end - start + 1 ) ) < score )
          {
            start = 0;
            end = 0;
          }
         trimmed_coordinates[0] = start;
         trimmed_coordinates[1] = end;
         return trimmed_coordinates;
   }

   private  void saveAsFile( String new_file_name, String old_file_name) throws Exception
   {
        File new_file = null; File old_file = null;
        BufferedWriter new_file_writer = null;
        BufferedReader old_file_reader = null;
        String line = null;
        try
        {
            new_file =   new File(new_file_name); old_file = new File(old_file_name);
            new_file_writer = new BufferedWriter( new FileWriter(new_file) );
            old_file_reader = new BufferedReader(new FileReader( old_file ) );
            while ((line = old_file_reader.readLine()) != null)
            {
                new_file_writer.write( line + LINE_SEPARATOR);
             }
            new_file_writer.flush();new_file_writer.close();
            old_file_reader.close();
        }
        catch(Exception e)
        {
        }

    }

   private  static String convertUnixFileNameIntoWindows(String filename)
    {
        //                /c/bio/plate_analysis/clone_samples/3277/4426
        String res = null;
        String directory_structure = null;
        if (filename.charAt(0) == '/') filename = filename.substring(1);
        int drive_separator = filename.indexOf( '/' );
        res = filename.substring(0, drive_separator )+":" + File.separator;
        directory_structure = filename.substring( drive_separator + 1);
        directory_structure = directory_structure.replace('/','\\');

        return res + directory_structure ;
    }

   private  static String convertUnixFileNameIntoWindowsBasedOnOneLetterApproach(String filename)
    {
        //                /c/bio/plate_analysis/clone_samples/3277/4426
        String res = null;
        ArrayList items = splitString(filename, "/") ;
        boolean isInsideName = false;
        for (int count = 0 ; count < items.size(); count++)
        {
            if ( ((String)items.get(count)).length() == 1)
            {
                res = (String)items.get(count)+":"+ File.separator;
                isInsideName = true;
                continue;
            }
            if ( isInsideName) res += (String)items.get(count)+ File.separator;
        }
        return res  ;
    }

   private  static ArrayList splitString(String value)
    {
       return splitString( value, null);

   }
   private static ArrayList splitString(String value, String spliter)
    {
        ArrayList res = new ArrayList();
        StringTokenizer st  = null;
        if (spliter == null)
            st = new StringTokenizer(value);
        else
            st = new StringTokenizer(value, spliter);
        while(st.hasMoreTokens())
        {
            String val = st.nextToken().trim();
            res.add( val );
        }
        return res;
    }

    protected class ScoredElement
    {
        private char i_base = '\u0000';
        private int  i_score = -1;
        private String  i_number = null;

        public ScoredElement(ArrayList elements)
        {
            if (elements == null || elements.size() != 3) return;
            i_base = ((String)elements.get(0)).charAt(0);
            i_score = Integer.parseInt( (String)elements.get(1));
            i_number = (String)elements.get(2);
        }

        public          int   getScore(){ return i_score;}
        public          String   getNumber(){ return i_number ; }
        public          char  getChar(){ return i_base;}

        public          String  toString(){ return i_base +" "+i_score +" "+i_number + LINE_SEPARATOR; }


    }
}
