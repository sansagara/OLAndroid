package tools;

/**
 * Created by sansagara on 20/04/16.
 */
public class Product {
    private String title;
    private String subtitle;

    public Product(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }
}
