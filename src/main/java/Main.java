import gui.DoctorDashboard;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    public static void main(String[] args) {
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
