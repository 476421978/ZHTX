package com.UI.db;

/**
 * Created by huang on 2018/11/25.
 */

public class g_add {

    /**
     * id : 126
     * name : test1
     * ownerid : 44
     * detail : test1
     */

    private int id;
    private String name;
    private int ownerid;
    private String detail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(int ownerid) {
        this.ownerid = ownerid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return "g_add{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerid=" + ownerid +
                ", detail='" + detail + '\'' +
                '}';
    }
}
