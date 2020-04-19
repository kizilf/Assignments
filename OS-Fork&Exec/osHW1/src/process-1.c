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
	FILE* process1 = fopen("process-1.txt","w");
	char buff[BUFSIZ];
	char key[BUFSIZ];
	char temp[BUFSIZ];

	if(pipe(fds) == -1){
		printf("Pipe error in process1\n");
		exit(EXIT_FAILURE);
	}


	read(STDIN_FILENO,key,BUFSIZ);

	strcpy(temp,key);
	char* tokenKey = strtok(temp,"-");
	int i=1;
	while(strtok(NULL,"-") != NULL){
				i++;
	}

	if((pid = fork()) == 0){
		/*Child Process*/
		/*Close write end of the pipe for child*/
		close(fds[1]);
		dup2(fds[0],STDIN_FILENO);
		close(fds[0]);

		char str[12];
		sprintf(str, "%d", i);
		char* args[] = {
		"./process-2"
		,str
		,argv[1]
		,NULL
		};
		execv(args[0], args);
		perror("execv");
		printf("Process 2 exec error\n");
		exit(EXIT_FAILURE);

	}
	else if(pid > 0){
		/*Parent Process*/
		/*Close read end of the pipe for parent*/
		close(fds[0]);


		int* keyValues = (int*)malloc(sizeof(int)*i);
		int* xorValues = (int*)malloc(sizeof(int)*i);


		i = 0;
		strcpy(temp,key);
		tokenKey = strtok(temp,"-");
		while(tokenKey != NULL){
			int ret = atoi(tokenKey);
			keyValues[i] = ret;
			i++;
			tokenKey = strtok(NULL,"-");
		}


		while(read(STDIN_FILENO,buff,BUFSIZ) != 0){
			char* tokenBuf = strtok(buff,"-");
			i = 0;
			while(tokenBuf != NULL){
				xorValues[i] = keyValues[i] ^ atoi(tokenBuf);
				tokenBuf = strtok(NULL,"-");
				i++;
			}
			int j;
			for(j=0; j<i;j++){
				if(j == (i-1)) fprintf(process1, "%d\n", xorValues[j]);
				else{ fprintf(process1, "%d-", xorValues[j]);}
			}
			write(fds[1], xorValues, i*sizeof(int));
		}
		free(keyValues);
		free(xorValues);
		close(fds[1]);
		waitpid(pid,&status,0);
		printf("Process-1 is done\n");
	}

	else{
		perror("Fork error in process1.c\n");
		exit(EXIT_FAILURE);
	}


	fclose(process1);
	return 0;
}
