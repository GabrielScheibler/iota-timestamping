package com.gabrielscheibler.service;

import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HashService
{
    @Autowired
    private AddressService addressService;
    @Autowired
    private StateService stateService;

    public ResponseEntity<Address> postHash(Hash hash)
    {
        //TODO implement iota transaction
        return ResponseEntity.ok(new Address(hash.getHash()));
    }
}
