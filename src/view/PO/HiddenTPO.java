/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.PO;

import controller.Controller;
import controller.WSR2;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import model.Activity;

/**
 *
 * @author s124392
 */
public class HiddenTPO extends TimedPO {

    /**
     * Constructor.
     * @param f has distribution field continuation.
     * @param px pivot x-displacement.
     * @param py pivot y-displacement.
     * @param a activity reference.
     * @param d delay in seconds.
     */
    public HiddenTPO(final Controller c, final boolean f, final double px,
            final double py, final String fn, final ArrayList<String> a,
            final ArrayList<Double> p, final String d) {
        super(c, f, px, py, fn, a, p, d);
    }

    @Override
    public void draw(float alignment) { 
        this.setMaximumSize(new Dimension(0, 0));
        this.setAlignmentX(alignment);
        timer.restart();
    }

    @Override
    public void refresh() { }
}
