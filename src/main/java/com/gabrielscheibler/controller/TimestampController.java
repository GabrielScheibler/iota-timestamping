package com.gabrielscheibler.controller;


import com.gabrielscheibler.dto.Hash;
import com.gabrielscheibler.dto.ResponseDto;
import com.gabrielscheibler.dto.TimestampListDto;
import com.gabrielscheibler.exceptions.*;
import com.gabrielscheibler.service.TimestampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/timestamp")
public class TimestampController
{
    @Autowired
    private TimestampService timestampService;

    @RequestMapping(value = "/{hash}", method = POST)
    public ResponseEntity<ResponseDto<TimestampListDto>> postTimestamp(@PathVariable Hash hash) throws NetworkOfflineException, TransactionErrorException, ApiBusyException, InvalidHashException, TimestampRetrievalErrorException
    {
        return ResponseEntity.ok(new ResponseDto<TimestampListDto>(timestampService.postTimestamp(hash),200,"OK"));
    }

    @RequestMapping(value = "/{hash}", method = GET)
    public ResponseEntity<ResponseDto<TimestampListDto>> getTimestampList(@PathVariable Hash hash) throws NetworkOfflineException, TransactionErrorException, ApiBusyException, InvalidHashException, TimestampRetrievalErrorException
    {
        return ResponseEntity.ok(new ResponseDto<TimestampListDto>(timestampService.getTimestampList(hash),200,"OK"));
    }

}
