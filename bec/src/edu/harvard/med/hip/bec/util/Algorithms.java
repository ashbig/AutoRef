package edu.harvard.med.hip.bec.util;


import java.util.*;


import java.io.*;

public class Algorithms
{
    
        /**
	 * This function takes the string and converts it into a reversed sequence.
	 * @param seq  A String object
	 * @return A String object
	 */
    public static String reverseString(String str)
    {
        String copy = "";  // The reversed copy.
        //       from str.length() - 1 down to 0.
        for ( int i = str.length() - 1;  i >= 0;  i-- )
        {
            // Append i-th char of str to copy.
            copy += str.charAt(i);
        }
        return copy;
    }
    public static String cleanWhiteSpaces(String str)
    {
        String copy = "";  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if (Character.isLetterOrDigit(ar[i]) ) copy += ar[i];
          
        }
        
        return copy;
    }
    
   
    public static int findFirstLetter(String str)
    {
  
        char[] ar =str.toCharArray();
        return findFirstLetter(ar);
    }
     public static int findFirstLetter(char[] ar)
    {
       int count = 0;
        for ( ; count < ar.length;  count++ )
        {
               if (Character.isLetter(ar[count]) ) break;
        }
        return count;
    }
    
    public static String cleanChar(String str, char ch)
    {
        String copy = "";  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if (ar[i] != ch ) 
                copy += ar[i];
          
        }
        
        return copy;
    }
   
    
    
    public static String replaceChar(String str, char oldch, char newchar)
    {
        StringBuffer copy = new StringBuffer();  // The reversed copy.
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if (ar[i] == oldch ) 
                copy.append( newchar );
            else
                copy.append( ar[i]);
        }
        
        return copy.toString();
    }
    
    
    
    public static String replaceString(String str, String oldstr, String newstr)
    {
        StringBuffer copy = new StringBuffer();  // The reversed copy.
        int curindex = 0;
        for (int count=0;count < str.length();count++)
        {
            curindex=str.indexOf(oldstr);
            if (curindex == -1 || curindex + 1 > str.length()) break;
            str = str.substring(0,curindex)+newstr +str.substring(curindex+oldstr.length());
            count=curindex+1;
        }
   
        return str;
    }
    
     public static ArrayList splitString(String value)
    {
        return  splitString( value, null);
    
    }
    public static ArrayList splitString(String value, String spliter)
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
    
    public static int[] getConvertStringToIntArray(String scores, String delim)
    {
        if ( scores == null ) return null;
        ArrayList arr_scores = Algorithms.splitString(scores,delim);
        int[] scores_numbers = new int[arr_scores.size()];
        for (int count = 0; count < arr_scores.size(); count++)
        {
            scores_numbers[count] =  (Integer.parseInt( (String) arr_scores.get(count)));
        }
        
        return scores_numbers;
    }
   
     // used to create quevery where parameter in range
    public static  String convertArrayToString(int[] arr, String delim)
    {
        StringBuffer res = new StringBuffer();
        if ( arr == null) return null;
        for (int count = 0; count < arr.length;count++)
        {
            res.append( arr[count]);
            if (count != arr.length - 1 ) res .append(delim);
        }
        return res.toString();
    }
    
     public static  String convertArrayToString(String[] arr, String delim)
    {
        StringBuffer res = new StringBuffer();
        if ( arr == null) return null;
        for (int count = 0; count < arr.length;count++)
        {
            res.append( arr[count]);
            if (count != arr.length - 1 ) res .append(delim);
        }
        return res.toString();
    }
    public static  String convertStringArrayToString(ArrayList arr, String delim)
    {
        StringBuffer res = new StringBuffer();
        if (arr == null) return null;
        for (int count = 0; count < arr.size();count++)
        {
            res.append((String)arr.get(count));
            if (count != arr.size()- 1 ) res.append(delim);
        }
        return res.toString();
    }
    
    
 
    public static int numberOf(String str, char ch)
    {
        int res = 0;
        char[] ar =str.toCharArray();
        //       from str.length() - 1 down to 0.
        for ( int i = 0; i < ar.length;  i++ )
        {
            if ( ar[i] == ch)
                res++;
          
        }
        
        return res;
    }
    //array manipulation
    // mode define type of array members
  
    public static ArrayList findArrayOverlap(ArrayList array1, ArrayList array2)
    {
        ArrayList res = new ArrayList();
        ArrayList small_array = null;ArrayList large_array = null;
        if (array1.size() <= array2.size())
        {
            small_array = array1;
            large_array = array2;
        }
        else
        {
            large_array = array1;
             small_array = array2;
        }
        for (int ind = 0; ind <  small_array.size(); ind++)
        {
            if (  large_array.contains( small_array.get(ind)))
            {
                res.add(small_array.get(ind));
            }
        }
        return res;
    }
 /*   
    //convert well nomenculature from A10 to int
    public static int convertWellFromA8_12toInt( String well) 
    {
        int position = -1;
        well = well.toLowerCase();

        int row = (int)well.charAt(0);

        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
               
        
        row_value = row - a_value + 1;
     
        return (column - 1) * 8 +  row_value ;
        
    }
    */
    public static int convertWellNumberIntoRowNumber(int position)
    {
        int row_number = position % 8 ;
        if ( row_number == 0) row_number= 8;
        return  row_number;
    }
    
    public static int convertWellNumberIntoColNumber(int position)
    {
        if ( position % 8 == 0 ) position --;
        return (int)Math.ceil(  position / 8)+1;
    }
    
    /*
     //convert well nomenculature from A10 to int
    public static String convertWellFromInttoA8_12( int well) 
    {
        String position = null;
                
        int a_value = (int) 'A';
        int row_value = well % 8;
        int column = (int) well / 8  +1 ;   
        char rowname = (char) (a_value + row_value - 1);
        String column_string ="";
        
        if (row_value == 0 )
        {
             if (column - 1 < 10)
                return "H" +  "0"+(column -1 );
            else
                return "H" +  (column -1);
        }
     
        else
        {
            if (column < 10)
                return "" +  rowname+"0"+column;
            else
                return "" +  rowname+column;
        }
        
  
    }
    */
    
    public static String convertWindowsFileNameIntoUnix(String filename)
    {
        String res = null;
        res = cleanChar(filename,':');
        if (res.charAt(0) != '/')
            res = "/"+res;
        res = replaceChar(res, File.separatorChar, '/');
        return res;
    }
    
    public static void writeArrayIntoFile( ArrayList messages, boolean isAppend, String file_name) throws Exception
    {
         writeArrayIntoFile(  messages,  isAppend,  file_name, true);
    }
    
    public static void writeArrayIntoFile( ArrayList messages, boolean isAppend, String file_name, boolean isPrintLineSeparator) throws Exception
    {
         FileWriter fr = null;
         try
         {
            fr =  new FileWriter(file_name, isAppend);
            for (int count = 0; count < messages.size(); count++)
            {
                    fr.write((String) messages.get(count));
                    if (isPrintLineSeparator) fr.write("\n");
            }
            fr.flush();
            fr.close();
          
        }
         catch(Exception e )
         {
             throw new BecUtilException("Can not write file " + file_name +"\n"+ e.getMessage());
         }
    
    }
    
    
    public static final int COMPARE_TYPE_RETURN_COMMON_MEMBERS = 0;
    public static final int COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY1_ONLY = 1;
    public static final int COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY2_ONLY = 2;
    //acssepts two sorted lists
    public static ArrayList  compareTwoLists( int mode, ArrayList ar_1,ArrayList ar_2)
    {
        ArrayList result = new ArrayList();
        int count_1 = 0; int count_2 = 0;
        while(count_1 < ar_1.size() && count_2 < ar_2.size()) 

        {
            int res = ((String)ar_1.get(count_1)).compareTo((String)ar_2.get(count_2));
            if( res == 0)
            {
                if ( mode == COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY1_ONLY )   result.add( ar_1.get(count_1));
                count_1++; count_2++;
            }
            else if ( res < 0 )
            {
                if ( mode == COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY1_ONLY )  result.add( ar_1.get(count_1));
                count_1++;
            }
            else if ( res > 0 )
            {
                if ( mode == COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY2_ONLY ) result.add( ar_2.get(count_2));
                count_2++;
            }
      }

      if ( mode == COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY1_ONLY)
        { 
            for (int countn = count_1; countn < ar_1.size(); countn++)
            {
                result. add( ar_1.get(countn));
            }
        }
         if ( mode == COMPARE_TYPE_RETURN_MEMBERS_FROM_ARRAY2_ONLY)
        { 
            for (int countn = count_2; countn < ar_2.size(); countn++)
            {
                result. add( ar_2.get(countn));
            }
        }
        return result;
    }
    
    
    
    
      // mode: 0 - return items common in both files;
    // 1 return itemes found in one, but not in 2
    // 2 return item not found in one, but found in 2
    public static ArrayList compareTwoLists(ArrayList ar1, ArrayList ar2, int mode)
    {
        ArrayList result = new ArrayList();
        String line = null;
       
        ar1 = sortStringArrayList(ar1);
        ar2 = sortStringArrayList(ar2);

        int count_1 = 0; int count_2 = 0;
        while(count_1 < ar1.size() && count_2 < ar2.size()) 

        {
            int res = ((String)ar1.get(count_1)).compareTo((String)ar2.get(count_2));
            if( res == 0)
            {
                if ( mode == 0 )   result.add( ar1.get(count_1));
                count_1++; count_2++;
            }
            else if ( res < 0 )
            {
                if ( mode == 1 )  result.add( ar1.get(count_1));
                count_1++;
            }
            else if ( res > 0 )
            {
                if ( mode == -1 ) result.add( ar2.get(count_2));
                count_2++;
            }
      }

      if ( mode == 1)
        { 
            for (int countn = count_1; countn < ar1.size(); countn++)
            {
                result. add( ar1.get(countn));
            }
        }
         if ( mode == - 1)
        { 
            for (int countn = count_2; countn < ar2.size(); countn++)
            {
                result. add( ar2.get(countn));
            }
        }
       return result;

    }
         
        
     public static ArrayList sortStringArrayList(ArrayList arr)
    {
          //sort array by containerid and position
            Collections.sort(arr, new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    String cl1 = (String)o1;
                    String cl2 = (String)o2;
                    return cl1.compareTo( cl2);
                     
                }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
        
        return arr;
    }    
  
    public static void main(String args[])
    {
      
       System.exit(0);
    }
    
    
}


