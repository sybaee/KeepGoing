// listdbl.cpp   Signed: Seungye Bae   Student Number: 21600326

/**
* File: listdbl.cpp, listdbl.h
*       implements a doubly linked list with sentinel nodes
*       and test it interactively
*
* 1. This implements a doubly linked list with two sentinel nodes which
*    provide with benifits of coding consistency and easy maintenance.
* 2. It does not implment C++ iterator (which is deprecated), but simulated
*    most of memeber functions defined in std::List.
*
* The following command removes some invisible bad character in the code file.
*    iconv -f utf-8 -t utf-8 -c file.txt
* will clean up UTF-8 file, skipping all the invalid characters in the cpp file.
*    -f is the source format
*    -t is the target format
*    -c skips any invalid sequence
*    -o sets for different output file
*
*/

#include <iostream>
#include <cassert>
#include <iomanip>
#include "listdbl.h"
using namespace std;

// returns the first node which List::head points to in the container.
pNode begin(pList p) {
	return p->head->next;
}

// returns the tail node referring to the past -the last- node in the list.
// The past -the last- node is the sentinel node which is used only as a sentinel
// that would follow the last node. It does not point to any node next, and thus
// shall not be dereferenced. Because the way we are going use during the iteration,
// we don't want to include the node pointed by this. this function is often used
// in combination with List::begin to specify a range including all the nodes in
// the list. This is a kind of simulated used in STL. If the container is empty,
// this function returns the same as List::begin.
pNode end(pList p) {
	return p->tail;          // not tail->next
}

pNode last(pList p) {
	return p->tail->prev;
}

// returns the first node of the second half of the list.
// If the number of nodes are odd, it returns the one at the center.
// For even numbers, it returns the first node of the second half.
// For example, for list [0, 1, 2, 3, 4, 5, 6, 7], it returns 4.
pNode half(pList p) {  // method 1
	// works for every integer
	// because odd number divided by 2 in C++ gets the floor of the quotient
	int N = size(p)/2 + 1;
	pNode c = begin(p);

	// go through the lists
	// break at the halfway point
	for (int i=1; i < N; i++) {
		if (c != end(p)) c = c->next;
	}

	// return the current pointer
	return c;
}

#if 0
pNode half(pList p) {  // method 2 - rabbit and turtle
	pNode rabbit = begin(p);
	pNode turtle = begin(p);
	while (rabbit != end(p)) {
		if (rabbit->next == end(p)) break; // odd number case
		rabbit = rabbit->next->next;
		turtle = turtle->next;
	}

	return turtle;
}
#endif

// Using the same logic in half() as shown above, but use two input
// nodes as arguments. This function is useful when you want to find
// the mid elements in the linked list with nodes only.
pNode half(pNode lo, pNode hi) {  // method 3 - rabbit and turtle
	if (lo == hi || lo -> next == hi) return hi;

	pNode temp = lo;
	while (temp->next != hi) {
		if (temp->next->next == hi) break;
		temp = temp->next->next;
		lo = lo->next;
	}

	return lo;
	// return slow;
}

// This is an iterative version of the binary search algorithm for
// It works for a singly-linked list as well as doubly-linked list.
// Notice that the argument "last" is not used in the code, but I kept it
// for the compatability with the current proto-type defined in listdbl.h.
pNode _binary_search(pNode start, pNode last, int key) {  // last is not used in the code
	DPRINT(cout << "key= " << key << endl;);
	pNode lo = start;
	pNode hi = nullptr;
	do {
		pNode mid = half(lo, hi);           // find the mid element
		if (mid == nullptr) return nullptr;	// not found

		DPRINT(cout << "mid lo=" << lo->item << "  mid=" << mid->item << endl;);
		if (mid->item == key) return mid;	// found the key

		// set the new boundary(lo, hi) for search
		mid->item < key ? lo = mid->next : hi = mid;
	} while (hi == nullptr || hi != lo);

	DPRINT(cout << "not found: lo=" << lo->item << " hi=" << hi->item << endl;);
	return nullptr;
}

// searches the key using binary search algorithm. The input sequence
// is not an array, but a linked list. The algorithm is the same, but
// the parameters are a bit different since we must pass nodes around
// instead of index since the elements are in linked list, not an array.
pNode binary_search(pList p, int key) {
	pNode mid = _binary_search(begin(p), end(p)->prev, key);
	return mid;
}

