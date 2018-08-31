package com.gabrielscheibler.controller;


import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.AddressResponse;
import com.gabrielscheibler.dto.Hash;
import com.gabrielscheibler.dto.ResponseDto;
import com.gabrielscheibler.exceptions.InvalidHashException;
import com.gabrielscheibler.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/{hash}", method = GET)
    public ResponseEntity<ResponseDto<AddressResponse>> get(@PathVariable Hash hash) throws InvalidHashException
    {
        AddressResponse retAddress = addressService.getAddressResponse(hash);
        return ResponseEntity.ok(new ResponseDto<AddressResponse>(retAddress,200,"OK"));
    }
}
