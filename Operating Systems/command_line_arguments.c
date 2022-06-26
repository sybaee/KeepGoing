#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[]) {
	int i = 0;
	int sum = 0;

	for(i=0; i < argc; i++)
		sum += atoi(argv[i]);
		
	printf("sum of %d numbers: %d\n", (argc - 1), sum);

	return 0;
}