// returns the first node with val found, the tail sentinel node
// returned by end(p) if not found. O(n)
pNode find(pList p, int val) {
	DPRINT(cout << ">find val=" << val << endl;);
	// begin(p) = the first node after the head
	pNode c = begin(p);
	// end(p) = the tail
	for (; c != end(p); c = c->next)
		if (c->item == val) return c;

	DPRINT(cout << "<find - not found\n";);
	// if return c here, c = the tail
	return c;
}

// Removes all elements from the list container(which are destroyed),
// and leaving the container with a size of 0.
void clear(pList p) {
	if (empty(p)) return;

	DPRINT(cout << ">clear: ";);
	pNode curr = begin(p);
	pNode head = curr->prev;		// saved head node
	while (curr != end(p)) {
		pNode prev = curr;
		curr = curr->next;
		DPRINT(cout << prev->item << " ";);
		delete prev;
	}
	DPRINT(cout << endl;);

	p->head->next = p->tail;			// set tail at head
	p->tail->prev = p->head;			// set head at tail
	cout << "\tAll things are cleared.\n";
}

// returns true if the list is empty, false otherwise.
// To clear a list, see List::clear.
bool empty(pList p) {
	return begin(p) == end(p);
}

// returns the number of nodes in the list container.
int size(pList p) {
	int count = 0;
	for (pNode c=begin(p); c != end(p); c = c->next)
		count++;
	return count;
}

//////////////////////////////////////////////////////////////////////////
/////////// Make the best use of the following two fucntions  ////////////
///////////         insert() and erase()                      ////////////
//////////////////////////////////////////////////////////////////////////
// inserts a new node with val at the position of the node x.
// The new node is actually inserted in front of the node x.
// This effectively increases the list size by one. O(1)
void insert(pNode x, int val) {
	DPRINT(cout << ">insert val=" << val << endl;);
	pNode node = new Node { val, x->prev, x };
	x->prev = x->prev->next = node;
	DPRINT(cout << "<insert\n";);
}

// removes from the list a single node x given.
// This effectively reduces the container by one which is destroyed.
// It is specifically designed to be efficient inserting and removing
// a node regardless of its positions in the list such as front, back
// or in the middle of the list. O(1)
void erase(pNode x) {
	x->prev->next = x->next;
	x->next->prev = x->prev;
	delete x;
}

void erase(pList p, pNode x) {	// checks if x is either tail or head
	if (x == p->tail || x == p->head || x == nullptr) return;
	x->prev->next = x->next;
	x->next->prev = x->prev;
	delete x;
}
///////////////////////////////////////////////////////////////////////////

/////////////////////// pop ///////////////////////////////////////////////
// removes the first node in the list. O(1)
void pop_front(pList p) {
	DPRINT(cout << ">pop_front\n";);
	if (!empty(p)) erase(begin(p));
	DPRINT(cout << "<pop_front\n";);
}

// removes the last node in the list. O(1)
void pop_back(pList p) {
	DPRINT(cout << ">pop_back\n";);
	if (!empty(p)) erase(end(p)->prev);
	DPRINT(cout << "<pop_back\n";);
}

// removes the first node with val and does nothing if not found.
// Unlike member function List::erase which erases a node by its
// position. Unlike pop(), pop_all() removes all the nodes with
// the value given.
void pop(pList p, int val) {
	DPRINT(cout << ">pop val=" << val << endl;);
	erase(p, find(p, val));

	DPRINT(cout << "<pop\n";);
}

// removes all the nodes with the same value given. O(n)
// This goes through the list once, not multiple times. Unlike
// erase(), which erases a node by its position node, this function
// removes nodes by its value. Unlike pop_all(), pop() removes the
// first node with the value given.
void pop_all(pList p, int val) {
	DPRINT(cout << ">pop_all val=" << val << endl;);
#if 1
	// O(n)
	for (pNode c=begin(p); c != end(p); c = c->next) {
		if (c->item == val) {
			pNode temp = c->prev;
			// cout << temp->item << endl; debugging
			erase(p, c);
			c = temp;
		}
	} // faster version

#else
	while (find(p, val) != end(p)) {
		pop(p, val);
	} // slower version

#endif
	DPRINT(cout << "<pop_all\n";);
}

