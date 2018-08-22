package com.gabrielscheibler.dto;

public class ResponseDto<T>
{
    private T data;
    private int error_code;
    private String error_message;



    public ResponseDto(T data, int error_code, String error_message)
    {
        this.data = data;
        this.error_code = error_code;
        this.error_message = error_message;
    }

    public T getData()
    {
        return data;
    }

    public int getError_code()
    {
        return error_code;
    }

    public String getError_message()
    {
        return error_message;
    }

}
