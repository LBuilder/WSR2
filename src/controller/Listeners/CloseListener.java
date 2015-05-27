/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.Listeners;

import controller.MainViewController;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author s124392
 */
public class CloseListener implements MouseListener {
    private MainViewController controller;
    
    public CloseListener(MainViewController c) {
        this.controller = c;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        // Do nothing.
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Do nothing.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        controller.close();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        controller.setCursor(Cursor.HAND_CURSOR);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        controller.setCursor(Cursor.DEFAULT_CURSOR);
    }
    
}
