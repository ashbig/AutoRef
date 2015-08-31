# MakeRefDB.py
# Ashkan Bigdeli
# 
#
# Connects to NCBI's ftp site and downloads the latest Human Reference Genome.
# Unzips and connactinates files to create a local, blastable database.
#
# Usage: python MakeRedDB.py /path/to/executable/makeblastdb /path/to/place/sequences
#
# System Requirments: YOU MUST HAVE A LOCAL COPY OF Blast 2.2.30+ OR GREATER!
#                     Available @ ftp://ftp.ncbi.nlm.nih.gov/blast/executables/blast+/LATEST/
#                     Please create the folder with which to place the database.


import os, gzip, glob, os.path, sys
from ftplib import FTP


# download
#
# @param1 = filenames: list of all files in ftp available for download.
# @param2 = file_id: specify which files in folder to be downloaded.
# @param3 = download_to: folder to place downloaded files.
# @param4 = ftp: ftp site from which to download
#
# Will download all specified files to a designated folder.
def download(filenames, file_id, download_to,ftp):    
    for filename in filenames:
        if filename.endswith(file_id):
            try:
                local_filename = os.path.join(download_to, filename)
                file = open(local_filename, 'wb')
                ftp.retrbinary('RETR ' + filename, file.write)
                file.close()
            except IOError:
                print "There has been an IO error! Please check all file paths."

# unzip
#
# @param1 = download_to: folder to place downloaded files.
#
# Uses gzip import to unzip files within the same folder.
def unzip(download_to):
    
    try:
        # for each file in the directory
        for gzip_path in glob.glob(download_to + "/*"):
            if os.path.isdir(gzip_path) == False:
                in_file = gzip.open(gzip_path, 'rb')
            
                # uncompress the file into temp
                temp = in_file.read()
                in_file.close()

                # get the filename
                gzip_filename = os.path.basename(gzip_path)
            
                # get original filename and remove the extension
                filename = gzip_filename[:-3]
                uncompressed_path = os.path.join(download_to, filename)

                # write uncompressed file
                open(uncompressed_path, 'w').write(temp)
    except Exception:
        print "There was a problem unzipping your files :( Check GZip."

# remove
#
# @param1 = suffix: file type to be removed.
# @param2 = download_to: folder to remove downloaded files.

# Removes all given file types from folder.
def remove(suffix, download_to):
    try:
        files_in_path = download_to + suffix
        files = glob.glob(files_in_path)
        for f in files:
            os.remove(f)
    except IOError:
        print "There was an IO error deleting your files, check your file path and authority!"
                         
# concat fasta
#
# @param1 = filenames: all files to be concatenated.
# @param2 = download_to: folder to concatenate files.
#
# Opens ALL files in folder and writes them to one .fasta file.
def concat_fasta(db_name, download_to):
    # create file list of all files to concatenate
    files_in_path = download_to + '*'
    filenames = glob.glob(files_in_path)
    db_path = download_to + db_name + '.fasta'
    try:
        #open each and write to single file
        with open( db_path, 'w') as outfile:
            for filename in filenames:
                with open(filename) as infile:
                    for line in infile:
                        outfile.write(line)
        outfile.close()
    except IOError:
        print "There was an IO error concatinating your files, check your file path and authority!"
        
# create_db
#
# @param1 = blast_path: path at which Blast program is locally downloaded.
# @param2 = db_type:  the type of database to be created. i.e nucleotide
# @param3 = db_name: the final name of your database.
# @param4 = download_to: the folder in which to place the database
#
# Makes an external call to local blast program and creates desired blast database.

def create_db(blast_path, db_type, db_name, download_to):
    try:
        os.chdir(download_to)
        os.system(blast_path + ' -in ' + db_name + '.fasta' + ' -dbtype ' + db_type + ' -out ' + db_name)
    except Exception:
        print "There was an error creating your blast db, check you have blast installed locally!"
        
        
def main():       
    
    # get system specific information from user
    if len(sys.argv) !=3:
        print "Please Enter the Blast path, followed by the folder to place your database!"
        print "Windows Example: python MakeRefDB.py C:\\NCBI\\blast-2.2.30+\\bin\\makeblastdb C:\\RefSeqDB\\"
    
    blast_path = sys.argv[1]
    download_to =sys.argv[2]
    

    # file type to download
    file_id = '.genomic.fna.gz'
    
    # set DB name
    db_name = "Human_Genomic_All"

    # type of blast database, i.e 'nucl' = nucleotide
    db_type = 'nucl'
    
    # clear folder of previous entries or databases
    remove('*', download_to)
    
    try:
        # set ftp location
        ftp = FTP('ftp.ncbi.nlm.nih.gov')
        
        # login to ftp
        # Omitting ftp.login('USERNAME', 'PASSWORD') will login as anonymous
        ftp.login()
    
        # Change directory in ftp to navigate to desired genome
        ftp.cwd('/refseq/H_sapiens/RefSeqGene/')
    
        # Create a list of the filenames in this location
        filenames = ftp.nlst()
        
        print "Downloading the latest Reference Genome ...."
        download(filenames, file_id, download_to,ftp)
        ftp.quit()
    except Exception:
        print "There has been a download issue, please check status of the ftp."
    
    print "Unzipping downloads.."
    unzip(download_to)
    
    # remove all compressed files
    remove('*.gz', download_to)
    
    # concate files and remove once concatenated
    print "Concatinating files..."
    concat_fasta(db_name, download_to)
    remove('*.fna', download_to)
    
    # profit!
    print "Creating Database"
    create_db(blast_path,db_type,db_name, download_to)
    print "Complete! Please check " + download_to + " to begin use :)"
main()                                 