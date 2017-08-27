package simulator;

import java.util.HashMap;
import java.util.Map;

public class Host {

    private int id;
    private Map<Integer, Integer> neighbours = new HashMap<>();

    public Host(int id){
        this.id = id;
    }

    public void addNeighbour(int nodeId, int latency){
        neighbours.put(nodeId, latency);
    }

    public int getLatency(int destNode) {
        return neighbours.get(destNode);
    }

    public void changeLatency(int destNode, int newLatency) {
        neighbours.put(destNode, newLatency);
    }

    public void removeNeighbour(int nodeId) {
        neighbours.remove(nodeId);
    }
}
