public class Service {
    public double applyDiscount(double price, double discount) {
        return price - (price * discount / 100);
    }

    public boolean verifyAge(int age) {
        return age >= 18 && age <= 120;
    }


}