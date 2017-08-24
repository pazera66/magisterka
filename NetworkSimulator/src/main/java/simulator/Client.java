package simulator;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

import static simulator.Settings.*;

@Setter
@Getter
class Client {

    private final int FPS = 50;

    private int id;
    private int uploadBandwidth;
    private int downloadBandwidth;
    private int playbackBitrate = getSource_Bitrate();
    private int minimalForwardBufferInSeconds;
    private Network network;
    private int samplingFrequency = Settings.getSamplingFrequency();
    private List<Integer> listOfNodes = new ArrayList<>();

    private List<DataPackage> networkBuffer = new ArrayList<>();
    private List<DataTransfer> outgoingNetworkBuffer = new ArrayList<>();
    private List<Frame> incompleteFrameBuffer = new ArrayList<>();
    private List<Frame> playbackBuffer = new ArrayList<>();
    private List<Integer> frameStorage = new ArrayList<>();
    private List<DataTransfer> incomingRequests = new ArrayList<>();
    private List<DataTransfer> outgoingRequests = new ArrayList<>();


    private int currentFrameOnPlayback = 0;
    private int dataPackageSize;
    private int frameSize;
    private boolean playbackOn = false;
    private int framePlaybackTime = 0;


    Client(int id, int uploadBandwidth, int downloadBandwidth, int minimalForwardBufferInSeconds, Network network){
        this.id = id;
        this.uploadBandwidth = uploadBandwidth;
        this.downloadBandwidth = downloadBandwidth;
        this.minimalForwardBufferInSeconds = minimalForwardBufferInSeconds;
        this.network = network;
        dataPackageSize = playbackBitrate / samplingFrequency;
        frameSize = dataPackageSize * getNumberOfFrameParts();
    }

    void receive() {
        int remainingDownloadBandwidth = downloadBandwidth / samplingFrequency;
        List<DataTransfer> incomingData = network.getDataTransfers().stream().filter(x -> x.getDestNodeId() == id).collect(Collectors.toList());

        for (DataTransfer dataTransfer:incomingData){
            if (dataTransfer.getSize() < remainingDownloadBandwidth){
                networkBuffer.addAll(dataTransfer.getPayload());
                remainingDownloadBandwidth = remainingDownloadBandwidth - dataTransfer.getSize();
            } else if (dataTransfer.getRequest() != null){
                incomingRequests.add(dataTransfer);
            } else {
                System.out.println("Not enough bandwidth to receive all data on node: " + String.valueOf(id));
                //TODO resend frame/part
            }
        }
        processData();
        processRequests();
    }

    private void processData(){
        processDataToIncompleteFrameBuffer();
        moveCompletedFramesToPlaybackBuffer();
        processPlayback();

        updateUI();
    }

    private void processDataToIncompleteFrameBuffer() {
        for (DataPackage dataPackage:networkBuffer) {
            int frameIndex = getIncompleteFrameIndex(dataPackage.getFrameNumber());

            if (frameIndex == -1){
                incompleteFrameBuffer.add(new Frame(dataPackage.getFrameNumber(), dataPackage.getFramePartNumber()));
            } else {
                incompleteFrameBuffer.get(frameIndex).addPart(dataPackage.getFramePartNumber());
            }
        }
        networkBuffer.clear();
    }

    private int getIncompleteFrameIndex(int frameNumber) {
        for (Frame frame:incompleteFrameBuffer) {
            if (frame.getFrameNumber() == frameNumber){
                return incompleteFrameBuffer.indexOf(frame);
            }
        }
        return -1;
    }

    private void moveCompletedFramesToPlaybackBuffer() {
        List<Frame> framesToBeRemoved = new ArrayList<>();

        for (Frame frame:incompleteFrameBuffer){
            if (frame.frameComplete()) {
                playbackBuffer.add(frame);
                framesToBeRemoved.add(frame);
            }
        }

        incompleteFrameBuffer.removeAll(framesToBeRemoved);
    }

