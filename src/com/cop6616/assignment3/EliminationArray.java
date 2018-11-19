package com.cop6616.assignment3;

import java.util.ArrayList;

public class EliminationArray<T>
{
    private static final int duration = ;
    private static final int timeUnit = ;

    public EliminationArray(int capacity)
    {
        exchangerArray = new ArrayList<Exchanger<T>>();

        for(int i =0; i < capacity; i++)
        {
            exchangerArray.add(new Exchanger<T>());
        }
    }

    private ArrayList<Exchanger<T>> exchangerArray;
}
