/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Random;

/**
 * A range specified by a lower- and upper bound such that 
 * lower bound <= upper bound holds.
 * @author s124392
 */
public class Range {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    /**
     * The lower bound of the range.
     */
    private double lowerbound;
    
    /**
     * The upper bound of the range.
     */
    private double upperbound;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Accessor Methods">
    //<editor-fold defaultstate="collapsed" desc="Get Methods">
    /**
     * @return the lowerbound
     */
    public double getLowerbound() {
        return lowerbound;
    }

    /**
     * @return the upperbound
     */
    public double getUpperbound() {
        return upperbound;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Set Methods">
    /**
     * @param lowerbound the lowerbound to set
     */
    public void setLowerbound(double lowerbound) {
        if (isProperRange(lowerbound, upperbound)) {
            this.lowerbound = lowerbound;
        } else {
            throw new IllegalArgumentException("Invalid setLowerbound. "
                    + "Lowerbound exceeds current upperbound.");
        }  
    }

    /**
     * @param upperbound the upperbound to set
     */
    public void setUpperbound(double upperbound) {
        if (isProperRange(lowerbound, upperbound)) {
            this.upperbound = upperbound;
        } else {
            throw new IllegalArgumentException("Invalid setUpperbound."
                    + "Upperbound is smaller than current lowerbound.");
        }
    }
    //</editor-fold>
    //</editor-fold>
    
    /**
     * Constructor.
     * @param l the lower bound.
     * @param u the upper bound.
     */
    public Range(final double l, final double u) {
        if (isProperRange(l, u)) {
            this.lowerbound = l;
            this.upperbound = u;
        } else {
            throw new IllegalArgumentException("Invalid Range constructed."
                    + "Lowerbound exceeds upperbound.");
        }
    }
    
    /**
     * Returns a pseudo-random {@code double} from within this range.
     * @return double.
     */
    public double getDoubleInRange() {
        Random random = new Random();
        double result = random.nextDouble();
        return ((result * (upperbound - lowerbound)) + lowerbound);
    }
    
    /**
     * Returns a pseudo-random (@code int} from within this range.
     * @return int.
     */
    public int getIntInRange() {
        return (int) Math.round(getDoubleInRange());
    }
    
    @Override
    public String toString() {
        return ("[" + String.valueOf((int) lowerbound) + "-" +
                String.valueOf((int) upperbound) + "]");
    }
    
    /**
     * Checks whether the lower bound is smaller or equal to the upperbound.
     * If not, a range is not proper.
     * @param l the lower bound.
     * @param u the upper bound.
     * @return {@code l <= u}.
     */
    private boolean isProperRange(double l, double u) {
        return (l <= u);
    }
}
