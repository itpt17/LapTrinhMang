package model;

import java.io.Serializable;

public class ClientInfo implements Serializable{
    private String name;
    private String socket;
    private boolean status;
    private int nomic;
    private int nospeak;
    private String host;
    private int port;
    public ClientInfo() {
    }

    public ClientInfo(String name, String socket, int nomic, int nospeak, String host, int port) {
        this.name = name;
        this.socket = socket;
        this.status = false;
        this.nomic = nomic;
        this.nospeak = nospeak;
        this.host = host;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNomic() {
        return nomic;
    }

    public void setNomic(int nomic) {
        this.nomic = nomic;
    }

    public int getNospeak() {
        return nospeak;
    }

    public void setNospeak(int nospeak) {
        this.nospeak = nospeak;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
}
