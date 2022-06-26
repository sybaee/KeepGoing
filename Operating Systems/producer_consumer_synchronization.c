#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <pthread.h>
#include <semaphore.h>

// Message buffer
#define BUFFER_SIZE 5
#define MAX_MESSAGE 64
char buffer[BUFFER_SIZE][MAX_MESSAGE];
int in = 0, out = 0, count = 0;

// Mutex and Semaphores
pthread_mutex_t mutex;
sem_t empty, full;

// Global variable to control threads
// Producer and Consumer threads terminates
// when main() sets repeat to zero
int repeat = 1;

void DisplayBuffer() {
    printf("Buffer contents:\n");
    printf("\tcount = %d, in = %d, out = %d\n", count, in, out);

    int p = out;
    for (int i=0; i < count; i++) {
        printf("\t%d] %s\n", p, buffer[p]);
        p = (p+1) % BUFFER_SIZE;
    }
}

void* Producer(void* pro) {
    int no_messages = 10;
    char *message[64] = {
        "Nice to see you!",
        "Aal izz well!",
        "I love you! God loves you!",
        "God loves you and has a wonderful plan for your life.",
        "God bless you!",
        "You are genius!",
        "Cheer up!",
        "Everything is gonna be okay",
        "You are so precious!",
        "Hakuna matata!"
    };

    // Repeat until repeat is set to zero
    while (repeat) {
        sleep(rand() % 3 + 1);
        // Generate a message by randomly choosing one of the following candidates
        int item = rand() % no_messages;
        
        // Implement entry section using mutex lock and semaphores
        sem_wait(&empty);
        // Print a message indicating the message was produced
        printf("[Producer] Created a message \"%s\"\n", message[item]);
        pthread_mutex_lock(&mutex);

        // Print a message indicating the start of critical section
        printf("----------------------PRODUCER------------------------\n");
        printf("Producer is entering critical section.\n");
        printf("[Producer] \"%s\" ==> buffer\n", message[item]);

        // Add the message to the buffer and increase counter
        strcpy(buffer[in], message[item]);
        in = (in+1) % BUFFER_SIZE;
        count++;

        // Display the content of buffer
        DisplayBuffer();

        // Print a message indicating the end of critical section
        printf("Producer is leaving critical section.\n");
        printf("------------------------------------------------------\n");

        // Implement exit section using mutex lock and semaphores
        pthread_mutex_unlock(&mutex);
        sem_post(&full);
    }
}

void* Consumer(void* con) {
    // Repeat until repeat is set to zero
    while (repeat) {
        // Implement entry section using mutex lock and semaphores
        sem_wait(&full);
        pthread_mutex_lock(&mutex);

        // Print a message indicating the start of critical section
        printf("----------------------CONSUMER------------------------\n");
        printf("Consumer is entering critical section.\n");

        // Delete the message from the buffer and decrease counter
        char* sentence = buffer[out];
        out = (out+1) % BUFFER_SIZE;
        count--;

        // Print a message indicating a message was retrieved from the buffer
        printf("[Consumer] buffer ==> \"%s\"\n", sentence);

        // Display the content of buffer
        DisplayBuffer();

        // Print a message indicating the end of critical section
        printf("Consumer is leaving critical section.\n");
        printf("------------------------------------------------------\n");

        // Implement exit section using mutex lock and semaphores
        pthread_mutex_unlock(&mutex);
        sem_post(&empty);

        // Print a message indicating the message was consumed
        printf("[Consumer] Consumed a message \"%s\"\n", sentence);

        // Randomly wait for 2~4 seconds (sleep(rand() % 3 + 2);)
        sleep(rand() % 3 + 2);
    }
}

int main(int argc, char* argv[]) {
    // Read duration from the command line arguments
    int duration = 0; // Duration specifies how long (in sec) the program runs
    if (argc == 1) { // Default value is 30 sec
        duration = 30;
	}

    else if (argc > 2) {
        fprintf(stderr, "usage: put only ONE duration\n");
		exit(1);
    }

    else duration = atoi(argv[1]);

    // Initialize
    pthread_t pro, con;
    pthread_mutex_init(&mutex, NULL);
    sem_init(&empty, 0, BUFFER_SIZE);
    sem_init(&full, 0, 0);

    // Display the initial content of buffer
    DisplayBuffer();

    // Create threads for producer and consumer
    pthread_create(&pro, NULL, (void*) Producer, NULL);
    pthread_create(&con, NULL, (void*) Consumer, NULL);
    
    // Wait for duration seconds
    sleep(duration);

    // Set repeat to zero to terminate Producer and Consumer
    repeat = 0;

    // If the value of full is zero, call sem_post(&full)
    int fullValue = 0;
    sem_getvalue(&full, &fullValue);
    if (fullValue == 0) sem_post(&full);

    // If the value of empty is zero, call sem_post(&empty)
    int emptyValue = 0;
    sem_getvalue(&empty, &emptyValue);
    if (emptyValue == 0) sem_post(&empty);

    // Wait for the Producer and Consumer
    pthread_join(pro, NULL);
    pthread_join(con, NULL);

    // Destroy mutex, full, and empty
    pthread_mutex_destroy(&mutex);
    sem_destroy(&full);
    sem_destroy(&empty);

    // Print a good-bye message
    printf("Bye!\n");
}