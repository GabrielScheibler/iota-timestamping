package com.gabrielscheibler.global_service;

import com.gabrielscheibler.dto.ResponseDto;
import com.gabrielscheibler.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RequestMapping
class GlobalControllerExceptionHandler
{
    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(InvalidHashException.class)
    @ResponseBody
    public ResponseDto<?> handleInvalidHashException() {
        return new ResponseDto<>(null,400,"invalid hash format");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(ApiBusyException.class)
    @ResponseBody
    public ResponseDto<?> handleApiBusyException() {
        return new ResponseDto<>(null,503,"server is currently busy processing another request");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(NetworkOfflineException.class)
    @ResponseBody
    public ResponseDto<?> handleNetworkOfflineException() {
        return new ResponseDto<>(null,420,"cannot reach iota-network");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(TimestampRetrievalErrorException.class)
    @ResponseBody
    public ResponseDto<?> handleTimestampRetrievalErrorException() {
        return new ResponseDto<>(null,420,"could not retrieve timestamps from the iota-network");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(TransactionErrorException.class)
    @ResponseBody
    public ResponseDto<?> handleTransactionErrorException() {
        return new ResponseDto<>(null,420,"could not post transaction on the iota-network");
    }

}