package com.gabrielscheibler.dto;


import java.util.ArrayList;

public class TimestampListDto
{
    private String hash; //hash for which the action was requested
    private ArrayList<TimestampDto> timestamps; //list of timestamps in the tangle for the given hash

    public TimestampListDto(String hash, ArrayList timestamps)
    {
        this.hash = hash;
        this.timestamps = timestamps;
    }

    public String getHash()
    {
        return hash;
    }

    public ArrayList<TimestampDto> getTimestamps()
    {
        return timestamps;
    }
}
