package simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
class DataTransfer {
    private int sourceNodeId;
    private int destNodeId;
    private List<Frame> payload;
    private int dataPackageSize;
    private int size;

    DataTransfer(int sourceNodeId, int destNodeId, List<Frame> payload, int dataPackageSize){
        this.sourceNodeId = sourceNodeId;
        this.destNodeId = destNodeId;
        this.payload = payload;
        this.dataPackageSize = dataPackageSize;
        size = calculateSize();
    }

    private int calculateSize() {
        return payload.size()*dataPackageSize;
    }
}
