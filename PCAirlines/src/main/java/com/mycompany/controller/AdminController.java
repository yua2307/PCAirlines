/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import com.mycompany.entity.Customer;
import com.mycompany.entity.UserRole;
import com.mycompany.service.CustomerService;
import com.mycompany.service.UserRoleService;
import com.mycompany.entity.Aircaft;
import com.mycompany.entity.AirlineBrand;
import com.mycompany.entity.Airport;
import com.mycompany.entity.Flight;
import com.mycompany.entity.FlightRoute;
import com.mycompany.object.InforAircraft;
import com.mycompany.object.InforFlight;
import com.mycompany.object.InforFlightRoute;
import com.mycompany.service.AircraftService;
import com.mycompany.service.AirlineBrandService;
import com.mycompany.service.AirportService;
import com.mycompany.service.FlightRouteService;
import com.mycompany.service.FlightService;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserRoleService userRoleService;
    
    @Autowired
    CustomerService customerService;
    

    @Autowired
    AircraftService aircraftService;
    
    @Autowired
    AirportService airportService;
    
    @Autowired
    AirlineBrandService airlineBrandService;
    
    @Autowired
    FlightRouteService flightRouteService;
    
    @Autowired
    FlightService flightService;

    @GetMapping("/index")
    public String hello(Model model, Principal principal, HttpSession session, HttpServletRequest request) {
        String userName = principal.getName();

        model.addAttribute("message", userName);
        
        session.setAttribute("username", userName);
        String uname = (String) request.getSession().getAttribute("uname");

        model.addAttribute("session", uname);
        return "admin/adminPage";
    }

    @GetMapping("/addAirport")
    public String addAirport(@ModelAttribute("result") String result, Model model , HttpServletRequest request) {
        List<Airport> list = airportService.getAllAirport();
        
        PagedListHolder pagedListHolder = new PagedListHolder(list);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
        model.addAttribute("result", result);
       // model.addAttribute("list", list);
        model.addAttribute("airport", new Airport());
        return "admin/addAirport";
    }

    @GetMapping("/addAircraft")
    public String addAircraft(@ModelAttribute("result") String result, Model model, HttpServletRequest request) {
        List<AirlineBrand> airlinebrandList = airlineBrandService.getAllAirlineBrand();
        List<Aircaft> templistAircraft = aircraftService.getAllAircaft();
           
        PagedListHolder pagedListHolder = new PagedListHolder(templistAircraft);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
        //model.addAttribute("listAircraft", templistAircraft);
        model.addAttribute("result", result);
        model.addAttribute("airlinebrandList", airlinebrandList);
        model.addAttribute("aircraft", new InforAircraft());
        return "admin/addAircraft";
    }

    @GetMapping("/addAirbrand")
    public String addAirbrand() {
        return "admin/addAirbrand";
    }

    @GetMapping("/addAirroute")
    public String addAirroute(@ModelAttribute("result") String result, Model model, HttpServletRequest request) {
        List<Airport> airportList = airportService.getAllAirport();
        List<FlightRoute> flightRouteList = flightRouteService.getAllFlightRoute();
        model.addAttribute("flightRoute", new InforFlightRoute());
        
        
        PagedListHolder pagedListHolder = new PagedListHolder(flightRouteList);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
        model.addAttribute("result", result);
        model.addAttribute(airportList);
        return "admin/addAirroute";
    }

    @GetMapping("/addFlight")
    public String addFlight(@ModelAttribute("result") String result, Model model , HttpServletRequest request) {
        List<FlightRoute> flightRouteList = flightRouteService.getAllFlightRoute();
        List<Aircaft> aircraftList = aircraftService.getAllAircaft();
        List<Flight> flightList = flightService.getAllFlight();
        PagedListHolder pagedListHolder = new PagedListHolder(flightList);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
        model.addAttribute("result", result);
        model.addAttribute("flightRouteList",flightRouteList);
        model.addAttribute("aircraftList",aircraftList);
        model.addAttribute("flight", new InforFlight());
        return "admin/addFlight";
    }

    @GetMapping("/addAdmin")
    public String addAdmin(Model model){
        model.addAttribute("customer", new Customer());
        return "/admin/addAdmin";
    }
    
    @PostMapping("/addAdminProcess")
    public String addAdminProcess(@ModelAttribute("customer") Customer customer, Model model){
        Customer check = customerService.getCustomerByUsername(customer.getUsername());
        if (check!=null){
            model.addAttribute("exist","Tên đăng nhập đã có người sử dụng");
            return "/admin/addAdmin";
        }
        customer.setEnabled(true);
        customerService.saveCustomer(customer);
        UserRole userRole = new UserRole();
        userRole.setCustomer(customer);
        userRole.setUserName(customer.getUsername());
        userRole.setUserRole("ROLE_ADMIN");
        userRoleService.saveUserRole(userRole);
//        customer.getListUserRole().add(userRole);
//        customerService.saveCustomer(customer);
        return"redirect:/admin/allAdmin";
    }
    public String addAdmin() {
        return "admin/addAdmin";
    }


    @GetMapping("/allCustomer")
    public String allCustomer(Model model, HttpServletRequest request) {
        List<Customer> listCustomer = userRoleService.getListCustomerByRole("ROLE_USER");
        PagedListHolder pagedListHolder = new PagedListHolder(listCustomer);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
        
     //  model.addAttribute("listCustomer", listCustomer);
        return "admin/allCustomer";
    }
    
     @GetMapping("/allAdmin")
    public String allAdmin(Model model, HttpServletRequest request){
         List<Customer> listAdmin = userRoleService.getListCustomerByRole("ROLE_ADMIN");
         PagedListHolder pagedListHolder = new PagedListHolder(listAdmin);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
         
       //  model.addAttribute("listAdmin", listAdmin);
        return "admin/allAdmin";
    }
    
    @GetMapping("searchCustomer")
    public String searchCustomer(@RequestParam("term") String searchInfo, Model model, HttpServletRequest request){
        List<Customer> listCustomerCheck = new ArrayList<Customer>();
        if (StringUtils.isEmpty(searchInfo)) {
            return "redirect:/admin/allCustomer";
        }
        List<Customer> listCustomer = userRoleService.getListCustomerByRole("ROLE_USER");
        for (Customer customer : listCustomer) {
            if(customer.getName().contains(searchInfo) || customer.getAddress().contains(searchInfo) 
                    || customer.getEmail().contains(searchInfo) || customer.getIdentifyNumber().contains(searchInfo) || customer.getPhoneNumber().contains(searchInfo))
                listCustomerCheck.add(customer);
        }
        
        PagedListHolder pagedListHolder = new PagedListHolder(listCustomerCheck);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
     //   model.addAttribute("listCustomer", listCustomerCheck);
        return "admin/allCustomer";
    }
    @GetMapping("searchAdmin")
    public String searchAdmin(@RequestParam("term") String searchInfo, Model model, HttpServletRequest request){
        List<Customer> listAdminCheck = new ArrayList<Customer>();
        if (StringUtils.isEmpty(searchInfo)) {
            return "redirect:/admin/allAdmin";
        }
         List<Customer> listAdmin = userRoleService.getListCustomerByRole("ROLE_ADMIN");

        
         for (Customer customer : listAdmin) {
            if(customer.getName().contains(searchInfo) || customer.getAddress().contains(searchInfo) 
                    || customer.getEmail().contains(searchInfo) || customer.getIdentifyNumber().contains(searchInfo)
                        || customer.getPhoneNumber().contains(searchInfo))
                listAdminCheck.add(customer);
        }
         
         PagedListHolder pagedListHolder = new PagedListHolder(listAdminCheck);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(5);
        model.addAttribute("pagedListHolder", pagedListHolder);
        //model.addAttribute("listAdmin", listAdminCheck);
        return "admin/allAdmin";
    }
    
    
   
    @GetMapping("/lockCustomer")
    public String lockCustomer(@RequestParam("customerID") int id){
        Customer cusFind = customerService.getCustomerById(id);
        cusFind.setEnabled(false);
        customerService.saveCustomer(cusFind);
        return "redirect:/admin/allCustomer";
    }
    @GetMapping("/unlockCustomer")
    public String unlockCustomer(@RequestParam("customerID") int id){
        Customer cusFind = customerService.getCustomerById(id);
        cusFind.setEnabled(true);
        customerService.saveCustomer(cusFind);
        return "redirect:/admin/allCustomer";
    }
    @PostMapping("/addAirportProcess")
    public String addAirportProcess(@ModelAttribute("airport") Airport airport, Model model) {
        airportService.saveAiport(airport);
        model.addAttribute("result", "Them san bay thanh cong");
        return "redirect:/admin/addAirport";
    }

    @PostMapping("/addAircraftProcess")
    public String addAircraftProcess(@ModelAttribute("aircraft") InforAircraft inforAircraft, Model model) {
        Aircaft aircraft = new Aircaft();
        aircraft.setAircraftName(inforAircraft.getAircraftName());
        aircraft.setModel(inforAircraft.getModel());
        aircraft.setSeatNumber(inforAircraft.getSeatNumber());
        AirlineBrand a = airlineBrandService.getAirlineBrandById(inforAircraft.getAirlineBrand());
        aircraft.setAirlineBrand(a);
        aircraftService.saveAircraft(aircraft);
        model.addAttribute("result", "Thêm máy bay thành công");
        return "redirect:/admin/addAircraft";
    }
    
    @PostMapping("/addAirrouteProcess")
    public String addAirrouteProcess(@ModelAttribute("flightRoute") InforFlightRoute inforFlightRoute, Model model) {
        FlightRoute flightRoute = new FlightRoute();
        Airport from = airportService.getAirportById(inforFlightRoute.getDeparturePlace());
        Airport to = airportService.getAirportById(inforFlightRoute.getArrivalPlace());
        flightRoute.setStandardPrice(inforFlightRoute.getStandardPrice());
        flightRoute.setDeparturePlace(from);
        flightRoute.setArrivalPlace(to);
        flightRouteService.saveFlightRoute(flightRoute);
        model.addAttribute("result", "Thêm đường bay thành công");
        return "redirect:/admin/addAirroute";
    }
    
    @PostMapping("/addFlightProcess")
    public String addFlightProcess(@ModelAttribute("flight") InforFlight inforFlight, Model model) throws ParseException {
        FlightRoute flightRoute =  flightRouteService.getFlightRouteById(inforFlight.getFlightRoute());
        Aircaft aircraft = aircraftService.getAircaftById(inforFlight.getAircraft());
        Flight flight = new Flight();
        flight.setAircaft(aircraft);
        flight.setFlightRoute(flightRoute);
        
        String date1 = inforFlight.getDepartureTime().substring(0, 10);
        String time1 = inforFlight.getDepartureTime().substring(11, 16);
        String date2 = inforFlight.getArrivalTime().substring(0, 10);
        String time2 = inforFlight.getArrivalTime().substring(11, 16);
        
        Date departureTime=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date1+" "+time1);  
        Date arrivalTime=new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(date2+" "+time2);  
        
        flight.setDepartureTime(departureTime);
        flight.setArrivalTime(arrivalTime);
        
        flight.setTotalSeat(aircraft.getSeatNumber());
        flight.setAvailableSeat(aircraft.getSeatNumber());
        flight.setOrderSeat(0);
        flightService.saveFlight(flight);
        model.addAttribute("result", "Thêm chuyến bay thành công");
        return "redirect:/admin/addFlight";
    }
}
