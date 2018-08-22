package com.gabrielscheibler.dao;


import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.TimestampDto;
import com.gabrielscheibler.exceptions.TimestampRetrievalErrorException;
import com.gabrielscheibler.exceptions.TransactionErrorException;
import com.gabrielscheibler.service.TimestampService;
import jota.IotaAPI;
import jota.dto.response.GetInclusionStateResponse;
import jota.dto.response.SendTransferResponse;
import jota.error.ArgumentException;
import jota.model.Transaction;
import jota.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;


@Configuration
@PropertySource("nodes.properties")
@EnableAsync
public class TimestampDao
{

    @Value("${rawNodes:https,nodes.testnet.iota.org,443}")
    private String rawNodes;

    @Autowired
    private TimestampService timestampService;

    private IotaAPI api;
    private List<String[]> nodes;
    private int listIndex;

    public TimestampDao()
    {

    }

    @PostConstruct
    private void init()
    {
        nodes = new ArrayList<String[]>();
        initNodes();
        listIndex = 0;
        initIotaApi();
    }


    /**
     * Creates Iota-Api with the node at the position of listindex in the nodes list
     */
    private void initIotaApi()
    {
        String[] node = nodes.get(listIndex);

        api = new IotaAPI.Builder()
                .protocol(node[0])
                .host(node[1])
                .port(node[2])
                .build();
    }

    /**
     * Reads nodes from nodes.properties file and saves them to the nodes class variable
     */
    private void initNodes()
    {
        String[] nodeArray = rawNodes.split(";");
        for (int i = 0; i < nodeArray.length; i++)
        {
            String[] node = nodeArray[i].split(",");
            if (node.length == 3)
                nodes.add(node);
        }
        if (nodes.size() == 0)
        {
            String[] defaultNode = {"https", "nodes.testnet.iota.org", "443"};
            nodes.add(defaultNode);
        }
    }

    /**
     * Issue a zero value iota transaction
     *
     * @param address address to which the transaction is issued
     * @return transactionResponse object or Error Information
     */
    @Async
    public Future<Void> postTimestamp(Address address) throws TransactionErrorException
    {
        final String TRANSACTION_SEED = "IOTA9TIMESTAMPING";
        final String TRANSACTION_ADDRESS = address.getAddress();
        final int MIN_WEIGHT_MAGNITUDE = 14;
        final int DEPTH = 9;

        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TRANSACTION_ADDRESS, 0, "IOTA9TIMESTAMPING", "99999IOTA9TIMESTAMPING"));

        SendTransferResponse tr;

        try
        {
            api.sendTransfer(TRANSACTION_SEED, 1, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false);
        } catch (ArgumentException e)
        {
            e.printStackTrace();
            throw new TransactionErrorException();
        }

        return null;
    }

    /**
     * retrieve a list of timestamps sent to a certain address
     *
     * @param address retrieve timestamps sent to this address
     * @return list of timestamps
     * @throws TimestampRetrievalErrorException
     */
    @Async
    public Future<ArrayList<TimestampDto>> getTimestampList(Address address) throws TimestampRetrievalErrorException
    {
        ArrayList<TimestampDto> ret_timestampList = new ArrayList<>();

        String[] addresses = {address.getAddress()};
        List<Transaction> tr;
        try
        {
            tr = api.findTransactionObjectsByAddresses(addresses);
        } catch (ArgumentException e)
        {
            e.printStackTrace();
            throw new TimestampRetrievalErrorException();
        }

        String[] hashes = new String[tr.size()];
        for (int i = 0; i < tr.size(); i++)
        {
            hashes[i] = tr.get(i).getHash();
        }

        GetInclusionStateResponse gisr;

        try
        {
            gisr = api.getLatestInclusion(hashes);
        } catch (ArgumentException e)
        {
            e.printStackTrace();
            throw new TimestampRetrievalErrorException();
        }

        for (int i = 0; i < gisr.getStates().length; i++)
        {
            ret_timestampList.add(new TimestampDto(gisr.getStates()[i],address.getAddress(),tr.get(i).getTimestamp() * 1000,tr.get(i).getHash()));
        }

        return new AsyncResult<ArrayList<TimestampDto>>(ret_timestampList);
    }
}
