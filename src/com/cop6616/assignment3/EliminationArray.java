package com.cop6616.assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class EliminationArray<T>
{
    private static final int duration = ;
    private static final int timeUnit = ;

    private Random random;

    public EliminationArray(int capacity)
    {
        exchangerArray = new ArrayList<Exchanger<T>>();

        for(int i =0; i < capacity; i++)
        {
            exchangerArray.add(new Exchanger<T>());
        }

        random = new Random();
    }

    private ArrayList<Exchanger<T>> exchangerArray;

    public T EliminationCheck(T value, int range) throws TimeoutException
    {
        int rndSlot = random.nextInt(range);

        int cnvtduration = convertToNanos(duration, timeUnit);

        return exchangerArray.get(rndSlot).Exchange(value, cnvtduration);
    }

}
