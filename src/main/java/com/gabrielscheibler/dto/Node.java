package com.gabrielscheibler.dto;

public class Node
{
    private String protocol,host,port;

    public Node(){}

    public Node(String protocol, String host, String port)
    {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public String getHost()
    {
        return host;
    }

    public String getPort()
    {
        return port;
    }

    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public void setPort(String port)
    {
        this.port = port;
    }
}
