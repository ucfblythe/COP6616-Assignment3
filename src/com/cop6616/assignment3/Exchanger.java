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

            switch (stamp)
            {

                case EMPTY:
                    if(slot.compareAndSet(curItem, newItem, EMPTY, WAITING))
                    {
                        while (System.nanoTime() < timeBound)
                        {
                            curItem = slot.get(stampHolder);
                            stamp = stampHolder[0];

                            if (stamp == BUSY)
                            {
                                slot.set(null, EMPTY);
                                return curItem;
                            }
                        }

                        if (slot.compareAndSet(newItem, null, WAITING, EMPTY))
                        {
                            throw new TimeoutException();
                        }
                        else
                        {
                            curItem = slot.get(stampHolder);
                            slot.set(null, EMPTY);
                            return curItem;
                        }
                    }
                    break;

                case WAITING:
                    if(slot.compareAndSet(curItem, newItem, WAITING, BUSY))
                    {
                        return curItem;
                    }
                    break;
                case BUSY:
                default:
                    break;
            }
        }
    }
}
