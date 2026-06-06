package util;

import entity.*;
import entity.enums.BloodType;
import entity.enums.ColorCode;
import entity.enums.MedicationForm;
import entity.enums.StatusType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class EntitySeeder {
    public static void main(String[] args) {
        StandardServiceRegistry registry = null;
        SessionFactory sessionFactory = null;

        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();

            Session session = sessionFactory.openSession();
            session.beginTransaction();

            // Addresses
            Address addr1 = new Address("Poland", "Warsaw", "Marszalkowska", "15A", "00-082");
            Address addr2 = new Address("Poland", "Krakow", "Florianska", "8", "31-019");
            Address addr3 = new Address("Poland", "Wroclaw", "Rynek", "42", "50-116");
            Address addr4 = new Address("Poland", "Gdansk", "Dluga", "12", "80-827");
            Address addr5 = new Address("Poland", "Poznan", "Polwiejska", "3", "61-888");
            Address addr6 = new Address("Poland", "Warsaw", "Nowy Swiat", "45", "00-029");
            Address addr7 = new Address("Poland", "Lodz", "Piotrkowska", "102", "90-001");
            Address addr8 = new Address("Poland", "Szczecin", "Waly Chrobrego", "1", "70-500");
            Address addr9 = new Address("Poland", "Lublin", "Krakowskie Przedmiescie", "55", "20-002");
            Address addr10 = new Address("Poland", "Katowice", "Mariacka", "20", "40-014");
            Address addr11 = new Address("Poland", "Bialystok", "Lipowa", "14", "15-424");
            Address addr12 = new Address("Poland", "Gdynia", "Swietojanska", "68", "81-393");
            Address addr13 = new Address("Poland", "Torun", "Szeroka", "9", "87-100");
            Address addr14 = new Address("Poland", "Zakopane", "Krupowki", "22", "34-500");
            Address addr15 = new Address("Poland", "Warsaw", "Aleje Jerozolimskie", "100", "00-807");

            // Departments
            Department cardiologyDepartment = new Department("Cardiology Department", 1, "CARD-01");
            Department neurologyDepartment = new Department("Neurology Department", 2, "NEUR-01");
            Department emergencyDepartment = new Department("Emergency Department", 0, "EMER-01");
            Department pediatricsDepartment = new Department("Pediatrics Department", 1, "PEDI-01");
            Department orthopedicsDepartment = new Department("Orthopedics Department", 3, "ORTH-01");
            Department surgeryDepartment = new Department("General Surgery Department", 2, "SURG-01");
            Department radiologyDepartment = new Department("Radiology Department", -1, "RADI-01");
            Department psychiatryDepartment = new Department("Psychiatry Department", 4, "PSYC-01");
            Department dermatologyDepartment = new Department("Dermatology Department", 3, "DERM-01");
            Department intensiveCareDepartment = new Department("Intensive Care Unit Department", 5, "ICU-01");

            session.persist(cardiologyDepartment);
            session.persist(neurologyDepartment);
            session.persist(emergencyDepartment);
            session.persist(pediatricsDepartment);
            session.persist(orthopedicsDepartment);
            session.persist(radiologyDepartment);
            session.persist(surgeryDepartment);
            session.persist(dermatologyDepartment);
            session.persist(intensiveCareDepartment);
            session.persist(psychiatryDepartment);

            // Specialization
            Specialization cardioSpec = new Specialization("Cardiology", "Heart and blood vessel disorders");
            Specialization neuroSpec = new Specialization("Neurology", "Nervous system disorders");
            Specialization emerSpec = new Specialization("Emergency Medicine", "Immediate care for acute illnesses and injuries");
            Specialization pediSpec = new Specialization("Pediatrics", "Medical care of infants, children, and adolescents");
            Specialization orthoSpec = new Specialization("Orthopedics", "Musculoskeletal system issues");
            Specialization surgSpec = new Specialization("General Surgery", "Surgical treatment of abdominal and general organs");
            Specialization radioSpec = new Specialization("Radiology", "Medical imaging to diagnose and treat diseases");
            Specialization psychSpec = new Specialization("Psychiatry", "Mental health, emotional and behavioral disorders");
            Specialization dermaSpec = new Specialization("Dermatology", "Skin, hair, and nail conditions");
            Specialization icuSpec = new Specialization("Critical Care", "Life support and intensive monitoring for severe illnesses");

            session.persist(cardioSpec);
            session.persist(neuroSpec);
            session.persist(emerSpec);
            session.persist(pediSpec);
            session.persist(orthoSpec);
            session.persist(radioSpec);
            session.persist(surgSpec);
            session.persist(psychSpec);
            session.persist(dermaSpec);
            session.persist(icuSpec);

            // Nurse
            Nurse nurse1 = new Nurse("NUR-1001", "Alice", "", "Wonderland", Set.of("+48 111 111 111"), new BigDecimal("5000"), "Day", Set.of("BLS Certification"), "Emergency", true);
            Nurse nurse2 = new Nurse("NUR-1002", "Bob", "", "Builder", Set.of("+48 222 222 222"), new BigDecimal("5200"), "Night", Set.of("ACLS Certification"), "ICU", false);
            Nurse nurse3 = new Nurse("NUR-1003", "Charlie", "M.", "Chaplin", Set.of("+48 333 333 333"), new BigDecimal("4900"), "Day", Set.of("Pediatric Nursing"), "Pediatrics", false);
            Nurse nurse4 = new Nurse("NUR-1004", "Diana", "", "Prince", Set.of("+48 444 444 444"), new BigDecimal("5500"), "Night", Set.of("Trauma Care"), "Surgery", true);
            Nurse nurse5 = new Nurse("NUR-1005", "Eve", "", "Polastri", Set.of("+48 555 555 555"), new BigDecimal("5100"), "Day", Set.of("Neurology Certification"), "Neurology", false);
            Nurse nurse6 = new Nurse("NUR-1006", "Frank", "J.", "Castle", Set.of("+48 666 666 666"), new BigDecimal("5300"), "Night", Set.of("Orthopedic Care"), "Orthopedics", true);
            Nurse nurse7 = new Nurse("NUR-1007", "Grace", "", "Shelby", Set.of("+48 777 777 777"), new BigDecimal("5000"), "Day", Set.of("Cardiac Nursing"), "Cardiology", false);
            Nurse nurse8 = new Nurse("NUR-1008", "Harry", "J.", "Potter", Set.of("+48 888 888 888"), new BigDecimal("4800"), "Night", Set.of("Psychiatric Nursing"), "Psychiatry", false);
            Nurse nurse9 = new Nurse("NUR-1009", "Ivy", "", "Poison", Set.of("+48 999 999 999"), new BigDecimal("5400"), "Day", Set.of("Dermatology Care"), "Dermatology", true);
            Nurse nurse10 = new Nurse("NUR-1010", "Jack", "", "Sparrow", Set.of("+48 000 000 000"), new BigDecimal("5600"), "Night", Set.of("Radiology Safety"), "Radiology", false);

            nurse1.addDepartment(emergencyDepartment);
            nurse2.addDepartment(intensiveCareDepartment);
            nurse3.addDepartment(pediatricsDepartment);
            nurse4.addDepartment(surgeryDepartment);
            nurse5.addDepartment(neurologyDepartment);
            nurse6.addDepartment(orthopedicsDepartment);
            nurse7.addDepartment(cardiologyDepartment);
            nurse8.addDepartment(psychiatryDepartment);
            nurse9.addDepartment(dermatologyDepartment);
            nurse10.addDepartment(radiologyDepartment);

            session.persist(nurse1);
            session.persist(nurse2);
            session.persist(nurse3);
            session.persist(nurse4);
            session.persist(nurse5);
            session.persist(nurse6);
            session.persist(nurse7);
            session.persist(nurse8);
            session.persist(nurse9);
            session.persist(nurse10);

            // Doctor
            Doctor doc1 = new Doctor("DOC-2001", "Stephen", "", "Strange", Set.of("+48 101 101 101"), new BigDecimal("15000"), "Day", "LIC-001", 500.0f, cardioSpec);
            Doctor doc2 = new Doctor("DOC-2002", "John", "H.", "Watson", Set.of("+48 102 102 102"), new BigDecimal("11000"), "Night", "LIC-002", 200.0f, cardioSpec);
            Doctor doc3 = new Doctor("DOC-2003", "Leonard", "H.", "McCoy", Set.of("+48 103 103 103"), new BigDecimal("12500"), "Day", "LIC-003", 250.0f, cardioSpec);
            Doctor doc4 = new Doctor("DOC-2004", "Perry", "", "Cox", Set.of("+48 104 104 104"), new BigDecimal("13000"), "Day", "LIC-004", 300.0f, cardioSpec);
            Doctor doc5 = new Doctor("DOC-2005", "Julius", "", "Hibbert", Set.of("+48 105 105 105"), new BigDecimal("10500"), "Night", "LIC-005", 150.0f, cardioSpec);
            Doctor doc6 = new Doctor("DOC-2006", "Gregory", "", "House", Set.of("+48 201 201 201"), new BigDecimal("18000"), "Day", "LIC-006", 600.0f, neuroSpec);
            Doctor doc7 = new Doctor("DOC-2007", "Derek", "", "Shepherd", Set.of("+48 202 202 202"), new BigDecimal("16000"), "Night", "LIC-007", 450.0f, neuroSpec);
            Doctor doc8 = new Doctor("DOC-2008", "Meredith", "", "Grey", Set.of("+48 301 301 301"), new BigDecimal("14000"), "Day", "LIC-008", 350.0f, surgSpec);
            Doctor doc9 = new Doctor("DOC-2009", "Martin", "", "Ellingham", Set.of("+48 302 302 302"), new BigDecimal("11500"), "Night", "LIC-009", 200.0f, surgSpec);
            Doctor doc10 = new Doctor("DOC-2010", "Hannibal", "", "Lecter", Set.of("+48 401 401 401"), new BigDecimal("17000"), "Day", "LIC-010", 550.0f, psychSpec);
            Doctor doc11 = new Doctor("DOC-2011", "Bruce", "", "Banner", Set.of("+48 501 501 501"), new BigDecimal("13500"), "Night", "LIC-011", 250.0f, radioSpec);
            Doctor doc12 = new Doctor("DOC-2012", "Douglas", "", "Ross", Set.of("+48 601 601 601"), new BigDecimal("12000"), "Day", "LIC-012", 150.0f, emerSpec);
            Doctor doc13 = new Doctor("DOC-2013", "Temperance", "", "Brennan", Set.of("+48 701 701 701"), new BigDecimal("14500"), "Day", "LIC-013", 400.0f, orthoSpec);
            Doctor doc14 = new Doctor("DOC-2014", "Michaela", "", "Quinn", Set.of("+48 801 801 801"), new BigDecimal("10000"), "Night", "LIC-014", 100.0f, pediSpec);
            Doctor doc15 = new Doctor("DOC-2015", "Beverly", "", "Crusher", Set.of("+48 901 901 901"), new BigDecimal("15500"), "Day", "LIC-015", 400.0f, icuSpec);

            doc1.addDepartment(cardiologyDepartment); cardioSpec.addDoctor(doc1);
            doc2.addDepartment(cardiologyDepartment); cardioSpec.addDoctor(doc2);
            doc3.addDepartment(cardiologyDepartment); cardioSpec.addDoctor(doc3);
            doc4.addDepartment(cardiologyDepartment); cardioSpec.addDoctor(doc4);
            doc5.addDepartment(cardiologyDepartment); cardioSpec.addDoctor(doc5);
            doc6.addDepartment(neurologyDepartment); neuroSpec.addDoctor(doc6);
            doc7.addDepartment(neurologyDepartment); neuroSpec.addDoctor(doc7);
            doc8.addDepartment(surgeryDepartment); surgSpec.addDoctor(doc8);
            doc9.addDepartment(surgeryDepartment); surgSpec.addDoctor(doc9);
            doc10.addDepartment(psychiatryDepartment); psychSpec.addDoctor(doc10);
            doc11.addDepartment(radiologyDepartment); radioSpec.addDoctor(doc11);
            doc12.addDepartment(emergencyDepartment); emerSpec.addDoctor(doc12);
            doc13.addDepartment(orthopedicsDepartment); orthoSpec.addDoctor(doc13);
            doc14.addDepartment(pediatricsDepartment); pediSpec.addDoctor(doc14);
            doc15.addDepartment(intensiveCareDepartment); icuSpec.addDoctor(doc15);

            session.persist(doc1);
            session.persist(doc2);
            session.persist(doc3);
            session.persist(doc4);
            session.persist(doc5);
            session.persist(doc6);
            session.persist(doc7);
            session.persist(doc8);
            session.persist(doc9);
            session.persist(doc10);
            session.persist(doc11);
            session.persist(doc12);
            session.persist(doc13);
            session.persist(doc14);
            session.persist(doc15);

            // Medication
            Medication clinicalMedication1 = new Medication("Normal Saline", Set.of("Sodium Chloride"), MedicationForm.INHALER, "0.9%", "Room Temperature", "Intravenous");
            Medication clinicalMedication2 = new Medication("Adrenaline", Set.of("Epinephrine"), MedicationForm.INJECTION, "1mg/ml", "Refrigerate", "Intramuscular / Intravenous");
            Medication clinicalMedication3 = new Medication("Diprivan", Set.of("Propofol"), MedicationForm.INJECTION, "10mg/ml", "Room Temperature", "Intravenous");

            Medication prescribedMedication1 = new Medication("Panadol", Set.of("Paracetamol"), MedicationForm.TABLET, "500mg", ColorCode.WHITE, true, 30);
            Medication prescribedMedication2 = new Medication("Augmentin", Set.of("Amoxicillin", "Clavulanate Potassium"), MedicationForm.TABLET, "1000mg", ColorCode.GREEN, false, 14);
            Medication prescribedMedication3 = new Medication("Advil", Set.of("Ibuprofen"), MedicationForm.TABLET, "400mg", ColorCode.WHITE, true, 20);

            Medication overlappingMedication1 = new Medication("Morphine Sulfate", Set.of("Morphine"), MedicationForm.INJECTION, "10mg/ml", "Intravenous / Intramuscular", "Locked Cabinet", ColorCode.RED, false, 5);
            Medication overlappingMedication2 = new Medication("Valium", Set.of("Diazepam"), MedicationForm.TABLET, "5mg", "Oral", "Room Temperature", ColorCode.RED, false, 10);
            Medication overlappingMedication3 = new Medication("Nexium", Set.of("Esomeprazole"), MedicationForm.INHALER, "40mg", "Oral", "Room Temperature", ColorCode.WHITE, false, 28);
            Medication overlappingMedication4 = new Medication("Lantus", Set.of("Insulin Glargine"), MedicationForm.INJECTION, "100 Units/ml", "Subcutaneous", "Refrigerate (2-8°C)", ColorCode.WHITE, false, 5);

            session.persist(clinicalMedication1);
            session.persist(clinicalMedication2);
            session.persist(clinicalMedication3);
            session.persist(prescribedMedication1);
            session.persist(prescribedMedication2);
            session.persist(prescribedMedication3);
            session.persist(overlappingMedication1);
            session.persist(overlappingMedication2);
            session.persist(overlappingMedication3);
            session.persist(overlappingMedication4);

            // Patients
            Patient pat1 = new Patient("Walter", "White", LocalDate.of(1958, 9, 7), Set.of("+48 111 222 333"), addr1, BloodType.O_POSITIVE);
            Patient pat2 = new Patient("Jesse", "Pinkman", LocalDate.of(1984, 9, 24), Set.of("+48 222 333 444"), addr2, BloodType.A_POSITIVE);
            Patient pat3 = new Patient("Michael", "Scott", LocalDate.of(1964, 3, 15), Set.of("+48 333 444 555"), addr3, BloodType.AB_POSITIVE);
            Patient pat4 = new Patient("Dwight", "Schrute", LocalDate.of(1968, 1, 20), Set.of("+48 444 555 666"), addr4, BloodType.B_POSITIVE);
            Patient pat5 = new Patient("Luke", "Skywalker", LocalDate.of(1977, 5, 25), Set.of("+48 555 666 777"), addr5, BloodType.A_NEGATIVE);
            Patient pat6 = new Patient("Han", "Solo", LocalDate.of(1942, 7, 13), Set.of("+48 666 777 888"), addr6, BloodType.O_NEGATIVE);
            Patient pat7 = new Patient("Frodo", "Baggins", LocalDate.of(1981, 9, 22), Set.of("+48 777 888 999"), addr7, BloodType.B_NEGATIVE);
            Patient pat8 = new Patient("Samwise", "Gamgee", LocalDate.of(1983, 4, 6), Set.of("+48 888 999 000"), addr8, BloodType.AB_NEGATIVE);
            Patient pat9 = new Patient("Thomas", "Anderson", LocalDate.of(1962, 3, 11), Set.of("+48 999 000 111"), addr9, BloodType.O_POSITIVE);
            Patient pat10 = new Patient("Bruce", "Wayne", LocalDate.of(1970, 2, 19), Set.of("+48 000 111 222"), addr10, BloodType.AB_POSITIVE);
            Patient pat11 = new Patient("Clark", "Kent", LocalDate.of(1978, 6, 18), Set.of("+48 123 123 123"), addr11, BloodType.O_NEGATIVE);
            Patient pat12 = new Patient("Chandler", "Bing", LocalDate.of(1968, 4, 8), Set.of("+48 234 234 234"), addr12, BloodType.A_POSITIVE);
            Patient pat13 = new Patient("Joey", "Tribbiani", LocalDate.of(1968, 1, 9), Set.of("+48 345 345 345"), addr13, BloodType.B_POSITIVE);
            Patient pat14 = new Patient("Jon", "Snow", LocalDate.of(1986, 12, 26), Set.of("+48 456 456 456"), addr14, BloodType.O_NEGATIVE);
            Patient pat15 = new Patient("Arya", "Stark", LocalDate.of(1997, 4, 15), Set.of("+48 567 567 567"), addr15, BloodType.A_NEGATIVE);

            session.persist(pat1);
            session.persist(pat2);
            session.persist(pat3);
            session.persist(pat4);
            session.persist(pat5);
            session.persist(pat6);
            session.persist(pat7);
            session.persist(pat8);
            session.persist(pat9);
            session.persist(pat10);
            session.persist(pat11);
            session.persist(pat12);
            session.persist(pat13);
            session.persist(pat14);
            session.persist(pat15);

            // Appointments
            LocalDateTime today = LocalDateTime.now();
            LocalDateTime tomorrow = today.plusDays(1);
            LocalDateTime twoDaysLater = today.plusDays(2);

            Appointment app1 = new Appointment("APP-001", today.withHour(9).withMinute(0), StatusType.COMPLETED, doc1, pat1);
            Appointment app2 = new Appointment("APP-002", today.withHour(9).withMinute(30), StatusType.SCHEDULED, doc1, pat2, "Patient reported mild headaches.");
            Appointment app3 = new Appointment("APP-003", today.withHour(10).withMinute(0), StatusType.CANCELED, doc1, pat3);
            Appointment app4 = new Appointment("APP-004", today.withHour(10).withMinute(30), StatusType.SCHEDULED, doc1, pat4);
            Appointment app5 = new Appointment("APP-005", today.withHour(11).withMinute(0), StatusType.SCHEDULED, doc1, pat5, "ECG is normal.");
            Appointment app6 = new Appointment("APP-006", tomorrow.withHour(11).withMinute(30), StatusType.RESCHEDULED, doc1, pat6, "Rescheduled to tomorrow.");
            Appointment app7 = new Appointment("APP-007", today.withHour(13).withMinute(0), StatusType.SCHEDULED, doc1, pat7);
            Appointment app8 = new Appointment("APP-008", today.withHour(13).withMinute(30), StatusType.SCHEDULED, doc1, pat8, "Post-op checkup looking good.");
            Appointment app9 = new Appointment("APP-009", today.withHour(14).withMinute(0), StatusType.SCHEDULED, doc1, pat9);
            Appointment app10 = new Appointment("APP-010", today.withHour(14).withMinute(30), StatusType.SCHEDULED, doc1, pat10, "Follow-up for minor fracture.");
            Appointment app11 = new Appointment("APP-011", tomorrow.withHour(9).withMinute(0), StatusType.SCHEDULED, doc1, pat11);
            Appointment app12 = new Appointment("APP-012", tomorrow.withHour(9).withMinute(30), StatusType.SCHEDULED, doc1, pat12, "Routine surgical consultation.");
            Appointment app13 = new Appointment("APP-013", tomorrow.withHour(10).withMinute(0), StatusType.SCHEDULED, doc1, pat13);
            Appointment app14 = new Appointment("APP-014", tomorrow.withHour(13).withMinute(0), StatusType.SCHEDULED, doc1, pat2, "Follow-up for headaches.");
            Appointment app15 = new Appointment("APP-015", tomorrow.withHour(11).withMinute(0), StatusType.SCHEDULED, doc1, pat14);
            Appointment app16 = new Appointment("APP-016", twoDaysLater.withHour(9).withMinute(0), StatusType.SCHEDULED, doc1, pat15);
            Appointment app17 = new Appointment("APP-017", twoDaysLater.withHour(9).withMinute(30), StatusType.RESCHEDULED, doc1, pat3, "Patient called to reschedule after missing today's slot.");

            doc1.addAppointment(app1); pat1.addAppointment(app1);
            doc1.addAppointment(app2); pat2.addAppointment(app2);
            doc1.addAppointment(app3); pat3.addAppointment(app3);
            doc1.addAppointment(app4); pat4.addAppointment(app4);
            doc1.addAppointment(app5); pat5.addAppointment(app5);
            doc1.addAppointment(app6); pat6.addAppointment(app6);
            doc1.addAppointment(app7); pat7.addAppointment(app7);
            doc1.addAppointment(app8); pat8.addAppointment(app8);
            doc1.addAppointment(app9); pat9.addAppointment(app9);
            doc1.addAppointment(app10); pat10.addAppointment(app10);
            doc1.addAppointment(app11); pat11.addAppointment(app11);
            doc1.addAppointment(app12); pat12.addAppointment(app12);
            doc1.addAppointment(app13); pat13.addAppointment(app13);
            doc1.addAppointment(app14); pat6.addAppointment(app14);
            doc1.addAppointment(app15); pat14.addAppointment(app15);
            doc1.addAppointment(app16); pat15.addAppointment(app16);
            doc1.addAppointment(app17); pat3.addAppointment(app17);

            session.persist(app1);
            session.persist(app2);
            session.persist(app3);
            session.persist(app4);
            session.persist(app5);
            session.persist(app6);
            session.persist(app7);
            session.persist(app8);
            session.persist(app9);
            session.persist(app10);
            session.persist(app11);
            session.persist(app12);
            session.persist(app13);
            session.persist(app14);
            session.persist(app15);
            session.persist(app16);
            session.persist(app17);

            // Invoice
            LocalDate todayDate = LocalDate.now();
            LocalDate tomorrowDate = todayDate.plusDays(1);
            LocalDate twoDaysLaterDate = todayDate.plusDays(2);

            Invoice inv1 = new Invoice("INV-001", new BigDecimal("500.00"), todayDate, true, 0.08f, app1);
            Invoice inv2 = new Invoice("INV-002", new BigDecimal("250.50"), todayDate, true, 0.08f, app2);
            Invoice inv3 = new Invoice("INV-003", new BigDecimal("100.00"), todayDate, false, 0.23f, app3);
            Invoice inv4 = new Invoice("INV-004", new BigDecimal("600.00"), todayDate, false, 0.08f, app4);
            Invoice inv5 = new Invoice("INV-005", new BigDecimal("450.00"), todayDate, true, 0.0f, app5);
            Invoice inv6 = new Invoice("INV-006", new BigDecimal("150.00"), todayDate, false, 0.08f, app6);
            Invoice inv7 = new Invoice("INV-007", new BigDecimal("300.00"), todayDate, false, 0.08f, app7);
            Invoice inv8 = new Invoice("INV-008", new BigDecimal("750.00"), todayDate, true, 0.08f, app8);
            Invoice inv9 = new Invoice("INV-009", new BigDecimal("400.00"), todayDate, false, 0.08f, app9);
            Invoice inv10 = new Invoice("INV-010", new BigDecimal("200.00"), todayDate, false, 0.08f, app10);
            Invoice inv11 = new Invoice("INV-011", new BigDecimal("550.00"), tomorrowDate, false, 0.08f, app11);
            Invoice inv12 = new Invoice("INV-012", new BigDecimal("800.00"), tomorrowDate, false, 0.08f, app12);
            Invoice inv13 = new Invoice("INV-013", new BigDecimal("350.00"), tomorrowDate, false, 0.08f, app13);
            Invoice inv14 = new Invoice("INV-014", new BigDecimal("150.00"), tomorrowDate, false, 0.08f, app14);
            Invoice inv15 = new Invoice("INV-015", new BigDecimal("420.00"), tomorrowDate, false, 0.08f, app15);
            Invoice inv16 = new Invoice("INV-016", new BigDecimal("600.00"), twoDaysLaterDate, false, 0.08f, app16);
            Invoice inv17 = new Invoice("INV-017", new BigDecimal("150.00"), twoDaysLaterDate, false, 0.08f, app17);

            app1.setInvoice(inv1);
            app2.setInvoice(inv2);
            app3.setInvoice(inv3);
            app4.setInvoice(inv4);
            app5.setInvoice(inv5);
            app6.setInvoice(inv6);
            app7.setInvoice(inv7);
            app8.setInvoice(inv8);
            app9.setInvoice(inv9);
            app10.setInvoice(inv10);
            app11.setInvoice(inv11);
            app12.setInvoice(inv12);
            app13.setInvoice(inv13);
            app14.setInvoice(inv14);
            app15.setInvoice(inv15);
            app16.setInvoice(inv16);
            app17.setInvoice(inv17);

            session.persist(inv1);
            session.persist(inv2);
            session.persist(inv3);
            session.persist(inv4);
            session.persist(inv5);
            session.persist(inv6);
            session.persist(inv7);
            session.persist(inv8);
            session.persist(inv9);
            session.persist(inv10);
            session.persist(inv11);
            session.persist(inv12);
            session.persist(inv13);
            session.persist(inv14);
            session.persist(inv15);
            session.persist(inv16);
            session.persist(inv17);

            // MedicalNotes & Past Appointments
            LocalDate pastDate1 = todayDate.minusMonths(6);
            LocalDate pastDate2 = todayDate.minusMonths(2);
            LocalDate pastDate3 = todayDate.minusWeeks(3);

            Appointment pastApp1 = new Appointment("APP-PAST-1", pastDate1.atTime(10, 0), StatusType.COMPLETED, doc1, pat1);
            Appointment pastApp2 = new Appointment("APP-PAST-2", pastDate2.atTime(14, 30), StatusType.COMPLETED, doc1, pat2);
            Appointment pastApp3 = new Appointment("APP-PAST-3", pastDate3.atTime(11, 0), StatusType.COMPLETED, doc1, pat5);

            Appointment pastApp4 = new Appointment("APP-PAST-4", pastDate2.minusDays(5).atTime(11, 0), StatusType.COMPLETED, doc1, pat2); // pat2'nin eski bir randevusu daha
            Appointment pastApp7 = new Appointment("APP-PAST-7", pastDate3.minusDays(2).atTime(15, 30), StatusType.COMPLETED, doc1, pat5); // pat5'in eski bir randevusu daha
            Appointment pastApp8 = new Appointment("APP-PAST-8", pastDate1.plusDays(10).atTime(9, 0), StatusType.COMPLETED, doc1, pat9); // pat9'un eski randevusu

            doc1.addAppointment(pastApp1); pat1.addAppointment(pastApp1);
            doc1.addAppointment(pastApp2); pat2.addAppointment(pastApp2);
            doc1.addAppointment(pastApp3); pat5.addAppointment(pastApp3);

            doc1.addAppointment(pastApp4); pat2.addAppointment(pastApp4);
            doc1.addAppointment(pastApp7); pat5.addAppointment(pastApp7);
            doc1.addAppointment(pastApp8); pat9.addAppointment(pastApp8);

            session.persist(pastApp1);
            session.persist(pastApp2);
            session.persist(pastApp3);
            session.persist(pastApp4);
            session.persist(pastApp7);
            session.persist(pastApp8);

            MedicalNotes note1 = new MedicalNotes(Set.of("Hypertension"), "Started blood pressure medication", pastDate1, pat1, pastApp1);
            MedicalNotes note2 = new MedicalNotes(Set.of("Type 2 Diabetes"), "Diet control and Metformin 500mg updated", todayDate, pat1, app1);
            MedicalNotes note3 = new MedicalNotes(Set.of("Migraine History"), "Reported frequent headaches, prescribed painkillers", pastDate2, pat2, pastApp2);
            MedicalNotes note4 = new MedicalNotes(Set.of("Acute Migraine", "Vitamin D Deficiency"), "Sumatriptan 50mg prescribed. Vitamin D supplements 1000 IU daily", todayDate, pat2, pastApp4);
            MedicalNotes note6 = new MedicalNotes(Set.of("Chest Pain"), "ECG ordered, preliminary check", pastDate3, pat5, pastApp3);
            MedicalNotes note7 = new MedicalNotes(Set.of("Arrhythmia", "Palpitations"), "Holter monitor scheduled for 24 hours", todayDate, pat5, pastApp7);
            MedicalNotes note8 = new MedicalNotes(Set.of("Post-op Recovery"), "Healing well, staples removed", todayDate, pat9, pastApp8);

            pat1.addMedicalNote(note1); pastApp1.setMedicalNotes(note1);
            pat1.addMedicalNote(note2); app1.setMedicalNotes(note2);
            pat2.addMedicalNote(note3); pastApp2.setMedicalNotes(note3);
            pat2.addMedicalNote(note4); pastApp4.setMedicalNotes(note4);
            pat5.addMedicalNote(note6); pastApp3.setMedicalNotes(note6);
            pat5.addMedicalNote(note7); pastApp7.setMedicalNotes(note7);
            pat9.addMedicalNote(note8); pastApp8.setMedicalNotes(note8);

            session.persist(note1);
            session.persist(note2);
            session.persist(note3);
            session.persist(note4);
            session.persist(note6);
            session.persist(note7);
            session.persist(note8);

            // Sick Note
            LocalDateTime pastDateTime2 = pastDate2.atTime(15, 0);
            LocalDateTime pastDateTime3 = pastDate3.atTime(11, 30);

            SickNote sickNote1 = new SickNote("SN-001", 3, pastDateTime2);
            note3.setSickNote(sickNote1);

            SickNote sickNote2 = new SickNote("SN-002", 5, pastDateTime3);
            note6.setSickNote(sickNote2);

            session.persist(sickNote1);
            session.persist(sickNote2);

            // Medication Order
            MedicationOrder order1 = new MedicationOrder("Once a day", 30);
            note2.addMedicationOrder(order1);
            prescribedMedication1.addMedicationOrder(order1);

            MedicationOrder order2 = new MedicationOrder("Twice a day as needed", 5);
            note4.addMedicationOrder(order2);
            prescribedMedication2.addMedicationOrder(order2);

            MedicationOrder order3 = new MedicationOrder("Once a day", 15);
            note7.addMedicationOrder(order3);
            prescribedMedication3.addMedicationOrder(order3);

            session.persist(order1);
            session.persist(order2);
            session.persist(order3);

            session.getTransaction().commit();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        } finally {
            if (sessionFactory != null) sessionFactory.close();
        }
    }
}