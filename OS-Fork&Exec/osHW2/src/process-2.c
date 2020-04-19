#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <pthread.h>
#include <limits.h>
#include <string.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <fcntl.h>


int main(int argc, char* argv[]){
  FILE* plain;
  FILE* output4 = fopen("process-1.txt","w");
  char buffer[BUFSIZ];
  int blockAmount = 0;
  int lineCount=0;
  int blockPerLine;

  if(!(plain = fopen(argv[1], "r"))){
		perror("plain.txt cannot be opened.\n");
		exit(EXIT_FAILURE);
	}

  while(fgets(buffer,BUFSIZ,plain) != NULL){
    buffer[strcspn(buffer, "\n")] = 0;
    char* token = strtok(buffer,"-");
    while(token != NULL){
      blockAmount++;
      token = strtok(NULL,"-");
    }
    lineCount++;
  }

  int memFd = shm_open("example_memory", O_RDWR, 0);
  if (memFd == -1)
  {
    perror("Can't open file");
    return 1;
  }

  int *sharedMemory = mmap(NULL, 1024, PROT_READ | PROT_WRITE, MAP_SHARED, memFd, 0);
  if (sharedMemory == NULL)
  {
    perror("Can't mmap");
    return -1;
  }

  blockPerLine = blockAmount / lineCount;
  int* lineSums =(int *) malloc(sizeof(int)*blockPerLine);
  int i;
  int j;
  for(i=0;i<blockPerLine;i++){
    lineSums[i] = 0;
  }
  int line;

  for(line = 0; line<blockPerLine;line++){
    for(j=line;j<blockAmount;j+= blockPerLine){
      lineSums[line] += sharedMemory[j];
      if(lineSums[line] >= 256){
        lineSums[line] %= 256;
        lineSums[line] ++;
      }
    }
    fprintf(output4, "%d-",lineSums[line]);
  }

  fclose(output4);
  return 0;
}
