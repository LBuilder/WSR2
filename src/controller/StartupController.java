/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.Listeners.StartupReloadListener;
import controller.Listeners.StartupStartListener;
import controller.Readers.GroupReader;
import controller.Readers.ActivityReader;
import controller.Readers.FieldReader;
import generated.ActivityT;
import generated.DistributionFieldT;
import generated.Group;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.bind.JAXBException;
import model.distributionField.DistributionField;
import view.StartupView;

/**
 *
 * @author s124392
 */
public class StartupController {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables"> 
    protected Controller controller;
    protected StartupView view;
    private File lastDirectory;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Constants">
        private static final String loadGroupsText = "Load Group Data";
        private static final String loadRangesText = "Load Range Data";
        private static final String loadFieldText = "Load Field Data";
        private static final String loadActivitiesText = "Load Activity Data";
        private static final String loadImageDirectoryText = "Select Image "
                + "Directory";
    //</editor-fold>
        
    public StartupController(Controller c) {
        this.controller = c;
        this.lastDirectory = null;
    }

    public void instanciate() {
        setStartEnabled(false);
        loadGroups();
    }
    
    public void show() {
        if (this.view == null) {
            this.view = new StartupView();
        }
        view.addReloadListener(new StartupReloadListener(this));
        view.addStartListener(new StartupStartListener(this));
        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setVisible(true);
        view.centerize();
    }
    
    public void start() {
        view.setVisible(false);
        controller.showMain();
    }
    
    public void reload() {
        view.clearText();
        instanciate();
    }
    
    private void setStartEnabled(boolean b) {
        view.getStartButton().setEnabled(b);
    }
    
    private void setReloadEnabled(boolean b) {
        view.getReloadButton().setEnabled(b);
    }
    
    private void loadGroups() {
        setReloadEnabled(false);
        JFileChooser fileChooser = new JFileChooser();
        setFileChooserSettings(fileChooser, JFileChooser.FILES_ONLY);
        int option = fileChooser.showDialog(view, loadGroupsText);
        if (option == JFileChooser.APPROVE_OPTION) {
            view.appendLog("> Loading groups...");
            GroupReader gr = new GroupReader();
            try {
                List<Group> groups = gr.getGroups(fileChooser.getSelectedFile().toString());
                lastDirectory = fileChooser.getSelectedFile();
                view.appendLog("Groups loaded.");
                view.appendLog("> Verifying groups...");
                List<String> verification = gr.verifyGroups(groups);
                if (verification.isEmpty()) { // no problems found
                    view.appendLog("Groups verified.");
                    controller.setGroups(groups);
                    loadFields();
                } else {
                    setReloadEnabled(true);
                    for (String s : verification) {
                        view.appendLog(s);
                    }
                }
            } catch (JAXBException ex) {
                Logger.getLogger(StartupController.class.getName()).log(Level.SEVERE, null, ex);
                view.appendLog("Error while loading groups. Please check selected file and restart.");
                view.appendLog(ex.toString());
                setReloadEnabled(true);
            }
        }
        if (option == JFileChooser.CANCEL_OPTION) {
            view.appendLog("Loading cancelled.");
            setReloadEnabled(true);
        }
    }
    
    private void loadFields() {
        JFileChooser fileChooser = new JFileChooser();
        setFileChooserSettings(fileChooser, JFileChooser.FILES_ONLY);
        int option = fileChooser.showDialog(view, loadFieldText);
        if (option == JFileChooser.APPROVE_OPTION) {
            view.appendLog("> Loading Distribution Fields...");
            FieldReader fr = new FieldReader();
            try {
                List<DistributionFieldT> fields = fr.getFields(fileChooser.getSelectedFile().toString());
                lastDirectory = fileChooser.getSelectedFile();
                view.appendLog("Distribution Fields loaded.");
                view.appendLog("> Verifying Distribution Fields...");
                List<String> verification = fr.verifyFields(fields);
                if (verification.isEmpty()) { // No problems found.
                    view.appendLog("Distribution Fields verified.");
                    List<DistributionField> data = fr.toDataStructure(fields);
                    controller.setFields(data);
                    loadActivities(data);
                } else {
                    setReloadEnabled(true);
                    for (String s : verification) {
                        view.appendLog(s);
                    }
                }
            } catch (JAXBException ex) {
                Logger.getLogger(StartupController.class.getName()).log(Level.SEVERE, null, ex);
                view.appendLog("Error while loading fields. Please check selected file and restart.");
                view.appendLog(ex.toString());
                setReloadEnabled(true);
            }
        }
        if (option == JFileChooser.CANCEL_OPTION) {
            view.appendLog("Loading cancelled.");
            setReloadEnabled(true);
        }
    }
    
