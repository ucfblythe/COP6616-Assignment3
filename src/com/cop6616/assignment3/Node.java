package com.cop6616.assignment3;

public class Node<T>
{
    T value;
    Node<T> next;

    Node( T _value)
    {
        value = _value;
        next = null;
    }
}
