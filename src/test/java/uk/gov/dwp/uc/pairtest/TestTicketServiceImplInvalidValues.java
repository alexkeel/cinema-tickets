package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class TestTicketServiceImplInvalidValues {
    // Create a set of invalid tests to be
    @Parameterized.Parameters(name= "Invalid Data: {index}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new TicketTypeRequest(TicketTypeRequest.Type.valueOf("nothing"), -1), -0L},
                {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 21), -0L},
                {new TicketTypeRequest(TicketTypeRequest.Type.valueOf("x"), -50), -1L},
                {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, -999), -999L},
                {new TicketTypeRequest(TicketTypeRequest.Type.valueOf("nothing"), 51), -9223372036854775808L},
                {new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10), -100000000000L}});
    }

    private final TicketTypeRequest invalidRequest;
    private final Long invalidAccountNo;

    public TestTicketServiceImplInvalidValues(final TicketTypeRequest invalidRequest, final Long invalidAccountNo) {
        this.invalidRequest = invalidRequest;
        this.invalidAccountNo = invalidAccountNo;
    }

    @Test (expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsInvalidTicketRequest() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, invalidRequest);
    }

    @Test (expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsInvalidAccountId() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(invalidAccountNo, validRequest);
    }

    @Test (expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsTestNoAdult() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        final TicketTypeRequest validRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, validRequest1, validRequest2);
    }

    @Test (expected = InvalidPurchaseException.class)
    public void testPurchaseTicketsMoreInfantsThanAdults() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 4);
        final TicketTypeRequest validRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, validRequest1, validRequest2);
    }
}
