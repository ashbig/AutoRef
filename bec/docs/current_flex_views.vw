-- C:\BEC\bec\docs\current_flex_views.vw
--
-- Generated for Oracle 8.1 on Tue Sep 16  16:08:58 2003 by Server Generator 6.5.91.0.9
 

PROMPT Creating View 'VIEW_FLEXBECCONSTRUCT'
CREATE OR REPLACE FORCE VIEW VIEW_FLEXBECCONSTRUCT
 (BECCONSTRUCTID
 ,FLEXCONSTRUCTID
 ,FORMAT
 ,REFSEQUENCEID
 ,CLONINGSTRATEGYID)
 AS SELECT AGG.CONSTRUCTID BECCONSTRUCTID
          ,FLO.FLEXCONSTRUCTID FLEXCONSTRUCTID
          ,AGG.FORMAT FORMAT
          ,AGG.REFSEQUENCEID REFSEQUENCEID
          ,AGG.CLONINGSTRATEGYID CLONINGSTRATEGYID
FROM ISOLATETRACKING ISG
    ,FLEXINFO FLO
    ,SEQUENCINGCONSTRUCT AGG
  WHERE flo.isolatetrackingid=isg.isolatetrackingid and agg.constructid=isg.constructid
/

PROMPT Creating View 'VIEW_BECFLEXCONTAINER'
CREATE OR REPLACE FORCE VIEW VIEW_BECFLEXCONTAINER
 (BECCONTAINERID
 ,LABEL
 ,FLEXCONTAINERID)
 AS SELECT COR.CONTAINERID BECCONTAINERID
          ,COR.LABEL LABEL
          ,FLO.FLEXSEQUENCINGPLATEID FLEXCONTAINERID
FROM CONTAINERHEADER COR
    ,FLEXINFO FLO
    ,ISOLATETRACKING ISG
    ,SAMPLE SAE
  WHERE FLO.ISOLATETRACKINGID=ISG.ISOLATETRACKINGID AND ISG.SAMPLEID=SAE.SAMPLEID AND SAE.CONTAINERID=COR.CONTAINERID
/

PROMPT Creating View 'VIEW_FLEXBECSEQUENCEID'
CREATE OR REPLACE FORCE VIEW VIEW_FLEXBECSEQUENCEID
 (FLEXSEQUENCEID
 ,BECSEQUENCEID)
 AS SELECT FLO.FLEXSEQUENCEID FLEXSEQUENCEID
          ,AGG.REFSEQUENCEID BECSEQUENCEID
FROM FLEXINFO FLO
    ,ISOLATETRACKING ISG
    ,SEQUENCINGCONSTRUCT AGG
  WHERE flo.isolatetrackingid=isg.isolatetrackingid and isg.constructid=agg.constructid
/

