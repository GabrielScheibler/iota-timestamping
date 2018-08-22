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

    /**
     * canonically set busyState only if old busyState is false
     *
     * @param busy new busy state to be set
     * @return old busy state
     */
    public boolean getSetBusy(boolean busy)
    {
        return apiState.getSetBusy(busy);
    }
}
