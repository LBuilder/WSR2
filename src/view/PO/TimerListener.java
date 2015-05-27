/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.PO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author s124392
 */
public class TimerListener implements ActionListener {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private TimedPO timedPO;
    //</editor-fold>
    
    /**
     * Constructor.
     * @param p the proceed option to listen to.
     */
    public TimerListener(final TimedPO t) {
        this.timedPO = t;
    }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (this.timedPO.counter > 0) {
                this.timedPO.counter -= 2;
                this.timedPO.refresh();
            } else {
                this.timedPO.timer.stop();
                this.timedPO.counter = this.timedPO.iterations; // reset.
                this.timedPO.controller.proceed(this.timedPO);
            }
        }
    }
