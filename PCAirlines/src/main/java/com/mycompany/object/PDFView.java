/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.object;

/**
 *
 * @author macbookpro
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.kernel.font.*;
import com.itextpdf.kernel.font.PdfFontFactory;
import static com.itextpdf.kernel.pdf.PdfName.FontFamily;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.kernel.font.PdfFontFactory;
import static com.itextpdf.kernel.pdf.PdfName.Encoding;
import com.mycompany.entity.Booking;
import com.mycompany.entity.Ticket;
import java.awt.Font;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PDFView extends NetJSAbstractViewPDF {

  @Override
  protected void buildPdfDocument(Map<String, Object> model, Document document, 
      PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
    // List of users that will be displayed in the PDF
   
    List<Ticket> tickets = (List<Ticket>)model.get("ticketList");
   document.add(new Paragraph("Receipt From PCAirlines Service with Booking Code " + tickets.get(0).getBooking().getBookingId()));
     document.add(new Paragraph("Thank you " + tickets.get(0).getBooking().getCustomer().getName()));
 
    // Create table with 6 columns of similar length
    Table table = new Table(new float[]{4, 4, 4, 4, 4 , 4});
    table.setWidth(UnitValue.createPercentValue(100));
    PdfFont bold = PdfFontFactory.createFont("Times-Bold");
    // adding header
    table.addHeaderCell(new Cell().add(new Paragraph("Name").setFont(bold)));
    table.addHeaderCell(new Cell().add(new Paragraph("Departure Place").setFont(bold)));
    table.addHeaderCell(new Cell().add(new Paragraph("Arrival Place").setFont(bold)));
    table.addHeaderCell(new Cell().add(new Paragraph("Ticket Class").setFont(bold)));
    table.addHeaderCell(new Cell().add(new Paragraph("Ticket Type").setFont(bold)));
    table.addHeaderCell(new Cell().add(new Paragraph("Ticket Price").setFont(bold)));
    
    
       // Locale loc = Locale.getDefault();
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
    // adding rows
    for(Ticket ticket : tickets) {
      table.addCell(ticket.getNamePersonBuyTicket());
      table.addCell(ticket.getFlight().getFlightRoute().getDeparturePlace().getCityName());
      table.addCell(ticket.getFlight().getFlightRoute().getArrivalPlace().getCityName());
      table.addCell(ticket.getTicketType().getTicketTypeName());
      table.addCell(ticket.getTicketClass().getClassName());
      
      table.addCell(String.valueOf(nf.format(ticket.getPrice())));
    }
    
    document.add(table);
    document.add(new Paragraph("Total Price :"  + String.valueOf(nf.format(tickets.get(0).getBooking().getTotalMoney()))));
    document.add(new Paragraph("For more information about our service  visit PCAirlines.com"));
    
  }

   
}
