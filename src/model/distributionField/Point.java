/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.distributionField;

/**
 *
 * @author s124392
 */
public class Point {
    protected double x;
    protected double y;
    
    /**
     * Constructor.
     * @param x
     * @param y 
     */
    public Point(double x, double y) {
        if (checkRange(x, y)) {
            this.x = x;
            this.y = y;
        }
    }
    
    /**
     * Checks whether or not x and y are in appropriate range.
     * @param x the x value to check
     * @param y the y value to check
     * @return boolean
     */
    private boolean checkRange(final double x, final double y) {
        if ((0 <= x && x <= (1 / Math.sqrt(2)))
                && (0 <= y && y <= (1 / Math.sqrt(2)))) {
            return true;
        } else {
            throw new IllegalArgumentException("Ill-defined point.");
        }
    }

    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }
}
