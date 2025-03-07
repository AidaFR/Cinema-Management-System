
/** Clasa pentru gestionarea de cereri pentru vanzarile de bilete
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */
package com.example.cinema.demo.controller;

import com.example.cinema.demo.model.Movie;
import com.example.cinema.demo.model.TicketSales;
import com.example.cinema.demo.service.MovieService;
import com.example.cinema.demo.service.TicketSalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.Map;  // pentru Map
import java.util.ArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TicketSalesController {

    @Autowired
    private TicketSalesService ticketSalesService;

    @Autowired
    private MovieService movieService;

    // Afișare pagina de vânzări de bilete
    @GetMapping("/sales")
    public String getSalesPage(Model model) {
        List<TicketSales> salesList = ticketSalesService.getAllTicketSales();  // Obținem toate vânzările
        model.addAttribute("sales", salesList);  // Afișăm lista de vânzări

        calculateStatistics(salesList, model);

        return "ticketsales";
    }
    @GetMapping("/sales/filter")
    public String getFilteredSalesPage(@RequestParam("filterDate") String filterDate, Model model) {
        // Verificăm dacă data introdusă este goală
        if (filterDate == null || filterDate.isEmpty()) {
            model.addAttribute("errorMessage", "Vă rugăm să introduceți o dată în format yyyy-MM-dd.");
            model.addAttribute("sales", ticketSalesService.getAllTicketSales());
            calculateStatistics(ticketSalesService.getAllTicketSales(), model);
            return "ticketsales";
        }

        // Verificam ca data sa fie in formatul cerut
        if (filterDate.length() != 10 || filterDate.charAt(4) != '-' || filterDate.charAt(7) != '-' ||
                !isDigits(filterDate.substring(0, 4)) || !isDigits(filterDate.substring(5, 7)) || !isDigits(filterDate.substring(8, 10))) {
            model.addAttribute("errorMessage", "Data introdusă trebuie să respecte formatul yyyy-MM-dd.");
            model.addAttribute("sales", ticketSalesService.getAllTicketSales());
            calculateStatistics(ticketSalesService.getAllTicketSales(), model);
            return "ticketsales";
        }

        List<TicketSales> allSales = ticketSalesService.getAllTicketSales();
        List<TicketSales> filteredSales = new ArrayList<>();

        // Filtrare după data introdusă
        for (TicketSales sale : allSales) {
            String saleDateStr = sale.getSaleDate().toString();  // Convertim LocalDate la String
            if (saleDateStr.compareTo(filterDate) >= 0) {
                filteredSales.add(sale);
            }
        }

        // Afișăm vânzările filtrate
        model.addAttribute("sales", filteredSales);
        model.addAttribute("filterDate", filterDate);  // Păstrăm input-ul

        calculateStatistics(allSales, model);

        return "ticketsales";
    }

    private void calculateStatistics(List<TicketSales> salesList, Model model) {

        List<Movie> moviesWithLowestSales = new ArrayList<>();
        List<Movie> topRevenueMovies = new ArrayList<>();
        List<Movie> mostPremiumSoldMovies = new ArrayList<>();
        List<Movie> mostStandardSoldMovies = new ArrayList<>();

        double maxRevenue = 0.0;
        int maxPremiumTickets = 0;
        int maxStandardTickets = 0;
        int minTotalTickets = Integer.MAX_VALUE;

        // Parcurgem vânzările și calculăm statisticile
        for (TicketSales sale : salesList) {
            Movie currentMovie = sale.getMovie();
            int totalTicketsSold = sale.getStandardTicketsSold() + sale.getPremiumTicketsSold();
            double revenue = sale.getStandardTicketsSold() * currentMovie.getStandardPrice() +
                    sale.getPremiumTicketsSold() * currentMovie.getPremiumPrice();

            if (totalTicketsSold < minTotalTickets) {
                moviesWithLowestSales.clear();
                moviesWithLowestSales.add(currentMovie);
                minTotalTickets = totalTicketsSold;
            } else if (totalTicketsSold == minTotalTickets) {
                moviesWithLowestSales.add(currentMovie);
            }

            if (revenue > maxRevenue) {
                topRevenueMovies.clear();
                topRevenueMovies.add(currentMovie);
                maxRevenue = revenue;
            } else if (revenue == maxRevenue) {
                topRevenueMovies.add(currentMovie);
            }

            if (sale.getPremiumTicketsSold() > maxPremiumTickets) {
                mostPremiumSoldMovies.clear();
                mostPremiumSoldMovies.add(currentMovie);
                maxPremiumTickets = sale.getPremiumTicketsSold();
            } else if (sale.getPremiumTicketsSold() == maxPremiumTickets) {
                mostPremiumSoldMovies.add(currentMovie);
            }

            if (sale.getStandardTicketsSold() > maxStandardTickets) {
                mostStandardSoldMovies.clear();
                mostStandardSoldMovies.add(currentMovie);
                maxStandardTickets = sale.getStandardTicketsSold();
            } else if (sale.getStandardTicketsSold() == maxStandardTickets) {
                mostStandardSoldMovies.add(currentMovie);
            }
        }

        String lowestSalesMoviesMessage = "Filmele cu cele mai puține bilete vândute: " +
                String.join(", ", getMovieTitles(moviesWithLowestSales)) + ". Necesita promovare!";
        String topRevenueMoviesMessage =
                String.join(", ", getMovieTitles(topRevenueMovies));
        String mostPremiumMoviesMessage =
                String.join(", ", getMovieTitles(mostPremiumSoldMovies));
        String mostStandardMoviesMessage =
                String.join(", ", getMovieTitles(mostStandardSoldMovies));

        model.addAttribute("promotionMessage", lowestSalesMoviesMessage);
        model.addAttribute("topRevenueMoviesMessage", topRevenueMoviesMessage);
        model.addAttribute("mostPremiumMoviesMessage", mostPremiumMoviesMessage);
        model.addAttribute("mostStandardMoviesMessage", mostStandardMoviesMessage);
    }


    private List<String> getMovieTitles(List<Movie> movies) {
        List<String> titles = new ArrayList<>();
        for (Movie movie : movies) {
            titles.add(movie.getTitle());
        }
        return titles;
    }


    // Funcție pt verificarea dacă un string conține doar cifre
    private boolean isDigits(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}

