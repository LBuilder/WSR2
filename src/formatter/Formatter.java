/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formatter;

import java.util.List;

/**
 *
 * @author s124392
 */
public interface Formatter {
    
    /**
     * Returns a pseudo-random formatted instance of {@code String s}
     * @param s the String to get an instance for.
     * @return formatted instance of {@code String s}.
     */
    public String getInstance(String s);
    
    /**
     * Returns a list of all possible formatting instances of {@code String s}.
     * @param s the String to get all possibilities for.
     * @return list of formatting possibilities for {@code String s}.
     */
    public List<String> getAllInstances(String s);
    
    /**
     * Generate a formatted {@code String} from {@code String} in which all
     * occurrences are resolved.
     * @param s the String to format.
     * @return completely formatted {@code String}.
     */
    public String generate(String s);
    
    /**
     * Generate a list of all possible formatting options for {@code String s}
     * in which all occurrences are resolved.
     * @param s the String to format.
     * @return list of all possible completely formatted {@code String}'s.
     */
    public List<String> generateAll(String s);
}
