import java.sql.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        try {
            //Connect to the sql server
            Connection connection = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost;database=QuickFoodMS",
                    "javaLogin",
                    "javalogin123"
            );

            //Pull the restaurant data from the sql server, and create an arrayList of restaurant objects

            Statement statement = connection.createStatement();
            ResultSet restaurantResults = statement.executeQuery("SELECT restaurantName, restaurantArea," +
                    "restaurantNumber, meal1, meal1Price, meal2, meal2Price, " +
                    "meal3, meal3Price FROM restaurantTable");

            ArrayList<Restaurant> restaurantList = new ArrayList<>();

            while (restaurantResults.next()) {

                String currentName = restaurantResults.getString("restaurantName");
                String currentArea = restaurantResults.getString("restaurantArea");
                String currentNumber = restaurantResults.getString("restaurantNumber");
                String currentMeal1 = restaurantResults.getString("meal1");
                double currentMeal1Price = restaurantResults.getDouble("meal1Price");
                String currentMeal2 = restaurantResults.getString("meal2");
                double currentMeal2Price = restaurantResults.getDouble("meal2Price");
                String currentMeal3 = restaurantResults.getString("meal3");
                double currentMeal3Price = restaurantResults.getDouble("meal3Price");

                Restaurant r = new Restaurant(currentName, currentArea, currentNumber, currentMeal1, currentMeal1Price,
                        currentMeal2, currentMeal2Price, currentMeal3, currentMeal3Price);

                restaurantList.add(r);

            }


            //Pull the customer data from the sql server, and create an arrayList of customer objects

            ResultSet customerResults = statement.executeQuery("SELECT orderNumber, " +
                    "customerName, customerNumber, customerAddress, customerArea, customerEmail, " +
                    "restaurantName, meal1quant, meal2quant, meal3quant, specialInstructions FROM customerTable");

            ArrayList<Customer> customerList = new ArrayList<>();

            while (customerResults.next()) {

                int currentOrderNumber = customerResults.getInt("orderNumber");
                String currentName = customerResults.getString("customerName");
                String currentNumber = customerResults.getString("customerNumber");
                String currentAddress = customerResults.getString("customerAddress");
                String currentArea = customerResults.getString("customerArea");
                String currentEmail = customerResults.getString("customerEmail");
                String currentRestaurantName = customerResults.getString("restaurantName");
                int currentMeal1quant = customerResults.getInt("meal1quant");
                int currentMeal2quant = customerResults.getInt("meal2quant");
                int currentMeal3quant = customerResults.getInt("meal3quant");
                String currentSpecialInstructions = customerResults.getString("specialInstructions");

                Customer c = new Customer(currentOrderNumber, currentName, currentNumber, currentAddress,
                        currentArea, currentEmail, currentRestaurantName, currentMeal1quant, currentMeal2quant,
                        currentMeal3quant, currentSpecialInstructions);

                customerList.add(c);


            }


            //Pull the driver data from the sql server, and create an arrayList of driver objects

            ArrayList<Driver> driverList = new ArrayList<>();
            ResultSet driverResults = statement.executeQuery("SELECT driverName, driverArea, driverLoad FROM driverTable");

            while (driverResults.next()) {

                String currentName = driverResults.getString("driverName");
                String currentArea = driverResults.getString("driverArea");
                int currentLoad = driverResults.getInt("driverLoad");

                Driver d = new Driver(currentName, currentArea, currentLoad);
                driverList.add(d);

            }

            //Call the order method and pass through all 3 lists that were just created
            order(customerList, restaurantList, driverList, statement);

        } catch (SQLException e) {

            e.printStackTrace();
        }


    }

    public static void order(ArrayList<Customer> customerList, ArrayList<Restaurant> restaurantList,
                             ArrayList<Driver> driverList, Statement statement) {

        //Ask the user if they're going to update an existing order or create a new one

        System.out.println("Hi! Please select an option (Enter the corresponding number):");
        System.out.println();

        Scanner sc = new Scanner(System.in);
        System.out.println("1. Create a new order \n2. Update an existing order\n:");
        int currentSelection = sc.nextInt();

        if (currentSelection == 1) {

            //User chooses a restaurant

            System.out.println("Please choose a restaurant!\n");


            System.out.println("1. " + restaurantList.get(0).restaurantName
                    + "\n2. " + restaurantList.get(1).restaurantName
                    + "\n3. " + restaurantList.get(2).restaurantName + "\n");

            System.out.println("Please enter the number of your choice: ");
            int restaurantChoiceInput = sc.nextInt();

            //Subtract 1 from the user input to get the array list index of the selected restaurant

            int restaurantChoiceIndex = restaurantChoiceInput - 1;

            //Declare variables
            int mealChoice = 0;
            int meal1quant = 0;
            int meal2quant = 0;
            int meal3quant = 0;


            //Logic for ordering

            while (mealChoice != 4) {

                System.out.println("What would you lke to order from " + restaurantList.get(restaurantChoiceIndex).restaurantName + "?");
                System.out.println("Again, type the corresponding number of what you'd like!");
                System.out.println("Type 4 when you're done!");

                System.out.println("1. " + restaurantList.get(restaurantChoiceIndex).meal1 + " - " +
                        restaurantList.get(restaurantChoiceIndex).meal1Price
                        + "\n2. " + restaurantList.get(restaurantChoiceIndex).meal2 +
                        " - " + restaurantList.get(restaurantChoiceIndex).meal2Price
                        + "\n3. " + restaurantList.get(restaurantChoiceIndex).meal3 + " - " +
                        restaurantList.get(restaurantChoiceIndex).meal3Price
                        + "\n4. Done ordering!");

                System.out.println("Enter your choice:");
                mealChoice = sc.nextInt();

                //These if statements add values to the respective variables of meal quantities

                if (mealChoice == 4) {
                    break;
                }

                System.out.println("How many of those would you like:");
                int currentQuant = sc.nextInt();

                if (mealChoice == 1) {

                    meal1quant += currentQuant;

                } else if (mealChoice == 2) {

                    meal2quant += currentQuant;

                } else if (mealChoice == 3) {

                    meal3quant += currentQuant;

                }

            }

            //Collect customer details
            System.out.println("\n\n");
            System.out.println("Please enter you info:\n");


            System.out.println("Name: ");
            Scanner sc2 = new Scanner(System.in);
            String customerName = sc2.nextLine();

            System.out.println("Number: ");
            String customerNumber = sc2.nextLine();

            System.out.println("Street Address: ");
            String customerAddress = sc2.nextLine();

            System.out.println("City: ");
            String customerArea = sc2.nextLine();

            System.out.println("Email: ");
            String customerEmail = sc2.nextLine();

            System.out.println("Special prep instructions: ");
            String customerInstructions = sc2.nextLine();

            //Generate the order number - I got this code from https://www.tutorialspoint.com/generate-10-random-four-digit-numbers-in-java

            Random rand = new Random();
            int orderNumber = rand.nextInt((9999 - 100) + 1) + 10;

            //Create a new customer and add it to the customerList

            Customer newCustomer = new Customer(orderNumber, customerName, customerNumber,
                    customerAddress, customerArea, customerEmail, restaurantList.get(restaurantChoiceIndex).restaurantName,
                    meal1quant, meal2quant, meal3quant, customerInstructions);

            customerList.add(newCustomer);

            //Check if the customer and restaurant are in the same area

            String restaurantArea = restaurantList.get(restaurantChoiceIndex).restaurantArea.toUpperCase();

            if (restaurantArea.equals(customerArea.toUpperCase())) {

                //Call the method that decides which driver to use

                chooseDriver(driverList, newCustomer, statement, restaurantList.get(restaurantChoiceIndex));

            } else {

                //Create the invoice but explain that the customer is too far away from the restaurant
                String output = "Sorry! The restaurant is too far away from you to be able to deliver to your location!";

                try {
                    Formatter f = new Formatter("src\\invoice.txt");
                    f.format(output);
                    f.close();
                } catch (Exception e) {
                    System.out.println("Error saving invoice");
                }

            }


        } else if (currentSelection == 2) {

            //Get the order number from the user

            System.out.println("Please enter your order number:");
            int orderNumberInput = sc.nextInt();


            try {
                //Get the information about that customer from SQL

                ResultSet r = statement.executeQuery("SELECT orderNumber, customerName, customerNumber, customerAddress" +
                        ", customerArea, customerEmail FROM customerTable WHERE orderNumber=" + orderNumberInput);

                //Create a customer object to make this information easier to handle
                //Note that I added placeholder data to restaurantName, meal1quant, meal2quant, meal3quant, and customer instructions
                //I did this because the user will not be allowed to update this information

                if (r.next()) {

                    Customer selectedCustomer = new Customer(r.getInt("orderNumber"), r.getString("customerName"),
                            r.getString("customerNumber"), r.getString("customerAddress"),
                            r.getString("customerArea"), r.getString("customerEmail"),
                            "blank", 0, 0, 0, "blank");

                    //Create a variable to hold each element

                    int selectedOrderNumber = r.getInt("orderNumber");
                    String selectedCustomerName = r.getString("customerName");
                    String selectedCustomerNumber = r.getString("customerNumber");
                    String selectedCustomerAddress = r.getString("customerAddress");
                    String selectedCustomerArea = r.getString("customerArea");
                    String selectedCustomerEmail = r.getString("customerEmail");


                    //Output the information to the user

                    //Create a variable that can be used to break the while loop
                    int changeVariable = 0;

                    while (changeVariable != 6) {

                        System.out.println("Please enter the corresponding number of what you'd like to change: \n");
                        System.out.println("1. Customer Name (" + selectedCustomerName + ")");
                        System.out.println("2. Customer Number (" + selectedCustomerNumber + ")");
                        System.out.println("3. Customer Address (" + selectedCustomerAddress + ")");
                        System.out.println("4. Customer City (" + selectedCustomerArea + ")");
                        System.out.println("5. Customer Email (" + selectedCustomerEmail + ")\n");
                        System.out.println("6. Done");

                        System.out.println("Selection: ");
                        changeVariable = sc.nextInt();

                        Scanner sc3 = new Scanner(System.in);

                        switch (changeVariable) {

                            case 1:
                                System.out.println("Enter a new customer name: ");

                                selectedCustomerName = sc3.nextLine();
                                break;

                            case 2:
                                System.out.println("Enter a new customer number: ");
                                selectedCustomerNumber = sc3.nextLine();
                                break;

                            case 3:
                                System.out.println("Enter a new customer address: ");
                                selectedCustomerAddress = sc3.nextLine();
                                break;

                            case 4:
                                System.out.println("Enter a new customer city: ");
                                selectedCustomerArea = sc3.nextLine();
                                break;

                            case 5:
                                System.out.println("Enter a new customer email: ");
                                selectedCustomerEmail = sc3.nextLine();
                                break;

                            case 6:
                                break;


                        }


                    }

                    statement.executeUpdate("UPDATE customerTable SET customerName='" + selectedCustomerName +
                            "', customerNumber='" + selectedCustomerNumber + "', customerAddress='" + selectedCustomerAddress +
                            "', customerArea='" + selectedCustomerArea + "', customerEmail='" + selectedCustomerEmail + "' WHERE orderNumber=" +
                            selectedOrderNumber);


                }
            } catch (SQLException e) {

                e.printStackTrace();

            }


        }


    }

    //Method that chooses the driver in the same area with the lowest load
    public static void chooseDriver(ArrayList<Driver> driverList, Customer currentCustomer, Statement statement,
                                    Restaurant currentRestaurant) {

        //Create a variable to hold the customer area and an arrayList to hold the drivers in the correct area

        String customerArea = currentCustomer.customerArea.toUpperCase();
        ArrayList<Driver> driverListCorrectArea = new ArrayList<>();

        //Loop through the driver list and add drivers with the same location as the customer
        //to the driverListCorrectArea list

        //Declare variables
        String lowestName = "";
        String lowestArea = "";
        int lowestLoad = 100;

        for (int i = 0; i < driverList.size(); i++) {

            String currentArea = driverList.get(i).driverArea.toUpperCase();

            if (currentArea.equals(customerArea)) {

                driverListCorrectArea.add(driverList.get(i));

            } else {

                //If the loop finds a match after this file has been created, it's okay.
                //Because, when the actual invoice is generated it will overwrite this

                String output = "Sorry! Our drivers are too far away from you to be able to deliver to your location!";
                try {
                    Formatter f = new Formatter("src\\invoice.txt");
                    f.format(output);
                    f.close();
                } catch (Exception e) {
                    System.out.println("Error saving invoice");
                }

            }

            //Now loop through driverListCorrectArea and find the driver with the lowest load


            for (int j = 0; j < driverListCorrectArea.size(); j++) {


                if (driverListCorrectArea.get(j).driverLoad < lowestLoad) {

                    lowestLoad = driverListCorrectArea.get(j).driverLoad;
                    lowestName = driverListCorrectArea.get(j).driverName;
                    lowestArea = driverListCorrectArea.get(j).driverArea;

                }

            }

        }

        //Create a new driver, which holds the information for the best driver

        int newLowestLoad = lowestLoad + 1;
        Driver bestDriver = new Driver(lowestName, lowestArea, newLowestLoad);

        //Update the SQL database by changing the driver load and adding the customer and save an invoice
        updateSQL(currentCustomer, bestDriver, statement, currentRestaurant);


    }

    public static void updateSQL(Customer newCustomer, Driver updatedDriver, Statement statement, Restaurant currentRestaurant) {


        try {

            //Add new customer

            statement.executeUpdate("INSERT INTO customerTable VALUES (" + newCustomer.customerOrderNumber + ", '"
                    + newCustomer.customerName + "', '" + newCustomer.customerNumber + "', '" + newCustomer.customerAddress
                    + "', '" + newCustomer.customerArea + "', '" + newCustomer.customerEmail + "', '"
                    + newCustomer.restaurantName + "', " + newCustomer.meal1quant + ", " + newCustomer.meal2quant +
                    ", " + newCustomer.meal3quant + ", '" + newCustomer.customerInstructions +
                    "')"
            );

            //Update the driver with the new load

            statement.executeUpdate("UPDATE driverTable SET driverLoad=" + updatedDriver.driverLoad +
                    " WHERE driverName='" + updatedDriver.driverName + "'");


            //Output an invoice

            double Total = 0;

            String output = "Order Number: " + newCustomer.customerOrderNumber;
            output += "\nCustomer: " + newCustomer.customerName;
            output += "\nEmail: " + newCustomer.customerEmail;
            output += "\nPhone Number: " + newCustomer.customerNumber;
            output += "\nLocation: " + newCustomer.customerArea;

            output += "\n\nYou have ordered the following from " + currentRestaurant.restaurantName
                    + "in " + currentRestaurant.restaurantArea + ":\n\n";

            if (newCustomer.meal1quant != 0) {

                output += newCustomer.meal1quant + " x " + currentRestaurant.meal1 + " (R " +
                        currentRestaurant.meal1Price + " each)";

                Total += currentRestaurant.meal1Price * newCustomer.meal1quant;

            }
            if (newCustomer.meal2quant != 0) {

                output += "\n" + newCustomer.meal2quant + " x " + currentRestaurant.meal2 + " (R " +
                        currentRestaurant.meal2Price + " each)";

                Total += currentRestaurant.meal2Price * newCustomer.meal2quant;

            }
            if (newCustomer.meal3quant != 0) {

                output += "\n" + newCustomer.meal3quant + " x " + currentRestaurant.meal3 + " (R " +
                        currentRestaurant.meal3Price + " each)";

                Total += currentRestaurant.meal3Price * newCustomer.meal3quant;

            }

            output += "\nSpecial instructions: " + newCustomer.customerInstructions;
            output += "\nTotal: R " + Total + "\n\n";

            output += updatedDriver.driverName + "is the nearest to the restaurant so he will be delivering your " +
                    "order to you at: \n\n";
            output += newCustomer.customerAddress + "\n";
            output += newCustomer.customerArea + "\n\n";
            output += "If you need to contact the restaurant, their number is " + currentRestaurant.restaurantNumber;

            try {
                Formatter f = new Formatter("src\\invoice.txt");
                f.format(output);
                f.close();
            } catch (Exception e) {
                System.out.println("Error saving invoice");
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }


}
