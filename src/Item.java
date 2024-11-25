public record Item(String name, String description, double startingPrice) {
    private static double highestBid = 0; // Field to store the current highest bid

    public static final Item iPhone = new Item("IPhone", "iPhone 12", 800);
    public static final Item ps5 = new Item("PS5", "Play Station 5", 500);
    public static final Item xbox = new Item("XBX", "Xbox Series X", 500);

    public static Item getIPhone() {
        return iPhone;
    }

    public static Item getPs5() {
        return ps5;
    }

    public static Item getXbox() {
        return xbox;
    }

    public static double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double bid) {
        highestBid = bid;
    }

    /**
     * @return String with name, description, and starting price
     */
    @Override
    public String toString() {
        return String.format("%s: %s ($%.2f)", name, description, startingPrice);
    }
}
