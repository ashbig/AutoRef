@ c:
@ cd c:\test\
@ ftp -s:c:\bec\bec\docs\script_ftpcommands.txt 128.103.32.164 
@ copy humangenes c:\test\Human\genes /y
@ del c:\test\humangenes
@ c:\blast_new\formatdb -pF -oT -i C:\test\Human\genes
@ copy yeastgenes c:\test\Yeast\genes /y
@ del c:\test\yeastgenes
@ c:\blast_new\formatdb -pF -oT -i C:\test\Yeast\genes
@ copy mgcgenes c:\test\MGC\genes /y
@ del c:\test\mgcgenes
@ c:\blast_new\formatdb -pF -oT -i C:\test\MGC\genes
@ copy pseudomonasgenes c:\test\Pseudomonas\genes /y
@ del c:\test\pseudomonasgenes
@ c:\blast_new\formatdb -pF -oT -i C:\test\Pseudomonas\genes
@ cd..
