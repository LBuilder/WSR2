/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author s124392
 */
public class View extends JFrame {

    public View() {
        super();
    }
    
    public void centerize() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2-this.getSize().width / 2, dim.height / 
                2 - this.getSize().height / 2);
    }
    
    public void toTop() {
        this.setLocation(0, 0);
    }
}
