package simulator;

public class ClientReceiveThread extends Thread {

    private Client client;

    ClientReceiveThread(Client client){
        this.client = client;
    }

    @Override
    public void run(){
        client.receive();
    }
}
