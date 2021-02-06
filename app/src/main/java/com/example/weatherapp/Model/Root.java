package com.example.weatherapp.Model;

import java.util.List;

public class Root {
    private String cod;
    private int message;
    private List<List>list ;

    public Root() {
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public List<List> getList() {
        return list;
    }

    public void setList(List<List> list) {
        this.list = list;
    }
}
