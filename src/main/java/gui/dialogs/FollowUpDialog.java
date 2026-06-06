package gui.dialogs;

import javax.swing.*;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FollowUpDialog extends JDialog {
    private LocalDateTime followUpDate = null;

    public FollowUpDialog(JFrame parent) {
        super(parent, "Schedule Follow Up", true);
        setSize(400, 250);
        setLayout(new BorderLayout());
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JComboBox<Integer> dayCombo = new JComboBox<>();
        for (int i = 1; i <= 31; i++) dayCombo.addItem(i);
        JComboBox<Integer> monthCombo = new JComboBox<>();
        for (int i = 1; i <= 12; i++) monthCombo.addItem(i);
        JComboBox<Integer> yearCombo = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        yearCombo.addItem(currentYear);
        yearCombo.addItem(currentYear + 1);

        datePanel.add(dayCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(monthCombo);
        datePanel.add(new JLabel("/"));
        datePanel.add(yearCombo);

        JComboBox<String> hourCombo = new JComboBox<>();
        for (int i = 8; i <= 18; i++) hourCombo.addItem(String.format("%02d", i));
        JComboBox<String> minuteCombo = new JComboBox<>(new String[]{"00", "15", "30", "45"});

        timePanel.add(hourCombo);
        timePanel.add(new JLabel(":"));
        timePanel.add(minuteCombo);

        centerPanel.add(new JLabel("Select Date (DD/MM/YYYY):"));
        centerPanel.add(datePanel);
        centerPanel.add(new JLabel("Select Time (HH:MM):"));
        centerPanel.add(timePanel);

        JButton scheduleButton = new JButton("Schedule");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(scheduleButton);
        buttonPanel.add(cancelButton);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        cancelButton.addActionListener(e -> dispose());

        scheduleButton.addActionListener(e -> {
            try {
                int day = (Integer) dayCombo.getSelectedItem();
                int month = (Integer) monthCombo.getSelectedItem();
                int year = (Integer) yearCombo.getSelectedItem();
                int hour = Integer.parseInt((String) hourCombo.getSelectedItem());
                int minute = Integer.parseInt((String) minuteCombo.getSelectedItem());

                LocalDateTime selectedDateTime = LocalDateTime.of(year, month, day, hour, minute);

                if (selectedDateTime.isBefore(LocalDateTime.now())) {
                    JOptionPane.showMessageDialog(this, "Follow-up date cannot be in the past.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                followUpDate = selectedDateTime;
                dispose();

            } catch (DateTimeException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date selected. Please select a valid calendar date.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public LocalDateTime getFollowUpDate() {
        return followUpDate;
    }
}