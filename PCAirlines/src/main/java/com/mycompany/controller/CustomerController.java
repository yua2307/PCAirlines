/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.controller;

import com.mycompany.entity.Booking;
import com.mycompany.entity.CreditCard;
import com.mycompany.entity.Customer;
import com.mycompany.entity.Flight;
import com.mycompany.entity.Ticket;
import com.mycompany.object.PaymentInfo;
import com.mycompany.object.SearchInformation;
import com.mycompany.object.TicketBooked;
import com.mycompany.object.TicketContainer;
import com.mycompany.object.TicketInfo;
import com.mycompany.service.BookingService;
import com.mycompany.service.CreditCardService;
import com.mycompany.service.CustomerService;
import com.mycompany.service.FlightRouteService;
import com.mycompany.service.FlightService;
import com.mycompany.service.TicketClassService;
import com.mycompany.service.TicketService;
import com.mycompany.service.TicketTypeService;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpRequest;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import jdk.tools.jlink.internal.plugins.StringSharingPlugin;

import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author DELL
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    FlightRouteService flightRouteService;

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    BookingService bookingService;

    @Autowired
    TicketService ticketService;

    @Autowired
    FlightService flightService;

    @Autowired
    TicketClassService ticketClassService;

    @Autowired
    TicketTypeService ticketTypeService;

    @Autowired
    CustomerService customerService;

    // Email 
    @Autowired
    JavaMailSender mailSenderObj;

    static final String emailFromRecipient = "pcairlinescompany@gmail.com";

    @GetMapping("/index")
    public String loginProcess(Model model, Principal principal, HttpSession session) {
        String userName = principal.getName();
        System.out.println("User Name: " + userName);
        model.addAttribute("message", userName);
        List<String> listPlace = flightRouteService.getAllPlace();
        model.addAttribute("listPlace", listPlace);
        model.addAttribute("searchInformation", new SearchInformation());
        session.setAttribute("username", userName);
        //login processing here
        return "index";
    }

    @PostMapping(value = "bookTicket")
    public String showFormAddInfo(@ModelAttribute("ticketBooked") TicketBooked t, Model model, HttpSession session) throws ParseException {
        Booking booking = new Booking();
        Flight flight = flightService.getFlightById(t.getFlightId());
        int availableSeat = flight.getAvailableSeat();
        flight.setAvailableSeat(availableSeat - t.getNumberAdult() - t.getNumberChildren());
        int orderSeat = flight.getOrderSeat();
        flight.setOrderSeat(orderSeat + t.getNumberAdult() + t.getNumberChildren());
        booking.setNumberTicketBuy(t.getNumberAdult() + t.getNumberChildren());

        booking.setTotalMoney(t.getNumberAdult() * t.getPriceAdultUnit() + t.getNumberChildren() * t.getPriceChildrenUnit());

        // set current date for booking
        Date date = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Customer customerFind = customerService.getCustomerByUsername((String) session.getAttribute("username"));
        booking.setCustomer(customerFind);

        booking.setPaymentDate(fmt.parse(fmt.format(date)));
        session.setAttribute("booking", booking);
        model.addAttribute("ticketInfo", new TicketInfo());
        session.setAttribute("flight", flight);
        session.setAttribute("seatClassId", t.getSeatClassId());
        session.setAttribute("customerId", t.getCustomerID());
        session.setAttribute("numberAdult", t.getNumberAdult());
        session.setAttribute("numberChildren", t.getNumberChildren());
        session.setAttribute("bookingId", booking.getBookingId());
        session.setAttribute("ticketClassId", t.getSeatClassId());
        session.setAttribute("priceChildrenUnit", t.getPriceChildrenUnit());
        session.setAttribute("priceAdultUnit", t.getPriceAdultUnit());
        return "bookTicket";
    }

    @PostMapping(value = "ticketInfo")
    public String ticketInfo(@ModelAttribute("ticketInfo") TicketInfo t, Model model, HttpSession session) throws ParseException {
       
        
        session.setAttribute("ticketInfo", t);
        Flight flight = (Flight) session.getAttribute("flight");
        Booking booking = (Booking) session.getAttribute("booking");
        List<Ticket> listTicketSession = new ArrayList<>();
        // save ticket adult
        if (t.getListNameAdultGo() != null) {
            for (int i = 0; i < t.getListNameAdultGo().size(); i++) {
                Ticket ticket = new Ticket();

                ticket.setBooking(booking);
                ticket.setPrice(t.getPriceUnitAdult());
                ticket.setFlight(flightService.getFlightById(t.getFlightId()));
                ticket.setTicketClass(ticketClassService.getTicketClassById(t.getTicketClassId()));
                // set current Date
                Date date = new Date();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ticket.setDateBuyTicket(date);
                // set current Date

                // set ticket type Adult
                ticket.setTicketType(ticketTypeService.getTicketTypeById(2));

                ticket.setNamePersonBuyTicket(t.getListNameAdultGo().get(i));
                ticket.setIdentify(t.getListIdentify().get(i));
                listTicketSession.add(ticket);
                // ticketService.saveTicket(ticket);
            }
        }
        // save ticket Children
        if (t.getListNameChildrenGo() != null) {
            for (int i = 0; i < t.getListNameChildrenGo().size(); i++) {
                Ticket ticket = new Ticket();
                ticket.setBooking(booking);
                ticket.setPrice(t.getPriceUnitChildren());
                ticket.setFlight(flightService.getFlightById(t.getFlightId()));
                ticket.setTicketClass(ticketClassService.getTicketClassById(t.getTicketClassId()));
                // set current Date
                Date date = new Date();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                ticket.setDateBuyTicket(fmt.parse(fmt.format(date)));
                // set current Date

                // set ticket type Children
                ticket.setTicketType(ticketTypeService.getTicketTypeById(1));

                ticket.setNamePersonBuyTicket(t.getListNameChildrenGo().get(i));
                listTicketSession.add(ticket);
                //   ticketService.saveTicket(ticket);
            }
        }
        session.setAttribute("booking", booking);
        session.setAttribute("flight", flight);
        session.setAttribute("listTicket", listTicketSession);
        model.addAttribute("paymentInfo", new PaymentInfo());
        return "payment";
    }

    @PostMapping(value = "payTicket")
    public String payTicket(@ModelAttribute("paymentInfo") PaymentInfo payInfo, Model model, HttpSession session) {
        
        String message = (String) session.getAttribute("message");
        if(message!=null){
            session.removeAttribute(message);
        }
        Booking booking = (Booking) session.getAttribute("booking");
        Flight flight = (Flight) session.getAttribute("flight");
        flightService.saveFlight(flight);
        List<Ticket> listTicket = (List<Ticket>) session.getAttribute("listTicket");

        // Valid Credit Card
        CreditCard creditCard = creditCardService.getCreditCardBy(payInfo.getCreditCardNumber(), payInfo.getNameOnCard(), payInfo.getCCV());
        if (creditCard == null) {
            session.setAttribute("message", "Thông tin thanh toán sai , quý khách vui lòng nhập lại");
            return "payment";
        } else {
            // Valid Credit Card
            double balance = creditCard.getBalance();
            if (balance - booking.getTotalMoney() >= 0) {
                creditCard.setBalance(balance - booking.getTotalMoney());
                booking.setCreditCard(creditCard);
                bookingService.saveBooking(booking);
                for (Ticket ticket : listTicket) {
                    ticketService.saveTicket(ticket);
                }
                // creditCard.getListBooking().add(booking);
                creditCardService.save(creditCard);
                model.addAttribute("listTicket", listTicket);
                model.addAttribute("booking", booking);
                // sending Email 
                String bookingCode = String.valueOf(booking.getBookingId());
                String emailSubject = "Hoá đơn đặt vé máy bay PCAirlines ";
                String emailMessage = "Thân gửi, " + booking.getCustomer().getName()
                        + "\n\n"
                        + "Cảm ơn bạn đã sử dụng dịch vụ đặt vé của PCAirlines. \n Email này xác nhận giao dịch đặt chỗ của bạn .\n"
                        + "Đây là mã code đặt chỗ cua bạn : " + bookingCode + " \n\n"
                        + "Nếu có câu hỏi gì hãy liên ngay với chúng tôi tại trang chủ của PCAirlines.\n"
                        + "\n"
                        + "Nếu bạn muốn in hoá đơn, hãy bấm vào đường link này http://localhost:8083/PCAirlines/customer/viewPDF?bookingID=" + booking.getBookingId() + " \n"
                        + "\n"
                        + "Trân trọng,\n"
                        + "\n"
                        + "PCAirlines\n"
                        + "\n";
                String emailToRecipient = booking.getCustomer().getEmail();
                System.out.println("\nReceipient?= " + emailToRecipient + ", Subject?= " + emailSubject + ", Message?= " + emailMessage + "\n");

                mailSenderObj.send(new MimeMessagePreparator() {
                    public void prepare(MimeMessage mimeMessage) throws Exception {

                        MimeMessageHelper mimeMsgHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        mimeMsgHelperObj.setTo(emailToRecipient);
                        mimeMsgHelperObj.setFrom(emailFromRecipient);
                        mimeMsgHelperObj.setText(emailMessage);
                        mimeMsgHelperObj.setSubject(emailSubject);

                    }
                });
                System.out.println("\nMessage Send Successfully.... Hurrey!\n");

                // free session
                session.removeAttribute("flight");
                session.removeAttribute("booking");
                session.removeAttribute("listTicket");
                session.removeAttribute("ticketInfo");
                return "paymentReceipt";
            } else {
                session.setAttribute("message", "Tài khoản của quý khách không đủ tiền để thanh toán");
                return "payment";
            }
        }
    }

    @RequestMapping(value = "/viewPDF", method = RequestMethod.GET)
    public ModelAndView viewPDF(@RequestParam("bookingID") int bookingID) throws Exception {

        List<Ticket> ticketList = bookingService.getBookingById(bookingID).getListTicket();
        // List<User> users = userList.getUsers();
        return new ModelAndView("viewPDF", "ticketList", ticketList);
    }

    @GetMapping("/redirectCustomer")
    public String redirectCustomer(Principal principal, HttpSession session) {
        String userName = principal.getName();
        Flight flight = new Flight();
        flight = (Flight) session.getAttribute("flight");
        if (flight != null) {
            session.setAttribute("username", userName);
            return "redirect:/customer/bookTicket";
        } else {
            return "redirect:/customer/index";
        }
    }

    @GetMapping(value = "history")
    public String showHistory(Model model, HttpSession session, HttpServletRequest request) {
        String userName = (String) session.getAttribute("username");
        Customer customerFind = customerService.getCustomerByUsername(userName);
        List<Booking> listBooking = customerFind.getListBooking();

        PagedListHolder pagedListHolder = new PagedListHolder(listBooking);
        int page = ServletRequestUtils.getIntParameter(request, "p", 0);
        pagedListHolder.setPage(page);
        pagedListHolder.setPageSize(2);
        model.addAttribute("pagedListHolder", pagedListHolder);
        //model.addAttribute("listBooking", listBooking);
        return "transactionHistory";
    }

    @GetMapping(value = "paymentMethod")
    public String showPaymentMethod(Model model) {
        model.addAttribute("paymentInfo", new PaymentInfo());
        return "paymentMethod";
    }

    @PostMapping(value = "addCreditCard")
    public String addCreditCard(@ModelAttribute("paymentInfo") PaymentInfo payInfo, Model model, HttpSession session) {
        CreditCard creditCard = new CreditCard();
        creditCard.setCreditCardNumber(payInfo.getCreditCardNumber());
        creditCard.setNameOnCard(payInfo.getNameOnCard());
        creditCard.setType("VISA");
        creditCard.setCCV(payInfo.getCCV());
        creditCard.setBalance(10000000);
        creditCardService.save(creditCard);
        TicketInfo t = (TicketInfo) session.getAttribute("ticketInfo");
        if(t!=null){
              return "payment";
        }
        else {
             return "redirect:/index";
        }
           
      //   model.addAttribute("ticketInfo", t);
        //session.removeAttribute("ticketInfo");
        // return "payment";
    }
    
    
}
