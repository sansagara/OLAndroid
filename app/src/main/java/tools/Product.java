package tools;

/**
 * Created by sansagara on 20/04/16.
 * A Product is an specific item that is going to be saled/auctioned on OfertaLoca.
 */
public class Product {
    protected String name;
    protected String description;
    protected Double accumulated_price;
    protected Double market_price;
    protected String image_url;

    //Default Const
    public Product(String name, String description ) {
        this.name = name;
        this.description = description;
    }

    //All Params Const
    public Product(String name, String description, Double accumulated_price, Double market_price, String image_url ) {
        this.name = name;
        this.description = description;
        this.accumulated_price = accumulated_price;
        this.market_price = market_price;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Double getAccumulated_price() {
        return accumulated_price;
    }
    public Double getMarket_price() {
        return market_price;
    }
    public String getImage_url() {
        return image_url;
    }

}
