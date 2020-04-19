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

// A structure to represent a queue
struct Queue
{
    int front, rear, size;
    unsigned capacity;
    int* array;
};

// function to create a queue of given capacity.
// It initializes size of queue as 0
struct Queue* createQueue(unsigned capacity)
{
    struct Queue* queue = (struct Queue*) malloc(sizeof(struct Queue));
    queue->capacity = capacity;
    queue->front = queue->size = 0;
    queue->rear = capacity - 1;  // This is important, see the enqueue
    queue->array = (int*) malloc(queue->capacity * sizeof(int));
    return queue;
}

// Queue is full when size becomes equal to the capacity
int isFull(struct Queue* queue)
{  return (queue->size == queue->capacity);  }

// Queue is empty when size is 0
int isEmpty(struct Queue* queue)
{  return (queue->size == 0); }

// Function to add an item to the queue.
// It changes rear and size
void enqueue(struct Queue* queue, int item)
{
    if (isFull(queue))
        return;
    queue->rear = (queue->rear + 1)%queue->capacity;
    queue->array[queue->rear] = item;
    queue->size = queue->size + 1;
    //printf("%d enqueued to queue\n", item);
}

// Function to remove an item from queue.
// It changes front and size
int dequeue(struct Queue* queue)
{
    if (isEmpty(queue))
        return INT_MIN;
    int item = queue->array[queue->front];
    queue->front = (queue->front + 1)%queue->capacity;
    queue->size = queue->size - 1;
    //printf("%d dequeued from queue\n", item);
    return item;
}

// Function to get front of queue
int front(struct Queue* queue)
{
    if (isEmpty(queue))
        return INT_MIN;
    return queue->array[queue->front];
}

// Function to get rear of queue
int rear(struct Queue* queue)
{
    if (isEmpty(queue))
        return INT_MIN;
    return queue->array[queue->rear];
}

/*Global Variables*/
char data[BUFSIZ];
char keyString[BUFSIZ];
int blockCount = 0;
int* keys;
int* sharedMemory;
struct Queue* xorQueue;
struct Queue* permQueue;
struct Queue* subsQueue;
struct Queue* lastQueue;
FILE* output1;
FILE* output2;
FILE* output3;

/*Define locks here*/
pthread_mutex_t xorQueueMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t permQueueMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t subsQueueMutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t lastQueueMutex = PTHREAD_MUTEX_INITIALIZER;

pthread_cond_t xorCondP = PTHREAD_COND_INITIALIZER;
pthread_cond_t xorCondC = PTHREAD_COND_INITIALIZER;
pthread_cond_t permCondP = PTHREAD_COND_INITIALIZER;
pthread_cond_t permCondC = PTHREAD_COND_INITIALIZER;
pthread_cond_t subsCondP = PTHREAD_COND_INITIALIZER;
pthread_cond_t subsCondC = PTHREAD_COND_INITIALIZER;
pthread_cond_t lastCondP = PTHREAD_COND_INITIALIZER;
pthread_cond_t lastCondC = PTHREAD_COND_INITIALIZER;

void* thread1_runner(void* args){
  FILE* plain;
  char* fileName = (char *)args;
  /*I/O files checking*/
  if(!(plain = fopen(fileName, "r"))){
		perror("plain.txt cannot be opened.\n");
		exit(EXIT_FAILURE);
	}
  while(fgets(data,BUFSIZ,plain) != NULL){
    data[strcspn(data, "\n")] = 0;
    /*Add to xor queue*/
    char* token = strtok(data,"-");
    while(token != NULL){
      pthread_mutex_lock(&xorQueueMutex);
      while(isFull(xorQueue)) pthread_cond_wait(&xorCondP,&xorQueueMutex);
      int val = atoi(token);
      enqueue(xorQueue,val);
      pthread_cond_signal(&xorCondC);
      pthread_mutex_unlock(&xorQueueMutex);

      token = strtok(NULL,"-");
    }

  }


  /* If the file is finished send a speacial value which indicates this for consumer*/
  pthread_mutex_lock(&xorQueueMutex);
  while(isFull(xorQueue)) pthread_cond_wait(&xorCondP,&xorQueueMutex);
  enqueue(xorQueue,-1);
  pthread_cond_signal(&xorCondC);
  pthread_mutex_unlock(&xorQueueMutex);

  fclose(plain);
  pthread_exit(NULL);
}

