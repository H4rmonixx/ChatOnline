import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


public class ChatServer {
    public static void main(String[] args) {
        Vector<ClientThread> v = new Vector<ClientThread>();
        try{
            ServerSocket s = new ServerSocket(2011);
            System.out.println("Server running...");
            while(true){
                Socket incoming = s.accept();
                v.addElement(new ClientThread(incoming));
                
                String userList = "";
                for(int i=0; i<v.size(); i++){
                    userList += v.get(i).getLogin()+";";
                }
                for(int i=0; i<v.size(); i++){
                    v.get(i).sendUserList(userList);
                }
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
