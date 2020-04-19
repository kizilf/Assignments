#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char* argv[]){

	FILE* plain;
	FILE* key;
	pid_t pid;
	int p1fds[2];
	int p3fds[2];
	char buff[BUFSIZ];
	char keyString[BUFSIZ];
	int status = 0;

	/*I/O files checking*/
	if(!(plain = fopen(argv[1], "r"))){
		perror("plain.txt cannot be opened.\n");
		exit(EXIT_FAILURE);
	}

	if(!(key = fopen(argv[2], "r"))){
		perror("key.txt cannot be opened.\n");
		exit(EXIT_FAILURE);
	}

	if(pipe(p1fds) == -1){
		perror("Pipe error in main\n");
		exit(EXIT_FAILURE);
	}

	if(pipe(p3fds) == -1){
		perror("Pipe error in main-process3 pipe\n");
		exit(EXIT_FAILURE);
	}

	if((pid = fork()) == 0){
		/*Child Process*/
		/*Close write end of the pipe for child*/
		close(p1fds[1]);
		dup2(p1fds[0],STDIN_FILENO);


		/*Also close global pipe that will be used between main and third process*/

		int *p;
		p = &p3fds[1];
		char buff[BUFSIZ];
		sprintf(buff,"%p",p);
		char* args[] = {
		"./process-1"
		,buff
		,NULL
		};
		close(p3fds[0]);
		execv(args[0], args);
		perror("execv");
		printf("Process 1 exec error\n");
		exit(EXIT_FAILURE);

	}

	else if(pid == -1){
		perror("Fork error in main.c\n");
		exit(EXIT_FAILURE);
	}


	/*Parent Process*/
	/*Close read end of the pipe for parent*/
	close(p1fds[0]);
	close(p3fds[1]);


	while(fgets(keyString,BUFSIZ,key) != NULL){
		keyString[strcspn(keyString, "\n")] = 0;
		write(p1fds[1], keyString, BUFSIZ);
	}

	while(fgets(buff,BUFSIZ,plain) != NULL){
		buff[strcspn(buff, "\n")] = 0;
		write(p1fds[1], buff, BUFSIZ);
	}

	close(p1fds[1]);
	waitpid(pid,&status,0);

	close(p3fds[0]);
	printf("Main is done\n");
	fclose(plain);
	fclose(key);
	return 0;
}
