@ d:
@ cd d:\blast_db
@ ftp -s:d:\bec\bec\docs\script_ftpcommands.txt 128.103.32.164 
@ copy humangenes d:\blast_db\Human\genes /y
@ del d:\blast_db\humangenes
@ d:\bio_programs\Blast\formatdb -pF -oT -i d:\blast_db\Human\genes
@ copy yeastgenes d:\blast_db\Yeast\genes /y
@ del d:\blast_db\yeastgenes
@ d:\bio_programs\Blast\formatdb -pF -oT -i d:\blast_db\Yeast\genes
@ copy mgcgenes d:\blast_db\MGC\genes /y
@ del d:\blast_db\mgcgenes
@ d:\bio_programs\Blast\formatdb -pF -oT -i d:\blast_db\MGC\genes
@ copy pseudomonasgenes d:\blast_db\Pseudomonas\genes /y
@ del d:\blast_db\pseudomonasgenes
@ d:\bio_programs\Blast\formatdb -pF -oT -i d:\blast_db\Pseudomonas\genes
@ del d:\blast_db\yp
@ d:\bio_programs\Blast\formatdb -pF -oT -i d:\blast_db\yp\genes
@ cd..
