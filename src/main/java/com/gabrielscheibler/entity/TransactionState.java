package com.gabrielscheibler.entity;



public class TransactionState
{
    private boolean attached;
    private boolean confirmed;

    public TransactionState(boolean confirmed, boolean attached)
    {
        this.attached = attached;
        this.confirmed = confirmed;
    }

    public boolean getAttached()
    {
        return attached;
    }

    public boolean getConfirmed()
    {
        return confirmed;
    }
}
