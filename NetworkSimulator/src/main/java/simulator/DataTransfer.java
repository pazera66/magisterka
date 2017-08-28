package simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
class DataTransfer {
    private int sourceNodeId;
    private int destNodeId;
    private List<DataPackage> payload;
    private List<Integer> requestPayload;
    private int size;
    private Settings.Requests request;

    DataTransfer(int sourceNodeId, int destNodeId, List<DataPackage> payload, int dataPackageSize){
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.payload = payload;
        this.size = dataPackageSize;
    }

    DataTransfer(int sourceNodeId, int destNodeId, List<Integer> payload, Settings.Requests request){
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.requestPayload = payload;
        this.request = request;
        size = 0;
    }
}
