/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import formatter.CompositeFormatter;
import generated.Group;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.ImageIcon;
import model.Activity;
import model.distributionField.DistributionField;
import view.PO.ProceedOption;

/**
 *
 * @author s124392
 */
public class Controller {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private StartupController startupController;
    private MainViewController mainViewController;
    private List<Group> groups;
    private List<DistributionField> fields;
    private List<Activity> activities;
    private String imageDirectory;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constants">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Accessor Methods">
    //<editor-fold defaultstate="collapsed" desc="Get Methods">
    /**
     * @return the groups
     */
    public List<Group> getGroups() {
        return groups;
    }
    
    /**
     * @return the fields
     */
    public List<DistributionField> getFields() {
        return fields;
    }
    
    /**
     * @return the activities
     */
    public List<Activity> getActivities() {
        return activities;
    }
    
    /**
     * @return the startupController
     */
    public StartupController getStartupController() {
        return startupController;
    }
    
    /**
     * @return the imageDirectory
     */
    public String getImageDirectory() {
        return imageDirectory;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Set Methods">   
    /**
     * @param groups the groups to set
     */
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
    
    /**
     * @param fields the fields to set
     */
    public void setFields(List<DistributionField> fields) {
        this.fields = fields;
    }
    
    /**
     * @param activities the activities to set
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
    
    /**
     * @param startupController the startupController to set
     */
    public void setStartupController(StartupController startupController) {
        this.startupController = startupController;
    }
    
    /**
     * @param imageDirectory the imageDirectory to set
     */
    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
    //</editor-fold>
    //</editor-fold>
    
    public Controller() {
        startupController = new StartupController(this);
        mainViewController = new MainViewController();
    }
    
    public void showStartup() {
        getStartupController().show();
        getStartupController().instanciate();
    }
    
    public void showMain() {
        mainViewController.show();
        mainViewController.setCurrentActivity(activities.get(0));
        mainViewController.setCurrentImage(getImage());
        mainViewController.setCurrentText(getText());
        mainViewController.drawActivity();
    }
    
    public void proceed(ProceedOption p) {
        mainViewController.stopSounds();
        mainViewController.stopProceedOptions();
        mainViewController.updateCurrentField(p, fields);
        mainViewController.updateCurrentActivity(p, activities);
        mainViewController.setCurrentImage(getImage());
        mainViewController.setCurrentText(getText());
        mainViewController.drawActivity();
    }
    
    /**
     * Finds an appropriate {@code ImageIcon} for the current activity.
     * @return 
     */
    private ImageIcon getImage() {
        Activity a = mainViewController.getCurrentActivity();
        CompositeFormatter cf = new CompositeFormatter(groups);
        String imagePath = a.getImagePath();
        imagePath = cf.generate(imagePath);
        ImageIcon image = new ImageIcon(imageDirectory + "\\" + imagePath);
        return image;
    }
    
    private String getText() {
        Activity a = mainViewController.getCurrentActivity();
        CompositeFormatter cf = new CompositeFormatter(groups);
        String text = a.getText();
        text = cf.generate(text);
        return text;
    }
}
