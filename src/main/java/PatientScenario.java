import entity.Patient;
import gui.patient.PatientAppointmentDashboard;
import org.hibernate.Session;
import util.HibernateUtil;

public class PatientScenario {
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // EntitySeeder'da oluşturulan hastalardan birini çekiyoruz
            Patient testPatient = session.createQuery("from Patient where firstName = 'Walter' and lastName = 'White'", Patient.class).uniqueResult();

            if (testPatient != null) {
                PatientAppointmentDashboard dashboard = new PatientAppointmentDashboard(testPatient);
                dashboard.setVisible(true);
            } else {
                System.out.println("Couldn't find Patient! Run EntitySeeder or check query!");
            }
        }
    }
}
