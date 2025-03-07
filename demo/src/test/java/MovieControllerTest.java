/** Clasa pentru test
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */
import com.example.cinema.demo.controller.MovieController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovieControllerTest {

    MovieController movieController = new MovieController();

    @Test
    public void testIsValidEmail() {

        // Teste pozitive (email valid)
        assertTrue(movieController.isValidEmail("admin@cinema.ro"));

        // Teste negative (email invalid)
        assertFalse(movieController.isValidEmail("admin@muzeu.com"));  // Domeniu greșit- muzeu
        assertFalse(movieController.isValidEmail("admin@cinema.ro@cinema.ro"));  // Setermina de 2 ori in @cinema.ro
        assertFalse(movieController.isValidEmail("admincinema.ro"));  // Fără "@"
    }
    @Test
    public void testIsValidPassword() {
        MovieController movieController = new MovieController();

        // Parolă validă
        assertNull(movieController.isValidPassword("Admin2025<3"));  // Null înseamnă parolă validă

        // Parole invalide
        assertEquals("Parola trebuie să conțină cel puțin o literă mare.", movieController.isValidPassword("admin2025<3"));
        assertEquals("Parola trebuie să conțină cel puțin 3 cifre.", movieController.isValidPassword("Admin<3"));
        assertEquals("Parola trebuie să conțină cel puțin un caracter special.", movieController.isValidPassword("Admin2025"));
    }
    @Test
    public void testIsValidTime() {
        // Test oră validă
        assertTrue(movieController.isValidTime("14:30"), "Ora 14:30 ar trebui să fie validă.");
        // Test oră invalidă (format greșit)
        assertFalse(movieController.isValidTime("2:30 PM"), "Ora 2:30 PM nu este validă.");
        // Test oră în afara intervalului
        assertFalse(movieController.isValidTime("25:00"), "Ora 25:00 este invalidă.");
    }

}
