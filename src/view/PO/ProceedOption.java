/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.PO;

import controller.Controller;
import controller.WSR2;
import java.util.ArrayList;
import javax.swing.JPanel;
import model.Activity;

/**
 *
 * @author s124392
 */
public abstract class ProceedOption extends JPanel {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    protected Controller controller;
    protected boolean field;
    protected double px;
    protected double py;
    private String setField;
    protected ArrayList<String> activityReferences;
    private ArrayList<Activity> activities;
    private ArrayList<Double> probabilities;
    //</editor-fold>
    
    /**
     * Constructor.
     * @param f has distribution field continuation.
     * @param px pivot x-displacement.
     * @param py pivot y-displacement.
     * @param a activityReferences.
     * @param p probabilities.
     */
    public ProceedOption(final Controller c, final boolean f, final double px,
            final double py, final String fn, final ArrayList<String> a,
            final ArrayList<Double> p) {
        this.controller = c;
        this.field = f;
        this.px = px;
        this.py = py;
        this.setField = fn;
        this.activityReferences = a;
        this.probabilities = p;
        this.activities = new ArrayList<Activity>();
    }
    
    public void setController(Controller c) {
        this.controller = c;
    }
    
    /**
     * Format the delay for possible range.
     */
    public abstract void formatDelay();
    
    /**
     * Draw the option.
     */
    public abstract void draw(float alignment);
    
    /**
     * Stop timer and reset.
     */
    public abstract void stop();
    
    /**
     * Fix references from String- to Activity objects.
     * @param b the Activity list to chose from.
     */
    public final void fixReferences(final ArrayList<Activity> b) {
        for (int i = 0; i < this.activityReferences.size(); i++) {
            String x = this.activityReferences.get(i);
            Activity y = b.get(i);
            if (x.equals(y.getName())) {
                this.getActivities().set(i, y);
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="Accessor Methods">
        //<editor-fold defaultstate="collapsed" desc="Get Methods">
            /**
             * @return the field
             */
            public boolean isField() {
                return field;
            }

            /**
             * @return the px
             */
            public double getPx() {
                return px;
            }

            /**
             * @return the py
             */
            public double getPy() {
                return py;
            }

            /**
             * @return the activity
             */
            public ArrayList<String> getActivityReferences() {
                return activityReferences;
            }
            
            /**
             * @return the activities
             */
            public ArrayList<Activity> getActivities() {
                return activities;
            }

            /**
             * @return the probabilities
             */
            public ArrayList<Double> getProbabilities() {
                return probabilities;
            }
            
            /**
             * @return the setField
             */
            public String getSetField() {
                return setField;
            }
        //</editor-fold>
    //</editor-fold>
}
