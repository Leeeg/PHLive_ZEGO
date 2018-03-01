package com.i5i58.live.model.entity.liveRoom;

/**
 * Created by Lee on 2017/4/27.
 */

public class MountConfig {


    /**
     * id : 1
     * name : 雅马哈摩托车
     * version : ee39f1d287234bd2b044f9644ba12fea
     * function : 0
     * path : ef626530be21037baeb05291aa2a5439
     * validity : 1
     */

    private int id;
    private String name;
    private String version;
    private String function;
    private String path;
    private int validity;


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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getValidity() {
        return validity;
    }

    public void setValidity(int validity) {
        this.validity = validity;
    }
}
