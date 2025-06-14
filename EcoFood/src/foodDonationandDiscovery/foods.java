package foodDonationandDiscovery;

import java.sql.Date;

public interface foods {
    public abstract void updateFood(String foodname, double quantity, String category, String status, Date expireDate) throws Exception;
    public abstract void viewAvailableFoods() throws Exception;
    public abstract void requestFood() throws Exception;
    public abstract void trackRequest() throws Exception;
    public abstract void updateFoodExpired() throws Exception;
}
