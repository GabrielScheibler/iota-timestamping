package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.TimestampDao;
import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.Hash;
import com.gabrielscheibler.dto.TimestampDto;
import com.gabrielscheibler.dto.TimestampListDto;
import com.gabrielscheibler.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    /**
     * post a timestamp for a given hash on the tangle
     *
     * @param hash a valid sha-256 hash value
     * @return list of timestamps in the tangle for the given hash
     * @throws TransactionErrorException
     * @throws NetworkOfflineException
     * @throws InvalidHashException
     * @throws ApiBusyException
     * @throws TimestampRetrievalErrorException
     */
    public TimestampListDto postTimestamp(Hash hash) throws TransactionErrorException, NetworkOfflineException, InvalidHashException, ApiBusyException, TimestampRetrievalErrorException
    {
        Address address = addressService.getAddress(hash);

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
        {
            ApiBusyException e = new ApiBusyException();
            logger.debug("",e);
            throw e;
        }

        Future<Void> tl = timestampDao.postTimestamp(address);

        TimestampListDto ret;

        try
        {
            tl.get(timeout_sec, TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e)
        {
            logger.debug("",e);
            throw new TransactionErrorException();
        }

        apiStateService.getSetBusy(false);

        ret = getTimestampList(hash);

        return ret;
    }

    /**
     * get timestamps for a given hash
     *
     * @param hash a valid sha-256 hash value
     * @return list of timestamps in the tangle for the given hash
     * @throws InvalidHashException
     * @throws TimestampRetrievalErrorException
     * @throws ApiBusyException
     * @throws TransactionErrorException
     */
    public TimestampListDto getTimestampList(Hash hash) throws InvalidHashException, TimestampRetrievalErrorException, ApiBusyException, TransactionErrorException
    {
        Address address = addressService.getAddress(hash);

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
        {
            ApiBusyException e = new ApiBusyException();
            logger.debug("",e);
            throw e;
        }

        Future<ArrayList<TimestampDto>> tl = timestampDao.getTimestampList(address);

        ArrayList<TimestampDto> list;

        try
        {
            list = tl.get(timeout_sec,TimeUnit.SECONDS);
        } catch (TimeoutException | ExecutionException | InterruptedException e)
        {
            logger.debug("",e);
            throw new TimestampRetrievalErrorException();
        }

        TimestampListDto ret = new TimestampListDto(hash.getHash(),list);

        apiStateService.getSetBusy(false);

        return ret;
    }
}
