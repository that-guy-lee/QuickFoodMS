public class Customer {

    //Attributes

    int customerOrderNumber;
    String customerName;
    String customerNumber;
    String customerAddress;
    String customerArea;
    String customerEmail;
    String restaurantName;
    int meal1quant;
    int meal2quant;
    int meal3quant;
    String customerInstructions;



    //Constructor

    public Customer(int customerOrderNumber, String customerName, String customerNumber, String customerAddress, String customerArea,
                    String customerEmail, String restaurantName, int meal1quant, int meal2quant,
                    int meal3quant, String customerInstructions) {

        this.customerOrderNumber = customerOrderNumber;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.customerAddress = customerAddress;
        this.customerArea = customerArea;
        this.customerEmail = customerEmail;
        this.restaurantName = restaurantName;
        this.meal1quant = meal1quant;
        this.meal2quant = meal2quant;
        this.meal3quant = meal3quant;
        this.customerInstructions = customerInstructions;


    }
}