// deletes N number of nodes, starting from the end.
// It deletes all the nodes if N is zero which is the default
// or out of the range of the list.  Since it simply calls
// pop_back() which is O(1) repeatedly, it is O(n).
void pop_backN(pList p, int N) {
	DPRINT(cout << ">pop_backN N=" << N << endl;);
	int psize = size(p);
	if (N <= 0 || N > psize) N = psize;
	for (int i=0; i < N; i++) {
		if (i % 10000 == 0)
			cout << setw(7) << "\r\tdeleting in [" << psize - i - 1 << "]        ";
		pop_back(p);
	}
	cout << "\n";
	DPRINT(cout << "<pop_backN\n";);
}

/////////////////////// push ///////////////////////////////////////////////
// inserts a new node with val at the beginning of the list. O(1)
void push_front(pList p, int val) {		// inserts a node at front of list
	DPRINT(cout << ">push_front val=" << val << endl;);
	insert(begin(p), val);
	DPRINT(cout << "<push_front\n";);
}

// adds a new node with val at the end of the list and returns the
// first node of the list. O(1)
void push_back(pList p, int val) {
	DPRINT(cout << ">push_back val=" << val << endl;);
	insert(end(p), val);
	DPRINT(cout << "<push_back\n";);
}

// inserts a new node with val at the position of the node with x.
// The new node is actually inserted in front of the node with x.
// It returns the first node of the list.
// This effectively increases the container size by one.
void push(pList p, int val, int x) {
	DPRINT(cout << ">push val=" << val << endl;);
	insert(find(p, x), val);

	DPRINT(cout << "<push\n";);
}

// adds N number of new nodes at the end of the list. O(n)
// if val == 0, the values for new nodes are randomly generated in the
// range of [0..(N + size(p))]. Otherwise, simply insert the same val
// for N times.
void push_backN(pList p, int N, int val) {
	DPRINT(cout << ">push_backN N=" << N;);
	int psize = size(p);

	if (val == 0) {
		int range = N + psize;
		srand((unsigned)time(NULL));
		for (int i=0; i < N; i++) {
			int val = (rand() * RAND_MAX + rand()) % range;
			push_back(p, val);
			if (i % 10000 == 0)
				cout << setw(7) << "\r\tinserting in [" << i + psize << "]=" << val << "        ";
		}
	}

	else {
		for (int i=0; i < N; i++) {
			push_back(p, val);
			if (i % 10000 == 0)
				cout << setw(7) << "\r\tinserting in [" << i + psize << "]=" << val << "        ";
		}
	}
	cout << "\n";

	DPRINT(cout << "<push_backN\n";);
}

/////////////////////// unique, reverse, shuffle ///////////////
// removes extra nodes that have duplicate values from the list.
// It removes all but the first node from every consecutive group
// of equal nodes. Notice that a node is only removed from the
// list if it compares equal to the node immediately preceding it.
// Thus, this function is especially useful for sorted lists. O(n)
void unique(pList p) {
	DPRINT(cout << ">unique N=" << size(p) << endl;);
	// if there is a only one node, no need to compare
	if (size(p) <= 1) return;

	for (pNode c=begin(p); c != end(p); c = c->next)
		if (c->item == c->prev->item) {
			c->prev->next = c->next;
			c->next->prev = c->prev;
			erase(p, c);
		}

	DPRINT(cout << "<unique";);
}

// reverses the order of the nodes in the list.
// The entire operation does not involve the construction and
// destruction of any element. Nodes are not moved, but poiters
// are moved within the list. O(n)
void reverse(pList p) {
	DPRINT(cout << ">reverse\n";);
	if (size(p) <= 1) return;

	// Using a loop, swap prev and next in every node in the list
	// including two sentinel nodes.
	// Once finished, then, swap two sentinel nodes.
	pNode curr = begin(p);
	while (curr != nullptr) {
		swap(curr->prev, curr->next);
		curr = curr->prev;
	}

	swap(p->head, p->tail);

	DPRINT(cout << "<reverse\n";);
}

