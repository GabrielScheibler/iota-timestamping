package com.gabrielscheibler.dto;


import java.util.concurrent.atomic.AtomicBoolean;

public class ApiState
{
    private AtomicBoolean busy; // true if api is busy processing a request
    private long time_started;

    public ApiState(boolean busy)
    {
        this.busy = new AtomicBoolean(busy);
        this.time_started = System.currentTimeMillis();
    }

    public boolean getBusy()
    {
        return busy.get();
    }

    public long getTime_started()
    {
        return time_started;
    }

    /**
     * canonically set busyState only if old busyState is false
     *
     * @param busy new busy state to be set
     * @return old busy state
     */
    public boolean getSetBusy(boolean busy)
    {
        boolean oldBusy = this.busy.get();
        this.busy.compareAndSet(!busy,busy);
        return oldBusy;
    }
}
