// listsort.cpp   Signed: Seungye Bae   Student Number: 21600326

// A typical recursive implementation of quick sort

#include <iostream>
#include "listdbl.h"
using namespace std;

#ifdef DEBUG
#define DPRINT(func) func;
#else
#define DPRINT(func) ;
#endif

int ascending(int, int);
int descending(int, int);

// This function takes last element as pivot, places the pivot element at its
// correct position in sorted array, and places all smaller (smaller than pivot)
// to left of pivot and all greater elements to right of pivot
pNode partition(pNode lo, pNode hi, int(*comp)(int, int)=ascending) {
	int x = hi->item;     // set pivot as hi value
	pNode i = lo->prev;   // Index of smaller element

	for (pNode j=lo; j != hi; j = j->next) {
		// If current element is smaller than or equal to pivot
		if (comp(x, j->item) > 0) {
			i = (i == nullptr) ? lo : i->next;    // increment index of smaller element
			std::swap(i->item, j->item);          // Swap current element with index
		}
	}
	i = (i == nullptr) ? lo : i->next;
	std::swap(i->item, hi->item);

	return i;
}

// quickSort helper function for recursive operation
// list[]: array to be sorted, lo: Starting index, h: Ending index
// N is added only for debugging or DPRINT
void _quickSort(pNode lo, pNode hi, int(*comp)(int, int)=ascending) {
	if (lo != nullptr && lo != hi && lo != hi->next) {
		pNode p = partition(lo, hi, comp); // Partitioning index
		_quickSort(lo, p->prev, comp);
		_quickSort(p->next, hi, comp);
	}
}

void quickSort(pList head, int(*comp)(int, int)) {
	_quickSort(begin(head), last(head), comp);
}

// This function takes last element as pivot, places the pivot element at its
// correct position in sorted array, and places all smaller (smaller than pivot)
// to left of pivot and all greater elements to right of pivot.
int partition(int list[], int lo, int hi, int(*comp)(int, int)) {
	int x = list[hi];  // pivot
	int i = (lo - 1);  // Index of smaller element

	for (int j=lo; j <= hi-1; j++) {
		// If current element is smaller than or equal to pivot
		if (comp(x, list[j]) > 0) {
			i++;    // increment index of smaller element
			std::swap(list[i], list[j]);  // Swap current element with index
		}
	}
	std::swap(list[i+1], list[hi]);
	return (i+1);
}

// quickSort helper function for recursive operation
// list[]: array to be sorted, lo: starting index, h: ending index
// N is added only for debugging or DPRINT
void _quickSort(int *list, int lo, int hi, int N, int(*comp)(int, int)) {
	if (lo < hi) 	{
		int pi = partition(list, lo, hi, comp); // Partitioning index
		DPRINT(for (int x=0; x < N; x++) cout << list[x] << " "; cout << endl;);
		_quickSort(list, lo, pi-1, N, comp);
		_quickSort(list, pi+1, hi, N, comp);
	}
}

void quickSort(int *a, int n, int(*comp)(int, int)) {
	_quickSort(a, 0, n-1, n, comp);
}

void bubbleSort(pList p, int(*comp)(int, int)) {
	bool swapped = true;
	DPRINT(cout << ">bubbleSort N=" << size(p) << endl;);
	if (sorted(p)) return reverse(p);

	pNode tail = end(p);
	pNode curr;
	for (pNode i=begin(p); i != end(p) && swapped; i = i->next) {
		swapped = false;
		for (curr = begin(p); curr->next != tail; curr = curr->next) {
			if (comp(curr->item, curr->next->item) > 0) {
				swap(curr->item, curr->next->item);
				swapped = true;
			}
		}
		DPRINT(show(p, false););
		tail = curr;
	}
	DPRINT(cout << "<bubbleSort N=" << size(p) << endl;);
}

void bubbleSort2(pList p, int(*comp)(int, int)) {
	bool swapped;
	DPRINT(cout << ">bubleSort2 N=" << size(p) << endl;);
	if (sorted(p)) return reverse(p);

	pNode tail = end(p);
	do {
		swapped = false;
		pNode curr = begin(p);
		while (curr->next != tail) {
			if (comp(curr->item, curr->next->item) > 0) {
				swap(curr->item, curr->next->item);
				swapped = true;
			}
			curr = curr->next;
		}
		DPRINT(show(p, false););
		tail = curr;
	} while (swapped);
	DPRINT(cout << "<bubbleSort N=" << size(p) << endl;);
}

void selectionSort(pList p, int(*comp)(int, int)) {
	DPRINT(cout << ">selectionSort N=" << size(p) << endl;);
	pNode i = begin(p);
	pNode min;

	for (; i->next != end(p); i = i->next) {
		min = i;
		for (pNode j = i->next; j != end(p); j = j->next) {
			if (j->item < min->item) {
				min = j;
			}
		} swap(i->item, min->item);
	}

	DPRINT(cout << "<selctionSort N=" << size(p) << endl;);
}

/** for your reference
void selectionSort(int *list, int n) {
	int min;
	for (int i=0; i < n-1; i++) {
		min = i;
		for (int j = i+1; j < n; j++)
			if (list[j] < list[min])
				min = j;
		// Swap min found with the first one of unsorted
		swap(list[i], list[min]);
		DPRINT(for (int x=0; x < n; x++) cout << list[x] << " "; cout << endl;);
	}
}
*/