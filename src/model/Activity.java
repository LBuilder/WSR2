/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import Sound.Sound;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import view.PO.ProceedOption;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author s124392
 */
public class Activity extends JPanel {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private ProceedOption[] proceedOptions;
    private String name, imagePath, text;
    private Sound sound;
    //</editor-fold>
    
    public Activity(final String n, final String i,
            final String t, final Sound s, final ProceedOption[] p) {
        this.name = n;
        this.imagePath = i;
        this.text = t;
        this.sound = s;
        if (validateOptions(p)) {
            this.proceedOptions = p;
        }
    }
    
    private boolean validateOptions(ProceedOption[] p) {
        if (p.length == 8) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid proceed options in"
                    + "activity, length not equal to eight.");
        }
    }

    /**
     * @return the proceedOptions
     */
    public ProceedOption[] getProceedOptions() {
        return proceedOptions;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }
    
    public void setText(String t) {
        this.text = t;
    }
    
    public Sound getSound() {
        return sound;
    }
}
