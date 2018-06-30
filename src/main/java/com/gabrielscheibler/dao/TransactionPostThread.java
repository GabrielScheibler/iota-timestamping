package com.gabrielscheibler.dao;


import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.ApiError;
import com.gabrielscheibler.service.TransactionService;

import jota.IotaAPI;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.SendTransferResponse;
import jota.error.ArgumentException;
import jota.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


public class TransactionPostThread extends Thread
{
    private Address address;
    private TransactionService transactionService;
    private IotaAPI api;

    public TransactionPostThread(Address address, TransactionService transactionService)
    {
        this.address = address;
        this.transactionService = transactionService;
    }

    @Override
    public void run()
    {
        super.run();
        initIotaApi();
        issueTransaction(this.address);
    }

    private void initIotaApi()
    {
        api = new IotaAPI.Builder()
                .protocol("https")
                .host("nodes.testnet.iota.org")
                .port("443")
                .build();
        GetNodeInfoResponse response = api.getNodeInfo();
    }

    private void issueTransaction(Address address)
    {
        final String TRANSACTION_SEED = "ORIGINSTAMP";
        final String TRANSACTION_ADDRESS = address.getAddress();
        final int MIN_WEIGHT_MAGNITUDE = 14;
        final int DEPTH = 9;

        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TRANSACTION_ADDRESS, 0, "", ""));

        boolean errorReturned = false;
        SendTransferResponse tr = new SendTransferResponse();

        try
        {
            tr = api.sendTransfer(TRANSACTION_SEED,1,DEPTH,MIN_WEIGHT_MAGNITUDE,transfers,null,null,false);
        }
        catch (ArgumentException e)
        {
            e.printStackTrace();
            ApiError err = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,e.toString());
            transactionService.makeResponse(new ResponseEntity<ApiError>(err,HttpStatus.INTERNAL_SERVER_ERROR));
            errorReturned = true;
        }
        finally
        {
            if (!errorReturned)
            {
                Boolean[] success = tr.getSuccessfully();
                boolean allSuccessfull = true;
                for (int i = 0; i < success.length; i++)
                {
                    if(success[i] == false)
                        allSuccessfull = false;
                }

                if(allSuccessfull)
                    transactionService.makeResponse(ResponseEntity.ok(tr.getTransactions()));
                else
                {
                    ApiError err = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "transaction was unsuccesfull");
                    transactionService.makeResponse(new ResponseEntity<ApiError>(err, HttpStatus.INTERNAL_SERVER_ERROR));
                }
            }
        }
    }
}
