package budget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class BudgetManager {
    private final Scanner scanner = new Scanner(System.in);
    private final String filename = "purchases.txt";
    private final String menu = """
            Choose your action:
            1) Add income
            2) Add purchase
            3) Show list of purchases
            4) Balance
            5) Save
            6) Load
            7) Analyze (Sort)
            0) Exit
            """;
    private final String typeOfPurchaseMenu = """
            Choose the type of purchase
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            5) Back
            """;
    private final String listOfPurchasesMenu = """
            Choose the type of purchases
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            5) All
            6) Back
            """;
    private final String analysisMenu = """
            How do you want to sort?
            1) Sort all purchases
            2) Sort by type
            3) Sort certain type
            4) Back
            """;
    private final String typeOfPurchaseAnalysisMenu =
            """
            Choose the type of purchase
            1) Food
            2) Clothes
            3) Entertainment
            4) Other
            """;
    private double income = 0.0;
    private List<Purchase> purchases = new ArrayList<>();

    BudgetManager() {

    }

    public void displayMenu() {
        int option;
        do {
            System.out.println(menu);
            option = scanner.nextInt();
            System.out.println("");

            switch(option) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addPurchase();
                    break;
                case 3:
                    displayPurchases();
                    break;
                case 4:
                    System.out.printf("Balance: $%.2f\n\n", getIncome());
                    break;
                case 5:
                    savePurchases();
                    break;
                case 6:
                    loadPurchases();
                    break;
                case 7:
                    displayAnalysisMenu();
                case 0:
                default:
                    System.out.println("Bye!");
                    break;
            }
        } while(option != 0);
    }

    private void addIncome() {
        System.out.println("Enter income:");
        income = scanner.nextInt();
        System.out.println("Income was added!\n");
    }

    public double getIncome() {
        return income;
    }

    private void subtractToIncome(double price) {
        income = income - price > 0 ? income - price : 0;
    }

    private void addPurchase() {
        int typeOfPurchaseId;

        Map<Integer, PurchaseCategory> typeOfPurchaseMenuOptions = new HashMap<>();
        typeOfPurchaseMenuOptions.put(1, PurchaseCategory.FOOD);
        typeOfPurchaseMenuOptions.put(2, PurchaseCategory.CLOTHES);
        typeOfPurchaseMenuOptions.put(3, PurchaseCategory.ENTERTAINMENT);
        typeOfPurchaseMenuOptions.put(4, PurchaseCategory.OTHER);

        do {
            System.out.println(typeOfPurchaseMenu);
            typeOfPurchaseId = scanner.nextInt();

            if (typeOfPurchaseId != 5) {
                scanner.nextLine();
                System.out.println("Enter purchase name:");
                String purchaseName = scanner.nextLine();

                System.out.println("Enter its price:");
                Double price = scanner.nextDouble();
                subtractToIncome(price);

                Purchase purchase = new Purchase(
                        purchaseName,
                        price,
                        typeOfPurchaseMenuOptions.get(typeOfPurchaseId)
                );
                purchases.add(purchase);

                System.out.println("Purchase was added!\n");
            }
        } while(typeOfPurchaseId != 5);
    }

    private void displayPurchases() {
        if (purchases.isEmpty()) {
            System.out.println("The purchase list is empty\n");
            return;
        }

        Map<Integer, PurchaseCategory> listOfPurchasesMenuOptions = new HashMap<>();
        listOfPurchasesMenuOptions.put(1, PurchaseCategory.FOOD);
        listOfPurchasesMenuOptions.put(2, PurchaseCategory.CLOTHES);
        listOfPurchasesMenuOptions.put(3, PurchaseCategory.ENTERTAINMENT);
        listOfPurchasesMenuOptions.put(4, PurchaseCategory.OTHER);
        listOfPurchasesMenuOptions.put(5, PurchaseCategory.ALL);

        int option;
        do {
            System.out.println(listOfPurchasesMenu);
            option = scanner.nextInt();

            if (option != 6) {
                PurchaseCategory purchaseCategory = listOfPurchasesMenuOptions.get(option);
                List<Purchase> purchasesByCategory = getPurchasesByCategory(purchaseCategory);

                System.out.printf("%s:\n", purchaseCategory.getValue());

                if (purchasesByCategory.isEmpty()) {
                    System.out.println("The purchase list is empty\n");
                } else {
                    displayPurchasesAndTotalSum(purchasesByCategory);
                }
            }
        } while(option != 6);
    }

    private List<Purchase> getPurchasesByCategory(PurchaseCategory purchaseCategory) {
        if (PurchaseCategory.ALL == purchaseCategory) {
            return purchases;
        }

        return purchases.stream()
                .filter(purchase -> purchase.getPurchaseCategory() == purchaseCategory)
                .collect(Collectors.toList());
    }

    private double getTotalByCategory(PurchaseCategory purchaseCategory) {
        List<Purchase> purchasesByCategory = getPurchasesByCategory(purchaseCategory);
        double total = 0.0;

        for (Purchase purchase : purchasesByCategory) {
            total += purchase.getPrice();
        }

        return total;
    }

    private void savePurchases() {
        File file = new File(filename);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.println(String.valueOf(income));
            for (Purchase purchase : purchases) {
                printWriter.printf("%s,%s,%s\n",
                    purchase.getPurchase(),
                    purchase.getPrice(),
                    purchase.getPurchaseCategory().getValue()
                );
            }
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
        }

        System.out.println("Purchases were saved!\n");
    }

    private void loadPurchases() {
        File file = new File(filename);
        Boolean firstLine = true;

        try (Scanner scannerFile = new Scanner(file)) {
            while (scannerFile.hasNext()) {
                if (firstLine) {
                    income = Double.parseDouble(scannerFile.nextLine());
                    firstLine = false;
                } else {
                    String[] data = scannerFile.nextLine().split("\\,");
                    Purchase purchase = new Purchase(
                            data[0],
                            Double.valueOf(data[1]),
                            PurchaseCategory.fromValue(data[2]));
                    purchases.add(purchase);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + file);
        }
        System.out.println("Purchases were loaded!\n");
    }

    private void displayAnalysisMenu() {
        int option;
        do {
            System.out.println(analysisMenu);
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    sortAllPurchases();
                    break;
                case 2:
                    sortByType();
                    break;
                case 3:
                    sortByCertainType();
                    break;
                case 4:
                default:
                    break;
            }
        } while(option != 4);
    }

    private void sortAllPurchases() {
        if (purchases.isEmpty()) {
            System.out.println("The purchase list is empty!\n");
            return;
        }

        Comparator<Purchase> priceComparatorDesc = Comparator.comparing(Purchase::getPrice).reversed();

        Collections.sort(purchases, priceComparatorDesc);
        displayPurchasesAndTotalSum(purchases);
    }

    private void sortByType() {
        System.out.printf("""
                Types:
                Food - $%.2f
                Entertainment - $%.2f
                Clothes - $%.2f
                Other - $%.2f
                Total sum: $%.2f\n
                """,
                getTotalByCategory(PurchaseCategory.FOOD),
                getTotalByCategory(PurchaseCategory.ENTERTAINMENT),
                getTotalByCategory(PurchaseCategory.CLOTHES),
                getTotalByCategory(PurchaseCategory.OTHER),
                getTotalByCategory(PurchaseCategory.ALL)
        );
    }

    private void sortByCertainType() {
        System.out.println(typeOfPurchaseAnalysisMenu);
        Map<Integer, PurchaseCategory> listOfPurchasesMenuOptions = new HashMap<>();
        listOfPurchasesMenuOptions.put(1, PurchaseCategory.FOOD);
        listOfPurchasesMenuOptions.put(2, PurchaseCategory.CLOTHES);
        listOfPurchasesMenuOptions.put(3, PurchaseCategory.ENTERTAINMENT);
        listOfPurchasesMenuOptions.put(4, PurchaseCategory.OTHER);

        int option = scanner.nextInt();
        PurchaseCategory purchaseCategory = listOfPurchasesMenuOptions.get(option);

        List<Purchase> purchases = getPurchasesByCategory(purchaseCategory);

        if (purchases.isEmpty()) {
            System.out.println("The purchase list is empty!\n");
            return;
        }

        System.out.printf("%s:\n", purchaseCategory.getValue());
        displayPurchasesAndTotalSum(purchases);
    }

    private void displayPurchasesAndTotalSum(List<Purchase> purchases) {
        double total = 0.0;

        for(Purchase purchase : purchases) {
            System.out.printf("%s $%.2f\n", purchase.getPurchase(), purchase.getPrice());
            total += purchase.getPrice();
        }

        System.out.printf("Total sum: $%.2f\n\n", total);
    }
}
