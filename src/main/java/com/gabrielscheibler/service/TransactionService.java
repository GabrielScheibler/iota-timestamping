package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.TransactionDao;
import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.ApiError;
import com.gabrielscheibler.entity.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TransactionService
{
    @Autowired
    private AddressService addressService;
    @Autowired
    private ApiStateService apiStateService;
    @Autowired
    private TransactionDao transactionDao;

    public ResponseEntity<?> postTransactionByHash(Hash hash)
    {
        ResponseEntity<?> address_response = addressService.getAddress(hash);

        if (address_response.getStatusCode() != HttpStatus.OK || !(address_response.getBody() instanceof Address))
            return address_response;

        Address address = ((ResponseEntity<Address>) address_response).getBody();

        return postTransactionByAddress(address);
    }

    public ResponseEntity<?> postTransactionByAddress(Address address)
    {
        if (!addressService.isValidAddress(address))
            return ResponseEntity.badRequest().body(new ApiError(HttpStatus.BAD_REQUEST, "not a valid IOTA address"));

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
            return new ResponseEntity<>(new ApiError(HttpStatus.SERVICE_UNAVAILABLE, "Api is currently busy processing a transaction"), HttpStatus.SERVICE_UNAVAILABLE);

        Future<ResponseEntity<?>> tr = transactionDao.postTransaction(address);

        ResponseEntity<?> ret;

        try
        {
            ret = tr.get();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
            ret = new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "ExecutionException while processing transaction"), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
            ret = new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "InterruptedException while processing transaction"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        apiStateService.getSetBusy(false);

        return ret;
    }
}
