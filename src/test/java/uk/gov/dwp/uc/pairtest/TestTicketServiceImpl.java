package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import static org.mockito.Mockito.mock;

// For non parameterised tests
public class TestTicketServiceImpl {

    @Test
    public void testPurchaseTicketsMultipleRequests() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validRequest1 = new TicketTypeRequest(Type.ADULT, 1);
        final TicketTypeRequest validRequest2 = new TicketTypeRequest(Type.INFANT, 2);
        final TicketTypeRequest validRequest3 = new TicketTypeRequest(Type.CHILD, 1);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, validRequest1, validRequest2, validRequest3);
        Mockito.verify(mockPaymentService, Mockito.times(1)).makePayment(1L,  30);
        Mockito.verify(mockReservationService, Mockito.times(1)).reserveSeat(1L, 2);
    }

    @Test (expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsTestNoAdult() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validRequest1 = new TicketTypeRequest(Type.INFANT, 2);
        final TicketTypeRequest validRequest2 = new TicketTypeRequest(Type.CHILD, 1);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, validRequest1, validRequest2);
    }
}
