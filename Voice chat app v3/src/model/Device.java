package model;

import java.io.Serializable;


public class Device implements Serializable {
    private String name;
    private int index;

    public Device() {
    }

    public Device(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
}
