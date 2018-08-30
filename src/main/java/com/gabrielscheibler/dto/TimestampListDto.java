package com.gabrielscheibler.dto;


import java.util.ArrayList;

public class TimestampListDto
{
    private String hash; //hash for which the action was requested
    private String address; //address corresponding to the hash
    private ArrayList<TimestampDto> timestamps; //list of timestamps in the tangle for the given hash

    public TimestampListDto(String hash, String address, ArrayList<TimestampDto> timestamps)
    {
        this.hash = hash;
        this.address = address;
        this.timestamps = timestamps;
    }

    public String getHash()
    {
        return hash;
    }

    public String getAddress()
    {
        return address;
    }

    public ArrayList<TimestampDto> getTimestamps()
    {
        return timestamps;
    }
}