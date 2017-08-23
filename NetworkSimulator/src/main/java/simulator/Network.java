package simulator;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;


class Network {

    @Getter
    private List<DataTransfer> dataTransfers;

    private int dataPackageSize;

    Network(){
        dataTransfers = new LinkedList<>();
        dataPackageSize = calculateDataPackageSize();
    }

    private int calculateDataPackageSize() {
        return Settings.getSource_Bitrate() / Settings.getSamplingFrequency();
    }

    void addDataTransfer(int source, int dest, List<DataPackage> payload){
        dataTransfers.add(new DataTransfer(source, dest, payload, dataPackageSize));
    }

    void addDataTransfer(int source, int dest, List<Integer> payload, String request){
        dataTransfers.add(new DataTransfer(source, dest, payload, request));
    }

    void clearNetwork(){
        dataTransfers.clear();
    }
}