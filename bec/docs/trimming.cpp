
#include <sys/types.h>
#include <dirent.h>
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <vector>
#include <string>

const double				MIN_AVG_QUAL = 10.0;
const int						MIN_SEQ_LEN = 50;
const double				MIN_PRB_VAL = 0.05;
const int					FIRST_BASE = 0;
const int					LAST_BASE = 0;


struct sDNAElem
{
	char							base;
	int								score;
	int								n2;
};

struct sTrimParams
{
	sTrimParams()
		:
		minPbdValue(MIN_PRB_VAL),
		minScore(MIN_AVG_QUAL),
		minSeqLength(MIN_SEQ_LEN),
		firstBase(FIRST_BASE),
		lastBase(LAST_BASE)

	{};

	double						minPbdValue;
	double						minScore;
	int								minSeqLength;
	int							firstBase ;
	int 						lastBase;
};

bool readDirFiles(const std::string& dirName, std::vector<std::string>& files)
{
  DIR*              dir;

  dir = opendir(dirName.c_str());

  if (dir == NULL)
  {
    return false;
  }

  files.resize(0);

  for(dirent* dirEnt = readdir(dir);dirEnt != NULL;dirEnt = readdir(dir))
  {
    //printf ("Read %s\n", dirEnt->d_name);
    if (dirEnt->d_name[0] != '.' && dirEnt->d_name[0] != '\0')
    {
      files.push_back(dirName + "/" + std::string(dirEnt->d_name));
     // printf ("Added\n");
    }
  }
  closedir(dir);

  return true;
}

void trimDNAArray(const std::vector<sDNAElem>& dna, const sTrimParams& params,
											int& trimStart, int& trimEnd)
{
//	trimStart = 0;
//	trimEnd = dna.size();
	float						probScore = 0.0;
	float						maxScore = 0.0;
	float						qualScore = 0.0;
	float						maxQualScore = 0.0;
	int								currStart=params.firstBase;


    int last_base_to_process = dna.size();

    if ( params.lastBase > 0 && last_base_to_process < dna.size() )
    {
    	last_base_to_process = params.lastBase;
	}

    //printf ("\t first = %i\n", params.firstBase);
    //printf ("\tlast = %i\n", last_base_to_process);
	for (int count = params.firstBase; count < last_base_to_process;count++)
	{

		probScore += params.minPbdValue - (float)pow((double)10.0, (double)(-dna[count].score / 10.0));
		qualScore += (float)dna[count].score;

		//printf ("\t count = %i, maxScore = %f, maxQualScore = %f, trimStart = %i, trimEnd = %i\n",	count,	maxScore, maxQualScore, trimStart, trimEnd);

		//printf ("elem %d : probScore = %f, qualScore = %f\n", count, probScore, qualScore);
		if (probScore <= 0.0)
		{
			probScore = 0;
			qualScore = 0;
			currStart = count + 1;		// Just predict...
		}
		if (probScore > maxScore)
		{
			trimStart = currStart;
			trimEnd = count + 1;
			maxScore = probScore;
			maxQualScore = qualScore;
			//printf ("\t count = %i, maxScore = %f, maxQualScore = %f, trimStart = %i, trimEnd = %i\n",	count,	maxScore, maxQualScore, trimStart, trimEnd);
		}
	}

//printf ("\tmaxScore = %f, maxQualScore = %f, trimStart = %d, trimEnd = %d\n",					maxScore, maxQualScore, trimStart, trimEnd);
	if (trimEnd - trimStart < params.minSeqLength ||
			maxQualScore / float(trimEnd - trimStart) < params.minScore)
	{
		trimStart = trimEnd = 0;
	}
}

void dumpTrimmedDNAArray(const std::vector<sDNAElem>& dna, int& trimStart, int& trimEnd, FILE* fileOut)
{
	for (int i = trimStart;i < trimEnd;i++)
	{
		fprintf (fileOut, "%c %d %d\n", dna[i].base, dna[i].score, dna[i].n2);
	}
}

bool processDNAFile(const std::string& fileName, const sTrimParams& params)
{
	int								status = 0;
	char							currString[256];
  FILE*             file;
  std::vector<std::string>
                    preBuff;
  std::vector<std::string>
                    postBuff;
	int					      trimStart, trimEnd;

  file = fopen(fileName.c_str(), "rt");
  if (file == NULL)
  {
    return false;
  }

	std::vector<sDNAElem>
										dna;

	// Read while not end of file
	while(fgets(currString, 255, file) != NULL)
	{
	//	printf ("Got string, %s\n", currString);
		switch(status)
		{
			// Not hit BEGIN_DNA
			case 0:
				if (strstr(currString, "BEGIN_DNA") != NULL)
				{
					status = 1;
				}
        preBuff.push_back(currString);
				break;
			case 1:
				if (strstr(currString, "END_DNA") != NULL)
				{
          postBuff.push_back(currString);
					status = 2;
				}
				else
				{
					sDNAElem	elem;

					if (sscanf(currString, "%c %d %d", &elem.base, &elem.score, &elem.n2) != 3)
					{
						// Did not scan right
            fclose(file);
						return false;
					}
					dna.push_back(elem);
				}
				break;
			case 2:
        postBuff.push_back(currString);
				break;
		}
	}

  fclose(file);

  file = fopen(fileName.c_str(), "wt");

  if (file == NULL)
  {
    return false;
  }

  for (int i = 0;i < preBuff.size();i++)
  {
    fputs(preBuff[i].c_str(), file);
  }

	trimDNAArray(dna, params, trimStart, trimEnd);
  dumpTrimmedDNAArray(dna, trimStart, trimEnd, file);

  for (int i = 0;i < postBuff.size();i++)
  {
    fputs(postBuff[i].c_str(), file);
  }

	return true;
}

int main(int argc, char **argv)
{
	if (argc < 2)
	{
		fprintf(stderr, "Usage trimming dirName [minScore]\n");
		return -1;
	}

	sTrimParams				params;

  std::vector<std::string>
                    fileNames;

  if (!readDirFiles(argv[1], fileNames))
  {
    fprintf(stderr, "Error reading from the directory %s\n", argv[1]);
    return -2;
  }
 	if (argc > 2)
    {
			double minPbdValue = atof(argv[2]);
			params.minPbdValue = (float)pow((double)10.0, (double)(-minPbdValue / 10.0));
			//printf ("Processing minPbdValue =%d\n", params.minPbdValue);
	}
	if (argc > 3)
	{
		params.firstBase = atoi(argv[3]);
		//printf ("Processing firstbase =%i\n", params.firstBase);
	}
	if (argc > 4)
    {
			params.lastBase = atoi(argv[4]);
			printf ("Processing lastbase=%i\n", params.lastBase);
	}


  for (int i = 0;i < fileNames.size();i++)
  {
//    printf ("Processing %s\n", fileNames[i].c_str());
  	if (!processDNAFile(fileNames[i], params))
  	{
  		fprintf (stderr, "Error processing DNA data\n");
  		return -3;
    }
	}

	return 0;
}
