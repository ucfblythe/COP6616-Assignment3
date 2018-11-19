package com.cop6616.assignment3;

import java.util.Random;

class StackTestThread implements Runnable
{
    private Stack<Integer> stack;
    private int range =0;

    StackTestThread(int _range, Stack<Integer> _stack)
    {
        range = _range;
        stack = _stack;
    }

    @Override
    public void run()
    {
        Random random = new Random();

        for(int i = 0; i < range; i ++)
        {
            if(i % 2 == 0)
            {
                Integer rnd = random.nextInt(10);
                stack.Push(rnd);
            }
            else
            {
                stack.Pop();
            }
        }
    }
}