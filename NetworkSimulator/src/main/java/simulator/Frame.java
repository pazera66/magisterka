package simulator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Frame {

    private int frameNumber;
    private boolean[] partsArray = new boolean[Settings.getNumberOfFrameParts()];

    Frame(int frameNumber, int framePartNumber) {
        this.frameNumber = frameNumber;
        partsArray[framePartNumber] = true;
    }

    void addPart(int partNumber){
        partsArray[partNumber] = true;
    }

    boolean frameComplete(){
        for (boolean check:partsArray) {
            if (!check) {
                return false;
            }
        }
        return true;
    }
}
