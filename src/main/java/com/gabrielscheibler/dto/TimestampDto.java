package com.gabrielscheibler.dto;

/**
 * Created by root on 20.08.18.
 */
public class TimestampDto
{
    private boolean confirm_status;
    private long timestamp;
    private String address;
    private String transaction;

    public TimestampDto(boolean confirm_status, String address, long timestamp, String transaction)
    {
        this.confirm_status = confirm_status;
        this.timestamp = timestamp;
        this.address = address;
        this.transaction = transaction;
    }

    public boolean getConfirm_status()
    {
        return confirm_status;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getAddress()
    {
        return address;
    }

    public String getTransaction()
    {
        return transaction;
    }
}
