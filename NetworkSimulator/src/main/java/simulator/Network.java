package simulator;

import lombok.Getter;

import java.util.*;


class Network {

    @Getter
    private List<DataTransfer> dataTransfers;
    private Map<Integer, List<DataTransfer>> bufferedDataTransfers = new HashMap<>();

    @Getter
    private Map<Integer, Host> hosts = new HashMap<>();

    private int counter = 0; //cycle counter, act as timer for latency calculation

    Network(){
        dataTransfers = new LinkedList<>();
    }

    void addDataTransfer(int source, int dest, List<DataPackage> payload, int transferSize){
        DataTransfer transfer = new DataTransfer(source, dest, payload, transferSize);
        addDataTransferToBuffer(transfer);
    }

    void addDataTransfer(int source, int dest, Map<Settings.Options, Object> payload, Settings.Requests request){
        DataTransfer transfer = new DataTransfer(source, dest, payload, request);
        addDataTransferToBuffer(transfer);
    }

    private void addDataTransferToBuffer(DataTransfer transfer) {
        int latency = hosts.get(transfer.getSourceNodeId()).getLatency(transfer.getDestNodeId());
        if (!bufferedDataTransfers.containsKey(counter+latency)){
            List<DataTransfer> list = new ArrayList<>();
            list.add(transfer);
            bufferedDataTransfers.put(counter+latency, list);
        } else {
            List<DataTransfer> list = bufferedDataTransfers.get(counter+latency);
            list.add(transfer);
            bufferedDataTransfers.put(counter+latency, list);
        }
    }

    void addDataTransfer(DataTransfer transfer){
        addDataTransferToBuffer(transfer);
    }

    void updateNetwork() {
        counter++;
        List<DataTransfer> list = bufferedDataTransfers.get(counter);
        if (!(list == null)) {
            dataTransfers.addAll(list);
            bufferedDataTransfers.remove(counter);
        }

}

    void clearNetwork(){
        dataTransfers.clear();
    }

    void addHost(int hostID){
        hosts.put(hostID, new Host(hostID));
        createHostsRelationship(hostID);
    }

    void addHostWithoutRelationship(int hostID){
        hosts.put(hostID, new Host(hostID));
    }

    private void createHostsRelationship(int hostID) {
        for (Map.Entry<Integer, Host> entry:hosts.entrySet()){
            if (!(entry.getKey() == hostID)) {
                entry.getValue().addNeighbour(hostID, Settings.getGlobalLatency());
                hosts.get(hostID).addNeighbour(entry.getKey(), Settings.getGlobalLatency());
            }
        }
    }

    void removeHostRelationshipFromAllHosts(int hostID) {
        for (Map.Entry<Integer, Host> entry:hosts.entrySet()){
            entry.getValue().removeNeighbour(hostID);
        }
    }

    void createHostRelationship(int hostID, int neighbourID, int latency) {
        Host host = hosts.get(hostID);
        host.addNeighbour(neighbourID, latency);
        hosts.put(hostID, host);
    }

    void updateHostRelationship(int hostID, int neighbourID, int latency) {
        Host host = hosts.get(hostID);
        host.changeLatency(neighbourID, latency);
        hosts.put(hostID, host);
    }
}