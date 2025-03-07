/** Clasa pentru logica aplicatiei cu privire la vanzarile de bilete
 * @author [Fratila Aida-Florina]
 * @version 12 Ianuarie 2025
 */
package com.example.cinema.demo.service;

import com.example.cinema.demo.model.TicketSales;
import com.example.cinema.demo.repository.TicketSalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketSalesService {

    @Autowired
    private TicketSalesRepository ticketSalesRepository;

    // Returnează toate vânzările de bilete
    public List<TicketSales> getAllTicketSales() {
        return ticketSalesRepository.findAll();
    }



}