// returns so called "perfectly shuffled" list. O(n)
// For example, 1234567890 returns 6172839405.
// The first half and the second half are interleaved each other.
// The shuffled list begins with the second half of the original p.
// The entire operation does not involve the construction,
// destruction of any element. It does not invoke insert().
void shuffle(pList p) {
	DPRINT(cout << ">shuffle\n";);
	if (size(p) <= 1) return;    // nothing to shuffle

	// find the mid node of the list p to split it into two lists.
	// remove 1st half from the list p, and keep it as a list "que".
						// the que does not have sentinel nodes
						// set the last node of que terminated by null

	// set the list p head such that it points the "mid" of the list p.
					 // the list "mid" becomes the list p.
					 // the list "mid" now has two sentinel nodes

	// interleave nodes in the "que" into "mid" in the list of p.
	// start inserting 1st node in "que" at 2nd node in "mid".

	pNode mid = half(p);
	pNode que = begin(p);

	mid->prev->next = nullptr;
	mid->prev = p->head;
	p->head->next = mid;

	mid = begin(p)->next;

	while (que != nullptr) {
		pNode q_next = que->next;
		pNode m_next = mid->next;

		mid->prev->next = que;
		que->prev = mid->prev;

		que->next = mid;
		mid->prev = que;

		mid = m_next;
		que = q_next;
	}

	DPRINT(cout << "<shuffle\n";);
}

///////////////////////// sort /////////////////////////////////////////////
int ascending (int a, int b) { return a - b; };
int descending(int a, int b) { return b - a; };
int more(int a, int b) { return (a - b); }
int less(int a, int b) { return (b - a); }

// returns the node of which value is larger than x found first,
// the tail sentinel node which is returned by end(p) otherwise.
pNode _more(pList p, int x) {
	pNode c = begin(p);
	for (; c != end(p); c = c->next)
		if (c->item > x) return c;

	return c;
}

// returns the node of which value is smaller than x found first,
// the tail sentinel node which is returned by end(p) otherwise.
pNode _less(pList p, int x) {
	pNode c = begin(p);
	for (; c != end(p); c = c->next)
		if (c -> item < x) return c;

	return c;
}

// returns true if sorted either by either ascending or descending
bool sorted(pList p) {
	DPRINT(cout << ">sorted up or dn\n";);
	return sorted(p, ascending) || sorted(p, descending);
}

// returns true if sorted according to comp fp provided
bool sorted(pList p, int(*comp)(int a, int b)) {
	DPRINT(cout << ">sorted?\n";);
	if (size(p) <= 1) return true;

	int item = begin(p)->item;
	for (pNode c = begin(p)->next; c != end(p); c = c->next) {
		if (comp(item, c->item) > 0) return false;
		item = c->item;
	}

	DPRINT(cout << "<sorted: true\n";);
	return true;
}

// inserts a node with val in sorted in the "sorted" list. O(n)
void push_sorted(pList p, int val) {
	DPRINT(cout << "<push_sorted val=" << val << endl;);
	if (sorted(p, ascending)) {
		pNode node = _more(p, val);
		insert(node, val);
	}

	else {
		pNode node = _less(p, val);
		insert(node, val);
	}

	DPRINT(cout << "<push_sorted\n";);
}

// inserts N number of nodes in sorted in the sorted list.
// If you invoke push_sort() by N times, it takes longer. Therefore,
// don't call push_sort() N timee. But if you may follow something
// like push_sort(), its time complexity will be O(n^2) or larger.
// The values for new nodes are randomly generated in the range of
// [0..(N + size(p))]. For mac users, you use rand(). For pc, use
// (rand() * RAND_MAX + rand()) instead of rand().
void push_sortedN(pList p, int N) {
	DPRINT(cout << "<push_sortedN N=" << N << endl;);

	int psize = size(p);
	int range = N + psize;

	srand((unsigned)time(NULL));	// initialize random seed

#if 1
	// O(n^2) implment your code here for O(n^2)
	// Refer to push_sorted(), but don't invoke push_sorted().
	bool status = sorted(p, ascending);
	for (int i=0; i < N; i++) {
		int val = (rand() * RAND_MAX + rand()) % range;
		if (status) {
			pNode node = _more(p, val);
			insert(node, val);
		}

		else {
			pNode node = _less(p, val);
			insert(node, val);
		}
	}

#endif

#if 0
	// O(n^3) Don't implement somethig like this, but in O(n^2).
	for (int i=0; i < N; i++) {
		int val = (rand() * RAND_MAX + rand()) % range;
		if (sorted(p, ascending)) {
			pNode node = _more(p, val);
			insert(node, val);
		}

		else {
			pNode node = _less(p, val);
			insert(node, val);
		}
	}

#endif

#if 0
	// O(n^3) Don't implement somethig like this, but in O(n^2).
	for (int i=0; i < N; i++) {
		int val = (rand() * RAND_MAX + rand()) % range;
		pNode (*fp)(pList, int) = sorted(p, ascending) ? _more : _less;
		insert(fp(p, val), val);
	}

#endif
	DPRINT(cout << "<push_sortedN\n";);
}

