package com.gabrielscheibler.controller;


import com.gabrielscheibler.Main;
import com.gabrielscheibler.dto.ApiState;
import com.gabrielscheibler.dto.ResponseDto;
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
    public ResponseEntity<ResponseDto<ApiState>> getApiState()
    {
        return ResponseEntity.ok(new ResponseDto<ApiState>(apiStateService.getState(),200,"OK"));
    }
}
