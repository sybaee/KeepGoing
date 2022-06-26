// stack.cpp   Signed: Seungye Bae   Student Number: 21600326

/*
* Description:	This program implements a stack using singly-linked list
* without a head structure and without sentinel nodes. It simply links
* node to node. The caller is responsible for maintaining the the first
* node (head) of the list. Most functions need the first node to work
* with the list and returns the first node of the list since it may be
* changed inside of the functions.
*/

#include <iomanip>
#include "stack.h"

// removes all the nodes from the list (which are destroyed),
// and leaving the container nullptr or its size to 0.
pNode clear(pNode p) {
	if (empty(p)) return nullptr;
	DPRINT(cout << "clear: ";);

	pNode temp = p;

	while (temp != nullptr) {
		p = p->next;
		delete temp;
		temp = p;
	}

	cout << "\n\tAll things are cleared.\n";
	return nullptr;
}

// returns the number of nodes in the list.
int size(pNode p) {
	if (empty(p)) return 0;
	int count = 0;

	while (p != nullptr) {
		count++;
		p = p->next;
	}

	return count;
}

// returns true if the list is empty or no nodes.
// To remove all the nodes of a list, see clear().
bool empty(pNode p) {
	return p == nullptr;
}

// inserts a new node with val at the beginning of the list.
// This effectively increases the list size by N.
pNode push(pNode hp, int val, int N) {
	DPRINT(cout << ">push val=" << val << " N="<< N << endl;);

	if (N == 1) {
		// If the list is empty, the new node becomes the head node
		if (empty(hp)) return new Node {val, nullptr};

		else return new Node {val, hp};
	}

	else {
		for (val; val < N; val++) {
			pNode temp = new Node {val, hp};
			hp = temp;
		}
	}

	DPRINT(cout << "<push size=" << size(hp) << endl;);
	return hp;
}

// removes the first node in the list and returns the new first node.
// This destroys the removed node, effectively reduces its size by N.
pNode pop(pNode hp, int N) {
	DPRINT(cout << ">pop size=" << size(hp) << " N="<< N << endl;);
	// If empty, returns nullptr
	if (empty(hp)) return hp;

	if (N == 1) {
		pNode temp = hp;
		hp = hp->next;
		delete temp;
	}

	else {
		pNode temp = hp;
		for (int i=0; i < N; i++) {
			// If the user specifies a number out of the range in P option,
			// it simply removes all the nodes
			if (temp != nullptr) {
				hp = hp->next;
				delete temp;
				temp = hp;
			}
		}
	}

	DPRINT(cout << "<pop size=" << size(hp) << endl;);
	return hp;
}

// returns the first node in the list. This does not destroy the node.
pNode top(pNode hp) {
	DPRINT(cout << ">top size=" << size(hp) << endl;);
	if (empty(hp)) return nullptr;

	return hp;
}

// shows the values of all the nodes in the list if all is true or
// the list size is less than or equal to pmax * 2. If there are more than
// (pmax * 2) nodes, then it shows only pmax number of nodes from
// the beginning and the end in the list.
void show(pNode hp, bool all, int pmax) {
	DPRINT(cout << "show(" << size(hp) << ")\n";);
	if (empty(hp)) {
		cout << "\n\tThe list is empty.\n";
		return;
	}

	else if (all == true || size(hp) <= (pmax * 2)) {
		int count = 0;
		while (hp != nullptr) {
			cout << " -> "<< hp->item;
			hp = hp->next;
			count++;
			if (count % 10 == 0) {
				cout << endl;
			}
		}
		cout << endl;
	}

	else {
		int r = size(hp) - (pmax * 2);

		for (int i=0; i < pmax; i++) {
			cout << " -> "<< hp->item;
			hp = hp->next;
		}
		cout << endl;

		for (int i=0; i < r; i++) {
			hp = hp->next;
		}
		cout << "...left out..." << endl;

		for (int i=0; i < pmax; i++) {
			cout << " -> "<< hp->item;
			hp = hp->next;
		}
		cout << endl;
	}
}