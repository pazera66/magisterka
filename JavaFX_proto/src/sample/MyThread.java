package sample;

public class MyThread extends Thread {

    @Override
    public void run(){
        new ServerController();
    }
}
