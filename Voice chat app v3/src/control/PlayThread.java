package control;

import model.Audio;

public class PlayThread {
    Audio audio;
    Connect con;
    int inputIdx;
    int outputIdx;
    boolean stopPlay;
    boolean stopRec;
    
    public PlayThread(Audio audio, Connect con, int inputIdx, int outputIdx) {
        this.audio = audio;
        this.con = con;
        this.inputIdx = inputIdx;
        this.outputIdx = outputIdx;
        stopPlay = false;
        stopRec  = false;
    }

    public void Start() {
        new Thread(){
            @Override
            public void run(){
            try {
                Thread.sleep(1000);
                audio.getInputLine(inputIdx);
                audio.startInputLine();
                while (!stopRec) {
                        byte[] buffer = new byte[1024];
                        audio.getTargetLine().read(buffer, 0, buffer.length);
                        con.sendBytes(buffer);
                }
                } catch (InterruptedException ex) {
                    
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                    try {
                        Thread.sleep(1000);
                        audio.getOutputLine(outputIdx); 
                        audio.startOutputLine();
                        int cnt = 0;
                        while (!stopPlay) {
                            byte[] buffer = con.rsvBytes();
                            boolean check = false;
                            for(int i = 0; i < buffer.length; i++){
                                if(buffer[i] != 0){
                                    check = true;
                                }
                            }
                            if(!check) cnt++;
                            if(cnt >= 20){
                                stopPlay = true;
                            }
                            audio.getSourceLine().write(buffer, 0, buffer.length);
                        }
                        stopRec = true;
                        for(int i = 0; i < 20; i++){
                            byte[] tmpBuff = new byte[1024];
                                con.sendBytes(tmpBuff);
                        }
                    }catch (InterruptedException ex) {
                   
                }
            }
        }.start();
    }

    public boolean isStopPlay() {
        return stopPlay;
    }

    public void setStopPlay(boolean stopPlay) {
        this.stopPlay = stopPlay;
    }

    public boolean isStopRec() {
        return stopRec;
    }

    public void setStopRec(boolean stopRec) {
        this.stopRec = stopRec;
    }
    
    
}