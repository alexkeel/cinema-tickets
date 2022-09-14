package uk.gov.dwp.uc.pairtest.domain;

/**
 * Represents a ticket purchase request to be passed to a TicketService
 */
public final class TicketTypeRequest {

    private int noOfTickets;
    private Type type;

    /**
     * Constructor
     * @param type The type of ticket to purchase
     * @param noOfTickets The number of tickets to purchase
     */
    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    /**
     * Returns the number of tickets requested
     * @return the number of tickets requested
     */
    public int getNoOfTickets() {
        return noOfTickets;
    }

    /**
     * Returns the type of ticket requested
     * @return the type of ticket requested
     */
    public Type getTicketType() {
        return this.type;
    }

    /**
     * Returns the cost of the purchase request (number of tickets multiplied by cost of ticket type)
     * @return the cost of the purchase request
     */
    public int getCost() {
        return this.noOfTickets * this.type.getPrice();
    }

    @Override
    public String toString() {
        return "Request for " + this.noOfTickets + " tickets for type " + type.toString();
    }
}
