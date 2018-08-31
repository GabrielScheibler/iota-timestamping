package com.gabrielscheibler.controller;


import com.gabrielscheibler.dto.*;
import com.gabrielscheibler.exceptions.*;
import com.gabrielscheibler.service.TimestampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/timestamp")
public class TimestampController
{
    @Autowired
    private TimestampService timestampService;

    @RequestMapping(value = "/create", method = POST)
    public ResponseEntity<ResponseDto<TimestampListDto>> postTimestamp(@RequestBody TimestampPostRequest tpr) throws NetworkOfflineException, TransactionErrorException, ApiBusyException, InvalidHashException, TimestampRetrievalErrorException, TimedOutException, InternalErrorException
    {
        return ResponseEntity.ok(new ResponseDto<TimestampListDto>(timestampService.postTimestamp(tpr),200,"OK"));
    }

    @RequestMapping(value = "/{hash}", method = GET)
    public ResponseEntity<ResponseDto<TimestampListDto>> getTimestampList(@PathVariable Hash hash) throws NetworkOfflineException, TransactionErrorException, ApiBusyException, InvalidHashException, TimestampRetrievalErrorException, TimedOutException, InternalErrorException
    {
        return ResponseEntity.ok(new ResponseDto<TimestampListDto>(timestampService.getTimestampList(hash),200,"OK"));
    }

}
