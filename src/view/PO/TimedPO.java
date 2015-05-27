/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.PO;

import controller.Controller;
import formatter.RangeFormatter;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.Timer;

/**
 *
 * @author s124392
 */
public abstract class TimedPO extends ProceedOption {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    protected String delay;
    protected int intDelay;
    protected int iterations;
    protected int counter;
    protected Timer timer;
    //</editor-fold>

    /**
     * Constructor.
     * @param f has field continuation.
     * @param px pivot x-displacement.
     * @param py pivot y-displacement.
     * @param a activity reference.
     * @param p probabilities.
     * @param d delay in seconds.
     */
    public TimedPO(final Controller c, final boolean f, final double px,
            final double py, final String fn, final ArrayList<String> a,
            final ArrayList<Double> p, final String d) {
        super(c, f, px, py, fn, a, p);
        this.delay = d;
        formatDelay();
        timer = new Timer(10, new TimerListener(this));
        timer.setRepeats(true);
        this.setMaximumSize(new Dimension(150, 170));
    }
    
    @Override
    public final void formatDelay() {
        RangeFormatter rf = new RangeFormatter();
        String r = rf.generate(delay);
        this.intDelay = Integer.parseInt(r);
        this.iterations = (100 * intDelay); // one iteration takes 0.01 seconds.;
        this.counter = this.iterations;
    }

    @Override
    public abstract void draw(float alignement);
    
    @Override
    public void stop() {
        this.timer.stop();
        this.counter = this.iterations;
    }
    
    /**
     * Refresh the drawn component.
     */
    public abstract void refresh();

    //<editor-fold defaultstate="collapsed" desc="Accessor Methods">
        //<editor-fold defaultstate="collapsed" desc="Get Methods">
            /**
             * @return the delay
             */
            public String getDelay() {
                return delay;
            }
            
            /**
             * @return the intDelay
             */
            public int getIntDelay() {
                return intDelay;
            }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Set Methods">
            /**
             * @param delay the delay to set
             */
            public void setDelay(String delay) {
                this.delay = delay;
                formatDelay();
            }
        //</editor-fold>
    //</editor-fold>
}
