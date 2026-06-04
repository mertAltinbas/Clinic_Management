import gui.DoctorDashboard;
import util.DataSeeder;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
        DataSeeder.seedDataIfEmpty();

        SwingUtilities.invokeLater(() -> {
            DoctorDashboard dashboard = new DoctorDashboard();
            dashboard.setVisible(true);

            dashboard.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent){
                    HibernateUtil.shutdown();
                    System.exit(0);
                }
            });
        });
    }
}
