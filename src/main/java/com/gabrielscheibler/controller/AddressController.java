package com.gabrielscheibler.controller;


import com.gabrielscheibler.entity.Hash;
import com.gabrielscheibler.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/address")
public class AddressController
{
    @Autowired
    private AddressService addressService;

    @RequestMapping(value = "", method = GET)
    //Returns ResponseEntity of type "Address" or "ApiError"
    public ResponseEntity<?> get(@RequestParam(name = "hash") Hash hash)
    {
        return addressService.getAddress(hash);
    }
}
