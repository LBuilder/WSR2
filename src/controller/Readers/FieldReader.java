/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.Readers;

import generated.ActT;
import generated.ActivityT;
import generated.DistributionFieldT;
import generated.FieldsT;
import generated.PointT;
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
import model.distributionField.ActivityPoint;
import model.distributionField.DistributionField;
import model.distributionField.Point;
import org.xml.sax.SAXException;

/**
 *
 * @author s124392
 */
public class FieldReader extends Reader {
    
    public FieldReader() {
        super();
    }
    
    /**
     * Get the list of DistrubitionFieldT from file {@code f}.
     * @param f the file to read.
     * @return List of {@code DistributionFieldT}.
     * @throws JAXBException 
     */
    public List<DistributionFieldT> getFields(String f) throws JAXBException {
        read(f);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema schema; 
        URL url = getClass().getResource("/schemas/fieldSchema.xsd");
        try {
            schema = schemaFactory.newSchema(url);
            unmarshaller.setSchema(schema);
            JAXBElement<FieldsT> unmarshalledObject = unmarshaller.unmarshal(source, FieldsT.class);
            return unmarshalledObject.getValue().getField();
        } catch (SAXException ex) {
            Logger.getLogger(FieldReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Verify parameterized DistributionFieldTs based on invariants.
     * Invariant 1: Every field name is unique.
     * Invariant 2: Every field has at least one point.
     * Invariant 3: Every point in every field has at least one reference.
     * @param l the list of fields to verify.
     * @return List of Strings which should be empty, but may contain
     * error messages to be sent to the boot log by controller.
     */
    public List<String> verifyFields(List<DistributionFieldT> l) {
        ArrayList<String> r = new ArrayList<String>();
        for (int i = 0; i < l.size(); i++) {
            DistributionFieldT f = l.get(i);
            if (f.getPoint().isEmpty()) { // Test Invariant 2.
                r.add("Field #" + (i + 1) + " has no ActivityPoints.");
            }
            for (int j = 0; j < f.getPoint().size(); j++) {
                PointT p = f.getPoint().get(j);
                if (p.getAct().isEmpty()) { // Test Invariant 3.
                    r.add("Field #" + (i + 1) + "'s ActivityPoint #" + (j + 1) + " has no reference.");
                }
            }
            for (int j = i + 1; j < l.size(); j++) {
                DistributionFieldT g = l.get(j);
                if (f.getName().equals(g.getName())) { // Test Invariant 1.
                    r.add("Field #" + (i + 1) + " and " + (j + 1)
                            + " have the same name.");
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
    public List<DistributionField> toDataStructure(List<DistributionFieldT> l) {
        ArrayList<DistributionField> r = new ArrayList<DistributionField>();
        for (DistributionFieldT f : l) {
            String name = f.getName();
            PointT pvs = f.getPivotStart();
            ArrayList<ActivityPoint> aux = new ArrayList<ActivityPoint>();
            for (PointT p : f.getPoint()) {
                ArrayList<String> aux2 = new ArrayList<String>();
                for (ActT a : p.getAct()) {
                    aux2.add(a.getName());
                }
                ActivityPoint ap = new ActivityPoint(normalize(p.getX()), normalize(p.getY()), aux2);
                aux.add(ap);
            }
            r.add(new DistributionField(name, normalize(pvs.getX()), normalize(pvs.getY()), aux)); 
            }
        return r;
    }
    
    
    
    /**
     * Verify parameterized list of DistributionFieldTs based on parameterized
     * list of ActivityTs and invariants.
     * Invariant 1: Every activity reference for every point for every field
     * exists in the given set of activities.
     * @param l the list of DistributionFieldTs.
     * @param m the list of ActivityTs.
     * @return List of Strings which should be empty, but may contain
     * error messages to be sent to the boot log by controller. 
     */
    public List<String> postVerifyFieldReferences(List<DistributionField> l, List<ActivityT> m) {
        ArrayList<String> r = new ArrayList<String>();
        for (int i = 0; i < l.size(); i++) {
            DistributionField f = l.get(i);
            for (int j = 0; j < f.getPoints().size(); j++) {
                ActivityPoint p = f.getPoints().get(j);
                for (String a : p.getActivities()) {
                    boolean found = false;
                    for (ActivityT b : m) {
                        if (b.getName().equals(a)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) { // Test Invariant 1.
                        r.add("Field #" + (i + 1) + "'s Point #" + (j + 1)
                                + "'s reference to Activity " + a
                                + " is invalid.\nThis activity does not exist"
                                + "in the loaded set of activities.");
                    }
                }
            }
        }
        return r;
    }
    
    private double normalize(double x) {
        return (x / Math.sqrt(2));
    }
}
