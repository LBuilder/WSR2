/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author s124392
 */
public class RangeFormatter implements Formatter {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    
    //</editor-fold>
    /**
     * Constructor.
     */
    public RangeFormatter() { }

    @Override
    public String getInstance(String s) {
        int lowerBound = getLowerBound(s);
        int upperBound = getUpperBound(s);
        Random random = new Random();
        double aux = random.nextDouble();
        aux = ((aux * (upperBound - lowerBound)) + lowerBound);
        return (String.valueOf(Math.round(aux)));
    }

    @Override
    public List<String> getAllInstances(String s) {
        ArrayList<String> aux = new ArrayList<String>();
        int lowerBound = getLowerBound(s);
        int upperBound = getUpperBound(s);
        for (int i = lowerBound; i <= upperBound; i++) {
            aux.add(String.valueOf(i));
        }
        return aux;
    }

    @Override
    public String generate(String s) {
        if (hasNextRange(s)) {
            String range = getNextRange(s);
            String instance = getInstance(range);
            range = "\\" + range;
            s = s.replaceFirst(range, instance);
            s = generate(s);
            return s;
        } else {
            return s;
        }
    }

    @Override
    public List<String> generateAll(String s) {
        List<String> aux = new ArrayList<String>();
        return recursivelyGenerateAll(aux, s);
    }
    
    private List<String> recursivelyGenerateAll(List<String> aux, String s) {
        if (hasNextRange(s)) {
            String range = getNextRange(s);
            int lowerbound = getLowerBound(range);
            int upperbound = getUpperBound(range);
            range = "\\" + range;
            for (int i = lowerbound; i <= upperbound; i++) {
                String temp = s.replaceFirst(range, String.valueOf(i));
                aux = recursivelyGenerateAll(aux, temp);
            }
        } else {
            aux.add(s);
        }
        return aux;
    }
    
    private int getLowerBound(String s) {
        int dash = getDash(s);
        return Integer.parseInt(s.substring(1, dash));
    }
    
    private int getUpperBound(String s) {
        int dash = getDash(s);
        return Integer.parseInt(s.substring(dash + 1, s.length() - 1));
    }
    
    private int getDash(String s) {
        int dash = 0;
        String aux = s.substring(dash, dash + 1);
        while (!(aux.equals("-"))) {
            dash++;
            aux = s.substring(dash, dash + 1);
        }
        return dash;
    }
    
    public String getNextRange(String s) {
        int start = 0;
        int end = 0;
        int limit = s.length();
        String c = s.substring(start, start + 1);
        while (!(c.equals("[")) && start < limit) {
            start++;
            c = s.substring(start, start + 1);
        }
        end = start;
        while (!(c.equals("]")) && end < limit) {
            end++;
            c = s.substring(end, end + 1);
        }
        return s.substring(start, end + 1);
    }
    
    private boolean hasNextRange(String s) {
        return (s.contains("[") && s.contains("-") && s.contains("]"));
    }
}
