package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.domain.Type;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Provides a public interface to purchase tickets
 */
public class TicketServiceImpl implements TicketService {

    private TicketPaymentService ticketPaymentService;
    private SeatReservationService seatReservationervice;

    private final int MAX_NO_OF_TICKETS_ALLOWED = 20;

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
        // Are more than 20 tickets being purchased?
        if(Arrays.stream(ticketTypeRequests).mapToInt(TicketTypeRequest::getNoOfTickets).sum() > MAX_NO_OF_TICKETS_ALLOWED)
        {
            throw new InvalidPurchaseException("Total number of tickets requested exceeds limit of " + MAX_NO_OF_TICKETS_ALLOWED);
        }
        // Is ID valid?
        if(accountId < 1)
        {
            throw new InvalidPurchaseException("The account ID " + accountId + " is invalid, must be greater than 0");
        }
        // Is there at least one adult ticket purchased?
        if(Arrays.stream(ticketTypeRequests).filter(req -> req.getTicketType() == Type.ADULT).count() < 1)
        {
            throw new InvalidPurchaseException("No adult ticket purchased");
        }
        // Check no ticket entries with orders below 0 have been submitted
        Arrays.stream(ticketTypeRequests).filter(req -> req.getNoOfTickets() < 1).findAny()
                .ifPresent(invalidReq -> {throw new InvalidPurchaseException(invalidReq.toString() + " was invalid");});

        // Get the total cost and call the ticket payment service
        final int amountToPay = Arrays.stream(ticketTypeRequests).mapToInt(TicketTypeRequest::getCost).sum();
        ticketPaymentService.makePayment(accountId, amountToPay);

        // Get the total allocated seats and call the seat reservation service
        final int seats = Arrays.stream(ticketTypeRequests).filter(req -> req.getTicketType() != Type.INFANT)
                        .mapToInt(TicketTypeRequest::getNoOfTickets).sum();
        seatReservationervice.reserveSeat(accountId, seats);
    }
}
