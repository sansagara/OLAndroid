package tools;

/**
 * Created by sansagara on 20/04/16.
 */
public class Product {
    private String name;
    private String description;
    private Double accumulated_price;
    private Double market_price;
    private int status;
    private String image_url;

    //Default Const
    public Product(String name, String description ) {
        this.name = name;
        this.description = description;
    }

    //All Params Const
    public Product(String name, String description, Double accumulated_price, Double market_price, int status, String image_url ) {
        this.name = name;
        this.description = description;
        this.accumulated_price = accumulated_price;
        this.market_price = market_price;
        this.status = status;
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
    public Integer getStatus() {
        return status;
    }
    public String getImage_url() {
        return image_url;
    }

}
