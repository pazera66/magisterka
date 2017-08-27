package simulator;

 class Settings {

    private static final int number_Of_Clients = 100;
    private static final int source_Bitrate = 8388000;
    private static final int server_Upload_Bandwidth = 1000000000;
    private static final int serverID = 9999;
    private static final int number_Of_Cycles = 60000;
    private static final int FPS = 50;
    private static final int samplingFrequency = 1000;
    private static final int globalLatency = 100;


    public enum Requests {
        RequestingListOfNodes,
        SendingListOfNodes,
        RequestingForListOfStoredFrames,
        SendingListOfStoredFrames,
        RequestingSpecificFrames,
        SendingSpecificFrames

    }

    static int getNumberOfFrameParts(){
        return samplingFrequency / FPS;
    }

    static int getNumber_Of_Clients() {
        return number_Of_Clients;
    }

    static int getSource_Bitrate() {
        return source_Bitrate;
    }

    static int getServer_Upload_Bandwidth() {
        return server_Upload_Bandwidth;
    }

    static int getServerID() {
        return serverID;
    }

    static int getNumber_Of_Cycles() {
        return number_Of_Cycles;
    }

    static int getFPS() {
        return FPS;
    }

    static int getSamplingFrequency() {
        return samplingFrequency;
    }

    static int getGlobalLatency(){return globalLatency;}
}
