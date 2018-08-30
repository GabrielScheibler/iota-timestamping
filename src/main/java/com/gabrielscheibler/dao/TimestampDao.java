package com.gabrielscheibler.dao;


import com.gabrielscheibler.dto.Address;
import com.gabrielscheibler.dto.Node;
import com.gabrielscheibler.dto.TimestampDto;
import com.gabrielscheibler.exceptions.NetworkOfflineException;
import com.gabrielscheibler.exceptions.TimestampRetrievalErrorException;
import com.gabrielscheibler.exceptions.TransactionErrorException;
import jota.IotaAPI;
import jota.dto.response.GetInclusionStateResponse;
import jota.dto.response.GetNodeInfoResponse;
import jota.error.ArgumentException;
import jota.model.Transaction;
import jota.model.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


@Configuration
@PropertySource("nodes.properties")
@PropertySource("timeout.properties")
@EnableAsync
public class TimestampDao
{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${rawNodes:https,nodes.testnet.iota.org,443}") //default value if not specified in properies-file
    private String rawNodes;

    @Value("${nodeInfoTimeout:10}") //default value if not specified in properies-file
    private int nodeInfoTimeout;

    private IotaAPI api;
    private List<Node> nodes;

    private ThreadPoolExecutor asyncCacheExecutor;


    /**
     * initialize class fields
     */
    @PostConstruct
    private void init()
    {
        nodes = new ArrayList<Node>();
        initNodes();
        initIotaApi(nodes.get(0));
        initExecutor();
    }

    /**
     * initialize the asyncChacheExecutor
     */
    private void initExecutor()
    {
        asyncCacheExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        asyncCacheExecutor.setKeepAliveTime(nodeInfoTimeout,TimeUnit.SECONDS);
    }

    /**
     * Reads nodes from nodes.properties file and saves them to the "nodes" class variable
     */
    private void initNodes()
    {
        String[] nodeArray = rawNodes.split(";");
        for (int i = 0; i < nodeArray.length; i++)
        {
            String[] node = nodeArray[i].split(",");
            if (node.length == 3)
                nodes.add(new Node(node[0],node[1],node[2]));
            else
                logger.error("error in nodes.properties file in value for rawNodes");
        }
    }

    /**
     * sets a new node to use for the iota-api
     * @param node node to use for iota-api requests
     */
    private void initIotaApi(Node node)
    {
        api = new IotaAPI.Builder()
                .protocol(node.getProtocol())
                .host(node.getHost())
                .port(node.getPort())
                .build();
    }

    /**
     * set a random node from the node list for the iota-api
     */
    private void setRandomNode()
    {
        Random r = new Random();
        int i = r.nextInt(nodes.size());

        initIotaApi(nodes.get(i));
    }

    /**
     * set a working node for the iota-api, preferring the current node
     *
     * @throws NetworkOfflineException
     */
    private void setWorkingNode() throws NetworkOfflineException
    {
        Node n = new Node(api.getProtocol(),api.getHost(),api.getPort());
        if(!isWorkingNode(n))
        {
            n = findWorkingNode();
            initIotaApi(n);
        }
        return;
    }

    /**
     * find a responding node from the node list
     *
     * @return a responding node
     * @throws NetworkOfflineException
     */
    private Node findWorkingNode() throws NetworkOfflineException
    {
        CompletableFuture<Node>[] futures = new CompletableFuture[nodes.size()];
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.setKeepAliveTime(nodeInfoTimeout,TimeUnit.SECONDS);

        for (int i = 0; i < futures.length; i++)
        {
            futures[i] = new CompletableFuture<Node>();
        }
        for (int i = 0; i < nodes.size(); i++)
        {
            int finalI = i;
            CompletableFuture.runAsync(()->{
                checkNode(futures[finalI],nodes.get(finalI));
            },asyncCacheExecutor);
        }

        CompletableFuture f = CompletableFuture.anyOf((CompletableFuture<?>[]) futures);

        Object ret;
        try
        {
            ret = f.get(nodeInfoTimeout,TimeUnit.SECONDS);
            return (Node) ret;
        } catch (Exception e)
        {
            throw new NetworkOfflineException();
        }
    }

