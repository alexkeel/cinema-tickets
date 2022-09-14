package uk.gov.dwp.uc.pairtest.domain;

/**
 * Specifies the type of ticket (INFANT, CHILD, ADULT) and the associated price
 */
public enum Type {
    INFANT(0),
    CHILD(10),
    ADULT(20);

    private final int price;

    /**
     * Constructor
     * @param price The price of the ticket
     */
    Type(final int price){
        this.price = price;
    }

    public int getPrice(){
        return price;
    }
}
