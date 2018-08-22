package com.gabrielscheibler.dto;


import java.util.ArrayList;

public class TimestampListDto
{
    private String hash;

    private ArrayList<TimestampDto> timestamps;

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
