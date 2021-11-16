package control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientInfo;
import model.Connect;
import model.ListUser;

public class ServerControl {
    ServerSocket server;
    ListUser list;
    List<Socket> sockets;
    List<Connect> connects;
    private int port;
    public ServerControl(int port){
        try {
            this.port = port;
            server= new ServerSocket(port);
            list= new ListUser();
            sockets= new ArrayList<>();
            connects = new ArrayList<>();
        } catch (IOException ex) {
            Logger.getLogger(ServerControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void RemoveClient(Socket socket){
        for(int i = 0; i< sockets.size(); i++){
            if(sockets.get(i).toString().equals(socket.toString())){
                list.getList().remove(i);
                connects.remove(i);
                sockets.remove(i);
                break;
            }
        }
    }
    
    public void ListenConnect() throws IOException{
        while(true){
            Socket socket = this.server.accept();
            sockets.add(socket);
            Connect newcon = new Connect(socket);
            System.out.println(newcon.getSocket().toString());
            connects.add(newcon);
            String infoStr = newcon.revString();
            String []info = infoStr.split("_");
            ClientInfo client = new ClientInfo(info[0],socket.toString(), Integer.parseInt(info[1]), Integer.parseInt(info[2]),info[3], Integer.parseInt(info[4]));
            list.addUser(client);
            for(int i = 0; i < sockets.size(); i++){
                ListUser tmp = new ListUser();
                for(ClientInfo j:list.getList()){
                   if(!j.getSocket().equals(sockets.get(i).toString())){
                       tmp.addUser(j);
                   }
                }
                if(!client.getSocket().equals(sockets.get(i).toString()) || (client.getSocket().equals(sockets.get(i).toString()) && !client.isStatus())){
                    connects.get(i).sendListUser(tmp);
                }
            }
            System.out.println("ListenConnect: " + list.getList().size());
            ListenClientStatus statusListener = new ListenClientStatus(this,socket,newcon);
            statusListener.start();
        }
    }
    
    public void setStatus(boolean stt, int idx){
        list.setStatus(stt,idx);
    }
    
    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public ListUser getList() {
        return list;
    }

    public void setList(ListUser list) {
        this.list = list;
    }

    public List<Socket> getSockets() {
        return sockets;
    }

    public void setSockets(List<Socket> sockets) {
        this.sockets = sockets;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public List<Connect> getConnects() {
        return connects;
    }

    public void setConnects(List<Connect> connects) {
        this.connects = connects;
    }
    
}

class ListenClientStatus extends Thread{
    private ServerControl control;
    private Socket socket;
    private Connect connect;
    public ListenClientStatus(ServerControl control, Socket socket, Connect connect) {
        this.control = control;
        this.socket = socket;
        this.connect = connect;
    }

    public ServerControl getControl() {
        return control;
    }

    public void setControl(ServerControl control) {
        this.control = control;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Connect getConnect() {
        return connect;
    }

    public void setConnect(Connect connect) {
        this.connect = connect;
    }

    @Override
    public void run(){
        try {
            InputStream ins = socket.getInputStream();
            boolean calling = false;
            synchronized(connect){
                while(true){
                if(calling){
                    connect.wait();
                }
                int stt = ins.read();
                System.out.println(stt);
                switch(stt){
                    case 0:{
                        System.out.println("Out Connect: " + control.getList().getList().size());
                        control.RemoveClient(socket);
                        for(int i = 0; i < control.getSockets().size(); i++){
                            ListUser tmp = new ListUser();
                            for(ClientInfo j:control.getList().getList()){
                               if(!j.getSocket().equals(control.getSockets().get(i).toString())){
                                   tmp.addUser(j);
                               }
                            }
                            control.getConnects().get(i).sendListUser(tmp);
                        }
                        break;
                    }
                    case 2:{
                        byte[] buffer = new byte[1024];
                        byte[] sbuffer= new byte[1024];
                        connect.sendInt(1);
                        ClientInfo tginfo = connect.revClientInfo();
                        connect.sendInt(1);
                        ClientInfo srinfo = connect.revClientInfo();
                        connect.sendInt(1);
                        System.out.println(srinfo.getSocket());
                        int tgindex = -1;
                        for(int i = 0; i < control.getSockets().size(); i++){
                            if(control.getSockets().get(i).toString().equals(tginfo.getSocket())){
                                tgindex = i;
                                break;
                            }
                        }
                        System.out.println("Accept call " + control.getList().getList().size());
                        if(tgindex != -1){
                            control.getConnects().get(tgindex).sendInt(1);
                            calling = true;
                            new CallingThread(connect, control.getConnects().get(tgindex)).start();
                            new CallingThread(control.getConnects().get(tgindex), connect).start();
                            try {
                                connect.wait();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ListenClientStatus.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        break;
                    }
                    case 3:{
                        ListUser tmp = new ListUser();
                        connect.sendListUser(tmp);
                        connect.sendInt(1);
                        ClientInfo tinfo = connect.revClientInfo();
                        connect.sendInt(1);
                        ClientInfo sinfo = connect.revClientInfo();
                        connect.sendInt(1);
                        String skTxt = sinfo.getSocket().split("\\[")[1].split(",")[2].split("=")[1].split("\\]")[0];
                        for(ClientInfo i: control.getList().getList()){
                            if(i.getSocket().contains("=" + skTxt + ",")){
                                sinfo.setName(i.getName());
                                sinfo.setSocket(i.getSocket());
                                break;
                            }
                        }
                        System.out.println(tinfo.getSocket());
                        boolean busy = true;
                        int tmpIdx = -1;
                        for(int i = 0; i < control.getSockets().size(); i++){
                            if(control.getSockets().get(i).toString().equals(tinfo.getSocket())){
                                tmpIdx = i;
                                if(!control.getList().getList().get(i).isStatus()){
                                    busy = false;
                                    control.setStatus(true, i);
                                    connect.sendInt(1);
                                }
                            }
                        }
                        if(busy) connect.sendInt(0);
                        else{
                            Socket tmpSk = control.getSockets().get(tmpIdx);
                            Connect tmpCn= control.getConnects().get(tmpIdx);
                            for(int i = 0; i < control.getSockets().size(); i++){
                                if(control.getSockets().get(i).toString().equals(socket.toString())){
                                    control.setStatus(true, i);
                                }
                            }
                            new Thread(){
                                @Override
                                public void run(){
                                        control.getList().addUser(null);
                                        tmpCn.sendListUser(control.getList());
                                        tmpCn.sendClientInfo(sinfo);
                                        System.out.println(sinfo.getName());
                                        control.getList().getList().remove(null);
                                        System.out.println("Calling " + control.getList().getList().size());
                                }
                            }.start();
                        }
                    break;
                    }
                    case 4:{
                        calling = true;
                        break;
                    }
                }
            }
            }
        } catch (IOException ex) {
    }   catch (InterruptedException ex) {
            Logger.getLogger(ListenClientStatus.class.getName()).log(Level.SEVERE, null, ex);
        }
}
}


class CallingThread extends Thread{
    Connect con1;
    Connect con2;

    public CallingThread(Connect con1, Connect con2) {
        this.con1 = con1;
        this.con2 = con2;
    }
    
    public Connect getCon1() {
        return con1;
    }

    public void setCon1(Connect con1) {
        this.con1 = con1;
    }

    public Connect getCon2() {
        return con2;
    }

    public void setCon2(Connect con2) {
        this.con2 = con2;
    }

    @Override
    public void run(){
        synchronized(con1){
            while(true){
                {
                    byte[] buffer = con1.rsvBytes();
                    con2.sendBytes(buffer);
                    con1.notify();
                }
            }
        }
    }
}