package com.gabrielscheibler.dto;

public class TimestampPostRequest
{
    private String hash;
    private Long iota_amount;

    public TimestampPostRequest(){}

    public TimestampPostRequest(String hash, Long iota_amount)
    {
        this.hash = hash;
        this.iota_amount = iota_amount;
    }

    public String getHash()
    {
        return hash;
    }

    public Long getIota_amount()
    {
        return iota_amount;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public void setIota_amount(Long iota_amount)
    {
        this.iota_amount = iota_amount;
    }
}
