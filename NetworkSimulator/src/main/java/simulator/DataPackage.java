package simulator;

import com.sun.istack.internal.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class DataPackage {

    private int chunkID;
    private int chunkPart;
    private int totalChunkPartNumber;

    DataPackage(int chunkID){
        this.chunkID = chunkID;
    }

    DataPackage(int chunkID, int part, int totalChunkPartsNumber){
        this.chunkID = chunkID;
        this.chunkPart = chunkPart;
        this.totalChunkPartNumber = totalChunkPartsNumber;
    }
}
