/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sound;

import com.sun.media.jfxmedia.MediaManager;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An actual playable object, producing sound.
 * @author s124392
 */
public class Playable {
    
    /**
     * Constructor.
     */
    public Playable(String path, boolean userSound) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream stream;
        if (userSound) {
           stream = AudioSystem.getAudioInputStream(new File(path));
        } else {
           stream = AudioSystem.getAudioInputStream(Playable.class.getResource(path)); 
        }
        Clip clip = AudioSystem.getClip();
        clip.open(stream);
        clip.start();
    }

}