    private void loadActivities(List<DistributionField> fields) {
        JFileChooser fileChooser = new JFileChooser();
        setFileChooserSettings(fileChooser, JFileChooser.FILES_ONLY);
        int option = fileChooser.showDialog(view, loadActivitiesText);
        if (option == JFileChooser.APPROVE_OPTION) {
            view.appendLog("> Loading Activities...");
            ActivityReader ar = new ActivityReader();
            try {
                List<ActivityT> activities = ar.getActivities(fileChooser.getSelectedFile().toString());
                lastDirectory = fileChooser.getSelectedFile();
                view.appendLog("Activities loaded.");
                view.appendLog("> Verifying activities...");
                List<String> verification = ar.verifyActivities(activities, fields, controller.getGroups());
                if (verification.isEmpty()) {
                    view.appendLog("Activities verified.");
                    controller.setActivities(ar.toDataStructure(activities, controller, controller.getGroups()));
                    view.appendLog("Post-verifying Distribution Fields...");
                    FieldReader fr = new FieldReader();
                    verification.clear();
                    verification = fr.postVerifyFieldReferences(controller.getFields(), activities);
                    if (verification.isEmpty()) {
                        view.appendLog("Distribution Fields post-verification complete.");
                        loadImageDirectory();
                    } else {
                        setReloadEnabled(true);
                        for (String s : verification) {
                            view.appendLog(s);
                        }
                    }
                } else {
                    setReloadEnabled(true);
                    for (String s : verification) {
                        view.appendLog(s);
                    }
                }
            } catch (JAXBException ex) {
                Logger.getLogger(StartupController.class.getName()).log(Level.SEVERE, null, ex);
                view.appendLog("Error while loading fields. Please check selected file and restart.");
                view.appendLog(ex.toString());
                setReloadEnabled(true);
            }
        }
        if (option == JFileChooser.CANCEL_OPTION) {
            view.appendLog("Loading cancelled");
            setReloadEnabled(true);
        }
    }
    
    private void loadImageDirectory() {
        JFileChooser fileChooser = new JFileChooser();
        setFileChooserSettings(fileChooser, JFileChooser.DIRECTORIES_ONLY);
        int option = fileChooser.showDialog(view, loadImageDirectoryText);
        if (option == JFileChooser.APPROVE_OPTION) {
            view.appendLog("> Loading image directory...");
            String path = fileChooser.getSelectedFile().toString();
            if (new File(path).isDirectory()) {
                view.appendLog("Image directory loaded.");
                view.appendLog("> Post-verifying Activities...");
                ActivityReader ar = new ActivityReader();
                List<String> verification = ar.postVerifyActivities(
                        controller.getActivities(), path,
                        controller.getGroups());
                if (verification.isEmpty()) {
                    view.appendLog("Activities post-verification complete.");
                    controller.setImageDirectory(path);
                    setReloadEnabled(true);
                    view.getStartButton().setEnabled(true);
                } else {
                    setReloadEnabled(true);
                    for (String s : verification) {
                        view.appendLog(s);
                    }
                }
            } else {
                setReloadEnabled(true);
                view.appendLog("Invalid directory selected.");
            }
        }
        if (option == JFileChooser.CANCEL_OPTION) {
            view.appendLog("Loading cancelled");
            setReloadEnabled(true);
        }
    }
    
    private void setFileChooserSettings(JFileChooser fileChooser, int mode) {
        if (lastDirectory != null) {
            fileChooser.setCurrentDirectory(lastDirectory);
        }
        fileChooser.setFileSelectionMode(mode);
        if (mode == JFileChooser.FILES_ONLY) {
            FileFilter filter = new FileNameExtensionFilter("XML Files","xml");
            fileChooser.setFileFilter(filter);
        }
    }
}