void* thread2_runner(void* args){
	output1 = fopen("process-0-thread-2.txt", "w");
  int counter = 0;

  while(1){
  pthread_mutex_lock(&xorQueueMutex);
  while(isEmpty(xorQueue)) pthread_cond_wait(&xorCondC,&xorQueueMutex);
  //Critical Section
  int val = dequeue(xorQueue);
  //Add to permutation queue
  pthread_mutex_lock(&permQueueMutex);
  while(isFull(permQueue)) pthread_cond_wait(&permCondP,&permQueueMutex);
  if(val == -1){
    enqueue(permQueue,-1);
    pthread_cond_signal(&permCondC);
    pthread_mutex_unlock(&permQueueMutex);
    pthread_cond_signal(&xorCondP);
    pthread_mutex_unlock(&xorQueueMutex);
    break;
  }
  int xorResult = val ^ keys[counter];
	if(counter==blockCount-1) fprintf(output1,"%d\n",xorResult);
	else{
		fprintf(output1,"%d-",xorResult);
	}

  counter++;
	counter %= blockCount;
  enqueue(permQueue,xorResult);
  //Done adding permitation queue
  pthread_cond_signal(&permCondC);
  pthread_mutex_unlock(&permQueueMutex);

  //Critical Section Ended
  pthread_cond_signal(&xorCondP);
  pthread_mutex_unlock(&xorQueueMutex);

  }
	fclose(output1);
  pthread_exit(NULL);
}
void* thread3_runner(void* args){
	output2 = fopen("process-0-thread-3.txt","w");
  struct Queue* tempQueue = createQueue(blockCount);
  int tempArr[blockCount];
  int flag=0;

  while(1){
    pthread_mutex_lock(&permQueueMutex);
    while(isEmpty(permQueue)) pthread_cond_wait(&permCondC,&permQueueMutex);
    //Critical Section
    int val = dequeue(permQueue);
    //Subs adding time
    pthread_mutex_lock(&subsQueueMutex);

    if(isFull(tempQueue)){
      /*Flush the queue*/
			int i;
      for(i = 0; i<tempQueue->capacity;i++) {
        tempArr[i] = dequeue(tempQueue);
      }

      flag = 1;
      enqueue(tempQueue,val);

      int c1 = 0;
      int c2 = blockCount/2;
      while(c1<blockCount/2){
        int temp;
        temp = tempArr[c1];
        tempArr[c1] = tempArr[c2];
        tempArr[c2] = temp;
        c1++;
        c2++;
      }

			int x;
      for(x=0; x<blockCount;x++){

        while(isFull(subsQueue)) pthread_cond_wait(&subsCondP,&subsQueueMutex);
        enqueue(subsQueue,tempArr[x]);
				if(x == blockCount-1) fprintf(output2, "%d",tempArr[x]);
				else{
						fprintf(output2, "%d-",tempArr[x]);
				}
        pthread_cond_signal(&subsCondC);
      }
			fprintf(output2, "\n");
    }

    if(val == -1){
      while(isFull(subsQueue)) pthread_cond_wait(&subsCondP,&subsQueueMutex);
      enqueue(subsQueue, -1);

      pthread_cond_signal(&subsCondC);
      pthread_mutex_unlock(&subsQueueMutex);
      pthread_cond_signal(&permCondP);
      pthread_mutex_unlock(&permQueueMutex);

      break;
    }

    else {
      if(flag == 1) flag = 0;
      else{
        enqueue(tempQueue,val);
      }
    }

    pthread_cond_signal(&subsCondC);
    pthread_mutex_unlock(&subsQueueMutex);
    pthread_cond_signal(&permCondP);
    pthread_mutex_unlock(&permQueueMutex);
  }
	fclose(output2);
  pthread_exit(NULL);

}

