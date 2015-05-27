/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.Readers;

import Sound.Bell;
import Sound.Clock;
import Sound.Metronome;
import Sound.Sound;
import Sound.UserSound;
import controller.Controller;
import formatter.CompositeFormatter;
import generated.ActivitiesT;
import generated.ActivityT;
import generated.Group;
import generated.OptionT;
import generated.RefT;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import model.Activity;
import model.Range;
import model.distributionField.DistributionField;
import org.xml.sax.SAXException;
import view.PO.HiddenTPO;
import view.PO.ManualPO;
import view.PO.ProceedOption;
import view.PO.VisibleTPO;

/**
 *
 * @author s124392
 */
public class ActivityReader extends Reader {
    //<editor-fold defaultstate="collapsed" desc="Constants">
    /**
     * Specifies the 'no sound' option.
     */
    
    private static final String NONE = "NONE";
    /**
     * Specifies the name of a Bell sound.
     */
    private static final String BELL = "BELL";
    
    /**
     * Specifies the name of a Clock sound.
     */
    private static final String CLOCK = "CLOCK";
    
    /**
     * Specifies the name of a Metronome sound.
     */
    private static final String METRONOME = "METRONOME";
    //</editor-fold>
    
    public ActivityReader() {
        super();
    }
    
    /**
     * Get list of ActivityT from file {@code f}.
     * @param f the file to read.
     * @return List of {@code ActivityT}.
     * @throws JAXBException 
     */
    public List<ActivityT> getActivities(String f) throws JAXBException {
        read(f);
        SchemaFactory schemaFactory =
                SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema schema; 
        URL url = getClass().getResource("/schemas/activitySchema.xsd");
        try {
            schema = schemaFactory.newSchema(url);
            unmarshaller.setSchema(schema);
            JAXBElement<ActivitiesT> unmarshalledObject =
                    unmarshaller.unmarshal(source, ActivitiesT.class);
            return unmarshalledObject.getValue().getActivity();
        } catch (SAXException ex) {
            Logger.getLogger(ActivityReader.class.getName()).log(Level.SEVERE,
                    null, ex);
            return null;
        }
    }
    
    /**
     * Verify parameterized ActivityTs based on invariants.
     * Invariant 1: All activities have a unique name.
     * Invariant 2: There is at least one activity.
     * refers to an existing distribution field.
     * Invariant 3: Every activity has at least one proceed option.
     * @param l the list of activities to verify.
     * @param m the list of distribution fields to test invariant 4 against.
     * @return List of Strings which should be empty, but may contain
     * error messages to be sent to the boot log by controller.
     */
    public List<String> verifyActivities(List<ActivityT> l,
            List<DistributionField> m, List<Group> groups) {
        ArrayList<String> r = new ArrayList<String>();
        if (l.size() == 0) { // Test invariant 2.
            r.add("Not at least one activity is present.");
        }
        for (ActivityT a : l) {
            if (a.getOption().isEmpty()) { // Test Invariant 3.
                r.add("No proceed option present for Activity " + a.getName());
            }
            r.add(verifyProceedOptions(l, m, a, groups));
            for (ActivityT b : l) {
                // Test Invariant 1.
                if (a != b && a.getName().equals(b.getName())) { 
                    r.add("Activity name " + a.getName() + " occurs at least "
                            + "twice.");
                }
            }
        }
        r = cleanVerification(r);
        return r;
    }
    
    /**
     * Post-verifies all Activities with respect to the presence of all image
     * files under {@code path}.
     * @param l the list of activities to verify.
     * @param path the path under which all images should be found.
     * @return 
     */
    public List<String> postVerifyActivities(List<Activity> l, String path,
            List<Group> groups) {
        ArrayList<String> r = new ArrayList<String>();
        File dir = new File(path);
        CompositeFormatter cf = new CompositeFormatter(groups);
        for (Activity a : l) {
            String imagePath = a.getImagePath();
            List<String> ipOptions = cf.generateAll(imagePath);
            for (String ip : ipOptions) {
                File file = new File(path + "\\" + ip);
                if (!(file.exists())) {
                    r.add("Activity " + a.getName() + " refers to non-existant "
                            + "image " + ip);
                }
            }
        }
        return r;
    }
    
