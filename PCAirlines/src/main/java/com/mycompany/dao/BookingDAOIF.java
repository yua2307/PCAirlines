/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.dao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.mycompany.entity.*;
/**
 *
 * @author macbookpro
 */
@Repository
public interface BookingDAOIF extends CrudRepository<Booking, Integer>  {
    
}
