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
    protected String pic_path;

    //Default constructor
    public Bid(String client, double accumulated, double value, String pic_path) {

        this.client = client;
        this.accumulated = accumulated;
        this.value = value;
        this.pic_path = pic_path;

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

    public String getPic_path(){
        return pic_path;
    }

}
