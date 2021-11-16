
package view;

import control.Connect;
import control.ListUserThread;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.Audio;
import model.ClientInfo;
import model.Device;
import model.ListUser;
import model.Params;

public class mainFrm extends javax.swing.JFrame {
    private Params params;
    private Connect con;
    private ListUserThread listUserThread;
    private boolean stop;
    private Audio audio;
    private int InputIndx;
    private int OutputIndx;
    List<Device> listInp;
    List<Device> listOup;
    public mainFrm() {
        initComponents();
        audio = new Audio();
        listInp = new ArrayList<>();
        listOup = new ArrayList<>();
        new Thread(){
            @Override
            public void run(){
                while(true){
                    listInp = new Audio().getInputDevice();
                    listOup = new Audio().getOutputDevice();
                    if(jComboBox1.getItemCount() != listInp.size() ||
                       jComboBox2.getItemCount() != listOup.size()){
                        jComboBox1.removeAllItems();
                        for(Device i:listInp){
                            jComboBox1.addItem(i.getName());
                        }
                        jComboBox2.removeAllItems();
                        for(Device i:listOup){
                            jComboBox2.addItem(i.getName());
                        }
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(mainFrm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }.start();
    }
    
    public void setName(){
        jLabel1.setText("Your name: " + params.getName());
    }
    
    public void setServerInfo(){
        jLabel2.setText("ServerInfo: " + params.getAddr() + ":" + params.getPort());
    }
    
    public int InitSocket(){
        con = new Connect(params.getAddr(), params.getPort());
        if(con.getSocket() == null){
            JOptionPane.showMessageDialog(this,"Không thể kết nối đến server");
            return 0;
        }else{
            con.sendString(params.getName() + "_" + audio.getInputDevice().size() + "_" + audio.getOutputDevice().size()
                        + "_" + con.getSocket().getLocalAddress().toString() + "_" + con.getSocket().getLocalPort());
            new Thread(){
                @Override
                public void run(){
                    listUserThread = new ListUserThread(con);
                    listUserThread.start();
                    while(true){
                        if(!stop){
                            ListUser list = listUserThread.getList();
                            if(list != null){
                                if(list.getList().isEmpty() || list.getList().get(list.getList().size()-1) != null){
                                    DefaultListModel listModel = new DefaultListModel();
                                    for(ClientInfo i:list.getList()){
                                        listModel.addElement(i.getName());
                                    }
                                    if(!jList1.getModel().toString().equals(listModel.toString())){
                                        jList1.setModel(listModel);
                                    };
                                }else{
                                    stop = true;
                                    byte[] buffer = new byte[1024];
                                    ClientInfo info = con.rcvClientInfo();
                                    System.out.println(info.getName());
                                    comingFrm call = new comingFrm();
                                    call.setCon(con);
                                    call.setSourceinfo(new ClientInfo(params.getName(),con.getSocket().toString(),listInp.size(), listOup.size()
                                                                      ,con.getSocket().getLocalAddress().toString(),con.getSocket().getLocalPort()));
                                    call.setTargetinfo(info);
                                    call.setCallInfo("Comming call...");
                                    call.setVisible(true);
                                }
                            }
                        }
                    }
                }
            }.start();
        }
        return 1;
    }
    
    // Getter and Setter
    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public Connect getCon() {
        return con;
    }

    public void setCon(Connect con) {
        this.con = con;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public int getInputIndx() {
        return InputIndx;
    }

    public void setInputIndx(int InputIndx) {
        this.InputIndx = InputIndx;
    }

    public int getOutputIndx() {
        return OutputIndx;
    }

    public void setOutputIndx(int OutputIndx) {
        this.OutputIndx = OutputIndx;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Voice Chat N8");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono Light", 0, 11)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 204, 0));
        jLabel1.setText("YourName");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono Light", 0, 11)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 204, 0));
        jLabel2.setText("ServerInfo");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono Light", 0, 11)); // NOI18N
        jLabel3.setText("Friends On Server");

        jList1.setFont(new java.awt.Font("JetBrains Mono", 1, 14)); // NOI18N
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jList1.setToolTipText("");
        jList1.setFocusable(false);
        jList1.setSelectionBackground(new java.awt.Color(84, 65, 121));
        jList1.setVisibleRowCount(4);
        jScrollPane1.setViewportView(jList1);

        jLabel4.setFont(new java.awt.Font("JetBrains Mono Light", 0, 11)); // NOI18N
        jLabel4.setText("Choose Microphone");

        jComboBox1.setFont(new java.awt.Font("JetBrains Mono", 0, 11)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox1.setSelectedIndex(-1);
        jComboBox1.setFocusable(false);
        jComboBox1.setLightWeightPopupEnabled(false);

        jLabel5.setFont(new java.awt.Font("JetBrains Mono Light", 0, 11)); // NOI18N
        jLabel5.setText("Choose Speaker");

        jComboBox2.setFont(new java.awt.Font("JetBrains Mono", 0, 11)); // NOI18N
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.setSelectedIndex(-1);
        jComboBox2.setFocusable(false);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/icon/accept-call (2).png"))); // NOI18N
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/icon/red-x (1).png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(83, 83, 83))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Send Message
        OutputStream os;
        try {
            os = con.getSocket().getOutputStream();
            int msg = 0;
            os.write(msg);
        } catch (IOException ex) {
        }
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int index = jList1.getSelectedIndex();
        this.setStop(true);
        listUserThread.setStop(true);
        if(index != -1){
            ClientInfo info = listUserThread.getList().getList().get(index);
            Socket socket   = con.getSocket();
            callFrm call = new callFrm();
            call.setSourceinfo(new ClientInfo(params.getName(),con.getSocket().toString(),listInp.size(), listOup.size()
                               ,con.getSocket().getLocalAddress().toString(),con.getSocket().getLocalPort()));
            call.setTargetinfo(info);
            call.setCon(con);
            call.setVisible(true);
            call.TryConnect();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        java.awt.EventQueue.invokeLater(() -> {
            new mainFrm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
