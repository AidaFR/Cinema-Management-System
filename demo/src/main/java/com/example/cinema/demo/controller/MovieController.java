
/** Clasa pentru gestionarea de cereri pentru filme
 * @author [Fratila Aida]
 * @version 12 Ianuarie 2025
 */
package com.example.cinema.demo.controller;

import com.example.cinema.demo.model.Movie;
import com.example.cinema.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.DayOfWeek;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Redirect către pagina de login la pornirea aplicației
    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
    // Afișare pagina de login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";  // returnează login.html
    }
    // Funcție pentru validarea email-ului
    public boolean isValidEmail(String email) {
        // Verificăm dacă email-ul conține exact un singur "@" și se termină cu "@cinema.ro"
        int Count = email.length() - email.replace("@", "").length();  // Numărăm câte "@" sunt în email
        if (Count != 1 || !email.endsWith("@cinema.ro")) {
            return false;
        }

        // Verificăm că textul înainte de "@" nu este gol
        String prefix = email.split("@")[0];
        if (prefix.isEmpty()) {
            return false;
        }

        return true;
    }


    // Funcție pentru validarea parolei
    public String isValidPassword(String password) {
        if (password.length() < 6) {
            return "Parola trebuie să aibă cel puțin 6 caractere.";
        }

        boolean hasUppercase = false;
        int digitCount = 0;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUppercase = true;
            }
            if (Character.isDigit(c)) {
                digitCount++;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        if (!hasUppercase) {
            return "Parola trebuie să conțină cel puțin o literă mare.";
        }
        if (digitCount < 3) {
            return "Parola trebuie să conțină cel puțin 3 cifre.";
        }
        if (!hasSpecialChar) {
            return "Parola trebuie să conțină cel puțin un caracter special.";
        }

        return null;  // Parola este validă
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("email") String email, @RequestParam("password") String password, Model model) {
        if (!isValidEmail(email)) {
            model.addAttribute("errorMessage", "Adresa de email nu este validă. Trebuie să conțină un singur '@' și să se termine cu '@cinema.ro'.");
            model.addAttribute("email", email);  // Păstrează email-ul în formular
            return "login";
        }

        String passwordError = isValidPassword(password);
        if (passwordError != null) {
            model.addAttribute("errorMessage", passwordError);
            model.addAttribute("email", email);  // Păstrează email-ul în formular
            return "login";
        }

        // Autentificarea după validarea email-ului și parolei
        if (!email.equals("Admin@cinema.ro") || !password.equals("Admin2025<3")) {
            model.addAttribute("errorMessage", "Email sau parolă incorectă.");
            model.addAttribute("email", email);  // Păstrează email-ul în formular
            return "login";
        }

        return "redirect:/movies";  // Login reușit
    }



    // Pagina principală "/"
    @GetMapping("/movies")
    public String getMoviesPage(Model model) {
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "index";
    }

    // Filtrare filme după dată
    @GetMapping("/filter")
    public String filterMovies(@RequestParam(required = false) String date, Model model) {
        List<Movie> filteredMovies = movieService.filterMovies(date);
        model.addAttribute("movies", filteredMovies);
        return "index";
    }

    // Afișare pagina de editare pentru un film
    @GetMapping("/edit/{id}")
    public String showEditPage(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "edit-movie";  // edit-movie.html
    }
    // Funcție pentru validarea orei
    public boolean isValidTime(String time) {
        // Verificare lungime (trebuie să fie exact "hh:mm")
        if (time == null || time.length() != 5) {
            return false;
        }

        if (time.charAt(2) != ':') {
            return false;
        }

        try {
            int hour = Integer.parseInt(time.substring(0, 2));
            int minute = Integer.parseInt(time.substring(3, 5));

            // Verificăm dacă ora este în intervalul valid 00-23 și minutele între 00-59
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false; // Timpul nu e valid
        }

        return true; // Timpul este valid
    }
    // Salvare modificări pentru un film
    @PostMapping("/update/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute("movie") Movie updatedMovie,Model model) {
        // Verificăm dacă prețurile sunt setate
        if (updatedMovie.getStandardPrice() == null || updatedMovie.getPremiumPrice() == null) {
            model.addAttribute("errorMessage", "Prețurile nu pot fi nule.");
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            return "index";
        }
        //Validare ora
        if (!isValidTime(updatedMovie.getShowtime())) {
            model.addAttribute("errorMessage", "Ora introdusă nu este validă! Trebuie să fie în format hh:mm și în intervalul 00:00 - 23:59.");
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            return "index";
        }
        // Validare diferență preț standard și premium
        LocalDate showDate = updatedMovie.getShowDate();
        if (showDate != null) {
            float standardPrice = updatedMovie.getStandardPrice();
            float premiumPrice = updatedMovie.getPremiumPrice();
            float maxAllowedPremium = 2.5f * standardPrice;

            // Verificăm dacă e duminică
            boolean isSunday = showDate.getDayOfWeek() == DayOfWeek.SUNDAY;

            // Condiții de validare preturi
            boolean invalidSundayPrice = isSunday && (premiumPrice != maxAllowedPremium || premiumPrice <= standardPrice);
            boolean invalidWeekdayPrice = !isSunday && (premiumPrice > maxAllowedPremium || premiumPrice <= standardPrice);

            if (invalidSundayPrice || invalidWeekdayPrice) {
                String message = isSunday
                        ? "Duminica, prețul premium trebuie să fie exact de 2.5 ori mai mare decât prețul standard."
                        : "Prețul premium trebuie să fie mai mare decât prețul standard și nu poate depăși de 2.5 ori prețul lui.";
                model.addAttribute("errorMessage", message);
                List<Movie> movies = movieService.getAllMovies();
                model.addAttribute("movies", movies);
                return "index";
            }
        }



        if (updatedMovie.getDescription() != null && updatedMovie.getDescription().length() > 50) {
            model.addAttribute("errorMessage", "Descrierea nu poate avea mai mult de 50 de caractere.");
            List<Movie> movies = movieService.getAllMovies();
            model.addAttribute("movies", movies);
            return "index";
        }
        movieService.updateMovie(id, updatedMovie);
        return "redirect:/movies";  // Redirect către pagina princip
    }

    // Ștergem filmul
    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable Long id, Model model) {
        movieService.deleteMovie(id);  // Ștergem filmul
        List<Movie> movies = movieService.getAllMovies();  // Obținem lista actualizată de filme
        model.addAttribute("movies", movies);  // Adaugăm lista de filme în model
        return "index";  // Afișam pagina de listă
    }


    // Pagina de adăugare a unui film nou
    @GetMapping("/add")
    public String showAddMoviePage(Model model) {
        model.addAttribute("movie", new Movie());
        List<Movie> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);

        return "add-movie";
    }

    @PostMapping("/add")
    public String addMovie(@ModelAttribute("movie") Movie movie, Model model) {
        if (movie.getStandardPrice() == null || movie.getPremiumPrice() == null) {
            model.addAttribute("errorMessage", "Prețurile nu pot fi nule.");
            return "add-movie";
        }
        // Validare ora
        if (!isValidTime(movie.getShowtime())) {
            model.addAttribute("errorMessage", "Ora introdusă nu este validă! Trebuie să fie în format hh:mm și în intervalul 00:00 - 23:59.");
            model.addAttribute("movie", movie);
            return "add-movie";
        }

        // Verificam diferența dintre prețul standard și cel premium
        LocalDate showDate = movie.getShowDate();
        if (showDate != null) {
            float standardPrice = movie.getStandardPrice();
            float premiumPrice = movie.getPremiumPrice();
            float maxAllowedPremium = 2.5f * standardPrice;

            boolean isSunday = showDate.getDayOfWeek() == DayOfWeek.SUNDAY;

            boolean invalidSundayPrice = isSunday && (premiumPrice != maxAllowedPremium || premiumPrice <= standardPrice);
            boolean invalidWeekdayPrice = !isSunday && (premiumPrice > maxAllowedPremium || premiumPrice <= standardPrice);

            if (invalidSundayPrice || invalidWeekdayPrice) {
                String message = isSunday
                        ? "Duminica, prețul premium trebuie să fie exact de 2.5 ori mai mare decât prețul standard."
                        : "Prețul premium trebuie să fie mai mare decât prețul standard și nu poate depăși de 2.5 ori prețul lui.";
                model.addAttribute("errorMessage", message);
                model.addAttribute("movie", movie);
                return "add-movie";
            }
        }


        if (movie.getDescription() != null && movie.getDescription().length() > 50) {
            model.addAttribute("errorMessage", "Descrierea nu poate avea mai mult de 50 de caractere.");
            model.addAttribute("movie", movie);
            return "add-movie";
        }
        movieService.saveMovie(movie);
        return "redirect:/movies";
    }
}
