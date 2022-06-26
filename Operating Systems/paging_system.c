#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define TRUE 1
#define FALSE 0

typedef struct {
    int bits_for_page_number;   // m-n
    int bits_for_offset;        // n
    int page_table_length;      // the length of pabe table = # of entries in page table
    int *frame_number;          // array to store frame numbers

    // computed from the above fields in ReadPageTable()
    int page_size;              // page_size = 2^n
    int limit;                  // page_table length * page size
} PageTable;

int ReadPageTable(const char *filename, PageTable *pt);
void DisplayPageTable(PageTable *pt);
int Translate(int logical_addr, PageTable *pt);
void DestroyPageTable(PageTable *pt);

int main(int argc, char *argv[]) {
    if (argc < 2) {
        printf("Usage: %s <pagetable_file>\n", argv[0]);
        return 0;
    }

    PageTable pt = { 0 };

    int ret = ReadPageTable(argv[1], &pt);
    if (ret == FALSE) {
        /* error handling routine */
        printf("Failed to read page table from %s\n", argv[1]);
		return -1;
    }

    DisplayPageTable(&pt);

    int addr_limit = pt.limit;      // pt.limit is computed by ReadPageTable()

    int step = 1;
    if (addr_limit > 256)
        step = addr_limit / 256;
    
    for (int logical_addr=0; logical_addr < addr_limit; logical_addr += step) {
        int physical_addr = Translate(logical_addr, &pt);
        printf("%04d (0x%04x) ==> %04d (0x%04x)\n", logical_addr, logical_addr, physical_addr, physical_addr);
    }

    DestroyPageTable(&pt);

    if ((1 << pt.bits_for_page_number) * (1 << pt.bits_for_offset) > addr_limit) {
        /* error handling routine */
        printf("Size of logical address space is larger than limit\n");
        exit(1);
    }

    return 0;
}

int ReadPageTable(const char *filename, PageTable *pt) {
	FILE *fp = fopen(filename, "r");
	if (fp == NULL){
		printf("Failed to open file %s\n", filename);
		return FALSE;
	}

    // Read a page table pt from a text file
    int lineCount = 1;
    char buffer[256];
    char *ptr = NULL;

    while (!feof(fp)) {
        fgets(buffer, sizeof(buffer), fp);
        ptr = strtok(buffer, " ");
        ptr = strtok(NULL, " ");
        // printf("%dth line -> %s\n", lineCount, ptr);

        switch (lineCount++) {
            case 1:
                pt->bits_for_page_number = atoi(ptr);
                break;

            case 2:
                pt->bits_for_offset = atoi(ptr);
                break;

            case 3:
                pt->page_table_length = atoi(ptr);
                break;
        }

        if (lineCount == 4) {
            // Dynamically allocate an array frame_number to store frame numbers
            pt->frame_number = (int *)malloc(sizeof(int) * pt->page_table_length);
            
            if (pt->frame_number == NULL)
                printf("dynamic allocation failed\n");

            break;
        }
    }

    for (int i=0; i < pt->page_table_length; i++) {
        fgets(buffer, sizeof(buffer), fp);
        ptr = strtok(buffer, " ");
        pt->frame_number[i] = atoi(ptr);
        ptr = strtok(NULL, " ");
    }

    fclose(fp);

    // Compute page_size and limit from bits_for_offset and page_table_length
    pt->page_size = 1 << pt->bits_for_offset;
    pt->limit = pt->page_table_length * pt->page_size;

	return TRUE;
}

void DisplayPageTable(PageTable *pt) {
    // Display a page table pt
	printf("BITS_FOR_PAGE_NUMBER %d (maximum # of pages = %d)\n",
		pt->bits_for_page_number, 1 << pt->bits_for_page_number);
	printf("BITS_FOR_OFFSET %d (page_size = %d)\n",
		pt->bits_for_offset, pt->page_size);
	printf("PAGE_TALBLE_LENGTH %d (address limit = %d)\n", pt->page_table_length, pt->limit);

	for (int i=0; i < pt->page_table_length; i++)
		printf("%3d: %d\n", i, pt->frame_number[i]);
}

int Translate(int logical_addr, PageTable *pt) {
    // Using a page table pt, return the physical address computed from the logical_addr

    // Separate page number and offset from logical address

    // Use shift right operator >> to get page number
    // <page number> = <logical address> >> n
    int page_number = logical_addr >> pt->bits_for_offset;
    // With the page number, look up the page table to fine the corresponding frame number
    int frame_number = pt->frame_number[page_number];

    // Use modulus operator % to get offset
    // <offset> = <logical address> % <page size>
    int page_offset = logical_addr % pt->page_size;
    //printf("page number -> %d, frame number -> %d, page offset -> %d\n", page_number, frame_number, page_offset);

    // Use shift left operator << and bit-wise OR operator |
    // to combine frame number and offset to make a physical address
    int physical_addr = (frame_number << 2) | page_offset;

    return physical_addr;
}

void DestroyPageTable(PageTable *pt) {
    // Deallocate the array frame_number
    free(pt->frame_number);
}