void* thread4_runner(void* args){
	output3 = fopen("process-0-thread-4.txt","w");
	int counter = 0;
  while(1){
    pthread_mutex_lock(&subsQueueMutex);
    while(isEmpty(subsQueue)) pthread_cond_wait(&subsCondC,&subsQueueMutex);
    int val = dequeue(subsQueue);

    pthread_mutex_lock(&lastQueueMutex);
    while(isFull(lastQueue)) pthread_cond_wait(&lastCondP,&lastQueueMutex);

    if(val == -1){
      enqueue(lastQueue,-1);
      pthread_cond_signal(&lastCondC);
      pthread_mutex_unlock(&lastQueueMutex);
      pthread_cond_signal(&subsCondP);
      pthread_mutex_unlock(&subsQueueMutex);
      break;
    }

    val = get_subbox_val(val);
    enqueue(lastQueue,val);
		if(counter==blockCount-1) fprintf(output3,"%d\n",val);
		else{
			fprintf(output3,"%d-",val);
		}

	  counter++;
		counter %= blockCount;

    pthread_cond_signal(&lastCondC);
    pthread_mutex_unlock(&lastQueueMutex);
    pthread_cond_signal(&subsCondP);
    pthread_mutex_unlock(&subsQueueMutex);
  }
	fclose(output3);
  pthread_exit(NULL);
}


void* thread5_runner(void* args){
  int counter = 0;
  while(1){
    pthread_mutex_lock(&lastQueueMutex);
    while(isEmpty(lastQueue)) pthread_cond_wait(&lastCondC,&lastQueueMutex);
    int val = dequeue(lastQueue);
    if(val == -1){

      pthread_mutex_unlock(&lastQueueMutex);
      break;
    }
    sharedMemory[counter] = val;
    counter++;

    pthread_cond_signal(&lastCondP);
    pthread_mutex_unlock(&lastQueueMutex);
  }
  pthread_exit(NULL);
}

int main(int argc, char* argv[]){


	FILE* key;
  pid_t pid;
  /*I/O files checking*/
	if(!(key = fopen(argv[2], "r"))){
		perror("key.txt cannot be opened.\n");
		exit(EXIT_FAILURE);
	}

  int memFd = shm_open("example_memory", O_CREAT | O_RDWR, S_IRWXU);
  if (memFd == -1)
  {
    perror("Can't open file");
    return EXIT_FAILURE;
  }

  int res = ftruncate(memFd, 1024);
  if (res == -1)
  {
    perror("Can't truncate file");
    return EXIT_FAILURE;
  }

  sharedMemory = mmap(NULL, 1024, PROT_READ | PROT_WRITE, MAP_SHARED, memFd, 0);
  if (sharedMemory == NULL)
  {
    perror("Can't mmap");
    return EXIT_FAILURE;
  }

  if((pid = fork()) == 0){
    /*Child Process*/
    char* args[] = {
    "./process-1"
		,argv[1]
    ,NULL
    };
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
  pthread_t tids[5];

  while(fgets(keyString,BUFSIZ,key) != NULL){
    keyString[strcspn(keyString, "\n")] = 0;
  }

  char temp[BUFSIZ];
  strcpy(temp,keyString);
  char* keyPiece = strtok(temp,"-");
  while(keyPiece != NULL){
    blockCount++;
    keyPiece = strtok(NULL,"-");
  }

  keys = malloc(sizeof(int) * blockCount);
  char* keyToken = strtok(keyString,"-");
	int j;
  for(j = 0; keyToken != NULL; j++){
    keys[j] = atoi(keyToken);
    keyToken = strtok(NULL,"-");
  }

  xorQueue = createQueue(5);
  permQueue = createQueue(5);
  subsQueue = createQueue(5);
  lastQueue = createQueue(5);

  pthread_create(&tids[0],NULL,thread1_runner,argv[1]);
  pthread_create(&tids[1],NULL,thread2_runner,NULL);
  pthread_create(&tids[2],NULL,thread3_runner,NULL);
  pthread_create(&tids[3],NULL,thread4_runner,NULL);
  pthread_create(&tids[4],NULL,thread5_runner,NULL);


  /*join the threads*/
  /*i.e wait for them all to finish*/
	int i;
  for(i = 0; i<5;i++){
    pthread_join(tids[i],NULL);
  }

	fclose(key);
  return 0;
}
