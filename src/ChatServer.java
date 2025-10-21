import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;


public class ChatServer {
    
    public static Vector<ClientThread> v = new Vector<>();

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
                System.out.println("Logging in...");
                
                if(!isLoginUnique(newUser)){
                    System.out.println("Connection refused: Login already connected!");
                    out.write("EXISTING\n".getBytes());
                    in.close();
                    out.close();
                    incoming.close();
                    continue;
                }
                
                out.write("ACCEPT\n".getBytes());
                
                System.out.println(newUser + ": Connected");
                
                v.addElement(new ClientThread(incoming, in, out, newUser, (msg, target) -> {
                    if(target.equals(";")) sendGlobalMessage(msg);
                    else sendDirectMessage(msg, target);
                }));
                
                updateUsersList();
                
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public static void sendGlobalMessage(String msg){
        Iterator<ClientThread> it = v.iterator();
        while (it.hasNext()) {
            ClientThread th = it.next();
            if (!th.sendGlobalMessage(msg)) {
                it.remove();
            }
        }
    }
    
    public static void sendDirectMessage(String msg, String target){
        
    }
    
    public static boolean isLoginUnique(String newUser){
        String[] logins = getUsersList().split(";");
        for(String x : logins){
            if(x.equals(newUser)){
                return false;
            }
        }
        return true;
    }
    
    public static void updateUsersList(){
        String list = getUsersList();
        Iterator<ClientThread> it = v.iterator();
        while (it.hasNext()) {
            ClientThread th = it.next();
            if (!th.sendUserList(list)) {
                it.remove();
            }
        }
    }
    
    public static String getUsersList(){
        String userList = "";
        Iterator<ClientThread> it = v.iterator();
        while (it.hasNext()) {
            ClientThread th = it.next();
            if(!th.isConnection()){
                it.remove();
                continue;
            }
            userList += th.getLogin() + ";";
        }
        return userList;
    }
    
}
