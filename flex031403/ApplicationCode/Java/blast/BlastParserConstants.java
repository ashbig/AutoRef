/** Store a table of all lexical constants for BlastParser and 
 *  BlastParserTokenManager 
 */

package flex.ApplicationCode.Java.blast;

public interface BlastParserConstants {

  int EOF = 0;
  int EOL = 1;
  int TWOEOLS = 2;
  int NOT_EOL = 3;
  int SPACE = 4;
  int CONSTANT = 5;
  int FLOAT = 6;
  int INTEGER = 7;
  int DIGIT = 8;
  int HOMOLOG_SEPARATOR = 9;
  int SEQ_START = 10;
  int SEQ = 11;
  int SEQ_END = 12;
  int PROGRAM = 32;
  int SEARCH_INFO = 33;
  int _EOL1 = 34;
  int ID = 36;
  int DBPATH = 37;
  int QRYLENGTH = 38;
  int SUB_ID = 39;
  int SUB_LENGTH = 40;
  int ALIGN_EVALUE = 41;
  int ALIGN_IDENTITY = 42;
  int ALIGN_STRAND = 43;
  int ALIGN_SCORE = 44;
  int ALIGN_GAPS = 45;
  int QRY_START = 46;
  int QRY_SEQ = 47;
  int QRY_END = 49;
  int _EOL3 = 51;
  int SUBJ_START = 52;
  int SUBJ_SEQ = 53;
  int SUBJ_END = 55;
  int _EOL4 = 57;
  int MATRIX = 58;
  int GAP = 59;
  int DBSIZE = 60;
  int DBLETTERS = 61;

  int DEFAULT = 0;
  int QRY_ID = 1;
  int DBPATH_STATE = 2;
  int QRYLENGTH_STATE = 3;
  int HOMOLOG_ID = 4;
  int HOMOLOG_LENGTH = 5;
  int ALIGN_INFO = 6;
  int ALIGN_SCORE_STATE = 7;
  int ALIGN_GAP = 8;
  int QRY_START_STATE = 9;
  int QRY_END_STATE = 10;
  int SUBJ_START_STATE = 11;
  int SUBJ_END_STATE = 12;
  int MATRIX_STATE = 13;
  int GAP_STATE = 14;
  int DBSIZE_STATE = 15;
  int DBLETTER_STATE = 16;

  String[] tokenImage = {
    "<EOF>",
    "<EOL>",
    "<TWOEOLS>",
    "<NOT_EOL>",
    "<SPACE>",
    "<CONSTANT>",
    "<FLOAT>",
    "<INTEGER>",
    "<DIGIT>",
    "\">\"",
    "<SEQ_START>",
    "<SEQ>",
    "<SEQ_END>",
    "<token of kind 13>",
    "<token of kind 14>",
    "<token of kind 15>",
    "<token of kind 16>",
    "<token of kind 17>",
    "<token of kind 18>",
    "<token of kind 19>",
    "<token of kind 20>",
    "<token of kind 21>",
    "<token of kind 22>",
    "<token of kind 23>",
    "<token of kind 24>",
    "<token of kind 25>",
    "<token of kind 26>",
    "<token of kind 27>",
    "<token of kind 28>",
    "<token of kind 29>",
    "<token of kind 30>",
    "<token of kind 31>",
    "<PROGRAM>",
    "<SEARCH_INFO>",
    "<_EOL1>",
    "<token of kind 35>",
    "<ID>",
    "<DBPATH>",
    "<QRYLENGTH>",
    "<SUB_ID>",
    "<SUB_LENGTH>",
    "<ALIGN_EVALUE>",
    "<ALIGN_IDENTITY>",
    "<ALIGN_STRAND>",
    "<ALIGN_SCORE>",
    "<ALIGN_GAPS>",
    "<QRY_START>",
    "<QRY_SEQ>",
    "<token of kind 48>",
    "<QRY_END>",
    "<token of kind 50>",
    "<_EOL3>",
    "<SUBJ_START>",
    "<SUBJ_SEQ>",
    "<token of kind 54>",
    "<SUBJ_END>",
    "<token of kind 56>",
    "<_EOL4>",
    "<MATRIX>",
    "<GAP>",
    "<DBSIZE>",
    "<DBLETTERS>",
  };

}
