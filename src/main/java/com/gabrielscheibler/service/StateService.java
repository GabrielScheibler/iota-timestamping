package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.StateDao;
import com.gabrielscheibler.entity.State;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StateService
{
    private StateDao stateDao;

    public StateService()
    {
        stateDao = new StateDao();
    }

    public ResponseEntity<State> getState()
    {
        return ResponseEntity.ok(stateDao.getState());
    }
}
