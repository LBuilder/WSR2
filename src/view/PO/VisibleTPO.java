/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view.PO;

import controller.Controller;
import controller.WSR2;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import model.Activity;

/**
 *
 * @author s124392
 */
public class VisibleTPO extends TimedPO {
    //<editor-fold defaultstate="collapsed" desc="Instance Variables">
    private boolean showValue;
    private float progress;
    private double pulseX;
    private String time;
    //</editor-fold>
    
    /**
     * Constructor.
     * @param f has distribution field continuation.
     * @param px pivot x-displacement.
     * @param py pivot y-displacement.
     * @param a activity references.
     * @param d the delay in seconds.
     * @param v show value.
     */
    public VisibleTPO(final Controller c, final boolean f, final double px, 
            final double py, final String fn, final ArrayList<String> a,
            final ArrayList<Double> p, final String d, final boolean v) {
        super(c, f, px, py, fn, a, p, d);
        this.showValue = v;
    }

    @Override
    public void draw(float alignment) {
        this.setPreferredSize(new Dimension(150, 150));
        this.setBackground(Color.BLACK);
        this.setAlignmentX(alignment);
        this.pulseX = 0.0;
        this.timer.restart();
    }

    @Override
    public void refresh() {
        double d = (double) intDelay;
        double c = (double) (counter / 100.0);
        double p = ((d - c) / d);
        progress = (float) p;
        setProgress(progress, formatTime((int)Math.ceil((float)counter / 100)));
        this.pulseX = (pulseX + (Math.PI * (2.0 / 100.0))) % (2 * Math.PI);
    }
    
    private void setProgress(float value, String time) {
            float old = progress;
            this.progress = value;
            this.time = time;
            firePropertyChange("progress", old, progress);
            repaint();
        }
    
    private String formatTime(int time) {
        String result;
        if (time >= 60) {
            int seconds = time % 60;
            int minutes = time / 60;
            if (seconds < 10) {
                result = Integer.toString(minutes) + ":0" + Integer.toString(seconds); 
            } else {
                result = Integer.toString(minutes) + ":" + Integer.toString(seconds);
            }
        } else {
            result = Integer.toString(time);
        }
        return result;
   }
    
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            Insets insets = getInsets();
            int width = getWidth() - (insets.left + insets.right);
            int height = getHeight() - (insets.bottom + insets.top);
            int raidus = Math.min(width, height);
            int x = insets.left + ((width - raidus) / 2);
            int y = (insets.right + ((height - raidus) / 2));

            if (showValue) {
                double extent = 360d * progress;

                g2d.setColor(Color.RED);
                Arc2D arc = null;
                g2d.fillOval(x, y, raidus, raidus);
                g2d.setColor(Color.PINK);
                extent = 360 - extent;
                arc = new Arc2D.Double(x, y, raidus, raidus, 90, extent, Arc2D.PIE);
                g2d.fill(arc);
                // Draw centered text
                FontMetrics fm = g.getFontMetrics();
                double textWidth = 0.0;
                if (time != null) {
                    textWidth = fm.getStringBounds(time, g).getWidth();
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 22)); 
                    g.drawString(time, (int) ((x + (raidus / 2))- textWidth),
                    (int) ((y + (raidus / 2)) + fm.getMaxAscent() / 2));
                }
                g2d.dispose();
            } else {
                g2d.setColor(Color.RED);
                g2d.fillOval(x, y, raidus, raidus);
                Color myColour = new Color(255, 192, 203, calculateAlpha(pulseX));
                g2d.setColor(myColour);
                g2d.fillOval(x, y, raidus, raidus);
                // Draw centered text
                FontMetrics fm = g.getFontMetrics();
                double textWidth = 0.0;
                textWidth = fm.getStringBounds("?", g).getWidth();
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 22)); 
                    g.drawString("?", (int) ((x + (raidus / 2))- textWidth),
                    (int) ((y + (raidus / 2)) + fm.getMaxAscent() / 2));
                g2d.dispose();
            }
            
    }
        
    private int calculateAlpha(double x) {
        return (int)(40 *(Math.cos(x) + 2));
    }
}
