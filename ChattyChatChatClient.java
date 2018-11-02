import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChattyChatChatClient implements Runnable{

    private Socket socket = null;
    private DataInputStream in = null;
    private DataOutputStream out = null;
    private ChattyChatChatClient(Socket a, DataInputStream b, DataOutputStream c){
        socket = a;
        in = b;
        out = c;
    }

    public void run(){
        try {
            try{
                while (true) {
                        System.out.println(in.readUTF());
                }
            }catch(SocketException se){}
        }
        catch(IOException i){
            i.printStackTrace();
        }
    }

    public static void main(String[] args){
        try {
            Scanner temp = new Scanner(System.in);

            Socket s = new Socket(args[0], Integer.parseInt(args[1]));
            DataInputStream in = new DataInputStream(s.getInputStream()); //for getting stuff sent from the server
            DataOutputStream out = new DataOutputStream(s.getOutputStream()); //for writing stuff to the server
            System.out.println(in.readUTF()); //prints whatever it got from server

            ChattyChatChatClient readFromServer = new ChattyChatChatClient(s, in, out);

            Thread server = new Thread(readFromServer);
            server.start();

            while(true){
                String sendIt = temp.nextLine();
                out.writeUTF(sendIt);

                if(sendIt.equals("/quit")){
                    System.out.println("Connection has been closed!");
                    break;
                }
            }

            temp.close();
            in.close();
            out.close();
        }
        catch(IOException i){
            i.printStackTrace();
        }
    }
}