    /**
     * Converts a list of type {@code DistributionFieldT} to a list of type
     * {@code DistributionField}.
     * @param l the list to be converted.
     * @return {@code List<DistributionField>}.
     */
    public List<Activity> toDataStructure(List<ActivityT> l, Controller c, List<Group> groups) {
        ArrayList<Activity> r = new ArrayList<Activity>();
        for (ActivityT a : l) {
            String name = a.getName();
            String imagePath = a.getImagePath();
            String text = a.getText();
            String value = a.getSound().getValue();
            int period = a.getSound().getPeriod();
            String repeats = a.getSound().getRepeats();
            Sound sound = createSound(value, period, repeats);
            ProceedOption[] options = new ProceedOption[8];
            int i = 0;
            for (OptionT o : a.getOption()) {
                ProceedOption po = createProceedOption(o, c, groups);
                options[i] = po;
                i++;
            }
            Activity b = new Activity(name, imagePath, text, sound, options);
            r.add(b);
        }
        return r;
    }
    
    /**
     * Verify parameterized ActivityT based on invariants.
     * Invariant 1: For every proceed option of every activity, 
     * if setField != 0 then setField's value is valid.
     * Invariant 2: For every proceed option of every activity, 
     * (buttonName = "" || delay = 0) holds.
     * Invariant 3: For every reference of every proceed option of every
     * activity, the referenced activity exists when isField() == false.
     * Invariant 4: For every proceed option of every activity, the sum of the 
     * probabilities of its references equal 1 when isField() == false.
     * @param l the ActivityT list to verify against.
     * @param m the DistributionField list to verify against.
     * @param a the ActivityT to be verified.
     * @return {@code String} for each failure to comply.
     */
    private String verifyProceedOptions(List<ActivityT> l,
            List<DistributionField> m, ActivityT a, List<Group> groups) {
        for (int i = 0; i < a.getOption().size(); i++) {
            OptionT o = a.getOption().get(i);
            boolean foundField = o.getSetField().equals("0");
            for (DistributionField f : m) {
                if (f.getName().equals(o.getSetField())) {
                    foundField = true;
                    break;
                }
            }
            if (!foundField) { // Test invariant 1.
                return "ProceedOption #" + (i + 1) + " has invalid setField "
                        + "value.";
            }
            String delay = o.getDelay();
            CompositeFormatter cf = new CompositeFormatter(groups);
            delay = cf.generate(delay);
            int d = Integer.parseInt(delay);
            if (!(o.getButtonName().equals("") || d == 0)) { // Test invariant 2.
                return "ProceedOption #" + (i + 1) + "  of " + a.getName() +
                        " has no button name nor "
                        + "delay.";
            }
            double sum = 0.0;
            boolean foundRef = false;
            for (RefT r : o.getReference()) {
                for (ActivityT b : l) {
                    if (b.getName().equals(r.getName())) {
                        foundRef = true;
                    }
                }
                sum += r.getProbability();
                if (!foundRef && !o.isIsField()) { // Test invariant 3.
                    return "Reference to " + r.getName() + " in ProceedOption "
                            + "#" + (i + 1) + " of Activity " + a.getName()
                    + " does not exist as Activity.";
                }
            }
                if (sum != 1.0 && !o.isIsField()) { // Test invariant 4.
                    return "The sum of probabilities of references of "
                            + "ProceedOption #" + (i + 1) + " of activity " +
                            a.getName() + "is not equal " + "to 1.";
                }
        }
        return null;
    }
    
    private ProceedOption createProceedOption(OptionT o, Controller c, List<Group> groups) {
        ArrayList<String> a = new ArrayList<String>();
        ArrayList<Double> p = new ArrayList<Double>();
        for (RefT r : o.getReference()) {
            a.add(r.getName());
            p.add(r.getProbability());
        }
        CompositeFormatter cf = new CompositeFormatter(groups);
        String delay = o.getDelay();
        String aux = cf.generate(delay);
        int d = Integer.parseInt(aux);
        if (d > 0) {
            if (o.isHideDelay()) {
                return new HiddenTPO(c, o.isIsField(), o.getPivotX(),
                        o.getPivotY(), o.getSetField(), a, p, delay);
            } else {
                return new VisibleTPO(c, o.isIsField(), o.getPivotX(),
                        o.getPivotY(), o.getSetField(), a, p, delay,
                        o.isShowValue());
            }
        } else {
            return new ManualPO(c, o.isIsField(), o.getPivotX(), o.getPivotY(),
                    o.getSetField(), a, p, o.getButtonName());
        }
    }
    
    private ArrayList<String> cleanVerification(ArrayList<String> l) {
        ArrayList<String> r = new ArrayList<String>();
        for (String s : l) {
            if (s != null) {
                r.add(s);
            }
        }
        return r;
    }
    
    private Sound createSound(String value, int p, String r) {
        if (value.equals(NONE)) {
            return null;
        }
        if (value.equals(BELL)) {
            return new Bell(p, r);
        }
        if (value.equals(CLOCK)) {
            return new Clock(r);
        }
        if (value.equals(METRONOME)) {
            return new Metronome(p);
        } else {
            return new UserSound(p, r, value);
        }
    }
}
