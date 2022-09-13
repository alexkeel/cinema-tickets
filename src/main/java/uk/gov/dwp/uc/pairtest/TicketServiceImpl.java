package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

/**
 * Provides a public interface to purchase tickets
 */
public class TicketServiceImpl implements TicketService {

    private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationervice;

    /**
     * Constructs a TicketServiceImpl object.
     * @param ticketPaymentService The ticket payment service to be used.
     * @param seatReservationService The seat reservation service to be used.
     */
    TicketServiceImpl(final TicketPaymentService ticketPaymentService, final SeatReservationService seatReservationService) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationervice = seatReservationService;
    }

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {

    }
}
