package ay.dev.entity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDate;
import java.util.Random;

@ApplicationScoped
public class PatientDataInitListener {
    @Inject
    EntityManager em;

    @Inject
    @ConfigProperty(name = "quarkus.profile")
    String profile;

    private static final String[] NAMES = {"Anna", "Ben", "Clara", "Dan", "Ella", "Finn", "Gina", "Hugo", "Ivy", "Jack"};
    private static final String[] GENDERS = {"F", "M"};
    private static final String[] NOTES = {"Diabetic", "Allergic", "Asthmatic", "Healthy", "Smoker", "Pregnant", "Hypertensive", "Obese", "Athlete", "Vegan"};
    private static final ICD10Code[] CODES = ICD10Code.values();

    @Transactional
    public void onStart(@Observes StartupEvent ev) {
        if (!"dev".equals(profile)) return;
        Long count = em.createQuery("SELECT COUNT(p) FROM Patient p", Long.class).getSingleResult();
        if (count > 0) return;
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            Patient p = new Patient();
            p.setName(NAMES[i]);
            p.setBirthDate(LocalDate.now().minusYears(20 + rand.nextInt(40)).minusDays(rand.nextInt(365)));
            p.setGender(GENDERS[i % 2]);
            p.setAddress("Avenue " + (i+1));
            p.setEmail(NAMES[i].toLowerCase() + "@example.com");
            p.setInsuranceNumber("INS" + (1000 + i));
            p.setMotherName("Mother" + NAMES[i]);
            p.setFatherName("Father" + NAMES[i]);
            p.setIcd10Code(CODES[rand.nextInt(CODES.length)]);
            p.setNotes(NOTES[i]);
            em.persist(p);
        }
    }
}
