package com.cop6616.assignment3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Stack<T>
{
    private AtomicReference<Node<T>> head;
    private AtomicInteger numOps;
    private T default_flag;

    //Constructor for the stack that takes in a default_flag. Since the type is generic, and we need to return a value
    //on a failed pop, we need an applicable T value to denote an empty pop. The head and numops are initialized as well.
    Stack(T _default_flag)
    {
        default_flag = _default_flag;
        head = new AtomicReference<>(null);
        numOps = new AtomicInteger(0);
    }

    //Allows a value to be pushed into the stack concurrently
    boolean Push(T x)
    {
        //Creates a temporary node to hold the value and point to the next node in the list AT THIS TIME
        Node<T> newNode = new Node<T>(x);
        //Loads the head node into the new nodes next pointer so the new node tops our new list.

        //Loop will try and set the atomic head of the list to the new node, but only if the current head is = to the
        //last time we examined the head (which we stored in new_node_next). If the head has changed between the last
        //check and the current check it is possible that some other thread has modified the stack. If so compare_exchange
        //will store the value of head into new_node->nexts location, which means the next pointer of our new node is
        //pointing to the new head of the stack (at least for that moment) and we can try again by going through the loop.
        boolean swapped = false;
        while(!swapped)
        {
            newNode.next = head.get();
            swapped = head.compareAndSet(newNode.next, newNode);
        }

        //Atomically increment the numOps value, since this variable is an atomic, the ++ operation acts like a fetch and add
        //https://en.cppreference.com/w/cpp/atomic/atomic/operator_arith
        //numOps is incremented atomically because multiple threads could have reached this point at the same time
        //Example: same time we finished pushing something could quickly finish popping the stack as this thread is
        //incrementing the value, the popping thread could acquire the wrong or a partial value.
        // As an atomic, this increment takes places in one instantaneous transaction and forms the
        // linearization point of the numops increment operation.
        numOps.getAndIncrement();

        return swapped;
    }

    //Allows a node to be popped from the stack concurrently
    T Pop()
    {
        T val = null;

        //Loop will try and set the atomic head of the list to the next node in the list, but only if the current head is = to the
        //last time we examined the head (which we stored in oldHead). If the head has changed between the last
        //check and the current check it is possible that some other thread has modified the stack. If so compare_exchange
        //will store the value of head into oldHead location, which means our old_head should be pointing to the new
        //head of the stack (at least for that moment) and we can try again by going through the loop.
        boolean swapped = false;
        while(!swapped)
        {

            //Saves the head we are looking to remove as a temp node
            Node<T> oldHead = head.get();

            if(oldHead == null)
            {
                return default_flag;
            }

            val = oldHead.value;

            // If successful, this is the linearization point of the pop operation, where it instantly swaps out the head
            //for the next pointer we had stored from our initial check of the head. As an atomic, this increment takes
            // place in one instantaneous transaction, forming the linearization point of the numops increment operation.

            swapped =  head.compareAndSet(oldHead, oldHead.next);
        }

        numOps.getAndIncrement();

        return val;
    }

    //Returns the number of operators
    int GetNumOps()
    {
        return numOps.get();
    }
}
