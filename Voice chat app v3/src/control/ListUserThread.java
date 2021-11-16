package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.ListUser;

public class ListUserThread extends Thread {
    private Connect connect;
    private ListUser list;
    private boolean stop;
    public ListUserThread(Connect connect){
        this.connect = connect;
    }
    @Override
    public void run(){
        int cnt = 0;
        synchronized(connect){
        while(true){
                if(stop == true) {
                    try {
                    connect.notifyAll();
                    connect.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ListUserThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    list = (ListUser) connect.rcvListUser();
                    cnt++;
                    if(list.getList().size() > 0 && list.getList().get(list.getList().size()-1) == null){
                        stop = true;
                    }
                }
            }
        }
    } 

    public Connect getConnect() {
        return connect;
    }

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    public ListUser getList() {
        return list;
    }

    public void setList(ListUser list) {
        this.list = list;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
}
