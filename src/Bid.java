/**
 * represents a bid for the auction
 * @param item the item being bid on
 * @param amount the bid amount
 */
 record Bid( Item item, double amount) {

    @Override
    public String toString() {
        return String.format(" $%.2f on %s",  amount, item.name());
    }
}

