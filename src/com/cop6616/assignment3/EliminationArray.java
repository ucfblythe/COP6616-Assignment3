/**********************************************************************************************************************
 * Author: Chris Blythe
 * Program: Elimination Array
 * Description: When a stack modification fails the operation places their target marker into a random index within the
 * seperate elimination backoff array structure. The operation will continue to check the status of their marker to see
 * if another thread had retrieved the value for it's own operations. Changes in the status stamp of the marker will be
 * picked up by the periodic check of each operation, after which the operation will take its continuing action (i.e a pop
 * waiting for a push). Upon success the pop and the push will resolve each other, upon failure each operation will try
 * again to modify the main stack. The actual elimination array is composed of exchanger objects, which contain a value
 * as well as the main exchange logic for each state in the exchange (EMPTY, WAITING, BUSY).
 *
 **********************************************************************************************************************/

package com.cop6616.assignment3;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class EliminationArray<T>
{
    private static final int duration = 1;
    private static final int timeUnit = 1000000;

    private Random random;


    public EliminationArray(int capacity)
    {
        //Creates the array based off the given capacity.
        exchangerArray = new ArrayList<Exchanger<T>>();

        for(int i =0; i < capacity; i++)
        {
            exchangerArray.add(new Exchanger<T>());
        }

        //instantiates a random object to be used for index selection.
        random = new Random();
    }

    private ArrayList<Exchanger<T>> exchangerArray;

    //Whenever an operation wants
    public T EliminationCheck(T value, int range) throws TimeoutException
    {
        //Chooses a random slot in the array to attempt an exchange instead of traversing the whole array
        int rndSlot = random.nextInt(range);

        //figures out the amount of time the exchanger should wait for another operation to come along and modify it's state.
        long cnvtduration =  duration * timeUnit;

        //Attempts the exchange on the random index.
        return exchangerArray.get(rndSlot).Exchange(value, cnvtduration);
    }

}
