/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.object;

import com.mycompany.entity.Ticket;
import java.util.List;

/**
 *
 * @author macbookpro
 */
public class TicketContainer {
     private List<Ticket> tickets;

    public TicketContainer(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public TicketContainer() {
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }
 
     
}
