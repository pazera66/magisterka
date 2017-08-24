package simulator;

import lombok.Getter;

import java.util.LinkedList;
import java.util.List;


class Network {

    @Getter
    private List<DataTransfer> dataTransfers;

    Network(){
        dataTransfers = new LinkedList<>();
    }

    void addDataTransfer(int source, int dest, List<DataPackage> payload, int dataPackageSize){
        dataTransfers.add(new DataTransfer(source, dest, payload, dataPackageSize));
    }

    void addDataTransfer(int source, int dest, List<Integer> payload, Settings.Requests request){
        dataTransfers.add(new DataTransfer(source, dest, payload, request));
    }

    void addDataTransfer(DataTransfer transfer){
        dataTransfers.add(transfer);
    }

    void clearNetwork(){
        dataTransfers.clear();
    }
}