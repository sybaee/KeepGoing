#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

#define BUFFER_SIZE 512

// global variables
char buffer[BUFFER_SIZE];
int buffer_size = 0;
int buffer_pos = 0;

int ReadTextLine(int fd, char str[], int max_len) {
	int j = 0;
	int ret = 0;
	
	// if current position is 0, reset buffer size and pos
	if (lseek(fd, 0, SEEK_CUR) == 0)
		buffer_pos = buffer_size = 0;

	while (j < max_len-1) {
		if (buffer_pos == buffer_size) {
			buffer[0] = 0;
			buffer_size = read(fd, buffer, BUFFER_SIZE);
			buffer_pos = 0;
		}

		if (buffer_size == 0) {
			if (j == 0)
				ret = EOF;

			break;
		}

		while(j < max_len-2 && buffer_pos < buffer_size) {
			str[j++] = buffer[buffer_pos++];
			if(str[j-1] == '\0' || str[j-1] == 10) {
				j--;			// to remove CR
				max_len = j;	// to terminate outer loop

				break;			// break inner loop
			}
		}
	}
	
	str[j] = 0;

	return ret;
}

int main() {
	int fd_cpuinfo;
	int fd_meminfo;
	int fd_loadavg;
	int	ret;
	
	char line[100];
	char name[100];
	char info[100];

	int i;

	double loadavg1;
	double loadavg5;
	double loadavg15;

	fd_cpuinfo = open("/proc/cpuinfo", O_RDONLY);
	
	// find cpu cores
	for (i=0; i < 13; i++)
		ret = ReadTextLine(fd_cpuinfo, line, 100);
	
	if (ret == 0) {
		sscanf(line, "%[^:] : %[^\n]", name, info);
		printf("# of processor cores = %d\n", atoi(info));
	}

	lseek(fd_cpuinfo, 0, SEEK_SET);
	
	// find model name
	for (i=0; i < 5; i++)
		ret = ReadTextLine(fd_cpuinfo, line, 100);
	
	if (ret == 0) {
	 sscanf(line, "%[^:]  : %[^\n]", name, info); 
	 printf("CPU model = %s\n", info);	
	}

	close(fd_cpuinfo);

	fd_meminfo = open("/proc/meminfo", O_RDONLY);

	// find total memory
	ret = ReadTextLine(fd_meminfo, line, 100);

	if (ret == 0) {
		sscanf(line, "%[^:] : %[^KB]", name, info);
		printf("MemTotal = %d\n", atoi(info));
	}

	close(fd_meminfo);

	fd_loadavg = open("/proc/loadavg", O_RDONLY);

	// find load times
	ret = ReadTextLine(fd_loadavg, line, 100);

	if (ret == 0) {
		sscanf(line, "%lf %lf %lf", &loadavg1, &loadavg5, &loadavg15);
		printf("loadavg1 = %lf, loadavg5 = %lf, loadavg15 = %lf\n", loadavg1, loadavg5, loadavg15);
	}

	close(fd_loadavg);

	return 0;
}
