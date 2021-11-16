package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.SerializationUtils;

public class Connect {
    private Socket socket;
    private InputStream inp;
    private OutputStream oup;
    public Connect(Socket socket) {
        this.socket = socket;
        try {
            inp = socket.getInputStream();
            oup = socket.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public void sendListUser(ListUser obj){
        byte[] data = SerializationUtils.serialize(obj);
        try {
            oup.write(data,0,data.length);
            oup.flush();
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ClientInfo revClientInfo(){
        byte[] data = new byte[1024];
        try {
            inp.read(data,0,data.length);
            ClientInfo info = (ClientInfo) SerializationUtils.deserialize(data);
            return info;
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String revString(){
        byte[] data = new byte[1024];
        try {
            inp.read(data,0,data.length);
            String info = new String(data).trim();
            return info;
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
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
    // Getter and Setter
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInp() {
        return inp;
    }

    public void setInp(InputStream inp) {
        this.inp = inp;
    }

    public OutputStream getOup() {
        return oup;
    }

    public void setOup(OutputStream oup) {
        this.oup = oup;
    }
}