    /**
     * check if a node is responding
     *
     * @param node node to check
     * @return true if node is responding timely
     */
    private boolean isWorkingNode(Node node) throws NetworkOfflineException
    {
        CompletableFuture<Node> future = new CompletableFuture<Node>();

        CompletableFuture.runAsync(()->{
            checkNode(future,node);
        },asyncCacheExecutor);

        try
        {
            future.get(nodeInfoTimeout,TimeUnit.SECONDS);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }


    /**
     * Complete passed Future if passed Node is responding
     *
     * @param f future to complete
     * @param node node to check
     * @throws CancellationException
     */
    private void checkNode(CompletableFuture<Node> f, Node node) throws CancellationException
    {
            IotaAPI testapi = new IotaAPI.Builder()
                    .protocol(node.getProtocol())
                    .host(node.getHost())
                    .port(node.getPort())
                    .build();


            try
            {
                GetNodeInfoResponse g = testapi.getNodeInfo();
                logger.debug("node responded: " + node.getProtocol() +","+ node.getHost() +","+ node.getPort() + " - AppVersion: " + g.getAppName());
                f.complete(node);
                logger.info("Executor: " + asyncCacheExecutor.getActiveCount() + " " + asyncCacheExecutor.getTaskCount() + " " + asyncCacheExecutor.getCompletedTaskCount());
            }
            catch(Exception e)
            {
                logger.debug("cant connect to node: " + node.getProtocol() +","+ node.getHost() +","+ node.getPort());
                return;
            }
    }


    /**
     * post timestamp and get timestamps
     *
     * @param address address to post timestamp on
     * @param transfer_amount amount of iota used for the timestamp
     * @return list of timestamps for given address
     * @throws TransactionErrorException
     * @throws TimestampRetrievalErrorException
     * @throws NetworkOfflineException
     */
    @Async
    public Future<ArrayList<TimestampDto>> postAndGetTimestamp(Address address, Long transfer_amount)
    {
        try
        {
            setWorkingNode();
        } catch (NetworkOfflineException e)
        {
            return AsyncResult.forExecutionException(new NetworkOfflineException());
        }

        try
        {
            postTransaction(address,transfer_amount);
        } catch (TransactionErrorException e)
        {
            setRandomNode();
            return AsyncResult.forExecutionException(new TransactionErrorException());
        }
        try
        {
            return getTransactionList(address);
        } catch (TimestampRetrievalErrorException e)
        {
            setRandomNode();
            return AsyncResult.forExecutionException(new TimestampRetrievalErrorException());
        }
    }


    /**
     * get timestamps for a given address
     *
     * @param address an iota address
     * @return list of timestamps for given address
     * @throws TimestampRetrievalErrorException
     * @throws NetworkOfflineException
     */
    @Async
    public Future<ArrayList<TimestampDto>> getTimestampList(Address address)
    {
        try
        {
            setWorkingNode();
        } catch (NetworkOfflineException e)
        {
            return AsyncResult.forExecutionException(new NetworkOfflineException());
        }

        try
        {
            return getTransactionList(address);
        } catch (TimestampRetrievalErrorException e)
        {
            return AsyncResult.forExecutionException(new TimestampRetrievalErrorException());
        }
    }


    /**
     * Issue an iota transaction
     *
     * @param address a valid iota-address
     * @return list of timestamps for given address
     */
    private void postTransaction(Address address, Long transfer_amount) throws TransactionErrorException
    {
        final String TRANSACTION_SEED = "IOTA9TIMESTAMPING";
        final String TRANSACTION_ADDRESS = address.getAddress();
        final int MIN_WEIGHT_MAGNITUDE = 14;
        final int DEPTH = 9;

        List<Transfer> transfers = new ArrayList<>();

        transfers.add(new Transfer(TRANSACTION_ADDRESS, transfer_amount, "IOTA9TIMESTAMPING", "99999IOTA9TIMESTAMPING"));

        try
        {
            api.sendTransfer(TRANSACTION_SEED, 1, DEPTH, MIN_WEIGHT_MAGNITUDE, transfers, null, null, false);
        } catch (Exception e)
        {
            logger.debug("",e);
            throw new TransactionErrorException();
        }
    }

    /**
     * retrieve a list of timestamps sent to a certain address
     *
     * @param address a valid iota-address
     * @return list of timestamps for given address
     * @throws TimestampRetrievalErrorException
     */
    private Future<ArrayList<TimestampDto>> getTransactionList(Address address) throws TimestampRetrievalErrorException
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
            logger.debug("",e);
            throw new TimestampRetrievalErrorException();
        }

        for (int i = 0; i < gisr.getStates().length; i++)
        {
            ret_timestampList.add(new TimestampDto(gisr.getStates()[i],address.getAddress(),tr.get(i).getTimestamp() * 1000,tr.get(i).getHash()));
        }

        return new AsyncResult<ArrayList<TimestampDto>>(ret_timestampList);
    }
}
