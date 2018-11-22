/**********************************************************************************************************************
 * Author: Chris Blythe
 * Program: Exchanger
 * Description: Structure signifies a possible exchange by containing an atomic slot value. This atomic slot is an
 * atomic stamped reference that contains both a a possible value and a 'stamp' that represents the current status of the
 * exchange. On initialization the stamp is marked as empty in each of the created slots. When an operation arrives at a
 * slot marked empty they CAS the slot with their value a status of waiting. The operation will then wait for another
 * operation to modify the state or until the operation times out. If other operations arrive at
 * the same slot they can try and retrieve the value by doing a CAS on the full slot. This way they can exchange their
 * values and mark the slot busy. The original operation will pick up the change in state and remark the slot as empty
 * for the next exchange.
 *
 **********************************************************************************************************************/

package com.cop6616.assignment3;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicStampedReference;

public class Exchanger<T>
{
    //Possible states of the slot (to be saved in the stamp portion).
    static final int EMPTY = 0;
    static final int BUSY = 1;
    static final int WAITING = 2;


    AtomicStampedReference<T> slot;

    //Initializes an empty slot
    Exchanger()
    {
        slot = new AtomicStampedReference<T>(null,EMPTY);
    }

    T Exchange(T newItem, long timeAllowed) throws TimeoutException
    {
        //How long the operation should wait
        long timeBound = System.nanoTime() + timeAllowed;

        int[] stampHolder = {EMPTY};

        //Will continue until the exchange is resolved or timeout
        while(true)
        {
            //Timeout!!!
            if(System.nanoTime() > timeBound)
            {
                throw new TimeoutException();
            }

            //gets the current item and stamp held by the slot
            T curItem = slot.get(stampHolder);

            //pulls out the status from the stamp
            int stamp = stampHolder[0];

            //Depending on the stamp state
            switch (stamp)
            {

                //Initial entry into the slot and no other value is present
                case EMPTY:

                    //Swap in our value and mark the slot as waiting for an exchange
                    if(slot.compareAndSet(curItem, newItem, EMPTY, WAITING))
                    {
                        //Until we timeout
                        while (System.nanoTime() < timeBound)
                        {
                            //get the stamp and value
                            curItem = slot.get(stampHolder);
                            stamp = stampHolder[0];

                            //check if another operation has made an exchange and marked the slot as busy
                            if (stamp == BUSY)
                            {
                                //reset the slot and return the value recieved in the exchange
                                slot.set(null, EMPTY);
                                return curItem;
                            }
                        }

                        //Timed out with no exchange, but we want to clean up after ourselves so we reset the slot
                        if (slot.compareAndSet(newItem, null, WAITING, EMPTY))
                        {
                            throw new TimeoutException();
                        }
                        else
                        //If we couldn't clean up then some other operation finally showed up so we take their value
                        // in the exchange and forcefully reset.
                        {
                            curItem = slot.get(stampHolder);
                            slot.set(null, EMPTY);
                            return curItem;
                        }
                    }
                    break;

                //Initial entry into the slot and some other operation is waiting for an exchange
                case WAITING:
                    //Attempts the exchange and sets the status to busy
                    if(slot.compareAndSet(curItem, newItem, WAITING, BUSY))
                    {
                        return curItem;
                    }
                    break;
                //Busy means some other exchange has occured and the current operation should look for another slot
                case BUSY:
                default:
                    break;
            }
        }
    }
}
