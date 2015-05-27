/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formatter;

import generated.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author s124392
 */
public class GroupFormatter implements Formatter {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private final List<Group> groups;
    //</editor-fold>

    /**
     * Constructor.
     * @param l the list of groups to work with.
     */
    public GroupFormatter(final List<Group> l) {
        this.groups = l;
    }
    
    @Override
    /**
     * Gets a pseudo-random instance of the group specified in {@code s}.
     * Warning: {@code s} MUST be of shape {@code \{name\}}.
     */
    public String getInstance(String s) {
        Group group = getGroupByName(s);
        Random random = new Random();
        int index = random.nextInt(group.getValues().size());
        return group.getValues().get(index);
    }

    @Override
    /**
     * Gets all possible instances of the group specified in {@code s}.
     * Warning: {@code s} MUST be of shape {@code \{name\}}.
     */
    public List<String> getAllInstances(String s) {
        Group group = getGroupByName(s);
        return group.getValues();
    }

    @Override
    /**
     * Generates one instance of {@code s} in which all groups have been
     * resolved.
     */
    public String generate(String s) {
        if (hasNextGroup(s)) {
            String group = getNextGroup(s);
            String instance = getInstance(group);
            instance = instance.replace("\\", "/");
            group = "\\" + group;
            s = s.replaceFirst(group, instance);
            s = generate(s);
            return s;
        } else {
            return s;
        }
    }

    @Override
    /**
     * Generates all possibles instances of {@code s} in which all groups have
     * been resolved.
     */
    public List<String> generateAll(String s) {
        List<String> aux = new ArrayList<String>();
        return recursivelyGenerateAll(aux, s);
    }
    
    /**
     * The recursive implementation of {@code generateAll(String s)}.
     * @param aux the auxiliary list to keep track of results so far.
     * @param s the string to generate a usable instance for.
     * @return {@code List<String>} of all possible instances.
     */
    private List<String> recursivelyGenerateAll(List<String> aux, String s) {
        if (hasNextGroup(s)) {
            String name = getNextGroup(s);
            Group group = getGroupByName(name);
            name = "\\" + name;
            for (String value : group.getValues()) {
                value = value.replace("\\", "/");
                String temp = s.replaceFirst(name, value);
                aux = recursivelyGenerateAll(aux, temp);
            }
        } else {
            aux.add(s);
        }
        return aux;
    }
    
    /**
     * Gets a {@code Group} object from {@code this.groups} with name
     * {@code name}.
     * @param name the name of the group you are looking for.
     * @return a group.
     */
    private Group getGroupByName(String name) {
        name = name.substring(1, name.length() - 1);
        Group group = null;
        for (Group g : groups) {
            if (g.getName().equals(name)) {
                group = g;
                break;
            }
        }
        return group;
    }
    
    /**
     * Gets the next word in {@code s} conforming to the shape {@code \{name\}}.
     */
    public String getNextGroup(String s) {
        int start = 0;
        int end = 0;
        int limit = s.length();
        String c = s.substring(start, start + 1);
        while (!(c.equals("{")) && start < limit) {
            start++;
            c = s.substring(start, start + 1);
        }
        end = start;
        while (!(c.equals("}")) && end < limit) {
            end++;
            c = s.substring(end, end + 1);
        }
        return s.substring(start, end + 1);
    }
    
    /**
     * A rough indicator of the presence of another group in {@code s}.
     * Rough here means that if {@code hasNextGroup() == false} then {@code s}
     * does certainly NOT contain a VALID group. However, if {@code
     * hasNextGroup() == true} then we CANNOT be CERTAIN that {@code s} still
     * contains a VALID group.
     * @param s the {@code String} to check for a group.
     * @return boolean indicator.
     */
    private boolean hasNextGroup(String s) {
        return (s.contains("{") && s.contains("}"));
    }
    
}