// inserts N number of nodes in sorted in the sorted list.
// The goal of this function is to make it O(n log n).
// Algorithm:
// 1. Generate N numbers to insert. Let's name this array, vals.
// 2. Sort vals using quicksort() of which time complexity
//    is O(n log n), in ascending or descending depending on
//    the list. .
// 3. Merge two lists. - This process is O(n).
//    Compare two values from the list and vals one by one.
//    For example, if sorted ascending and vals is smaller,
//    insert the vals into the list and go for the next val.
//    the list pointer does not increment.
//    If vals is larger, then the list pointer increment, but
//    vals index does not increment.
// 4. If the list is exhausted, then exit the loop. If vals
//    is not exhausted, insert the rest of vals at the end
//    of the list.
//    Make sure that you go through a loop the list and vals
//    together once. This is the same concept used in the
//    most famous "mergesort" algorithm except recursion.
// The values for new nodes are randomly generated in the range of
// [0..(N + size(p))). For mac users, you use rand(). For pc, use
// (rand() * RAND_MAX + rand()) instead of rand().
void push_sortedNlog(pList p, int N) {
	DPRINT(cout << "<push_sortedNlog N=" << N << endl;);

	int psize = size(p);
	int range = N + psize;
	int* vals = new int[N];
 
	for (int i=0; i < N; i++)
		vals[i] = (rand() * RAND_MAX + rand()) % range;

	quickSort(vals, N, ascending);

	pNode i = begin(p);
	int j = 0;

	while (i != end(p)) {
		if (vals[j] <= i->item) {
			insert(i, vals[j]);
			j++;
		}
		else i = i->next;
		if (j == N) break;
	}

	if (j > psize || vals[j] > end(p)->prev->item) {
		for(; j < N; j++)
			insert(end(p), vals[j]);
	}

	delete[] vals;
	DPRINT(cout << "<push_sortedNlog\n";);
}

// returns a list of nodes sorted in ascending order if not
// sorted by default, using bubble or selection sort algorithm
// which is O(n^2).
// If the list is already sorted, it reverses the list such that
// the ascending ordered list becomes a descending order list and
// vice versa. It is O(n).
// In the bubble sort, it checks values of two adjacent node
// whether they are in ascending order or not, if not then we
// swap the value. It does this until every element get its right
// position.  In 1st pass the largest value get its right position
// and in 2nd pass 2nd largest value get its position and in 3rd
// pass 3rd largest element get its position and so on.
void sort(pList p) {
	DPRINT(cout << ">sort N=" << size(p) << endl;);
	if (sorted(p)) return reverse(p);
	bubbleSort(p);
}

///////////////////////// show /////////////////////////////////////////////
// shows the values of all the nodes in the list if all is true or
// the list size is less than pmax * 2. If there are more than
// (pmax * 2) nodes, then it shows only pmax number of nodes from
// the beginning and the end in the list.
void show(pList p, bool all) {
	DPRINT(cout << "show(" << size(p) << ")\n";);
	if (empty(p)) {
		cout << "\n\tThe list is empty.\n";
		return;
	}
	int i;
	int pmax = 10;   // a magic number, max number of items per line
	pNode curr;
	const int N = size(p);

	if (all || N < pmax * 2) {
		for (i=1, curr=begin(p); curr != end(p); curr = curr->next, i++) {
			cout << " -> " << curr->item;
			if (i % pmax == 0) cout << endl;
		}
		if (N % pmax != 0) cout << endl;
		return;
	}

	// print the first pmax items
	for (i=1, curr=begin(p); curr != end(p) && i <= pmax; curr = curr->next, i++)
		cout << " -> " << curr->item;

	if (N > pmax * 2)
		cout << "\n...left out..." << half(p)->item << "...left out...\n";
	else
		cout << "\n";

	// print the last pmax items
	// move the pointer to the place where pmax items are left.
	for (i=0, curr=begin(p); i < (N-pmax); i++, curr = curr->next);
	for (i=1; curr != end(p) && i <= pmax; curr = curr->next, i++)
		cout << " -> " << curr->item;
	cout << "\n";
}