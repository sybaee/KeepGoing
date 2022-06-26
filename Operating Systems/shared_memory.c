/*
	This program creates a child process by fork().
	The parent reads text messages from the user, and then, sends it to the child through a global buffer.
	The child prints the message in the global buffer.
	However, this program does not work correctly.

	To fix the problem, the parent and child should communicate through a shared memory buffer instead of the global buffer.
	Complete this program using POSIX shared memory API.
		(shm_open(), mmap(), munmap(), shm_unlink() ...)

	You MUST compile with -lrt option
	Ex) gcc shm_02.c -lrt

*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <sys/stat.h>
#include <sys/ipc.h>
#include <sys/shm.h>

// struct for message buffer
typedef struct {
	char message[512];			// message buffer
	int filled;					// 1 if the message is filled. 0 otherwise.
} Buffer;

Buffer global_buffer;

void parent(int shm_id);		// function for the parent
void child(int shm_id);			// function for the child

#define SIZE 4096

int main(int argc, char argv[0]) {
	int shm_id = 0;

	const char *name = "OS";

	// allocate shared memory block for the shared buffer
	shm_id = shm_open(name, O_CREAT | O_RDWR, 0666);
	ftruncate(shm_id, SIZE);

	pid_t child_pid = fork();

	if (child_pid < 0) {
		printf("Failed to create child process\n");
		exit(-1);
	}
	
	else if (child_pid > 0){
		parent(shm_id);
	}
	
	else {
		child(shm_id); // child
	}

	return 0;
}

void parent(int shm_id) {
	Buffer *buffer = mmap(0, SIZE, PROT_WRITE | PROT_READ, MAP_SHARED, shm_id, 0);
	// printf("parent buffer: %p\n", buffer);	
	buffer->filled = 0;

	sleep(2);			// wait for the child to start
	printf("[parent] Input a message and I'll send to the child.\n");

	while (1){
		char message[512];
		message[511] = 0;	// for safety
		
		fgets(message, 512, stdin);
		message[strlen(message) - 1] = 0;	// trim '\n'

		while (buffer->filled) { // wait for the child to print
			// printf("parent while usleep\n");
			usleep(50000);
		}
		
		//printf("After: parent: buffer->filled = %d\n", buffer->filled);
		strcpy(buffer->message, message);
		buffer->filled = 1;
		
		if (strcmp(message, "quit") == 0)
			break;
	}

	munmap(buffer, SIZE);
	// shm_unlink

	printf("[parent] Terminating.\n");
	fflush(stdout);
}

void child(int shm_id) {
	sleep(1);			// wait for the parent to initialize the shared buffer

	printf("[child] Started\n");
	fflush(stdout);

	Buffer *buffer = mmap(0, SIZE, PROT_WRITE | PROT_READ, MAP_SHARED, shm_id, 0);
	// printf("child buffer: %p\n", buffer);

	while (1) {
		while (!buffer->filled) {
			// printf("child while usleep\n");
			usleep(50000);
		}

		if (buffer->message[0]) {
			printf("[child] %s\n", buffer->message);
			fflush(stdout);
			//printf("Before: child: buffer->filled = %d\n", buffer->filled);
			buffer->filled = 0;
			//printf("After: child: buffer->filled = %d\n", buffer->filled);
		}

		if (strcmp(buffer->message, "quit") == 0)
			break;
	}

	munmap(buffer, SIZE);

	printf("[child] Terminating.\n");
	fflush(stdout);
}

