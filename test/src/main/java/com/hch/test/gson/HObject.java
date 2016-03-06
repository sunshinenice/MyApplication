package com.hch.test.gson;

import com.google.gson.Gson;

/**
 * Created by 奔向阳光 on 2016/3/3.
 */
public class HObject implements hjson{

    private String objectId;
    private final Gson gson = new Gson();

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toJson() {
        return gson.toJson(this);
    }

    @Override
    public String formJson() {
        return null;
    }
}
