package gui.doctor.dialogs;

import entity.SickNote;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

public class SickNoteDialog extends JDialog {
    private SickNote createdSickNote = null;

    public SickNoteDialog(JFrame parent) {
        super(parent, "Issue Sick Note", true);
        setSize(350, 200);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel label = new JLabel("Enter rest days for Sick Note:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField daysField = new JTextField(10);
        daysField.setFont(new Font("Arial", Font.PLAIN, 14));
        daysField.setMaximumSize(new Dimension(200, 30));
        daysField.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(label);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(daysField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());

        okButton.addActionListener(e -> {
            String inputText = daysField.getText().trim();
            if (inputText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter the number of days.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int days = Integer.parseInt(inputText);
                if (days < 1  || days > 730) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number of days (1 to 730).", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String generatedId = "SN-" + System.currentTimeMillis();
                createdSickNote = new SickNote(generatedId, days, LocalDateTime.now());
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid integer number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public SickNote getSickNote() {
        return createdSickNote;
    }
}