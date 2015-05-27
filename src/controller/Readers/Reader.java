/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.Readers;

import formatter.GroupFormatter;
import formatter.RangeFormatter;
import generated.ObjectFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author s124392
 */
public abstract class Reader {
    protected JAXBContext jaxbContext;
    protected Unmarshaller unmarshaller;
    protected Source source;
    protected GroupFormatter groupFormatter;
    protected RangeFormatter rangeFormatter;
    
    public Reader() {
        this.rangeFormatter = new RangeFormatter();
    }
    
    protected void read(String f) throws JAXBException {
        // Create JAXBContext instance.
        jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        
        // Use JAXBContext instance to create Unmarshaller.
        unmarshaller = jaxbContext.createUnmarshaller();
        
        // Use the Unmarshaller to unmarshal the XML document to get an
        // instance of JAXBElement.
        source = new StreamSource(f);
    }
}
