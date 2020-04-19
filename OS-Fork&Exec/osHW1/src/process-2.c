#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char* argv[]){
  int fds[2];
  int status = 0;
  pid_t pid;
  FILE * process2 = fopen("process-2.txt", "w");

  if(pipe(fds) == -1){
		printf("Pipe error in process1\n");
		exit(EXIT_FAILURE);
	}

  if((pid = fork()) == 0){
    /*Child Process*/
		/*Close write end of the pipe for child*/
		close(fds[1]);
		dup2(fds[0],STDIN_FILENO);
		close(fds[0]);
    char* args[] = {
    "./process-3"
    ,argv[1]
    ,argv[2]
    ,NULL
    };
    execv(args[0], args);
    perror("execv");
    printf("Process 3 exec error\n");
    exit(EXIT_FAILURE);
  }

  else if(pid > 0){
		/*Parent Process*/
		/*Close read end of the pipe for parent*/
    close(fds[0]);
    int bufferSize = atoi(argv[1]);
    int* buff = malloc(bufferSize*sizeof(int));

    while(read(STDIN_FILENO,buff,bufferSize* sizeof(int)) != 0){

        int i = 0;
        int j = bufferSize - 1;
        while(i<bufferSize/2){
          int temp;
          temp = buff[i];
          buff[i] = buff[j];
          buff[j] = temp;
          i++;
          j--;
        }
        int k;
        for (k= 0 ;k < bufferSize; k++) {
          if(k==(bufferSize -1)) fprintf(process2, "%d\n", buff[k]);
          else{ fprintf(process2, "%d-",  buff[k]);}
        }

        write(fds[1], buff, bufferSize*sizeof(int));
    }

    close(fds[1]);
		waitpid(pid,&status,0);
		printf("Process-2 is done\n");
	}

	else{
		perror("Fork error in process2.c\n");
		exit(EXIT_FAILURE);
	}

  fclose(process2);
  return 0;
}
