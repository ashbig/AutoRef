/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpack;

import org.rosuda.JRI.Rengine;

/**
 *
 * @author DZuo
 */
public class RengineManager {

    public static Rengine createRengine() throws RpackException {
        // just making sure we have the right version of everything
        if (!Rengine.versionCheck()) {
            throw new RpackException("R Version mismatch - Java files don't match library version.");
        }

        System.out.println("Creating Rengine (with arguments)");
        // 1) we pass the arguments from the command line
        // 2) we won't use the main loop at first, we'll start it later
        //    (that's the "false" as second argument)
        // 3) the callbacks are implemented by the TextConsole class above
        Rengine re = new Rengine(null, false, null);
        System.out.println("Rengine created, waiting for R");
        // the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            throw new RpackException("Cannot load R");
        }

        return re;
    }
    
    public static void stopRengine(Rengine re) {
        if(re != null)
            re.end();
        System.out.println("Rengine stopped.");
    }
}
