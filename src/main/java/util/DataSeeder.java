package util;

import entity.*;
import entity.enums.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class DataSeeder {
    public static void seedDataIfEmpty() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long appointmentCount = (Long) session.createQuery("select count(a) from Appointment a").uniqueResult();
            if (appointmentCount != null && appointmentCount > 0) {
                return;
            }

            Transaction tx = session.beginTransaction();

            // 1. Uzmanlık Alanları
            Specialization spec1 = new Specialization("Time Travel Medicine", "Fixes paradoxes and timeline diseases");
            Specialization spec2 = new Specialization("Vibe Diagnostics", "Cures bad vibrations and negative energy");
            Specialization spec3 = new Specialization("Donut Rehabilitation", "Extreme cholesterol management");
            session.persist(spec1);
            session.persist(spec2);
            session.persist(spec3);

            // 2. Doktorlar
            Set<String> doc1Phones = new HashSet<>();
            doc1Phones.add("E-MC2");
            Doctor doc1 = new Doctor("D-001", "Albert", "", "Einstein", doc1Phones, new BigDecimal("100000"), "Morning", "RELATIVITY-1", 999.99f, spec1);
            spec1.addDoctor(doc1);
            session.persist(doc1);

            Set<String> doc2Phones = new HashSet<>();
            doc2Phones.add("420-420-420");
            Doctor doc2 = new Doctor("D-002", "Bob", "", "Marley", doc2Phones, new BigDecimal("42000"), "Afternoon", "JAH-001", 420.0f, spec2);
            spec2.addDoctor(doc2);
            session.persist(doc2);

            Set<String> doc3Phones = new HashSet<>();
            doc3Phones.add("DOH-123-456");
            Doctor doc3 = new Doctor("D-003", "Gregory", "", "House", doc3Phones, new BigDecimal("50000"), "Night", "VICODIN-99", 500.0f, spec3);
            spec3.addDoctor(doc3);
            session.persist(doc3);

            // 3. Hastalar
            Set<String> pat1Phones = new HashSet<>();
            pat1Phones.add("I-AM-BATMAN");
            Address addr1 = new Address("USA", "Gotham", "Wayne Manor", "Batcave", "00000");
            Patient pat1 = new Patient("Bruce", "Wayne", LocalDate.of(1970, 5, 27), pat1Phones, addr1, BloodType.AB_NEGATIVE);
            session.persist(pat1);

            Set<String> pat2Phones = new HashSet<>();
            pat2Phones.add("SAY-MY-NAME");
            Address addr2 = new Address("USA", "Albuquerque", "Negra Arroyo Lane", "308", "87104");
            Patient pat2 = new Patient("Walter", "White", LocalDate.of(1958, 9, 7), pat2Phones, addr2, BloodType.O_POSITIVE);
            session.persist(pat2);

            Set<String> pat3Phones = new HashSet<>();
            pat3Phones.add("BEER-000");
            Address addr3 = new Address("USA", "Springfield", "Evergreen Terrace", "742", "12345");
            Patient pat3 = new Patient("Homer", "Simpson", LocalDate.of(1956, 5, 12), pat3Phones, addr3, BloodType.A_POSITIVE);
            session.persist(pat3);

            // 4. Randevular (Hepsi SCHEDULED statüsünde)
            pat1.scheduleAppointment(doc1, "APP-BAT-01", LocalDateTime.now().plusHours(1), "Batwing crash injuries and existential dread.");
            pat2.scheduleAppointment(doc2, "APP-METH-01", LocalDateTime.now().plusHours(3), "Severe coughing and bad vibes from cartel management.");
            pat3.scheduleAppointment(doc3, "APP-DOH-01", LocalDateTime.now().plusHours(5), "Ate too many radioactive donuts, liver failure incoming.");

            // İlişkilerden dolayı listeye eklenen randevuları veritabanına persist ediyoruz
            for(Appointment a : pat1.getAppointments()) session.persist(a);
            for(Appointment a : pat2.getAppointments()) session.persist(a);
            for(Appointment a : pat3.getAppointments()) session.persist(a);

            tx.commit();
            System.out.println("Test verileri basariyla yuklendi.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}