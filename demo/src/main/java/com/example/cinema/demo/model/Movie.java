
/** Clasa pentru reprezentarea unui film
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */

package com.example.cinema.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import jakarta.persistence.*;


@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Integer duration;
    private String showtime;
    @Column(name = "standard_price")  // Mapare cu coloana din baza de date
    private Float standardPrice;
    @Column(name = "premiumPrice")
    private Float premiumPrice;
    @Column(name = "show_date")
    private LocalDate showDate;

    // Constructor fără parametri
    public Movie() {}

    // Constructor cu param
    public Movie(String title, String description, Integer duration, String showTime, Float standardPrice, Float premiumPrice, LocalDate showDate) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.showtime = showtime;
        this.standardPrice = standardPrice;
        this.premiumPrice = premiumPrice;
        this.showDate = showDate;
    }

    // Getteri și setteri
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getShowtime() {
        return showtime;
    }

    public void setShowtime(String showtime) {
        this.showtime = showtime;
    }

    public Float getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(Float standardPrice) {
        this.standardPrice = standardPrice;
    }

    public Float getPremiumPrice() {
        return premiumPrice;
    }

    public void setPremiumPrice(Float premiumPrice) {
        this.premiumPrice = premiumPrice;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }
}
