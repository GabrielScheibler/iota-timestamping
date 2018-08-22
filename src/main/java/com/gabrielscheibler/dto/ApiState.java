package com.gabrielscheibler.dto;


import java.util.concurrent.atomic.AtomicBoolean;

public class ApiState
{
    private AtomicBoolean busy;

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

    public boolean getSetBusy(boolean busy)
    {
        boolean oldBusy = this.busy.get();
        this.busy.compareAndSet(!busy,busy);
        return oldBusy;
    }
}
