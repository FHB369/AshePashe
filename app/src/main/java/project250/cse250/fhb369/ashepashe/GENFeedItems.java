package project250.cse250.fhb369.ashepashe;

public class GENFeedItems {

    private String TITLE, PHOTO, PACKAGE_1_PRICE, key, rating;

    public GENFeedItems(String TITLE, String PHOTO, String PACKAGE_1_PRICE, String key, String rating) {
        this.TITLE = TITLE;
        this.PHOTO = PHOTO;
        this.PACKAGE_1_PRICE = PACKAGE_1_PRICE;
        this.key = key;
        this.rating = rating;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getPHOTO() {
        return PHOTO;
    }

    public void setPHOTO(String PHOTO) {
        this.PHOTO = PHOTO;
    }

    public String getPACKAGE_1_PRICE() {
        return PACKAGE_1_PRICE;
    }

    public void setPACKAGE_1_PRICE(String PACKAGE_1_PRICE) {
        this.PACKAGE_1_PRICE = PACKAGE_1_PRICE;
    }

    public String getkey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
