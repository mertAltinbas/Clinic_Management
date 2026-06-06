import gui.DoctorDashboard;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        DoctorDashboard dashboard = new DoctorDashboard();
        dashboard.setVisible(true);
    }
}
