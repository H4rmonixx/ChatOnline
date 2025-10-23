
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Admin
 */
public class GlobalChatFrame extends javax.swing.JFrame {

    public GlobalChatFrame(String login, Socket socketS, InputStream inS, OutputStream outS) {
        initComponents();
        this.login = login;
        jLabel2.setText("Zalogowano jako: " + login);
        
        this.userListModel = new DefaultListModel<>();
        jList1.setModel(this.userListModel);
        
        this.socket = socketS;
        this.in = inS;
        this.out = outS;
        
        jTextField1.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("ENTER"), "enter");
        jTextField1.getActionMap().put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButton2ActionPerformed(e);
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }
        });

        Thread loopThread = new Thread(() -> {
            try{
                
                while(true){
                    
                    Thread.sleep(500);
                    
                    StringBuffer str = new StringBuffer();
                    int k;
                    while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                    String header = str.toString();
                    
                    if(header.equals("usersList")){
                        str = new StringBuffer();
                        while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                        String list = str.toString();
                        String[] logins = list.split(";");
                        userListModel.clear();
                        for(String l : logins){
                            if(l.equals(login)) continue;
                            userListModel.addElement(l);
                        }
                    }
                    if(header.equals("globalMessage")){
                        str = new StringBuffer();
                        while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                        String rawSource = str.toString();
                        str = new StringBuffer();
                        while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                        String msg = str.toString();
                        
                        String source;
                        if(rawSource.equals(this.login)) source = "** " + rawSource;
                        else source = rawSource;
                        
                        SwingUtilities.invokeLater(() -> {
                            jTextArea1.append(source + ": " + msg + "\n");
                        });
                    }
                    if(header.equals("directMessage")){
                        str = new StringBuffer();
                        while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                        String source = str.toString();
                        str = new StringBuffer();
                        while((k = in.read()) != -1 && k != '\n') str.append((char)k);
                        String msg = str.toString();
                        
                        Iterator<DirectChatFrame> it = this.dms.iterator();
                        while (it.hasNext()) {
                            DirectChatFrame dm = it.next();
                            // Test
                        }
                    }
                    
                }
                
            } catch(Exception e){
                e.printStackTrace();
            }
        });

        loopThread.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Czat globalny");

        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Aktywni użytkownicy:");

        jButton1.setText("Otwórz czat osobisty");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(255, 255, 255));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea1);

        jButton2.setText("Wyślij");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel2.setText("Zalogowano jako:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 539, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField1)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Open direct message chat
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Sent global chat message
        String msg = jTextField1.getText().trim();
        if(msg.length() == 0) return;
        try{
            this.out.write(("message\n;\n"+msg+"\n").getBytes());
            jTextField1.setText("");
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Nie udalo sie wyslac wiadomosci!");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void closeWindow(){
        try{
            this.out.write("disconnect\n".getBytes());
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Nie udalo sie powiadomic serwera!");
        }
        this.dispose();
    }
    
    private final Vector<DirectChatFrame> dms = new Vector<>();
    private final String login;
    private final DefaultListModel<String> userListModel;
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
