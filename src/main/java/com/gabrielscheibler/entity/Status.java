package com.gabrielscheibler.entity;


public class Status
{
    private boolean inDatabase;
    private boolean attachedToTangle;
    private boolean confirmedInTangle;
    private long confirmationTime;

    public Status(boolean inDatabase, boolean attachedToTangle, boolean confirmedInTangle, long confirmationTime)
    {
        this.inDatabase = inDatabase;
        this.attachedToTangle = attachedToTangle;
        this.confirmedInTangle = confirmedInTangle;
        this.confirmationTime = confirmationTime;
    }
}
