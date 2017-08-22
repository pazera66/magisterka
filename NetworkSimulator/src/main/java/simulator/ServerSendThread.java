package simulator;

import java.util.LinkedList;
import java.util.List;

public class ServerSendThread extends Thread {

    private long dataPackagesGenerated;
    private Network network;
    private int serverId;
    private int nodeId;

    ServerSendThread(long dataPackagesGenerated, Network network, int serverId, int nodeId) {
        this.dataPackagesGenerated = dataPackagesGenerated;
        this.network = network;
        this.serverId = serverId;
        this.nodeId = nodeId;
    }

    @Override
    public void run(){
        List<Frame> payload = new LinkedList<>();
        //payload.add(new Frame(dataPackagesGenerated));
        network.addDataTransfer(serverId, nodeId, payload);
    }
}
