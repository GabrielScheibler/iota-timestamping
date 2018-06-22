package com.gabrielscheibler.controller;


import com.gabrielscheibler.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/state")
public class StateController
{
    @Autowired
    private StateService stateService;

    @RequestMapping(value = "", method = GET)
    //Returns ResponseEntity of type "State" or "ApiError"
    public ResponseEntity<?> get()
    {
        return stateService.getState();
    }
}
