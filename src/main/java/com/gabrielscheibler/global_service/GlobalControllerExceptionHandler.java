package com.gabrielscheibler.global_service;

import com.gabrielscheibler.dto.ResponseDto;
import com.gabrielscheibler.exceptions.*;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
@RequestMapping
class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler
{
    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(InvalidHashException.class)
    @ResponseBody
    public ResponseDto<?> handleInvalidHashException() {
        return new ResponseDto<>(null,400,"Bad Request");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(ApiBusyException.class)
    @ResponseBody
    public ResponseDto<?> handleApiBusyException() {
        return new ResponseDto<>(null,503,"Service Unavailable");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(NetworkOfflineException.class)
    @ResponseBody
    public ResponseDto<?> handleNetworkOfflineException() {
        return new ResponseDto<>(null,504,"Gateway Timeout");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(TimestampRetrievalErrorException.class)
    @ResponseBody
    public ResponseDto<?> handleTimestampRetrievalErrorException() {
        return new ResponseDto<>(null,420,"Method Failed");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(TransactionErrorException.class)
    @ResponseBody
    public ResponseDto<?> handleTransactionErrorException() {
        return new ResponseDto<>(null,420,"Method Failed");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(InternalErrorException.class)
    @ResponseBody
    public ResponseDto<?> handleInternalErrorException() {
        return new ResponseDto<>(null,500,"Internal Error");
    }

    @ResponseStatus(HttpStatus.OK)  // 200
    @ExceptionHandler(TimedOutException.class)
    @ResponseBody
    public ResponseDto<?> handleTimedOutException() {
        return new ResponseDto<>(null,504,"Gateway Timeout");
    }

    public GlobalControllerExceptionHandler()
    {
        super();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,500,"Internal Server Error"));
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,500,"Internal Server Error"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,400,"Bad Request"));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,500,"Internal Server Error"));
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest)
    {
        return ResponseEntity.ok(new ResponseDto<>(null,500,"Internal Server Error"));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}