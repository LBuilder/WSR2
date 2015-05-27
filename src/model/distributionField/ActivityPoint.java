/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model.distributionField;

import java.util.ArrayList;
import java.util.Random;
import model.Activity;

/**
 *
 * @author s124392
 */
public class ActivityPoint extends Point {
    private ArrayList<String> activities;
    
    public ActivityPoint(final double x, final double y, ArrayList<String> a) {
        super(x, y);
        this.activities = a;
    }
    
    /**
     * Calculate the Euclidean distance between this- and parameter point p.
     * @param p the point to calculate distance to.
     * @return double
     */
    public double getDistance(final Point p) {
        return Math.sqrt(Math.pow((this.x - p.getX()), 2)
                + Math.pow((this.y - p.getY()), 2));
    }
    
    /**
     * Get appropriate activity from the list.
     * @return Activity
     */
    public String getActivity() {
        if (this.getActivities().size() == 1) {
            return this.getActivities().get(0);
        } else {
            return this.getActivities().get(randIndex(0, this.getActivities().size()));
        }
    }
    
     /**
     * Returns a pseudo-random number between min and max, exclusive.
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, exclusive.
     */
    private static int randIndex(final int min, final int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt(max - min) + min;
        return randomNum;
    }

    /**
     * @return the activities
     */
    public ArrayList<String> getActivities() {
        return activities;
    }
}
