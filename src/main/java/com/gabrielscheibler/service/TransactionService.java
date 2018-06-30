package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.TransactionPostThread;
import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.ApiError;
import com.gabrielscheibler.entity.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService
{
    @Autowired
    private AddressService addressService;
    @Autowired
    private ApiStateService apiStateService;

    public ResponseEntity<?> postTransactionByHash(Hash hash)
    {
        ResponseEntity<?> address_response = addressService.getAddress(hash);

        if(address_response.getStatusCode() != HttpStatus.OK || !(address_response.getBody() instanceof Address))
            return address_response;

        Address address = ((ResponseEntity<Address>) address_response).getBody();

        return postTransactionByAddress(address);
    }

    public ResponseEntity<?> postTransactionByAddress(Address address)
    {
        if(!addressService.isValidAddress(address))
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "not a valid IOTA address"));

        boolean busy = apiStateService.getSetBusy(true);

        if(busy)
            return new ResponseEntity<ApiError>(new ApiError(HttpStatus.SERVICE_UNAVAILABLE,"Api is currently busy processing a transaction"),HttpStatus.SERVICE_UNAVAILABLE);

        new TransactionPostThread(address,this).start();

        return ResponseEntity.ok(address);
    }

    public void makeResponse(ResponseEntity<?> response)
    {
        apiStateService.getSetBusy(false);
    }
}
