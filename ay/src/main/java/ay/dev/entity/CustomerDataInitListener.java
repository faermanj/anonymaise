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
public class CustomerDataInitListener {
    @Inject
    EntityManager em;

    @Inject
    @ConfigProperty(name = "quarkus.profile", defaultValue = "prod")
    String profile;

    private static final String[] NAMES = {"Alice", "Bob", "Carol", "Dave", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy"};
    private static final String[] COLORS = {"Red", "Blue", "Green", "Yellow", "Purple", "Orange", "Pink", "Black", "White", "Gray"};
    private static final String[] BANDS = {"Beatles", "Queen", "Nirvana", "U2", "Pink Floyd", "Metallica", "ABBA", "Coldplay", "Radiohead", "Oasis"};

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (!"dev".equals(profile)) return;
        Long count = em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class).getSingleResult();
        if (count > 0) return;
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            Customer c = new Customer();
            c.setName(NAMES[i]);
            c.setPhone(String.format("555-01%03d", i));
            c.setAddress("Street " + (i+1));
            c.setMotherName("Mother" + NAMES[i]);
            c.setFatherName("Father" + NAMES[i]);
            c.setPreferedName(NAMES[i].substring(0, 2) + "y");
            c.setPreferedColor(COLORS[rand.nextInt(COLORS.length)]);
            c.setPreferedBand(BANDS[rand.nextInt(BANDS.length)]);
            em.persist(c);
        }
    }
}
