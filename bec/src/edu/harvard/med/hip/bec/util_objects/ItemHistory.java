//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * ItemHistory.java
 *
 * Created on November 4, 2003, 5:09 PM
 */

package edu.harvard.med.hip.bec.util_objects;

import java.util.*;
/**
 *
 * @author  HTaycher
 */
public class ItemHistory
{
    
   public static final   int   HISTORY_PROCESSED = 1;
       public static final     int  HISTORY_FAILED = -1;
       
        private     String          i_item_id = null;
        private     ArrayList       i_history_items = null;
        private     int             i_status = HISTORY_FAILED;
        
        public ItemHistory(String itemid, ArrayList history_items, int status)
        {
            i_status = status;
            i_item_id = itemid;
            i_history_items=history_items;
        }
        public     String           getItemId(){ return i_item_id ;}
        public     ArrayList        getHistory(){ return i_history_items ;}
        public     int              getStatus(){ return i_status ;}
        
    
}
