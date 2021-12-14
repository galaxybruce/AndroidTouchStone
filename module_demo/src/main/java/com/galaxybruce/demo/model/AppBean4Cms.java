package com.galaxybruce.demo.model;


import com.galaxybruce.component.net.model.AppGenericBean;

/**
 *
 * Created by bruce.zhang on 2015/10/10.
 */
public class AppBean4Cms<T> extends AppGenericBean<T> {

    @Override
    public boolean isSuccessful() {
        return CODE_SUCCESS_0.equals(code);
    }

    @Override
    public boolean isExpireLogin() {
        return false;
    }

    @Override
    public String getCode() {
        return code;
    }

}
