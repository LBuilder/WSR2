/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.Readers;

import generated.Group;
import generated.Groups;
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
import org.xml.sax.SAXException;

/**
 *
 * @author s124392
 */
public class GroupReader extends Reader {
    
    public GroupReader() {
        super();
    }
    
    /**
     * Get the list of Groups from file {@code f}.
     * @param f the file to read.
     * @return List of {@code Group}.
     * @throws JAXBException 
     */
    public List<Group> getGroups(String f) throws JAXBException {
        read(f);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	Schema schema; 
        URL url = getClass().getResource("/schemas/groupSchema.xsd");
        try {
            schema = schemaFactory.newSchema(url);
            unmarshaller.setSchema(schema);
            JAXBElement<Groups> unmarshalledObject = unmarshaller.unmarshal(source, Groups.class);
            return unmarshalledObject.getValue().getGroup();
        } catch (SAXException ex) {
            Logger.getLogger(GroupReader.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    /**
     * Verify parameterized groups based on invariants.
     * Invariant 1: Every group has at least one value.
     * Invariant 2: Every group name is unique.
     * @param l the list of groups to verify.
     * @return List of Strings which should be empty, but may contain
     * error messages to be sent to the boot log by controller.
     */
    public List<String> verifyGroups(List<Group> l) {
        ArrayList<String> r = new ArrayList<String>();
        for (int i = 0; i < l.size(); i++) {
            Group g = l.get(i);
            if (g.getValues().isEmpty()) { // Test Invariant 1.
                r.add("Group " + g.getName() + " has no value.");
            }
            for (int j = i + 1; j < l.size(); j++) {
                Group q = l.get(j);
                if (g.getName().equals(q.getName())) { // Test Invariant 2.
                    r.add("Group #" + (i + 1) + " and " + (j + 1)
                            + " have the same name.");
                }
            }
        }
        return r;
    }   
}
