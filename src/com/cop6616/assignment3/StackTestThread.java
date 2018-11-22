package com.cop6616.assignment3;

import java.util.Random;

class StackTestThread implements Runnable
{
    private Stack<Integer> stack;
    private int range =0;
    private float pushr=0;
    private float popr=0;
    private float opsr=0;
    private float pushCount=0;
    private float popCount=0;
    private float numOpsCount=0;
    private float totalCount=0;

    //Concurrent driver for the test
    StackTestThread(int _range, Stack<Integer> _stack, float _pushr, float _popr, float _opsr)
    {
        range = _range;
        stack = _stack;
        pushr = _pushr;
        popr = _popr;
        opsr = _opsr;
    }

    //Runs a test with a random choice of action, bounded by passed in proportions on each action.
    @Override
    public void run()
    {
        Random random = new Random();

        for(int i = 0; i < range; i ++)
        {
            boolean found = false;

            while(!found)
            {
                if ( pushCount == 0 || pushCount / totalCount <= pushr)
                {
                    Integer rnd = random.nextInt(10);
                    stack.Push(rnd);
                    pushCount++;
                    found = true;

                    //System.out.println("PUSH: " + rnd);
                }
                else
                if ( popCount == 0 || popCount / totalCount <= popr)
                {
                    Integer val = stack.Pop();
                    popCount++;
                    found = true;

                    //System.out.println("POP: " + val);
                }
                else
                if (numOpsCount == 0 || numOpsCount / totalCount <= opsr)
                {
                    int ops = stack.GetNumOps();
                    numOpsCount++;
                    found = true;

                    //System.out.println("OPs: " + ops);
                }

            }

            totalCount++;
        }
    }
}