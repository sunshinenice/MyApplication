package com.hch.hchlib.been;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by 奔向阳光 on 2016/2/19.
 */
public class HchObject implements Serializable {

    private static final long serialVersionUID = -2363517834146547354L;
    private Gson mGson;
    private String objectId;

    public HchObject() {
        mGson = new Gson();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String toJson(){
        return mGson.toJson(this).toString();
    }
}
