package com.gabrielscheibler.dto;


import java.util.concurrent.atomic.AtomicBoolean;

public class ApiState
{
    private AtomicBoolean busy; // true if api is busy processing a request

    public ApiState(boolean busy)
    {
        this.busy = new AtomicBoolean(busy);
    }

    public ApiState(ApiState state)
    {
        this.busy = new AtomicBoolean(state.getBusy());
    }

    public boolean getBusy()
    {
        return busy.get();
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
