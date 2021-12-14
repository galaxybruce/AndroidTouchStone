package com.galaxybruce.demo.model;

import android.text.TextUtils;

import com.galaxybruce.component.proguard.IProguardKeeper;

import java.util.Objects;

/**
 */
public class SocketHost implements IProguardKeeper {

    public SocketHost() {
    }

    public SocketHost(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private String host;
    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean valid() {
        return !TextUtils.isEmpty(host) && port > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketHost that = (SocketHost) o;
        return port == that.port &&
                Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
