package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.Arrays;

import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class TestTicketServiceImplValidValues {
    // Create a set of Valid tests parameters, test items are: {  ticket request, customer ID, expected total cost, expected seats to allocate}
    @Parameterized.Parameters(name= "Valid Data: {index}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1), 5L, 10, 1},
                {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 15), 1L, 0, 0},
                {new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 20), 999L, 19980, 999},
                {new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2), 9223372036854775807L, 0, 0},
                {new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 10), 100000000000L, 100, 10}});
    }

    private final TicketTypeRequest validRequest;
    private final Long validAccountNo;
    private final int expectedCost;
    private final int expectedAllocatedSeats;

    public TestTicketServiceImplValidValues(final TicketTypeRequest validRequest, final Long validAccountNo,
                                            final int expectedCost, final int expectedAllocatedSeats) {
        this.expectedCost = expectedCost;
        this.validRequest = validRequest;
        this.validAccountNo = validAccountNo;
        this.expectedAllocatedSeats = expectedAllocatedSeats;
    }

    @Test
    public void testPurchaseTicketsValidTicketRequest() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Create valid ticket request
        final TicketTypeRequest validAdultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, validAdultRequest, validRequest);
        // Verify that the services are called
        Mockito.verify(mockPaymentService, Mockito.times(1)).makePayment(1L, expectedCost + 20);
        Mockito.verify(mockReservationService, Mockito.times(1)).reserveSeat(1L, expectedAllocatedSeats + 1);
    }

    @Test
    public void testPurchaseTicketsValidAccountId() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validAdultRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(validAccountNo, validAdultRequest);
        Mockito.verify(mockPaymentService, Mockito.times(1)).makePayment(1L,  20);
        Mockito.verify(mockReservationService, Mockito.times(1)).reserveSeat(1L, 1);
    }

    @Test
    public void testPurchaseTicketsMultipleRequests() {
        // Mock the external services
        final TicketPaymentService mockPaymentService = mock(TicketPaymentService.class);
        final SeatReservationService mockReservationService = mock(SeatReservationService.class);
        // Create valid ticket request
        final TicketTypeRequest validRequest1 = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 1);
        final TicketTypeRequest validRequest2 = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2);
        final TicketTypeRequest validRequest3 = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 1);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(1L, validRequest1, validRequest2, validRequest3);
        Mockito.verify(mockPaymentService, Mockito.times(1)).makePayment(1L,  30);
        Mockito.verify(mockReservationService, Mockito.times(1)).reserveSeat(1L, 2);
    }
}
