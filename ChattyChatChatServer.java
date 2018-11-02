import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;

class clientHandler implements Runnable{
    private Long ID;
    private String nickname;
    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;

    clientHandler(Socket s, DataInputStream input, DataOutputStream output){
        byte[] array = new byte[40];
        new Random().nextBytes(array);
        nickname = new String(array, Charset.forName("UTF-8"));

        socket = s;
        in = input;
        out = output;
    }

    void setID(long id){
        ID = id;
    }

    public void run(){
        try {
            out.writeUTF("Connection started: port " + socket.getLocalPort());
            while (true) {
                String temp = in.readUTF();
                String[] words = temp.split(" ");

                if(words[0].equals("/quit")) {
                    System.out.println("Client at port " + socket.getPort() +
                            " sends exit. removing from client list . . . . .");
                    socket.close();

                    long valToBeRemoved = 0;
                    for(Long e: ChattyChatChatServer.clients.keySet()){
                        if(this.ID.equals(e))
                            valToBeRemoved = e;
                    }

                    ChattyChatChatServer.clients.remove(valToBeRemoved);
                    System.out.println("removed");
                    break;
                }
                else if(words[0].equals("/dm")){
                    if(words.length > 2) {

                        String temporary = "";
                        for(int i = 2; i < words.length; ++i){
                            temporary += words[i] + " ";
                        }
                        temporary = temporary.substring(0, temporary.length() - 1);

                        for(clientHandler item: ChattyChatChatServer.clients.values()){
                            if(item.nickname.equals(words[1]))
                                item.out.writeUTF(temporary);
                        }
                    }
                }
                else if(words[0].equals("/nick")){   //can mess up if just /dm is typed
                    if(words.length > 1) {
                        nickname = words[1];
                        out.writeUTF("nickname has been set to: " + nickname);
                    }
                    else
                        out.writeUTF("no nickname specified, current nickname is still [" + nickname + "]");
                }
                else{
                    for(clientHandler item: ChattyChatChatServer.clients.values()) {
                        if (!item.ID.equals(this.ID))
                            item.out.writeUTF(temp);
                    }
                }
            }
        }
        catch(IOException i){
            i.printStackTrace();

            System.out.println("Client " + socket + "crashed! removing from client list . . . . .");

            try{socket.close();}catch(IOException c){c.printStackTrace();}

            long valToBeRemoved = 0;
            for(Long e: ChattyChatChatServer.clients.keySet()){
                if(this.ID.equals(e))
                    valToBeRemoved = e;
            }
            ChattyChatChatServer.clients.remove(valToBeRemoved);
        }
    }
}

public class ChattyChatChatServer{
    static HashMap<Long, clientHandler> clients = new HashMap<>();

    public static void main(String[] args){
        try{
            ServerSocket chatServer = new ServerSocket(Integer.parseInt(args[0]));
            System.out.println("Server has booted");

            while (true) {
                Socket ch;
                ch = chatServer.accept();

                System.out.println("A new client is connected: " + ch);

                DataOutputStream out1 = new DataOutputStream(ch.getOutputStream()); //to write stuff to client
                DataInputStream in1 = new DataInputStream(ch.getInputStream()); // to read from client
                out1.writeUTF("You have connected to the server!");

                System.out.println("Assigning new thread for this client");

                clientHandler c1 = new clientHandler(ch, in1, out1);
                Thread temp = new Thread(c1);
                temp.start();

                c1.setID(temp.getId());

                ChattyChatChatServer.clients.put(temp.getId(), c1);
            }
        }
        catch(IOException x){
            x.printStackTrace();
        }
    }
}