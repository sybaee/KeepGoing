#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/ipc.h>
#include <sys/msg.h>
#include <sys/ioctl.h>
#include <unistd.h>

#define BUFFER_SIZE 1024

typedef struct {
	long data_type;
	char data_buff[BUFFER_SIZE];
} data_st;

struct winsize w;
int repeat_receiver = 1;

void* receiver(void* rcv_q) {
	data_st rcv_msg;
	int rcv_id = *(int*) rcv_q;
	int msg_size = sizeof(rcv_msg) - sizeof(rcv_msg.data_type);

	//printf("rcvid: %d\n", rcv_id);
	ioctl(0, TIOCGWINSZ, &w);
	int space = (w.ws_col/2) - 7;

	while (repeat_receiver == 1) {
		memset(rcv_msg.data_buff, 0x00, sizeof(rcv_msg.data_buff)); // clear message buffer
		rcv_msg.data_type == 1;
		// receive a message from rcv_queue into the message buffer
		// if the message buffer is not empty, print the message
		if (msgrcv(rcv_id, &rcv_msg, msg_size, 1, IPC_NOWAIT) >= 0) {
			//fprintf(stdout, "[mesg] ");
			for (int i=0; i < space; i++)
				printf(" ");

			fprintf(stdout, "[incoming] \"%s\"\n", rcv_msg.data_buff);
			fprintf(stdout, "[mesg] ");
			fflush(stdout);
		}

		usleep(1000); // sleep for 1000 ms
	}
}

void* sender(void* snd_q) {
	data_st snd_msg;
	int snd_id = *(int*) snd_q;
	int result;
	int msg_size = sizeof(snd_msg) - sizeof(snd_msg.data_type);

	//printf("snd_id = %d\n", snd_id);
	while (1) {
		// print a prompt message
		fprintf(stdout, "[mesg] ");
		
		// read a sentence from the user
		fgets(snd_msg.data_buff, BUFFER_SIZE, stdin);
		snd_msg.data_buff[strlen(snd_msg.data_buff)-1] = 0; //trim '\n'
		snd_msg.data_type = 1;

		// if the user types "quit", break
		if (strncmp(snd_msg.data_buff, "quit", BUFFER_SIZE) == 0) {
			repeat_receiver = 0;
			break;
		}

		// send the message to snd_queue
		if ((result = msgsnd(snd_id, &snd_msg, msg_size, 0)) == -1)
			printf("Failed to send message.\n");			
	}
}

int main(int argc, char* argv[]) {
	pthread_t snd_thread; // the thread identifier
	pthread_t rcv_thread;
	pthread_attr_t attr; // set of thread attributes
	void* thread_status;
	
	if (argc < 3) {
		fprintf(stderr, "usage: %s <snd_key> <rcv_key>\n", argv[0]);
		exit(0);
	}

	key_t snd_key = atoi(argv[1]);
	key_t rcv_key = atoi(argv[2]);

	if (snd_key < 0) {
		fprintf(stderr, "%d must be <= 0\n", snd_key);
		exit(0);
	}

	if (rcv_key < 0) {
		fprintf(stderr, "%d must be <= 0\n", rcv_key);
		exit(0);
	}

	printf("snd_key = %d, rcv_key = %d\n", snd_key, rcv_key);

	int snd_queue = msgget((key_t)snd_key, IPC_CREAT | 0666);
	int rcv_queue = msgget((key_t)rcv_key, IPC_CREAT | 0666);

	if (snd_queue == -1) {
		fprintf(stderr, "snd_key msgget() failed");
		exit(0);
	}

	if (rcv_queue == -1) {
		fprintf(stderr, "rcv_key msgget() failed");
		exit(0);
	}

	pthread_attr_init(&attr); // get the default attributes
	
	// launch a thread to run sender() passing in &snd_queue as argument
	if (pthread_create(&snd_thread, &attr, sender, &snd_queue) < 0) {
		perror("sender pthread_create error\n");
		exit(1);
	}
	
	// lauch a thread to run receiver() passing in &rcv_queue as argument
	if (pthread_create(&rcv_thread, &attr, receiver, &rcv_queue) < 0) {
		perror("receiver pthread_create error\n");
		exit(1);
	}
	
	// use receiver and sender function
	// wait for the two child threads
	pthread_join(snd_thread, &thread_status);
	pthread_join(rcv_thread, &thread_status);

	// deallocate the two message queues
	msgctl(snd_queue, IPC_RMID, 0);
	msgctl(rcv_queue, IPC_RMID, 0);

	pthread_exit(0);
	
	return 0;
}

