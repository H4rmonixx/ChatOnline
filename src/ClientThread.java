import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Vector;


public class ClientThread extends Thread {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String userLogin;

    ClientThread(Socket socket) throws IOException{
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        
        StringBuffer str = new StringBuffer();
        int k;
        while((k = this.in.read()) != -1 && k != '\n') str.append((char)k);
        this.userLogin = str.toString();
        
        start();
    }

    public String getLogin(){
        return this.userLogin;
    }

    public void sendUserList(String list) throws IOException{
        this.out.write("usersList\n".getBytes());
        this.out.write((list+"\n").getBytes());
    }

    public void run(){
        try{
            boolean done = false;
            int k;			
            StringBuffer sb;

            while(!done){
                
            }
            in.close();
            out.close();
            socket.close();
        } catch(IOException e){
                System.out.println(e);
        }
    }
}
