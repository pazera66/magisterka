package simulator;

import org.apache.commons.lang3.time.StopWatch;

import java.util.LinkedList;
import java.util.List;

public class MainSimulatorThread extends Thread {

    private final int numberOfCycles = 600000;

    private Server server;
    private List<Client> nodes = new LinkedList<>();
    private Network network = new Network();
    private GUI gui;
    private StopWatch stopWatch = new StopWatch();

    private int NUMBER_OF_NODES = Settings.getNumber_Of_Clients();

    MainSimulatorThread(GUI gui){
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
        for (int i = 0; i < NUMBER_OF_NODES; i++) {
            nodes.add(new Client(i, 10000000, 10000000, 3, network));
            network.addHost(i);
        }
        server = new Server(nodes, network);
        network.addHost(server.getServerId());

        StopWatch watch = new StopWatch();
        watch.start();

        runSimulation();

        System.out.println("Done");
        watch.stop();
        System.out.println(watch.toString());
    }

    private void runSimulation() {

        for (int i = 0; i < numberOfCycles; i++){
            network.updateNetwork();
            performSendCycle(i);
            performReceiveCycle(i);
            network.clearNetwork();
            System.out.println(i);
        }
    }

    private void performSendCycle(int cycle) {
        server.send(cycle);

        for (Client node:nodes){
            node.send(cycle);
        }

    }

    private void performReceiveCycle(int cycle) {
        stopWatch.reset();
        stopWatch.start();

        server.receive(cycle);
        for (Client node:nodes) {
            node.receive(cycle);
        }

        stopWatch.stop();
        System.out.println("Cycle time: " + stopWatch.toString());
    }

//    void addNode(){
//        nodes.
//    }
}
