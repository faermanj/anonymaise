package ay.dev.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Random;

@ApplicationScoped
public class ProductDataInitListener {
    @Inject
    EntityManager em;

    @Inject
    @ConfigProperty(name = "quarkus.profile", defaultValue = "prod")
    String profile;

    private static final String[] NAMES = {"Widget", "Gadget", "Thingamajig", "Doodad", "Gizmo", "Contraption", "Device", "Apparatus", "Instrument", "Tool"};
    private static final String[] DESCRIPTIONS = {"Useful", "Handy", "Popular", "New", "Classic", "Advanced", "Compact", "Durable", "Lightweight", "Heavy-duty"};
    private static final double[] PRICES = {9.99, 19.99, 29.99, 39.99, 49.99, 59.99, 69.99, 79.99, 89.99, 99.99};

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (!"dev".equals(profile)) return;
        Long count = em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult();
        if (count > 0) return;
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            Product p = new Product();
            p.setName(NAMES[i]);
            p.setDescription(DESCRIPTIONS[i]);
            p.setPrice(PRICES[i]);
            em.persist(p);
        }
    }
}
