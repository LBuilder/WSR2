/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import Sound.Clock;
import Sound.UserSound;
import formatter.CompositeFormatter;
import formatter.GroupFormatter;
import formatter.RangeFormatter;
import generated.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import model.Range;

/**
 *
 * @author s124392
 */
public class WSR2 {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private Controller controller;
    //</editor-fold>
    
    public WSR2() {
        this.controller = new Controller();
        controller.showStartup();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WSR2 wsr = new WSR2();
//        List<Group> groups = new ArrayList<Group>();
//        for (int i = 1; i <= 4; i++) {
//            Group group = new Group();
//            group.setName("Group " + i);
//            for (int j = 1; j <= 4; j++) {
//                String value = "Value " + j;
//                group.getValues().add(value);
//            }
//            groups.add(group);
//        }
//        CompositeFormatter cf = new CompositeFormatter(groups);
//        List<String> options = cf.generateAll("[1-5][1-5]");
//        for (String s : options) {
//            System.out.println(s);
//        }
    }
}
