/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.service;

import com.mycompany.dao.UserRoleDAOIF;
import com.mycompany.entity.Customer;
import com.mycompany.entity.Flight;
import com.mycompany.entity.UserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author DELL
 */
@Service
public class UserRoleService {

    @Autowired
    UserRoleDAOIF userRoleDAO;
    
    
    public UserRole getUserRoleById(int id){
         Optional<UserRole> userRoleOpt = userRoleDAO.findById(id);
	return userRoleOpt.isPresent() ? userRoleOpt.get() : null;
    }
    
    @Transactional(rollbackFor={Exception.class})
    public void saveUserRole(UserRole userRole) {
        userRoleDAO.save(userRole);
    }
    
    public UserRole getUserRoleByName (String role){
           Optional<UserRole> userRoleOpt = userRoleDAO.findByuserRole(role);
	return userRoleOpt.isPresent() ? userRoleOpt.get() : null;
    }
    
    public List<Customer> getListCustomerByRole (String role){
        List<UserRole> listUserRoles = (List<UserRole>) userRoleDAO.findAll();
        List<Customer> listCustomer = new ArrayList<Customer>();
        for (UserRole listUserRole : listUserRoles) {
            if(listUserRole.getUserRole().equalsIgnoreCase(role))
            {
                listCustomer.add(listUserRole.getCustomer());
            }
        }
        return listCustomer;
    }
}
