#include <stdio.h>
#include <string.h>
#include <ctype.h>

#define MAX_CMD 2048
#define MAX_ARG 256

void ParseCommand(char *command, int *argc, char *argv[]) {
	int from = 0;
	int to;
	
	for (int i=0; i <= *argc; i++)
		argv[i] = NULL;
	*argc = 0;

	while (1) {
		while (isspace(command[from])) {
			++from;
		}
		
		if (command[from] == '\0')
			break;
		
		to = from;
		
		if (command[to] == '\'' || command[to] == '\"') {
			++to;
			while (1) {
				if (command[to] == '\'' || command[to] == '\"')
					break;
				++to;
			}
			argv[(*argc)++] = &command[from+1];
		}

		else {
			while (command[to] && !isspace(command[to])) {
				++to;
			}
			argv[(*argc)++] = &command[from];
		}

		command[to] = '\0';
		from = to + 1;
	}
}

int main() {
	char command[MAX_CMD];
	command[0] = command[MAX_CMD-1] = 0; // for safety

	int argc = 0;
	char *argv[MAX_ARG] = { NULL };

	while (1) {
		printf("$ ");
		fgets(command, MAX_CMD-1, stdin);
		command[strlen(command)-1] = 0; // trim \r

		if (strcmp(command, "quit") == 0 || strcmp(command, "exit") == 0)
			break;

		ParseCommand(command, &argc, argv);

		printf("argc = %d\n", argc);
		for(int i=0; i < argc; i++)
			printf("argv[%d] = %s\n", i, argv[i]);
			
		printf("argv[%d] = %p\n", argc, argv[argc]); // argv[argc] must be NULL
	}

	printf("Bye!\n");

	return 0;
}
