package spaceinvaders;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsDialog extends JDialog {
    private JComboBox<String> themeSelector;
    private JComboBox<String> pictureSelector;

    public SettingsDialog(JFrame parentFrame, String[] themes, String[] profilePictures) {
        super(parentFrame, "Settings", true);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        themeSelector = new JComboBox<>(themes);
        themeSelector.setBounds(50, 50, 150, 30);
        panel.add(themeSelector);

        pictureSelector = new JComboBox<>(profilePictures);
        pictureSelector.setBounds(50, 100, 150, 30);
        panel.add(pictureSelector);

        JButton okButton = new JButton("OK");
        okButton.setBounds(50, 150, 100, 30);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // close the dialog
            }
        });
        panel.add(okButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parentFrame);
    }

    public String getSelectedTheme() {
        return (String) themeSelector.getSelectedItem();
    }

    public String getSelectedPicture() {
        return (String) pictureSelector.getSelectedItem();
    }
}