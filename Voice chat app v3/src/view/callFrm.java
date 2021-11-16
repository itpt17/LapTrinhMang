package view;

import control.Connect;
import control.PlayThread;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Audio;
import model.ClientInfo;

public class callFrm extends javax.swing.JFrame {
    private ClientInfo sourceinfo;
    private ClientInfo targetinfo;
    private Connect con;
    private Audio audio;
    boolean accept = false;
    private int micIdx;
    private int spkIdx;
    private boolean isAlive;
    PlayThread play;
    public callFrm() {
        initComponents();
        audio = new Audio();
        isAlive = false;
    }

    public void TryConnect() {
                jLabel1.setText(targetinfo.getName());
                con.sendInt(3);
                System.out.println("running");
                int res = con.rcvInt();
                System.out.println("call1 " + res);
                if (res == 1) {
                    con.sendClientInfo(targetinfo);
                    res = con.rcvInt();
                    System.out.println("call2 " + res);
                    con.sendClientInfo(sourceinfo);
                    res = con.rcvInt();
                    System.out.println("call3 " + res);
                }
                res = con.rcvInt();
                if (res != 1) {
                    new Thread() {
                        @Override
                        public void run() {
                            int times = 0;
                            jLabel2.setText("Friend is busy !!!!");
                            while (times < 3) {
                                times++;
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(callFrm.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            setVisible(false);
                        }
                    }.start();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            int stt = con.rcvInt();
                            if (stt == 0) {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        int times = 0;
                                        jLabel2.setText("Friends not response !!!!");
                                        while (times < 3) {
                                            times++;
                                            try {
                                                Thread.sleep(1500);
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(callFrm.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        setVisible(false);
                                    }
                                }.start();
                            } else {
                                con.sendInt(4);
                                acceptCall();
                            }
                        }
                    }.start();  
        }
    }

    public void setCallInfo(String msg) {
        jLabel1.setText(targetinfo.getName());
        jLabel2.setText(msg);
    }

    public void acceptCall() {
        jLabel2.setText("Calling");
        jLabel2.setForeground(Color.GREEN);
        System.out.println("accept");
        System.out.println(micIdx + " " + spkIdx);
        play = new PlayThread(audio, con, micIdx, spkIdx);
        play.Start();
        new Thread(){
            @Override
            public void run(){
                while(true){
                    if(play.isStopRec())
                    {
                        isAlive = false;
                        setVisible(false);
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(callFrm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    public ClientInfo getSourceinfo() {
        return sourceinfo;
    }

    public void setSourceinfo(ClientInfo sourceinfo) {
        this.sourceinfo = sourceinfo;
    }

    public ClientInfo getTargetinfo() {
        return targetinfo;
    }

    public void setTargetinfo(ClientInfo targetinfo) {
        this.targetinfo = targetinfo;
    }

    public Audio getAudio() {
        return audio;
    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }

    public Connect getCon() {
        return con;
    }

    public void setCon(Connect con) {
        this.con = con;
    }

    public int getMicIdx() {
        return micIdx;
    }

    public void setMicIdx(int micIdx) {
        this.micIdx = micIdx;
    }

    public int getSpkIdx() {
        return spkIdx;
    }

    public void setSpkIdx(int spkIdx) {
        this.spkIdx = spkIdx;
    }

    public boolean isIsAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 255, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Hung");

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Connecting...");

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/icon/phone.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(83, 83, 83)
                .addComponent(jButton1)
                .addContainerGap(85, Short.MAX_VALUE))
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(25, 25, 25))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        isAlive = false;
        play.setStopPlay(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(callFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        java.awt.EventQueue.invokeLater(() -> {
            new callFrm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
