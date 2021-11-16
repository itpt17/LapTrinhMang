package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListUser implements Serializable {
    private List<ClientInfo> list;
    
    public ListUser(){
        list = new ArrayList<>();
    }
    
    public void addUser(ClientInfo client){
        list.add(client);
    }
    
    public List<ClientInfo> getList() {
        return list;
    }

    public void setList(List<ClientInfo> list) {
        this.list = list;
    }
    
    public void setStatus(boolean stt, int idx){
        list.get(idx).setStatus(stt);
    }
}
