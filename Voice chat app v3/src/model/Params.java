package model;

public class Params {
    private String addr;
    private int port;
    private String name;
    public Params(){}

    public Params(String addr, int port, String name) {
        this.addr = addr;
        this.port = port;
        this.name = name;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