    private void processPlayback(){
        playbackOn = checkIfEnoughFramesAreBuffered();

        if (playbackOn) {
            framePlaybackTime++;

            if (framePlaybackTime == getNumberOfFrameParts()) {
                framePlaybackTime = 0;
                int frameIndexToBeMovedToStorage;

                for (Frame frame:playbackBuffer){
                    if (frame.getFrameNumber() == currentFrameOnPlayback){
                        frameIndexToBeMovedToStorage = playbackBuffer.indexOf(frame);
                        playbackBuffer.remove(frameIndexToBeMovedToStorage);
                        frameStorage.add(frame.getFrameNumber());
                        break;
                    }
                }
                currentFrameOnPlayback++;
            }
        }


    }

    private void updateUI() {

    }

    private boolean checkIfEnoughFramesAreBuffered() {
        int requiredNumberOfFramesInBufferForPlayback = minimalForwardBufferInSeconds * FPS;
        int numberOfBufferedFrames = playbackBuffer.stream().filter(x -> x.getFrameNumber() >= currentFrameOnPlayback)
                .sorted(Comparator.comparing(Frame::getFrameNumber))
                .collect(Collectors.toList()).size();
        return numberOfBufferedFrames >= requiredNumberOfFramesInBufferForPlayback;
    }

    private void processRequests() {
        for (DataTransfer transfer: incomingRequests){
            switch (transfer.getRequest()){
                case SendingListOfNodes:
                    listOfNodes = transfer.getRequestPayload();
                    updateNodeTree();
                    break;
                case RequestingForListOfStoredFrames:
                    sendListOfStoredFrames(transfer.getSourceNodeId());
                    break;
                case SendingListOfStoredFrames:
                    saveReceivedFrames(transfer.getRequestPayload());
                    break;
                case RequestingSpecificFrames:
                    requestSpecificFrames(transfer.getSourceNodeId());
                    break;
                case SendingSpecificFrames:
                    sendSpecificFrames(transfer.getSourceNodeId(), transfer.getRequestPayload());
                    break;
            }
        }
    }

    void send() {
        if (listOfNodes.isEmpty()) {
            requestListOfNodesFromServer();
            return;
        }

        for (DataTransfer transfer:outgoingRequests){
            network.addDataTransfer(transfer);
        }

        int remainingUploadBandwidth = uploadBandwidth / samplingFrequency;
        for (DataTransfer transfer:outgoingNetworkBuffer){
            if (transfer.getSize() < remainingUploadBandwidth){
                network.addDataTransfer(transfer);
                remainingUploadBandwidth = remainingUploadBandwidth - transfer.getSize();
            }
        }


    }

    private void requestListOfNodesFromServer() {
        outgoingRequests.add(new DataTransfer(id, getServerID(), null, Requests.RequestingListOfNodes));
    }

    private void requestListOfStoredFrames(int destNode){
        outgoingRequests.add(new DataTransfer(id, destNode, null, Requests.RequestingForListOfStoredFrames));
    }

    private void sendListOfStoredFrames(int destNode){
        outgoingRequests.add(new DataTransfer(id, destNode, frameStorage, Requests.SendingListOfStoredFrames));
    }

    private void requestSpecificFrames(int destNode){
        outgoingRequests.add(new DataTransfer(id, destNode, null, Requests.RequestingSpecificFrames));
    }

    private void sendSpecificFrames(int destNode, List<Integer> requestedFrames){
        List<DataPackage> payload = new ArrayList<>();
        for (Integer frame:requestedFrames){
            payload.add(new DataPackage(frame));
        }
        outgoingNetworkBuffer.add(new DataTransfer(id, destNode, payload, (payload.size() * frameSize)));
    }

    private void saveReceivedFrames(List<Integer> receivedFrames){
        frameStorage.addAll(receivedFrames);

        Collections.sort(frameStorage);
    }

    private void gatherLatencyStats(){}

    private void updateNodeTree() {

    }

}