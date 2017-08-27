package simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static simulator.Settings.*;

@Getter
@Setter
class Server {

    private final int FPS = 50;

    private int serverId = getServerID();
    private int sourceBitrate = getSource_Bitrate();
    private int uploadBandwidth = getServer_Upload_Bandwidth();
    private int samplingFrequency = Settings.getSamplingFrequency();
    private int numberOfFrameParts = Settings.getNumberOfFrameParts();

    private List<Integer> connectedNodes;
    private Network network;
    private int framePartBeingStreamed = 0;
    private int frameBeingStreamed = 0;

    private int dataPackageSize;

    Server(List<Client> connectedNodes, Network network){
        this.connectedNodes = connectedNodes.stream().map(Client::getId).collect(toList());
        this.network = network;
        calculateDataPackageSize();
        calculateHowManyPartsPerFrame();
    }

    private void calculateDataPackageSize() {
        dataPackageSize = sourceBitrate / samplingFrequency;
    }

    private void calculateHowManyPartsPerFrame() {
        numberOfFrameParts = samplingFrequency / FPS;
    }

    void send(int cycle){
        int remainingBandwidth = uploadBandwidth / samplingFrequency;

        for (int nodeId:connectedNodes){
            if (remainingBandwidth < dataPackageSize){
                System.out.println("Not enough server bandwidth to handle all nodes");
                break;
            }

            if (!(framePartBeingStreamed < numberOfFrameParts)) {
                frameBeingStreamed++;
                framePartBeingStreamed = 0;
            }

            List<DataPackage> payload = new LinkedList<>();
            payload.add(new DataPackage(framePartBeingStreamed, frameBeingStreamed));
            network.addDataTransfer(serverId, nodeId, payload, dataPackageSize);
            remainingBandwidth = remainingBandwidth - dataPackageSize;
        }

         framePartBeingStreamed++;
    }

    void receive(int cycle){
        List<DataTransfer> incomingData = network.getDataTransfers()
                .stream()
                .filter(x -> x.getDestNodeId() == serverId)
                .collect(Collectors.toList());

        for (DataTransfer data:incomingData){
            switch (data.getRequest()){
                case RequestingListOfNodes:
                    network.addDataTransfer(serverId, data.getSourceNodeId(), connectedNodes, Requests.SendingListOfNodes);
            }
        }
    }

}
