package com.gabrielscheibler.service;


import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.AddressResponse;
import com.gabrielscheibler.dto.Hash;
import com.gabrielscheibler.exceptions.InvalidHashException;
import jota.error.ArgumentException;
import jota.utils.InputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class AddressService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * create the corresponding iota-address to a given hash value
     *
     * @param hash a sha-256 hash value
     * @return an iota-address
     * @throws InvalidHashException
     */
    public Address getAddress(Hash hash) throws InvalidHashException
    {
        if (!isSha256(hash))
        {
            InvalidHashException e = new InvalidHashException();
            logger.debug("",e);
            throw e;
        }

        Address address = generateAddress(hash);
        return address;
    }

    /**
     * return hash and address pair
     *
     * @param hash a sha-256 hash value
     * @return an addressResponse Entity
     * @throws InvalidHashException
     */
    public AddressResponse getAddressResponse(Hash hash) throws InvalidHashException
    {
        return new AddressResponse(getAddress(hash).getAddress(),hash.getHash());
    }

    /**
     * check if given hash is a valid sha-256 hash value
     *
     * @param hash hash value to check
     * @return true if hash is valid
     */
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

    /**
     * append zero values to address
     *
     * @param address address to append to
     * @return an 81-char long address
     */
    private Address appendZero(Address address)
    {
        String s = address.getAddress();
        char[] zeros = new char[81 - s.length()];
        Arrays.fill(zeros, '9');
        return new Address(s + String.valueOf(zeros));
    }

    /**
     * generate trenary address from hexadecimal hash value
     *
     * @param hash hexadecimal hash to translate to trenary
     * @return a address in trenary representation
     */
    private Address generateAddress(Hash hash)
    {
        char[] tryteValues = "9ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

        String s = hash.getHash();
        int len = s.length();
        char[] terneary = new char[len];
        for (int i = 0; i < len; i += 2)
        {
            // value of next Byte
            int byteValue = ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));

            int firstValue = byteValue % 27; //value of first tryte
            int secondValue = (byteValue - firstValue) / 27; //value of second tryte

            //get trenary representations for values
            terneary[i] = tryteValues[firstValue];
            terneary[i + 1] = tryteValues[secondValue];
        }

        Address ret = new Address(String.valueOf(terneary));
        ret = appendZero(ret);

        return ret;
    }

    /**
     * check if given address is a valid iota-address
     *
     * @param address to check
     * @return true if address is valid
     */
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
