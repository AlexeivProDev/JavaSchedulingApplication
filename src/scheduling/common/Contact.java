package scheduling.common;

/**
 * This class contains information of a Contact.
 *
 * @author Alexeiv Perez
 */
public class Contact {

    private int contactId;
    private String contactName;
    private String email;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" + "contactId=" + contactId + ", contactName=" + contactName + ", email=" + email + '}';
    }

}
