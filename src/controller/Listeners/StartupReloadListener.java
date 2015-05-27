/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.Listeners;

import controller.StartupController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author s124392
 */
public class StartupReloadListener implements ActionListener {
    private StartupController controller;
    
    public StartupReloadListener(StartupController c) {
        this.controller = c;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.reload();
    }
    
}
