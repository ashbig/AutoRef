@ c:
@ cd c:\blast_db
@ ftp -s:c:\bec\bec\docs\script_ftpcommands.txt 128.103.32.164 
@ copy humangenes c:\blast_db\Human\genes /y
@ del c:\blast_db\humangenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\Human\genes
@ copy yeastgenes c:\blast_db\Yeast\genes /y
@ del c:\blast_db\yeastgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\Yeast\genes
@ copy mgcgenes c:\blast_db\MGC\genes /y
@ del c:\blast_db\mgcgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\MGC\genes
@ copy pseudomonasgenes c:\blast_db\Pseudomonas\genes /y
@ del c:\blast_db\pseudomonasgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\Pseudomonas\genes
@ copy ypgenes c:\blast_db\YP\genes /y
@ del c:\blast_db\ypgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\YP\genes
@ copy ypgenes c:\blast_db\FT\genes /y
@ del c:\blast_db\ftgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\FT\genes
@ copy ypgenes c:\blast_db\Clontech\genes /y
@ del c:\blast_db\clontechgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\Clontech\genes
@ copy ypgenes c:\blast_db\NIDDK\genes /y
@ del c:\blast_db\NIDDKgenes
@ c:\blast_new\formatdb -pF -oT -i c:\blast_db\NIDDK\genes
@ cd..

