package com.gabrielscheibler.controller;


import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.Hash;
import com.gabrielscheibler.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/transaction")
public class TransactionController
{
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(value = "/hash/{hash}", method = POST)
    //Returns ResponseEntity of type "Address" or "ApiError"
    public ResponseEntity<?> post(Hash hash)
    {
        return transactionService.postTransactionByHash(hash);
    }

    @RequestMapping(value = "/address/{address}", method = POST)
    //Returns ResponseEntity of type "Address" or "ApiError"
    public ResponseEntity<?> post(Address address)
    {
        return transactionService.postTransactionByAddress(address);
    }
}
