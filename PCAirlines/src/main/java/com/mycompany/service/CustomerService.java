/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mycompany.dao.*;
import com.mycompany.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author macbookpro
 */
@Service
public class CustomerService {

    @Autowired
    CustomerDAOIF customerDAO;
    
    @Transactional(rollbackFor={Exception.class})
    public void saveCustomer(Customer customer) {
        customerDAO.save(customer);
    }

    public Customer getCustomerByUsername(String username) {
        Optional<Customer> customerOpt = customerDAO.findByUsername(username);
        return customerOpt.isPresent() ? customerOpt.get() : null;
    }
    
    public Customer updateCustomer(Customer customer){
        return customerDAO.save(customer);
    }
    
    public Customer getCustomerById(int id){
      Optional<Customer> customerOpt = customerDAO.findById(id);
        return customerOpt.isPresent() ? customerOpt.get() : null;
    }
    
    public List<Customer> getAllCustomer(){
        return (List<Customer>)  customerDAO.findAll();
        
    }
    
    public List<Customer> getCustomersByName(String name){
        return (List<Customer>) customerDAO.findByNameContaining(name);
    }
    
}
