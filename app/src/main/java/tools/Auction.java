package tools;

/**
 * Created by sansagara on 20/04/16.
 * Extends Product.
 * An Auction is a temporal sale/auction happening in OfertaLoca.
 *
 */
public class Auction extends Product {
    protected int id;
    protected int status;
    protected int remaining_time = 1;
    protected double accumulated_price;
    protected double last_bid;

    //Default Const
    public Auction(String product_name, String product_description ) {
        //Create Product.
        super(product_name, product_description);

    }

    //Constructor including Product data.
    public Auction(String product_name, String product_description, double product_market_price, String product_image_url, int auction_id, int auction_status, int auction_remaining_time, double auction_accumulated_price) {

        //Create Product.
        super(product_name, product_description, product_market_price, product_image_url);
        //Create Auction_specific attributes.
        this.id = auction_id;
        this.status = auction_status;
        this.remaining_time = auction_remaining_time;
        this.accumulated_price = auction_accumulated_price;

    }

    public int getID() {
        return id;
    }
    public int getRemainingTime() {
        return remaining_time;
    }
    public Integer getStatus() {
        return status;
    }
    public Double getAccumulated_price() {
        return accumulated_price;
    }
    public Double getLast_bid() {
        return last_bid;
    }
}