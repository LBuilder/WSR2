/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Sound.Sound;
import controller.Listeners.CloseListener;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import model.Activity;
import model.distributionField.ActivityPoint;
import model.distributionField.DistributionField;
import view.MainView;
import view.PO.ProceedOption;

/**
 *
 * @author s124392
 */
public class MainViewController {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private MainView view;
    private Activity currentActivity;
    private DistributionField currentField;
    private ImageIcon currentImage;
    private String currentText;
    private Sound currentSound;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constants">
    /**
     * Specifies the full screen size in pixels.
     */
    private static final Dimension fullSize = new Dimension(1920, 1080);
    
    /**
     * Specifies the small screen size in pixels.
     */
    private static final Dimension smallSize = new Dimension(800, 450);
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Accessor Methods">
    //<editor-fold defaultstate="collapsed" desc="Get Methods">
    /**
     * @return the currentActivity
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }
    
    /**
     * @return the currentField
     */
    public DistributionField getCurrentField() {
        return currentField;
    }
    
    /**
     * @return the currentSound
     */
    public Sound getCurrentSound() {
        return currentSound;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Set Methods">
    /**
     * @param currentActivity the currentActivity to set
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
    
    /**
     * @param currentField the currentField to set
     */
    public void setCurrentField(DistributionField currentField) {
        this.currentField = currentField;
    }
    
    /**
     * @param currentImage the currentImage to set
     */
    public void setCurrentImage(ImageIcon currentImage) {
        this.currentImage = currentImage;
    }
    
    /**
     * @param currentText the currentText to set
     */
    public void setCurrentText(String currentText) {
        this.currentText = currentText;
    }
    
    /**
     * @param currentSound the currentSound to set
     */
    public void setCurrentSound(Sound currentSound) {
        this.currentSound = currentSound;
    }
    //</editor-fold>
    //</editor-fold>
    
    /**
     * Constructor.
     */
    public MainViewController() {
        
    }
    
    public void show() {
        if (this.view == null) {
            view = new MainView(this);
        }
        view.addCloseListener(new CloseListener(this));
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setVisible(true);
        view.centerize();
    }
    
    public void drawActivity() { 
        for (ProceedOption p : currentActivity.getProceedOptions()) {
            if (p != null) {
               p.formatDelay(); 
            }
        }
        view.drawActivity(scaleImage(currentImage), currentText,
                currentActivity.getProceedOptions());
        Sound sound = currentActivity.getSound();
        if (sound != null) {
            currentSound = sound;
            currentSound.play();
        } 
    }
    
    public void close() {
        view.setVisible(false);
        System.exit(0);
    }
    
    public void setCursor(int cursor) {
        view.setCursor(Cursor.getPredefinedCursor(cursor));
    }
    
    /**
     * Stops all the timers of ProceedOptions of {@code currentActivity}.
     */
    public void stopProceedOptions() {
        for (ProceedOption p : currentActivity.getProceedOptions()) {
            if (p != null) {
               p.stop(); 
            }
        }
    }
    
    /**
     * Stops all sounds.
     */
    public void stopSounds() {
        if (currentSound != null) {
           currentSound.stop(); 
        }  
    }
    
    /**
     * Updates {@code currentField} based on parameterized {@code ProceedOption
     * p} and {@code List<DistributionField> distributionFields}.
     * @param p the ProceedOption from which to update.
     * @param distributionFields the fields to pick from.
     */
    public void updateCurrentField(ProceedOption p, 
            List<DistributionField> distributionFields) {
        if (!p.getSetField().equals("0")) {
            String fieldName = p.getSetField();
            for (DistributionField f : distributionFields) {
                if (f.getName().equals(fieldName)) {
                    currentField = f;
                }
            }
        }
    }
    
    /**
     * Updates {@code currentActivity} based on parameterized {@code
     * ProceedOption p} and {@code List<Activity> activities}.
     * @param p the ProceedOption from which to update.
     * @param activities the fields to pick from.
     */
    public void updateCurrentActivity(ProceedOption p, 
            List<Activity> activities) {
        Activity n = null;
        if (p.isField()) {
            n = getActivityByName(currentField.getActivity(), activities);
        } else {
            n = getActivityFromLists(p.getActivityReferences(),
                    p.getProbabilities(), activities);
        }
        if (n != null) {
            this.currentActivity = n;
        } else {
            throw new IllegalArgumentException("Invalid proceed option, "
                    + "refers to non-existant activity!");
        }
    }
    
    /**
     * Returns a scaled instance of {@code image} with respect to {@code view}
     * 's {@code imagePanel}.
     * @param image the image to scale.
     * @return 
     */
    private ImageIcon scaleImage(ImageIcon image) {
        Dimension size = view.imageLabelSize.getSize();
        int labelWidth = (int) size.getWidth();
        int labelHeight = (int) size.getHeight();
        int imageWidth = image.getIconWidth();
        int imageHeight = image.getIconHeight();
        BufferedImage bufferedImage = toBufferedImage(image);
        double imageRatio = (((double) imageWidth) / ((double) imageHeight));
        int newImageWidth = (int) (imageRatio * labelHeight);
        Image scaledInstance = bufferedImage.getScaledInstance(newImageWidth,
                labelHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledInstance);
    }
    
    private BufferedImage toBufferedImage(ImageIcon icon) {
        BufferedImage bi = new BufferedImage(
        icon.getIconWidth(),
        icon.getIconHeight(),
        BufferedImage.TYPE_INT_RGB);
        Graphics g = bi.createGraphics();
        // paint the Icon to the BufferedImage.
        icon.paintIcon(null, g, 0,0);
        g.dispose();
        return bi;
    }
    
    private Activity getActivityFromLists(ArrayList<String> a,
            ArrayList<Double> p, List<Activity> activities) {
        String s = null;
        Random rand = new Random();
        double x = rand.nextDouble();
        double lb = 0.0;
        double ub = 0.0;
        for (int i = 0; i < p.size(); i++) {
            double l = p.get(i);
            ub += l;
            if (lb <= x && x <= ub) {
                s = a.get(i);
                break;
            }
            lb = ub;
        }
        return getActivityByName(s, activities);
    }
   
    private Activity getActivityByName(String n, List<Activity> activities) {
        Activity res = null;
        for (Activity a : activities) {
            if (a.getName().equals(n)) {
                res = a;
                break;
            }
        }
        return res;
    }
}
