#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <string.h>

unsigned char SUBBOX[256] = {47, 164, 147, 166, 221, 246, 1, 13, 198, 78, 102, 219, 75, 97, 62, 140,
			     84, 69, 107, 99, 185, 220, 179, 61, 187, 0, 92, 112, 8, 33, 15, 119,
			     209, 178, 192, 12, 121, 239, 117, 96, 100, 126, 118, 199, 208, 50, 42, 168,
			     14, 171, 17, 238, 158, 207, 144, 58, 127, 182, 146, 71, 68, 157, 154, 88,
			     248, 105, 131, 235, 98, 170, 22, 160, 181, 4, 254, 70, 202, 225, 67, 205,
			     216, 25, 43, 222, 236, 128, 122, 77, 59, 145, 167, 54, 20, 55, 152, 149,
			     230, 211, 224, 111, 165, 124, 16, 243, 213, 114, 116, 63, 64, 176, 31, 161,
			     9, 229, 95, 247, 193, 18, 134, 79, 133, 173, 82, 51, 57, 136, 6, 49,
			     5, 197, 115, 65, 169, 255, 249, 195, 30, 162, 150, 53, 83, 46, 228, 81,
			     237, 104, 28, 223, 217, 251, 200, 60, 132, 194, 151, 137, 191, 74, 201, 103,
			     29, 80, 113, 101, 250, 172, 234, 180, 73, 141, 204, 27, 241, 188, 153, 155,
			     86, 94, 177, 87, 39, 91, 2, 48, 35, 40, 120, 159, 184, 123, 215, 138,
			     210, 108, 76, 106, 36, 189, 125, 226, 252, 37, 66, 156, 253, 218, 85, 203,
			     110, 10, 244, 45, 34, 242, 72, 93, 52, 135, 44, 245, 3, 32, 196, 163,
			     232, 240, 227, 24, 139, 183, 38, 233, 130, 143, 109, 41, 174, 231, 129, 23,
			     148, 89, 212, 19, 21, 142, 7, 214, 56, 90, 11, 190, 175, 206, 26, 186};

unsigned char get_subbox_val (unsigned char val)
{
  unsigned char sub_box_val = SUBBOX[val];

  return sub_box_val;
}

int main(int argc, char* argv[]){
	FILE* cipher;
  FILE * process3 = fopen("process-3.txt", "w");
  int bufferSize = atoi(argv[1]);
  int buff[bufferSize];

	if(!(cipher =  fopen("cipher.txt","w"))){
		perror("cipher.txt cannot be opened.\n");
		exit(EXIT_FAILURE);
	}

	int *pipeAdress;

	sscanf(argv[2], "%p", (int **)&pipeAdress);

  while(read(STDIN_FILENO,buff, sizeof(buff)) != 0){
		int i;
    for (i = 0; i < bufferSize; i++) {
      buff[i] = get_subbox_val(buff[i]);
      if(i==(bufferSize -1)) {
				fprintf(process3, "%d\n", buff[i]);
				fprintf(cipher, "%d\n", buff[i]);
			}

      else{
				fprintf(process3, "%d-",  buff[i]);
				fprintf(cipher, "%d-", buff[i]);
			}
    }


  }
  fclose(process3);
  printf("Process-3 is done\n");
  return 0;
}
