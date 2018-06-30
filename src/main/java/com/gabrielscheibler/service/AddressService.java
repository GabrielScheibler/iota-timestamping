package com.gabrielscheibler.service;


import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.ApiError;
import com.gabrielscheibler.entity.Hash;
import jota.error.ArgumentException;
import jota.utils.InputValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Service
public class AddressService
{

    public ResponseEntity<?> getAddress(Hash hash)
    {
        if (!isSha256(hash))
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "not a sha-256 hash value"));

        Address address = generateAddress(hash);
        return new ResponseEntity<Address>(address, HttpStatus.OK);
    }

    private boolean isSha256(Hash hash)
    {
        String s = hash.getHash();
        int len = s.length();

        if (len != 64)
            return false;

        for (int i = 0; i < len; i++)
        {
            int ascii = (int) s.charAt(i);
            if (!((ascii >= 48 && ascii <= 57) ||
                    (ascii >= 65 && ascii <= 70) ||
                    (ascii >= 97 && ascii <= 102)))
                return false;
        }

        return true;
    }

    private Address appendZero(Address address)
    {
        String s = address.getAddress();
        char[] zeros = new char[81 - s.length()];
        Arrays.fill(zeros, '9');
        return new Address(s + String.valueOf(zeros));
    }

    private Address generateAddress(Hash hash)
    {
        char[] tryteValues = "9ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        String s = hash.getHash();
        int len = s.length();
        char[] terneary = new char[len];
        for (int i = 0; i < len; i += 2)
        {
            int byteValue = ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

            int firstValue = byteValue % 27;
            int secondValue = (byteValue - firstValue) / 27;

            terneary[i] = tryteValues[firstValue];
            terneary[i + 1] = tryteValues[secondValue];
        }

        Address ret = new Address(String.valueOf(terneary));
        ret = appendZero(ret);

        return ret;
    }

    public boolean isValidAddress(Address address)
    {
        String s = address.getAddress();

        try
        {
            return InputValidator.checkAddress(s);
        }
        catch (ArgumentException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
