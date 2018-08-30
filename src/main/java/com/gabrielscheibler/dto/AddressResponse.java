package com.gabrielscheibler.dto;

public class AddressResponse
{
    private String address;
    private String hash;

    public AddressResponse(String address_string, String hash_string)
    {
        this.address = address_string;
        this.hash = hash_string;
    }

    public String getAddress()
    {
        return address;
    }

    public String getHash()
    {
        return hash;
    }
}
