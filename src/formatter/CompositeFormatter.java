/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formatter;

import generated.Group;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author s124392
 */
public class CompositeFormatter {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    /**
     * The GroupFormatter.
     */
    private GroupFormatter groupFormatter;
    
    /**
     * The RangeFormatter.
     */
    private RangeFormatter rangeFormatter;
    //</editor-fold>
    
    public CompositeFormatter(final List<Group> l) {
        this.groupFormatter = new GroupFormatter(l);
        this.rangeFormatter = new RangeFormatter();
    }
    
    public String generate(String s) {
        s = groupFormatter.generate(s);
        s = rangeFormatter.generate(s);
        return s;
    }
    
    public List<String> generateAll(String s) {
        List<String> aux1 = new ArrayList<String>();
        List<String> aux2 = groupFormatter.generateAll(s);
        for (String temp1 : aux2) {
            List<String> aux3 = rangeFormatter.generateAll(temp1);
            for (String temp2 : aux3) {
                aux1.add(temp2);
            }
        }
        return aux1;
    }
}
