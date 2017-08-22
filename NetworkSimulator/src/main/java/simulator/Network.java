package simulator;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;


public class Network {

    @Getter
    private List<DataTransfer> dataTransfers;

    private int dataPackageSize;

    Network(int sourceBitrate){
        dataTransfers = new LinkedList<>();
        dataPackageSize = calculateDataPackageSize(sourceBitrate);
    }

    private int calculateDataPackageSize(int sourceBitrate) {
        return sourceBitrate / 1000;
    }

    void addDataTransfer(int source, int dest, List<Frame> payload){
        dataTransfers.add(new DataTransfer(source, dest, payload, dataPackageSize));
    }

    public void clearNetwork(){
        dataTransfers.clear();
    }
}