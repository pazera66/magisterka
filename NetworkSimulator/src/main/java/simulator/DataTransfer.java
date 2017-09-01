package simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
class DataTransfer {
    private int sourceNodeId;
    private int destNodeId;
    private List<DataPackage> payload;
    private Map<Settings.Options, Object> requestOptions;
    private int size;
    private Settings.Requests request;
    private boolean isRequest;

    DataTransfer(int sourceNodeId, int destNodeId, List<DataPackage> payload, int transferSize){
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.payload = payload;
        this.size = transferSize;
        isRequest = false;
    }

    DataTransfer(int sourceNodeId, int destNodeId, Map<Settings.Options, Object> requestOptions, Settings.Requests request){
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.requestOptions = requestOptions;
        this.request = request;
        size = 0;
        isRequest = true;
    }
}
