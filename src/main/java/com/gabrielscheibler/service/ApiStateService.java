package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.ApiStateDao;
import com.gabrielscheibler.dto.ApiState;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiStateService
{
    private ApiStateDao apiStateDao;

    public ApiStateService()
    {
        apiStateDao = new ApiStateDao();
    }

    public ApiState getState()
    {
        return apiStateDao.getApiState();
    }

    /**
     * canonically set busyState only if old busyState is false
     *
     * @param busy new busy state to be set
     * @return old busy state
     */
    public boolean getSetBusy(boolean busy)
    {
        return apiStateDao.getSetBusy(busy);
    }
}
