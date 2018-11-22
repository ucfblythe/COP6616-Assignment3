/**********************************************************************************************************************
 * Author: Chris Blythe
 * Program: Elimination Backoff Stack
 * Description: The idea behind this application is to implement a lock-free stack, with the usage of an elimination
 * stack to aid in alieving contention on the stacks top node. Since stack operations are primiarily focused with the
 * managing the top node in the stack structure, concurrent stack operations may lead to high contention. Elimination
 * backoff can help address this by parallelising operations that hacve failed the initial attempt at modifyng the stack.
 * Upon their failure the operation places their target marker into a random index within a seperate elimination
 * backoff array structure. The operation will continue to check the status of their marker to see if another thread has
 * retrieved the value for it's own operations. Changes in the status stamp of the marker will be picked up by the periodic
 * check of each operation, after which the operation will take its contiuing action (i.e a pop waiting for a push). Upon
 * success the pop and the push will resolve each other, upon failure each operation will try again to modify the main stack.
 *
 **********************************************************************************************************************/


package com.cop6616.assignment3;

import java.util.Vector;

public class Main {

    //Runs each indiviual test as a driver with a given number of threads and proportions for each action.
    static void ConcurStackTest(int test, int numThreads,  float _pushr, float _popr, float _opsr) throws Exception
    {

        //Shared stack
        Stack<Integer> stack = new Stack<Integer>(-1);

        Vector<Thread> threads = new Vector<Thread>();

        //NUmber of operations each thread will attempt
        int range = 100000;

        //Creates a StackTestThread instance thread and stores it for retrieval later
        for(int i=0; i < numThreads; i++)
        {
            StackTestThread r = new StackTestThread(range, stack,  _pushr, _popr, _opsr);

            Thread t = new Thread(r);

            //Start function in StackTestThread as runnable class
            t.start();

            threads.add(t);
        }

        //Times total of all thread completions
        long startTime = System.nanoTime();

        for(int i=0; i < numThreads; i++)
        {
            threads.elementAt(i).join();
        }

        long endTime = System.nanoTime();

        //Figure out how long all threads took
        double elapsedTime = ((double)(endTime - startTime) / (double)1000000000.0f);


        System.out.println("Test #" + test + ": " + numThreads + " Threads / " +
                elapsedTime +" Seconds");

    }

    //Runs tests using from 1 to 32 threads and 3 severate proportion of actions.
    public static void main(String[] args)
    {
        for(int i =0; i < 6; i ++)
        {
            try
            {
                ConcurStackTest(i + 1, (int) Math.pow(2, i), 0.45f, 0.05f, 0.50f);
                ConcurStackTest(i + 1, (int) Math.pow(2, i), 0.35f, 0.15f, 0.50f);
                ConcurStackTest(i + 1, (int) Math.pow(2, i), 0.50f, 0.35f, 0.15f);
            }
            catch(Exception e)
            {
                System.out.println("ERROR: Unable to run test!!!");
            }
        }
    }
}
