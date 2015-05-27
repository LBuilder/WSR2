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
public class DistributionField {
    private String name;
    private Pivot pivot;
    private ArrayList<ActivityPoint> points;
    private ArrayList<Double> probabilities;
    /**
     * Constructor.
     * @param x pivot x-coordinate.
     * @param y pivot y-coordinate.
     */
    public DistributionField(final String n, final double x, final double y, ArrayList<ActivityPoint> p) {
        this.pivot = new Pivot(x, y);
        this.name = n;
        this.points = p;
        this.probabilities = new ArrayList<Double>();
    }
    
    private void calculateProbabilities() {
        this.probabilities.clear();
        double sum = 0.0;
        for (ActivityPoint p : this.getPoints()) {
            double pb = 1 - p.getDistance(this.pivot);
            sum += pb;
            this.probabilities.add(pb);
        }
        for (int i = 0; i < this.getPoints().size(); i++) {
            this.probabilities.set(i, (this.probabilities.get(i) / sum));
        }
    }
    
    public String getActivity() {
        calculateProbabilities();
        double lb = 0.0;
        double ub = 0.0;
        double r = randDouble();
        String a = null;
        for (int i = 0; i < this.probabilities.size(); i++)  {
            double p = this.probabilities.get(i);
            ub += p;
            if (lb <= r && r <= ub) {
                a = this.getPoints().get(i).getActivity();
                break;
            }
            lb += p;
        }
        return a;
    }
    
    /**
     * Returns a pseudo-random number.
     * @return Double between 0.0 and 1.0, exclusive.
     */
    private static double randDouble() {
        Random rand = new Random();
        return rand.nextDouble();
    }

    /**
     * @return the pivot
     */
    public Pivot getPivot() {
        return pivot;
    }
    
    public ArrayList<Double> getProbabilities() {
        return probabilities;
    }

    /**
     * @return the points
     */
    public ArrayList<ActivityPoint> getPoints() {
        return points;
    }
    
    public String getName() {
        return name;
    }
    
}
