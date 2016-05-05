package tools;

/**
 * Created by sansagara on 20/04/16.
 * Extends Product.
 * An Auction is a temporal sale/auction happening in OfertaLoca.
 *
 */
public class Bid  {
    protected String client;
    protected double accumulated;
    protected double value;

    //Default constructor
    public Bid(String client, double accumulated, double value) {

        this.client = client;
        this.accumulated = accumulated;
        this.value = value;

    }


    public String getClient() {
        return client;
    }

    public double getAccumulated() {
        return accumulated;
    }

    public double getValue() {
        return value;
    }

}
