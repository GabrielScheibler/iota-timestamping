package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.TimestampDao;
import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.Hash;
import com.gabrielscheibler.dto.TimestampDto;
import com.gabrielscheibler.dto.TimestampListDto;
import com.gabrielscheibler.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class TimestampService
{
    @Autowired
    private AddressService addressService;
    @Autowired
    private ApiStateService apiStateService;
    @Autowired
    private TimestampDao timestampDao;

    private long timeout_sec = 100;

    public TimestampService(long timeout_sec)
    {
        this.timeout_sec = timeout_sec;
    }

    public TimestampService()
    {
    }

    public TimestampListDto postTimestamp(Hash hash) throws TransactionErrorException, NetworkOfflineException, InvalidHashException, ApiBusyException, TimestampRetrievalErrorException
    {
        Address address = addressService.getAddress(hash);

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
            throw new ApiBusyException();

        Future<Void> tl = timestampDao.postTimestamp(address);

        TimestampListDto ret;

        try
        {
            tl.get(timeout_sec, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e)
        {
            throw new TransactionErrorException();
        }

        apiStateService.getSetBusy(false);

        ret = getTimestampList(hash);

        return ret;
    }

    public TimestampListDto getTimestampList(Hash hash) throws InvalidHashException, TimestampRetrievalErrorException, ApiBusyException, TransactionErrorException
    {
        Address address = addressService.getAddress(hash);

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
            throw new ApiBusyException();

        Future<ArrayList<TimestampDto>> tl = timestampDao.getTimestampList(address);

        ArrayList<TimestampDto> list;

        try
        {
            list = tl.get(timeout_sec,TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e)
        {
            throw new TimestampRetrievalErrorException();
        }

        TimestampListDto ret = new TimestampListDto(hash.getHash(),list);

        apiStateService.getSetBusy(false);

        return ret;
    }
}
