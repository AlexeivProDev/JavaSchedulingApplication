package scheduling.util;

import java.util.HashMap;
import java.util.List;
import scheduling.common.Customer;
import scheduling.jdbc.CustomerDB;

/**
 * This class contains Customer related utility methods.
 *
 * @author Alexeiv Perez
 */
public class CustomerHelper {

    /**
     * Loops through the given list of Contacts retrieved from DB, and populates
     * Map using LAMBDA Expression.
     *
     * Justification for LAMBDA Expression: Looping through collection items
     * involves declaration of index variables and accessing them might result
     * in IndexOutOfBoundsException, hence making use of LAMBDA Expression.
     *
     *
     * @return The customers Map.
     */
    public static HashMap<Integer, String> getCustomersMap() {

        //Customer id and their names.
        HashMap<Integer, String> customerMap = new HashMap<Integer, String>();
        List<Customer> customers = CustomerDB.getCustomers();
        if (customers != null) {
            //LAMBDA Expression.
            customers.forEach(c -> {
                customerMap.put(c.getCustomerId(), c.getCustomerName());
            });
        }

        return customerMap;

    }
}
