package budget;

public enum PurchaseCategory {
    FOOD("Food"),
    CLOTHES("Clothes"),
    ENTERTAINMENT("Entertainment"),
    OTHER("Other"),
    ALL("All");

    private final String value;

    PurchaseCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PurchaseCategory fromValue(String value) {
        for (PurchaseCategory category : PurchaseCategory.values()) {
            if (category.value.equalsIgnoreCase(value)) {
                return category;
            }
        }
        // If no matching enum is found, return null or throw an exception
        throw new IllegalArgumentException("No enum constant with value " + value + " found");
    }
}
