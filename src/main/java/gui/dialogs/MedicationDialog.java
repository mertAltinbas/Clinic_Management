package gui.dialogs;

import entity.Medication;
import entity.MedicationOrder;
import entity.enums.ColorCode;
import entity.enums.MedicationForm;
import org.hibernate.Session;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class MedicationDialog extends JDialog {

    public class MedicationResult {
        public MedicationOrder order;
        public Medication manualMedication;
    }

    private MedicationResult result = null;

    public MedicationDialog(JFrame parent) {
        super(parent, "Prescribe Medication", true);
        setSize(400, 350);
        setLayout(new GridLayout(7, 2, 10, 10));
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        DefaultComboBoxModel<Medication> comboModel = new DefaultComboBoxModel<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Medication> meds = session.createQuery("from Medication", Medication.class).list();
            for (Medication m : meds) {
                comboModel.addElement(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JComboBox<Medication> medicationComboBox = new JComboBox<>(comboModel);
        JCheckBox manuelEntryCheckBox = new JCheckBox("Medication Not Found (Manual Entry)");
        JTextField manuelNameField = new JTextField("");
        JTextField manuelDoseField = new JTextField("");
        manuelNameField.setEnabled(false);
        manuelDoseField.setEnabled(false);

        JTextField frequencyField = new JTextField("");
        JTextField durationDayField = new JTextField("");

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        manuelEntryCheckBox.addActionListener(e -> {
            boolean isManual = manuelEntryCheckBox.isSelected();
            medicationComboBox.setEnabled(!isManual);
            manuelNameField.setEnabled(isManual);
            manuelDoseField.setEnabled(isManual);
        });

        add(new JLabel("Select from Catalog:")); add(medicationComboBox);
        add(manuelEntryCheckBox); add(new JLabel(""));
        add(new JLabel("Manual Med Name:")); add(manuelNameField);
        add(new JLabel("Manual Dose (e.g., 500mg):")); add(manuelDoseField);
        add(new JLabel("Frequency:")); add(frequencyField);
        add(new JLabel("Duration (Days):")); add(durationDayField);
        add(confirmButton); add(cancelButton);

        cancelButton.addActionListener(e -> dispose());

        confirmButton.addActionListener(e -> {
            String freqStr = frequencyField.getText().trim();
            String durStr = durationDayField.getText().trim();

            if (freqStr.isEmpty() || durStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Frequency and Duration cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                int durationDays = Integer.parseInt(durStr);
                if (durationDays < 1 || durationDays > 730) {
                    JOptionPane.showMessageDialog(this, "Duration must be at least 1 day or maximum 730 days", "Validation Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                result = new MedicationResult();
                Medication selectedMedication = null;

                if (manuelEntryCheckBox.isSelected()) {
                    String manualName = manuelNameField.getText().trim();
                    String manualDose = manuelDoseField.getText().trim();
                    if (manualName.isEmpty() || manualDose.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please enter name and dose.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    selectedMedication = new Medication(manualName, Set.of("Unknown"), MedicationForm.TABLET, manualDose, ColorCode.WHITE, true, durationDays);
                    result.manualMedication = selectedMedication;
                } else {
                    selectedMedication = (Medication) medicationComboBox.getSelectedItem();
                }

                result.order = new MedicationOrder(freqStr, durationDays);
                result.order.setMedication(selectedMedication);

                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid duration! Please enter a valid number of days.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public MedicationResult getResult() {
        return result;
    }
}