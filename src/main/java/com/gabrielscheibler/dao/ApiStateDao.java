package com.gabrielscheibler.dao;


import com.gabrielscheibler.dto.ApiState;

public class ApiStateDao
{
    private ApiState apiState;

    public ApiStateDao()
    {
        apiState = new ApiState(false);
    }

    public ApiState getApiState()
    {
        return new ApiState(this.apiState);
    }

    public void setApiState(ApiState newApiState)
    {
        apiState = newApiState;
    }

    public boolean getSetBusy(boolean busy)
    {
        return apiState.getSetBusy(busy);
    }
}
