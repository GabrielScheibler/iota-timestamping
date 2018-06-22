package com.gabrielscheibler.controller;


import com.gabrielscheibler.entity.Hash;
import com.gabrielscheibler.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/hash")
public class HashController
{
    @Autowired
    private HashService hashService;

    @RequestMapping(value = "/{hash}", method = POST)
    //Returns ResponseEntity of type "Address" or "ApiError"
    public ResponseEntity<?> post(Hash hash)
    {
        return hashService.postHash(hash);
    }
}
