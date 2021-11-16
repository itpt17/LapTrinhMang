package control;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.Audio;

public class PlayThread {
    Audio audio;
    Connect con;
    int inputIdx;
    int outputIdx;

    public PlayThread(Audio audio, Connect con, int inputIdx, int outputIdx) {
        this.audio = audio;
        this.con = con;
        this.inputIdx = inputIdx;
        this.outputIdx = outputIdx;
    }

    public void Start() {
        new RecordThread(con, audio, inputIdx).start();
        new ReplayThread(con, audio, outputIdx).start();
    }
}

class RecordThread extends Thread {

    Connect con;
    Audio audio;
    int index;

    public RecordThread(Connect con, Audio audio, int index) {
        this.con = con;
        this.audio = audio;
        this.index = index;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            audio.getInputLine(index);
            audio.startInputLine();
            while (true) {
                byte[] buffer = new byte[1024];
                audio.getTargetLine().read(buffer, 0, buffer.length);
                con.sendBytes(buffer);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ReplayThread extends Thread {

    Connect con;
    Audio audio;
    int index;

    public ReplayThread(Connect con, Audio audio, int index) {
        this.con = con;
        this.audio = audio;
        this.index = index;
    }

    @Override
    public void run() {
            try {
                Thread.sleep(1000);
                audio.getOutputLine(index);
                audio.startOutputLine();
                while (true) {
                    byte[] buffer = con.rsvBytes();
                    audio.getSourceLine().write(buffer, 0, buffer.length);
                }
            }catch (InterruptedException ex) {
            Logger.getLogger(ReplayThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
}
