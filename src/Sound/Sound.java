/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

/**
 *
 * @author s124392
 */
public abstract class Sound {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    /**
     * The period between two ticks in milliseconds.
     * Must be between 100 and Integer.MAX_VALUE;
     */
    protected int period;
    
    /**
     * The number of repeats (e.g. occurrence AFTER the initial occurrence).
     * Must be between 0 and Integer.MAX_NUMBER;
     */
    protected int repeats;
    
    /**
     * The number of repeats to go.
     */
    protected int toGo;
    
    /**
     * Indicates whether or not this sound is user defined.
     */
    protected boolean userSound;
    
    /**
     * The period timer.
     */
    protected Timer timer;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constants">
    /**
     * Not truly a constant, but treated as one.
     * Stores the location of the sound to play.
     */
    public String SOUNDPATH;
    //</editor-fold>
    
    public Sound(final int p, final String r, final String sp, final boolean u)
    {
        setPeriod(p);
        setRepeats(r);
        this.toGo = this.repeats;
        this.userSound = u;
        this.timer = new Timer(period, new SoundListener());
        SOUNDPATH = sp;
    }
    
    /**
     * Initiates the sound.
     */
    public void play() {
        try {
            Playable playable = new Playable(SOUNDPATH, userSound);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
            Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
        }
        timer.setRepeats(true);
        timer.start();
    }
    
    /**
     * Stop the sound.
     */
    public void stop() {
        toGo = repeats;
        timer.stop();
    }
    
    private void setPeriod(final int p) {
        if (100 <= p && p <= Integer.MAX_VALUE) {
            this.period = p;
        } else {
            throw new IllegalArgumentException("Specified period " + p +
                    " is not valid.");
        }
    }
    
    private void setRepeats(final String r) {
        //TODO: Add Group & Range formatting.
        int aux = 0;
        if (r.equals("INF")) {
            aux = Integer.MAX_VALUE;
        } else {
            aux = Integer.parseInt(r);
        }
        if (0 <= aux && aux <= Integer.MAX_VALUE) {
            this.repeats = aux;
        } else {
            throw new IllegalArgumentException("Specified repeats " + r +
                    " is not valid.");
        }
    }
    
    private class SoundListener implements ActionListener {    
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (toGo > 0) {
                    toGo--;
                    Playable playable = new Playable(SOUNDPATH, userSound);
                } else {
                    toGo = repeats;
                    timer.stop();
                }
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                Logger.getLogger(SoundListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
    }
}
