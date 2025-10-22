import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

interface ServerSendMessage {
    void run(String msg, String target, String source);
}
interface ServerDeclareDisconnect {
    void run(String nick);
}

public class ClientThread extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String userLogin;
    private Vector<ClientThread> v;
    private final ServerSendMessage servermsg;
    private final ServerDeclareDisconnect serverdisconnect;

    ClientThread(Socket socket, InputStream in, OutputStream out, String login, ServerSendMessage servermsg, ServerDeclareDisconnect serverdisconnect) throws IOException{
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.userLogin = login;
        this.servermsg = servermsg;
        this.serverdisconnect = serverdisconnect;
        start();
    }

    public String getLogin(){
        return this.userLogin;
    }
    
    public boolean isConnection(){
        try{
            this.out.write("\n".getBytes());
        } catch(IOException e){
            return false;
        }
        return true;
    }

    public boolean sendUserList(String list){
        try{
            this.out.write("usersList\n".getBytes());
            this.out.write((list+"\n").getBytes());
        } catch(IOException e){
            return false;
        }
        return true;
    }
    
    public boolean sendMessage(String message, String target, String source){
        try{
            if(target.equals(";")){
                this.out.write("globalMessage\n".getBytes());
                this.out.write((source + "\n").getBytes());
                this.out.write((message + "\n").getBytes());
            } else {
                
            }
        } catch(IOException e){
            return false;
        }
        return true;
    }

    public void run(){
        try{
            boolean done = false;
            int k;			
            StringBuffer str;

            while(!done){
                str = new StringBuffer();
                while((k = this.in.read()) != -1 && k != '\n') str.append((char)k);
                String header = str.toString();
                
                if(header.equals("message")){
                    str = new StringBuffer();
                    while((k = this.in.read()) != -1 && k != '\n') str.append((char)k);
                    String target = str.toString();
                    str = new StringBuffer();
                    while((k = this.in.read()) != -1 && k != '\n') str.append((char)k);
                    String msg = str.toString();
                    
                    this.servermsg.run(msg, target, this.userLogin);
                }
                
                if(header.equals("disconnect")){
                    this.serverdisconnect.run(this.userLogin);
                }
                
            }
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
