package SubscriptionManageMent;

public interface Subscription{
    public abstract  void buyingPlan(String planName,String userName,boolean UpDown);
    public abstract void upgrade();
    public abstract void downgrade();
    public abstract void cencellation();
}
