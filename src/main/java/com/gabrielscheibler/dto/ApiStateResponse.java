package com.gabrielscheibler.dto;


import java.util.concurrent.atomic.AtomicBoolean;

public class ApiStateResponse
{
    private boolean busy; // true if api is busy processing a request
    private long time_started;

    public ApiStateResponse(boolean busy)
    {
        this.busy = busy;
        this.time_started = System.currentTimeMillis();
    }

    public ApiStateResponse(ApiState state) //clone object
    {
        this.busy = state.getBusy();
        this.time_started = state.getTime_started();
    }

    public boolean getBusy()
    {
        return busy;
    }

    public long getTime_started()
    {
        return time_started;
    }

}
