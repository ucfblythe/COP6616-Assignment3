package com.cop6616.assignment3;

import java.util.Vector;

public class Main {



    static void ConcurStackTest(int test, int numThreads) throws Exception
    {

        Stack<Integer> stack = new Stack<Integer>(-1);

        Vector<Thread> threads = new Vector<Thread>();

        int range = 1000000;

        for(int i=0; i < numThreads; i++)
        {
            StackTestThread r = new StackTestThread(range, stack);

            Thread t = new Thread(r);

            t.start();

            threads.add(t);
        }

        long startTime = System.nanoTime();

        for(int i=0; i < numThreads; i++)
        {
            threads.elementAt(i).join();
        }

        long endTime = System.nanoTime();

        double elapsedTime = ((double)(endTime - startTime) / (double)1000000000.0f);


        System.out.println("Test #" + test + ": " + numThreads + " Threads / " +
                elapsedTime +" Seconds");

    }

    public static void main(String[] args)
    {

        for(int i =1; i < 6; i ++)
        {
            try
            {
                ConcurStackTest(i + 1, (int) Math.pow(2, i));
            }
            catch(Exception e)
            {
                System.out.println("ERROR: Unable to run test!!!");
            }
        }
    }
}
