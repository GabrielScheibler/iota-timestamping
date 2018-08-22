package com.gabrielscheibler.service;


import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.Hash;
import com.gabrielscheibler.exceptions.InvalidHashException;
import jota.error.ArgumentException;
import jota.utils.InputValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class AddressService
{

    public Address getAddress(Hash hash) throws InvalidHashException
    {
        if (!isSha256(hash))
            throw new InvalidHashException();

        Address address = generateAddress(hash);
        return address;
    }

    public static boolean isSha256(Hash hash)
    {
        String line = hash.getHash();
        String pattern = "(^)([0123456789abcdefABCDEF]{64})($)";

        Pattern r = Pattern.compile(pattern);

        Matcher m = r.matcher(line);
        if (m.find( )) {
            return true;
        }else {
            return false;
        }
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
