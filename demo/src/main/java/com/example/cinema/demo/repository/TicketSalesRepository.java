
/** Interfata pentru vanzarile de bilete
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */
package com.example.cinema.demo.repository;

import com.example.cinema.demo.model.TicketSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketSalesRepository extends JpaRepository<TicketSales, Long> {

}
