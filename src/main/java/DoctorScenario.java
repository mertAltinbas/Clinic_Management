import entity.Doctor;
import gui.DoctorDashboard;
import org.hibernate.Session;
import util.HibernateUtil;

public class DoctorScenario {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Doctor testDoctor = session.createQuery("from Doctor where employeeCode = 'DOC-2001'", Doctor.class).uniqueResult();

            if (testDoctor != null) {
                DoctorDashboard dashboard = new DoctorDashboard(testDoctor);
                dashboard.setVisible(true);
            } else {
                System.out.println("Couldn't find Doctor! Run EntitySeeder or check query!");
            }
        }
    }
}
