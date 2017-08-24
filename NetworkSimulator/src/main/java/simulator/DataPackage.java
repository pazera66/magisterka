package simulator;

import com.sun.istack.internal.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class DataPackage {

    private int framePartNumber;
    private int frameNumber;

    DataPackage(@Nullable int framePartNumber, int frameNumber){
        this.framePartNumber = framePartNumber;
        this.frameNumber = frameNumber;
    }

    DataPackage(int frameNumberme) {
        this.frameNumber = frameNumberme;
    }
}
