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
public class Pivot extends Point {
    
    /**
     * Constructor.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public Pivot(double x, double y) {
        super(x, y);
    }
    
    public void update(double x, double y) {
        if (checkUpdateRange(x, y)) {
            this.x = Math.min(Math.max(0.0, this.x += x), (1 / Math.sqrt(2)));
            this.y = Math.min(Math.max(0.0, this.y += y), (1 / Math.sqrt(2)));
        }
    }
    
    /**
     * Checks whether update values are in appropriate range.
     * @param x
     * @param y
     * @return 
     */
    private boolean checkUpdateRange(double x, double y) {
        if (((-1 / Math.sqrt(2)) <= x && x <= (1 / Math.sqrt(2))) 
                && ((-1 / Math.sqrt(2)) <= y && y <= (1 / Math.sqrt(2)))) {
            return true;
        } else {
            throw new IllegalArgumentException("Ill-defined pivot update!");
        }
    }
}
