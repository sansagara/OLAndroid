package tools;

import java.util.List;

/**
 * Created by sansagara on 20/04/16.
 * Extends Product.
 * An Auction is a temporal sale/auction happening in OfertaLoca.
 *
 */
public class Auction extends Product {
    protected int id;
    protected int status;
    protected int remaining_time = 20;
    protected double accumulated_price;
    protected double list_price = 10.0;
    protected double last_bid;
    protected List<Bid> bids;
    protected String description_path;

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



    //Constructor including Product data and bids list.
    public Auction(String product_name, String product_description, double product_market_price, String product_image_url, int auction_id, int auction_status, int auction_remaining_time, double auction_accumulated_price, List<Bid> bids_list) {

        //Create Product.
        super(product_name, product_description, product_market_price, product_image_url);
        //Create Auction_specific attributes.
        this.id = auction_id;
        this.status = auction_status;
        this.remaining_time = auction_remaining_time;
        this.accumulated_price = auction_accumulated_price;
        this.bids = bids_list;

    }

    //Constructor including Product data, bids list and product_description_url
    public Auction(String product_name, String product_description, double product_market_price, String product_image_url, int auction_id, int auction_status, int auction_remaining_time, double auction_accumulated_price, List<Bid> bids_list, String description_path, double last_bid) {

        //Create Product.
        super(product_name, product_description, product_market_price, product_image_url);
        //Create Auction_specific attributes.
        this.id = auction_id;
        this.status = auction_status;
        this.remaining_time = auction_remaining_time;
        this.accumulated_price = auction_accumulated_price;
        this.bids = bids_list;
        this.description_path = description_path;
        this.last_bid = last_bid;

    }

    public double getList_price() {
        return list_price;
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
    public Double getLastBid() {
        return last_bid;
    }
    public List<Bid> getBids() {
        return bids;
    }
    public String getDescriptionPath() {
        return description_path;
    }
    public String getLastUser() {
        return bids.get(0).getClient();
    }
    public String getLastUserPic() {
        return bids.get(0).getPic_path();
    }

}
