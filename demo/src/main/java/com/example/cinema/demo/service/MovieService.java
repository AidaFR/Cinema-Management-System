/** Clasa pentru logica aplicatiei legata de filme
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */
package com.example.cinema.demo.service;

import com.example.cinema.demo.model.Movie;
import com.example.cinema.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDate;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> filterMovies(String date) {
        LocalDate parsedDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
        return movieRepository.findByShowDate(parsedDate);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public void saveMovie(Movie movie) {
        movieRepository.save(movie);
    }
    @Transactional
    public void updateMovie(Long id, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Filmul nu există!"));
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setDuration(updatedMovie.getDuration());
        existingMovie.setShowDate(updatedMovie.getShowDate());
        existingMovie.setShowtime(updatedMovie.getShowtime());
        existingMovie.setStandardPrice(updatedMovie.getStandardPrice());
        existingMovie.setPremiumPrice(updatedMovie.getPremiumPrice());
        movieRepository.save(existingMovie);  // Salvare în baza de date
    }
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

}