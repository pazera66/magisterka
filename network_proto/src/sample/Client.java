package sample;

import java.io.Serializable;
import java.util.function.Consumer;

public class Client extends NetworkConnection {

    private String ip;
    private int port;

    public Client(String ip, int port, Consumer<byte[]> onReceiveCallback){
        super(onReceiveCallback);
        this.port = port;
        this.ip = ip;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
