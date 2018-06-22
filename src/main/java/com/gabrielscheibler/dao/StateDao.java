package com.gabrielscheibler.dao;


import com.gabrielscheibler.entity.State;

public class StateDao
{
    private State state;

    public StateDao()
    {
        state = new State(false);
    }

    public State getState()
    {
        return this.state;
    }
}
