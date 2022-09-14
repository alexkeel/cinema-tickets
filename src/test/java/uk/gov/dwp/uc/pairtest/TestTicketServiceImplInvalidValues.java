package uk.gov.dwp.uc.pairtest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import java.util.Arrays;
import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class TestTicketServiceImplInvalidValues {
    // Create a set of invalid test data, items are { Ticket request, account number }
    @Parameterized.Parameters(name= "Invalid Data: {index}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {new TicketTypeRequest(Type.ADULT, -1), -0L},
                {new TicketTypeRequest(Type.ADULT, 0), -0L},
                {new TicketTypeRequest(Type.CHILD, -50), -1L},
                {new TicketTypeRequest(Type.ADULT, -999), -999L},
                {new TicketTypeRequest(Type.INFANT, -2), -9223372036854775808L},
                {new TicketTypeRequest(Type.CHILD, -505050), -100000000000L}});
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
        final TicketTypeRequest validRequest = new TicketTypeRequest(Type.ADULT, 2);
        // Create ticket service under test
        final TicketService underTest = new TicketServiceImpl(mockPaymentService, mockReservationService);
        // Call ticket service with valid account ID and invalid values
        underTest.purchaseTickets(invalidAccountNo, validRequest);
    }
}
