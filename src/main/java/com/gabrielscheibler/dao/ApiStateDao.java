package com.gabrielscheibler.dao;


import com.gabrielscheibler.dto.ApiState;
import com.gabrielscheibler.dto.ApiStateResponse;

public class ApiStateDao
{
    private ApiState apiState;

    public ApiStateDao()
    {
        apiState = new ApiState(false);
    }

    public ApiStateResponse getApiState()
    {
        return new ApiStateResponse(this.apiState);
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
