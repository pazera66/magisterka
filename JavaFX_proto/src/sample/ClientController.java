package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.*;
import java.net.Socket;

public class ClientController {

    private communicatorWindowGUI comInterface;
    Socket socket;
    DataInputStream is;
    DataOutputStream os;
    PrintWriter p;

    public ClientController(){
        comInterface = new communicatorWindowGUI("Client window");
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
                    if (socket != null) socket.close();
                    if (os != null) os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        initSocket();
    }


    private void initSocket() {
        try {
            socket = new Socket("0.0.0.0", 11236);
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            p = new PrintWriter(os);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    private void sendMessage(String msg){
//        p.write(msg);
//        p.flush();
        try {
            os.writeUTF(msg);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
