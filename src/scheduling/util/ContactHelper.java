package scheduling.util;

import java.util.HashMap;
import java.util.List;
import scheduling.common.Contact;
import scheduling.jdbc.ContactDB;

/**
 * This class contains Contact related utility methods.
 *
 * @author Alexeiv Perez
 */
public class ContactHelper {

    /**
     * Returns a Map containing contactId as key and contactName as value.
     *
     * @return
     */
    public static HashMap<Integer, String> getContactsMap() {
        /**
         * Contact id and their names.
         */
        HashMap<Integer, String> contactMap = new HashMap<Integer, String>();

        List<Contact> countacts = ContactDB.getContacts();
        if (countacts != null) {
            for (Contact contact : countacts) {
                contactMap.put(contact.getContactId(), contact.getContactName());
            }
        }

        return contactMap;

    }
}
