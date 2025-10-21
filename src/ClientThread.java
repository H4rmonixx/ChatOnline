import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;

interface ServerFunc {
    void sendMessage(String msg, String target);
}

public class ClientThread extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String userLogin;
    private Vector<ClientThread> v;
    private final ServerFunc server;

    ClientThread(Socket socket, InputStream in, OutputStream out, String login, ServerFunc server) throws IOException{
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.userLogin = login;
        this.server = server;
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
    
    public boolean sendGlobalMessage(String message){
        try{
            this.out.write("globalMessage\n".getBytes());
            this.out.write((message+"\n").getBytes());
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
                    
                    this.server.sendMessage(this.userLogin + ": " + msg, target);
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
