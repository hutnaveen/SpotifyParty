package server;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Objects;


public class ClientInfo implements Serializable {

    public InetAddress address;
    public Integer port;

    public ClientInfo(InetAddress address, Integer port){
        this.address = address;
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientInfo that = (ClientInfo) o;
        return Objects.equals(address, that.address) &&
                Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, port);
    }
}
