package ui.tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PopUpListener implements ActionListener {
    private JFrame frame = new JFrame("Popup Window Example");

    @Override
    public void actionPerformed(ActionEvent e) {
        JDialog popup = new JDialog(frame, "Popup Window", true);
        popup.setLayout(new FlowLayout());
        popup.add(new JLabel("This is a popup window!"));
        popup.add(new JButton("this is button"));
        popup.add(new TextField("hello"));
        popup.setSize(200, 100);
        popup.setLocationRelativeTo(frame);
        popup.setVisible(true);
    }
}
