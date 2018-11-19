package com.cop6616.assignment3;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

public class Exchanger<T>
{
    static final int EMPTY = 0;
    static final int BUSY = 1;
    static final int WAITING = 2;


    AtomicStampedReference<T> slot;

    Exchanger()
    {
        slot = new AtomicStampedReference<T>(null,0);
    }

    T Exchange(T newItem, long timeAllowed) throws TimeoutException
    {
        long timeBound = System.nanoTime() + timeAllowed;

        int[] stampHolder = {EMPTY};

        while(true)
        {
            if(System.nanoTime() > timeBound)
            {
                throw new TimeoutException();
            }

            T curItem = slot.get(stampHolder);

            int stamp = stampHolder[0];

            switch(stamp)
            {
                case EMPTY:
                    EmptySlot(curItem, newItem, stamp, timeAllowed);
                    break;
                case BUSY:
                    break;
                case WAITING:
                    break;
                default:
                    break;
            }
        }
    }

    private T EmptySlot(T curItem, T newItem, int stamp, long timeAllowed)
    {

    }
}
