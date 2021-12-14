package com.galaxybruce.demo.model;

import com.galaxybruce.component.proguard.IProguardKeeper;

/**
 */
public class GroupSocketHost implements IProguardKeeper {

    private Server servers;

    public Server getServers() {
        return servers;
    }

    public void setServers(Server servers) {
        this.servers = servers;
    }

    public static class Server implements IProguardKeeper {
        private SocketHost tcp;

        public SocketHost getTcp() {
            return tcp;
        }

        public void setTcp(SocketHost tcp) {
            this.tcp = tcp;
        }
    }
}
