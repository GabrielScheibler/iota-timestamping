package com.gabrielscheibler.controller;


import com.gabrielscheibler.service.ApiStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/state")
public class ApiStateController
{
    @Autowired
    private ApiStateService apiStateService;

    @RequestMapping(value = "", method = GET)
    //Returns ResponseEntity of type "ApiState" or "ApiError"
    public ResponseEntity<?> get()
    {
        return apiStateService.getState();
    }
}
