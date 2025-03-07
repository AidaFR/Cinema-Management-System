/** Interfata pentru filme
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */
package com.example.cinema.demo.repository;

import com.example.cinema.demo.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.time.LocalDate;


@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m FROM Movie m WHERE :date IS NULL OR m.showDate = :date")
    List<Movie> findByShowDate(@Param("date") LocalDate date);
}

