//package flex.process;
import java.util.*;

public class Protocol
{
   public static final Protocol IDENTIFY_SEQUENCE = new Protocol();
   public static final Protocol APPROVE_SEQUENCE = new Protocol();
   public static final Protocol DESIGN_CONSTRUCTS = new Protocol();
   public static final Protocol CREATE_OLIGO_ORDER = new Protocol();
   public static final Protocol RECEIVE_OLIGO_ORDER = new Protocol();
   public static final Protocol CREATE_PCR = new Protocol();
   public static final Protocol CREATE_GEL = new Protocol();
   public static final Protocol CREATE_FILTER = new Protocol();
   public static final Protocol CREATE_BP = new Protocol();
   public static final Protocol CREATE_TRANSFORMATION = new Protocol();
   public static final Protocol CREATE_CULTURE = new Protocol();
   public static final Protocol CREATE_DNA = new Protocol();

   public void check()
   {}

   private Protocol()
   {}
} // Protocol
