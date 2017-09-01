package simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    private int numberOfChunksPerSecond = Settings.getNumberOfFrameParts();

    private List<Integer> allNodesInNetwork;
    private Network network;
    private int chunkBeingStreamed = 0;
    private int countdownBetweenChunkGeneration = 0;

    private int chunkSize;

    Server(List<Client> allNodesInNetwork, Network network){
        this.allNodesInNetwork = allNodesInNetwork.stream().map(Client::getId).collect(toList());
        this.network = network;
        calculateChunkSize();
    }

    private void calculateChunkSize() {
        chunkSize = sourceBitrate / Settings.getChunkSize();
    }

    void send(int cycle){
        int remainingBandwidth = uploadBandwidth / samplingFrequency;

        for (int nodeId: allNodesInNetwork){
            if (remainingBandwidth < chunkSize){
                System.out.println("Not enough server bandwidth to handle all nodes");
                break;
            }

            if (!(chunkBeingStreamed < numberOfChunksPerSecond)) {
                frameBeingStreamed++;
                chunkBeingStreamed = 0;
            }

            List<DataPackage> payload = new LinkedList<>();
            payload.add(new DataPackage(chunkBeingStreamed, frameBeingStreamed));
            network.addDataTransfer(serverId, nodeId, payload, chunkSize);
            remainingBandwidth = remainingBandwidth - chunkSize;
        }

         chunkBeingStreamed++;
    }

    void receive(int cycle){
        List<DataTransfer> incomingData = network.getDataTransfers()
                .stream()
                .filter(x -> x.getDestNodeId() == serverId)
                .collect(Collectors.toList());

        for (DataTransfer data:incomingData){
            switch (data.getRequest()){
                case RequestingListOfNodes:
                    Map<Options, Object> map = new HashMap<>();
                    map.put(Options.ListOfAllNodes, allNodesInNetwork);
                    network.addDataTransfer(serverId, data.getSourceNodeId(), map, Requests.SendingListOfAllNodes);
            }
        }
    }

}
