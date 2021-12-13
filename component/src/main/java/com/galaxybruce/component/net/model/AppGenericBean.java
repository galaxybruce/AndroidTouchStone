package com.galaxybruce.component.net.model;

/**
 *
 * Created by bruce.zhang on 2015/10/10.
 */
public class AppGenericBean<T> extends AppBaseBean {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
