
/** Clasa pentru reprezentarea vanzarilor de bilete
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */


package com.example.cinema.demo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ticket_sales")  // Mapează către tabelul din baza de date
public class TicketSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "standard_tickets_sold", nullable = false)
    private Integer standardTicketsSold;

    @Column(name = "premium_tickets_sold", nullable = false)
    private Integer premiumTicketsSold;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    // Constructor fără param
    public TicketSales() {}

    // Constructor cu param
    public TicketSales(Movie movie, Integer standardTicketsSold, Integer premiumTicketsSold, LocalDate saleDate) {
        this.movie = movie;
        this.standardTicketsSold = standardTicketsSold;
        this.premiumTicketsSold = premiumTicketsSold;
        this.saleDate = saleDate;
    }

    // Getteri și setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Integer getStandardTicketsSold() {
        return standardTicketsSold;
    }

    public void setStandardTicketsSold(Integer standardTicketsSold) {
        this.standardTicketsSold = standardTicketsSold;
    }

    public Integer getPremiumTicketsSold() {
        return premiumTicketsSold;
    }

    public void setPremiumTicketsSold(Integer premiumTicketsSold) {
        this.premiumTicketsSold = premiumTicketsSold;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}
