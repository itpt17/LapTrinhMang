package control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ClientInfo;
import model.ListUser;
import org.apache.commons.lang3.SerializationUtils;

public class Connect {
    private Socket socket;
    private InputStream inp;
    private OutputStream oup;
    public Connect(){}
   
    public Connect(String addr, int port){
        try {
            socket  = new Socket(addr,port);
            inp   = socket.getInputStream();
            oup  = socket.getOutputStream();
        } catch (IOException ex) {
        }
    }

    public void sendString(String info){
        try {
            oup.write(info.getBytes(), 0, info.length());
            oup.flush();
        } catch (IOException ex) {
        }
    }
    
    public void sendInt(int n){
        try {
            oup.write(n);
            oup.flush();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int rcvInt(){
        try {
            return inp.read();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public ListUser rcvListUser(){
        try {
            byte[] data = new byte[1024];
            inp.read(data,0,data.length);
            ListUser list = (ListUser) SerializationUtils.deserialize(data);
            return list;
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ClientInfo rcvClientInfo(){
        try {
            byte[] data = new byte[1024];
            inp.read(data,0,data.length);
            ClientInfo info = (ClientInfo) SerializationUtils.deserialize(data);
            return info;
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public void sendClientInfo(ClientInfo obj){
        byte[] data = SerializationUtils.serialize(obj);
        try {
            oup.write(data,0,data.length);
            oup.flush();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sendBytes(byte[] data){
        try {
            oup.write(data, 0, data.length);
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public byte[] rsvBytes(){
        byte[] data = new byte[1024];
        try {
            inp.read(data, 0, data.length);
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
