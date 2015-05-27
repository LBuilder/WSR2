/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.PO;

import controller.Controller;
import controller.WSR2;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import model.Activity;

/**
 *
 * @author s124392
 */
public class ManualPO extends ProceedOption {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private String name;
    //</editor-fold>

    /**
     * Constructor.
     * @param f has distribution field continuation.
     * @param px pivot x-displacement.
     * @param py pivot y-displacement.
     * @param a activity pointer.
     * @param p probabilities.
     * @param n name of the button.
     */
    public ManualPO(final Controller c, final boolean f, final double px,
            final double py, final String fn, final ArrayList<String> a,
            final ArrayList<Double> p, final String n) {
        super(c, f, px, py, fn, a, p);
        this.name = n;
    }

    @Override
    public void draw(float alignment) {
        this.removeAll();
        this.setMaximumSize(new Dimension(150, 32));
        this.setBackground(Color.BLACK);
        this.setAlignmentX(alignment);
        JButton button = new JButton(this.name);
        button.addActionListener(new ButtonListener(this));
        this.add(button); 
    }

    @Override
    public void stop() {
         // nothing happens.
    }
    
    @Override
    public void formatDelay(){ }
    
    class ButtonListener implements ActionListener {
        //<editor-fold defaultstate="collapsed" desc="Instance Variables">
        private ManualPO manualPO;
        //</editor-fold>
        
        ButtonListener(ManualPO m) {
            this.manualPO = m;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            this.manualPO.controller.proceed(manualPO);
        }
    }
    
    public String getName() {
        return this.name;
    }
}
