import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

public class ChatServer {
    
    public final static Vector<ClientThread> cthreads = new Vector<>();
    public final static Vector<String> usernames = new Vector<>();

    public static void main(String[] args) {
        
        StringBuffer str;
        int k;
        
        try{
            ServerSocket s = new ServerSocket(2011);
            System.out.println("Server running...");
            while(true){
                Socket incoming = s.accept();
                System.out.println("New connection received...");
                OutputStream out = incoming.getOutputStream();
                InputStream in = incoming.getInputStream();
                str = new StringBuffer();
                while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                String newUser = str.toString();
                
                if(usernames.contains(newUser)){
                    System.out.println("Connection refused: Login already connected!");
                    out.write("EXISTING\n".getBytes());
                    in.close();
                    out.close();
                    incoming.close();
                    continue;
                }
                
                out.write("ACCEPT\n".getBytes());
                System.out.println(newUser + ": Connected");
                
                usernames.add(newUser);
                cthreads.addElement(new ClientThread(incoming, in, out, newUser, (msg, target, source) -> {
                    sendMessage(msg, target, source);
                }, (nick) -> {
                    System.out.println(nick + ": Disconnected");
                    disconnectUser(nick);
                }));
                
                updateUsersList();
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void disconnectUser(String username){
        usernames.remove(username);
        checkConnections();
        updateUsersList();
    }
    
    public static void sendMessage(String msg, String target, String source){
        Iterator<ClientThread> it = cthreads.iterator();
        while (it.hasNext()) {
            ClientThread th = it.next();
            if (!th.sendMessage(msg, target, source)) {
                it.remove();
            }
        }
    }
    
    public static void updateUsersList(){
        Iterator<ClientThread> it = cthreads.iterator();
        while (it.hasNext()) {
            ClientThread th = it.next();
            if (!th.sendUserList(String.join(";", usernames))) {
                it.remove();
            }
        }
    }
    
    public static void checkConnections(){
        Iterator<ClientThread> it = cthreads.iterator();
        while (it.hasNext()) {
            ClientThread th = it.next();
            if(!usernames.contains(th.getLogin())){
                it.remove();
            }
            if (!th.isConnection()) {
                it.remove();
            }
        }
    }
    
}
