package simulator;

import javafx.application.Platform;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainSimulatorThread extends Thread {

    private final int SOURCE_BITRATE = 8388000; // number of bits per second
    private final int SERVER_UPLOAD_BANDWIDTH = 1000000000;
    private final static int serverId = 9999;
    private final int numberOfCycles = 600000;
    private final int samplingFrequency = 1000; // how many times per simulated second calculations are done

    private Server server;
    private List<Client> nodes = new LinkedList<>();
    private Client[] nodesArray;
    private Network network = new Network(SOURCE_BITRATE);
    private GUI gui;
    private StopWatch stopWatch = new StopWatch();

    private int NUMBER_OF_NODES;

    MainSimulatorThread(int numberOfNodes, GUI gui){
        this.NUMBER_OF_NODES = numberOfNodes;
        this.gui = gui;
    }

    @Override
    public void run(){
        startSimulation();

//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                gui.getLabel().setText("BBBBBBBBBBBBBBBBB");
//            }
//        });
    }

    private void startSimulation(){
        for (int i = 0; i < NUMBER_OF_NODES; i++){
            nodes.add(new Client(i, 10000000, 10000000, SOURCE_BITRATE, 3, network, samplingFrequency));
        }
        server = new Server(serverId, SOURCE_BITRATE, SERVER_UPLOAD_BANDWIDTH, nodes, network, samplingFrequency);
        initializeNodesArray();

        StopWatch watch = new StopWatch();
        watch.start();

        runSimulation();

        System.out.println("Done");
        watch.stop();
        System.out.println(watch.toString());
    }

    private void initializeNodesArray() {
        nodesArray = new Client[nodes.size()];
        nodes.toArray(nodesArray);
    }

    private void runSimulation() {

        for (int i = 0; i < numberOfCycles; i++){
            performSendCycle();

            performReceiveCycle();



            network.clearNetwork();

            System.out.println(i);
        }
    }

    private void performSendCycle() {

        server.send();

        //for each node : nodes, node.send
    }

    private void performReceiveCycle() {
        //runWithThreads();
        stopWatch.reset();
        stopWatch.start();
        for (Client node:nodes) {

            node.receive();

        }
        stopWatch.stop();
        System.out.println("Cycle time: " + stopWatch.toString());
    }

    private void runWithThreads() {
        Thread threads[] = new Thread[nodes.size()];

        for (int i = 0; i < nodes.size(); i++){
            threads[i] = new ClientReceiveThread(nodesArray[i]);
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("There was a problem with joining threads");
            }
        }
    }
}
