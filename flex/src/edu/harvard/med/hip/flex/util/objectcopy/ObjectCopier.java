/*
 * ObjectCopier.java
 *
 * Created on October 4, 2007, 4:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.util.objectcopy;


 import java.io.*;
import java.util.*;
/**
 *
 * @author htaycher
 */
public class ObjectCopier 
{
/**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
    public static Object fast_copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            FastByteArrayOutputStream fbos =
                    new FastByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(fbos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Retrieve an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in =
                new ObjectInputStream(fbos.getInputStream());
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of
 * objects. Objects are first serialized and then deserialized. Error
 * checking is fairly minimal in this implementation. If an object is
 * encountered that cannot be serialized (or that references an object
 * that cannot be serialized) an error is printed to System.err and
 * null is returned. Depending on your specific application, it might
 * make more sense to have copy(...) re-throw the exception.
 *
 * A later version of this class includes some minor optimizations.
 */

    /**
     * Returns a copy of the object, or null if the object cannot
     * be serialized.
     */
    

    public static Object copy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

     public static InputStream copyInputStream(InputStream orig) throws Exception
     {
       try 
        {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len=0;

            while((len=orig.read(buf))>0)
            {              bos.write(buf,0,len);}


            // Make an input stream from the byte array and read
            // a copy of the object back in.
         /*   ObjectInputStream in = new ObjectInputStream(
                new ByteArrayInputStream(bos.toByteArray()));
            obj = (InputStream) in.readObject();*/
            ByteArrayInputStream in = new ByteArrayInputStream(bos.toByteArray());
            return in;
        }
        catch(Exception e) 
        {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    
    }
     
public static void main(String[] args) {
        // Make a reasonable large test object. Note that this doesn't
        // do anything useful -- it is simply intended to be large, have
        // several levels of references, and be somewhat random. We start
        // with a hashtable and add vectors to it, where each element in
        // the vector is a Date object (initialized to the current time),
        // a semi-random string, and a (circular) reference back to the
        // object itself. In this case the resulting object produces
        // a serialized representation that is approximate 700K.
        Hashtable obj = new Hashtable();
        for (int i = 0; i < 100; i++) {
            Vector v = new Vector();
            for (int j = 0; j < 100; j++) {
                v.addElement(new Object[] {
                    new Date(),
                    "A random number: " + Math.random(),
                    obj
                 });
            }
            obj.put(new Integer(i), v);
        } 

        int iterations = 10;

        // Make copies of the object using the unoptimized version
        // of the deep copy utility.
        long unoptimizedTime = 0L;
        for (int i = 0; i < iterations; i++) {
            long start = System.currentTimeMillis();
            Object copy = ObjectCopier.copy(obj);
            unoptimizedTime += (System.currentTimeMillis() - start);

            // Avoid having GC run while we are timing...
            copy = null;
            System.gc();
        }

        // Repeat with the optimized version
        long optimizedTime = 0L;
        for (int i = 0; i < iterations; i++) {
            long start = System.currentTimeMillis();
            Object copy = ObjectCopier.fast_copy(obj);
            optimizedTime += (System.currentTimeMillis() - start);

            // Avoid having GC run while we are timing...
            copy = null;
            System.gc();
        }

        System.out.println("Unoptimized time: " + unoptimizedTime);
        System.out.println("  Optimized time: " + optimizedTime);
        try
        {
             BufferedReader in = null;  String line = null;
          
            InputStream s = new FileInputStream("Z:\\HTaycher\\HIP projects\\ORF_clones\\ORF_Submission\\for_submission\\finished\\clone_sequences.txt");
           
            InputStream sc = ObjectCopier.copyInputStream(s);
            in = new BufferedReader(new InputStreamReader(s));
        
            while((line = in.readLine()) != null) 
            {System.out.println(line);}
              
            in = new BufferedReader(new InputStreamReader(sc));
        
            while((line = in.readLine()) != null) 
            {System.out.println(line);}
        }
        catch(Exception e){}
    }
}
