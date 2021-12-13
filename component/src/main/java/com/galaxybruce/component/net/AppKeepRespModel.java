package com.galaxybruce.component.net;


import com.galaxybruce.component.proguard.IProguardKeeper;

import okhttp3.ResponseBody;

public class AppKeepRespModel implements IProguardKeeper {
    private ResponseBody body;

    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }
}
