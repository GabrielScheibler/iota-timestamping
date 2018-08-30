package com.gabrielscheibler.service;

import com.gabrielscheibler.dao.TimestampDao;
import com.gabrielscheibler.dto.*;
import com.gabrielscheibler.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@PropertySource("timeout.properties")
public class TimestampService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AddressService addressService;
    @Autowired
    private ApiStateService apiStateService;
    @Autowired
    private TimestampDao timestampDao;

    @Value("${postAndGetTimeout:60}") //default value if not specified in properies-file
    private int postAndGetTimeout;

    @Value("${getTimeout:20}") //default value if not specified in properies-file
    private int getTimeout;


    /**
     * post a timestamp for a given hash on the tangle
     *
     * @param tpr a dto for the request parameters
     * @return list of timestamps in the tangle for the given hash
     * @throws TransactionErrorException
     * @throws NetworkOfflineException
     * @throws InvalidHashException
     * @throws ApiBusyException
     * @throws TimestampRetrievalErrorException
     */
    public TimestampListDto postTimestamp(TimestampPostRequest tpr) throws TransactionErrorException, NetworkOfflineException, InvalidHashException, ApiBusyException, TimestampRetrievalErrorException, InternalErrorException, TimedOutException
    {
        Hash hash = new Hash(tpr.getHash());

        Address address = addressService.getAddress(hash);

        Long transfer_amount = tpr.getIota_amount();

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
        {
            ApiBusyException e = new ApiBusyException();
            logger.debug("",e);
            throw e;
        }

        if(transfer_amount == null)
            transfer_amount = 0L;

        ArrayList<TimestampDto> list;

        Future<ArrayList<TimestampDto>> listFuture = timestampDao.postAndGetTimestamp(address,transfer_amount);


        try
        {
            list = listFuture.get(postAndGetTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e)
        {
            apiStateService.getSetBusy(false);
            logger.debug("",e);
            throw new InternalErrorException();
        } catch (TimeoutException e)
        {
            apiStateService.getSetBusy(false);
            logger.debug("",e);
            throw new TimedOutException();
        } catch (ExecutionException e)
        {
            apiStateService.getSetBusy(false);
            logger.debug("",e);
            if(e.getCause().getClass() == NetworkOfflineException.class)
                throw new NetworkOfflineException();
            if(e.getCause().getClass() == TimestampRetrievalErrorException.class)
                throw new TimestampRetrievalErrorException();
            throw new TransactionErrorException();
        }

        TimestampListDto ret = new TimestampListDto(hash.getHash(),address.getAddress(),list);

        apiStateService.getSetBusy(false);

        return ret;
    }

    /**
     * get timestamps for a given hash
     *
     * @param hash a sha-256 hash value
     * @return list of timestamps in the tangle for the given hash
     * @throws InvalidHashException
     * @throws TimestampRetrievalErrorException
     * @throws ApiBusyException
     * @throws TransactionErrorException
     */
    public TimestampListDto getTimestampList(Hash hash) throws InvalidHashException, TimestampRetrievalErrorException, ApiBusyException, TransactionErrorException, NetworkOfflineException, InternalErrorException, TimedOutException
    {
        Address address = addressService.getAddress(hash);

        boolean busy = apiStateService.getSetBusy(true);

        if (busy)
        {
            ApiBusyException e = new ApiBusyException();
            logger.debug("",e);
            throw e;
        }

        //CompletableFuture<ArrayList<TimestampDto>> listFuture = new CompletableFuture<ArrayList<TimestampDto>>();
        ArrayList<TimestampDto> list;

        Future<ArrayList<TimestampDto>> listFuture = timestampDao.getTimestampList(address);


        try
        {
            list = listFuture.get(getTimeout,TimeUnit.SECONDS);
        } catch (InterruptedException e)
        {
            apiStateService.getSetBusy(false);
            logger.debug("",e);
            throw new InternalErrorException();
        } catch (TimeoutException e)
        {
            apiStateService.getSetBusy(false);
            logger.debug("",e);
            throw new TimedOutException();
        }catch (ExecutionException e)
        {
            apiStateService.getSetBusy(false);
            logger.debug("",e);
            if(e.getCause().getClass() == NetworkOfflineException.class)
                throw new NetworkOfflineException();
            throw new TimestampRetrievalErrorException();
        }

        TimestampListDto ret = new TimestampListDto(hash.getHash(),address.getAddress(),list);

        apiStateService.getSetBusy(false);

        return ret;
    }
}
