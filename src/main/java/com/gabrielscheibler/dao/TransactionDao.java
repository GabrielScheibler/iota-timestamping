package com.gabrielscheibler.dao;


import com.gabrielscheibler.entity.Address;
import com.gabrielscheibler.entity.ApiError;
import com.gabrielscheibler.service.TransactionService;

import jota.IotaAPI;
import jota.dto.response.GetNodeInfoResponse;
import jota.dto.response.SendTransferResponse;
import jota.error.ArgumentException;
import jota.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class TransactionDao
{

    @Value("${rawNodes:https,nodes.testnet.iota.org,443}")
    private String rawNodes;

    @Autowired
    private TransactionService transactionService;

    private IotaAPI api;
    private List<String[]> nodes;
    private int listIndex;

    public TransactionDao()
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

    @Async
    public Future<ResponseEntity<?>> postTransaction(Address address)
    {
        return issueTransaction(address);
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
     * Issues a zero value iota transaction
     *
     * @param address address to which the transaction is issued
     * @return transactionResponse object or Error Information
     */
    public Future<ResponseEntity<?>> issueTransaction(Address address)
    {
        final String TRANSACTION_SEED = "ORIGINSTAMP";
        final String TRANSACTION_ADDRESS = address.getAddress();
        final int MIN_WEIGHT_MAGNITUDE = 14;
        final int DEPTH = 9;

        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TRANSACTION_ADDRESS, 0, "", ""));

        SendTransferResponse tr;

        try
        {
            tr = api.sendTransfer(TRANSACTION_SEED, 1, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false);
        } catch (ArgumentException e)
        {
            e.printStackTrace();
            ApiError err = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
            return new AsyncResult<ResponseEntity<?>>(new ResponseEntity<ApiError>(err, HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return new AsyncResult<ResponseEntity<?>>(ResponseEntity.ok(tr));
    }
}
