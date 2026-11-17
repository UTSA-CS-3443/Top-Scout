package edu.utsa.cs3443.topscout.model.fields;
import java.util.HashMap;


public class Contact extends Field{

    private HashMap<String, String> contactInfo;

    public Contact(String id, String label, String hint, boolean required, HashMap<String, String> contactInfo) {
        super(id, label, hint, required);
        this.contactInfo = contactInfo;
        implementEmptyContactInfo();
    }

    public String toString() {
        return "Contact{" +
                "contactInfo=" + contactInfo +
                "} " + super.toString();
    }
    @Override 
     public void clear() {
        super.clear();
        setName("");
        setEmail("");
        setPhone("");
        setStreet("");
        setCity("");
        setState("");
        setZip("");
    }

    private void implementEmptyContactInfo() {
        contactInfo.putIfAbsent("name", "");
        contactInfo.putIfAbsent("email", "");
        contactInfo.putIfAbsent("phone", "");
        contactInfo.putIfAbsent("street", "");
        contactInfo.putIfAbsent("city", "");
        contactInfo.putIfAbsent("state", "");
        contactInfo.putIfAbsent("zip", "");
    }

    public String getName() {
        return contactInfo.get("name");
    }
    public void setName(String name) {
        contactInfo.put("name", name);
    }
    public String getEmail() {
        return contactInfo.get("email");
    }
    public void setEmail(String email) {
        contactInfo.put("email", email);
    }
    public String getPhone() {
        return contactInfo.get("phone");
    }
    public void setPhone(String phone) {
        contactInfo.put("phone", phone);
    }
    public String getStreet() {
        return contactInfo.get("street");
    }
    public void setStreet(String street) {
        contactInfo.put("street", street);
    }
    public String getCity() {
        return contactInfo.get("city");
    }
    public void setCity(String city) {
        contactInfo.put("city", city);
    }
    public String getState() {
        return contactInfo.get("state");
    }
    public void setState(String state) {
        contactInfo.put("state", state);
    }
    public String getZip() {
        return contactInfo.get("zip");
    }
    public void setZip(String zip) {
        contactInfo.put("zip", zip);
    }
}
