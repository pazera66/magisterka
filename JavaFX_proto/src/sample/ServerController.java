package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerController implements Runnable {

    private communicatorWindowGUI comInterface;
    ServerSocket socket;
    Socket serviceSocket;
    DataInputStream is;
    PrintStream ps;
    PrintWriter p;
    Thread thread = null;

    public ServerController(){
        comInterface = new communicatorWindowGUI("Server window");
        comInterface.button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendMessage(comInterface.msg.getText());
            }
        });

        comInterface.ExitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    is.close();
                    thread.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        start();
        //initSocket();
    }

    private void start() {
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    private void initSocket() {
        boolean connected = false;
        while (!connected) {
            try {
                socket = new ServerSocket( 11236);
                serviceSocket = socket.accept();
                System.out.println("Connection accepted");
                is = new DataInputStream(serviceSocket.getInputStream());
                ps = new PrintStream(serviceSocket.getOutputStream());
                p = new PrintWriter(ps);
                String test = "test";
                connected = true;
            } catch (IOException e){
                System.out.println("Connection refused");
            }
        }
        getData();
    }

    private void getData() {

        while (true) {
            try {
                String message = is.readUTF();
                if (message != null || message.equals("")){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder prev = new StringBuilder(comInterface.receivedMsg.getText());
                            comInterface.receivedMsg.setText(prev.append(message + "\n").toString());

                        }
                    });

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMessage(String msg){
//        p.println(msg);
//        p.flush();
    }

    @Override
    public void run() {
        initSocket();
    }
}
