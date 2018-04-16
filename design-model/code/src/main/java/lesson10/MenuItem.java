package lesson10;

/**
 * created by ycc at 2018\4\16 0016
 */
public class MenuItem {
    private String name;
    private String des;
    private boolean vegetarian;
    private double price;

    public MenuItem(String name, String des, boolean vegetarian, double price) {
        this.name = name;
        this.des = des;
        this.vegetarian = vegetarian;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
