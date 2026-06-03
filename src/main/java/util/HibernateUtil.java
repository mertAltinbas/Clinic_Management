package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    static {
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
            throw new ExceptionInInitializerError("SessionFactory can not start: " + e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
