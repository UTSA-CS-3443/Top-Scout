package edu.utsa.cs3443.topscout.model.fields;
import java.util.HashMap;


public class Contact extends Field{

    private HashMap<String, String> contactInfo;

    public Contact(String id, String label, String hint, boolean required, HashMap<String, String> contactInfo) {
        super(id, label, hint, required);
        this.contactInfo = (contactInfo == null) ? new HashMap<>() : contactInfo;
        implementEmptyContactInfo();
    }

    @Override 
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
    @Override 
    public boolean isFilled() {
        // The workbook asks for as much as reasonably possible, so a name is enough to count
        return super.isFilled() && !getName().isEmpty();
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
        String name;
        if((name = contactInfo.get("name")) == null) {
            return "";
        }
        return name;
    }
    public void setName(String name) {
        contactInfo.put("name", name);
    }
    public String getEmail() {
        String email;
        if((email = contactInfo.get("email")) == null) {
            return "";
        }
        return email;
    }
    public void setEmail(String email) {
        contactInfo.put("email", email);
    }
    public String getPhone() {
        String phone;
        if((phone = contactInfo.get("phone")) == null) {
            return "";
        }
        return phone;
    }
    public void setPhone(String phone) {
        contactInfo.put("phone", phone);
    }
    public String getStreet() {
        String street;
        if((street = contactInfo.get("street")) == null) {
            return "";
        }
        return street;
    }
    public void setStreet(String street) {
        contactInfo.put("street", street);
    }
    public String getCity() {
        String city;
        if((city = contactInfo.get("city")) == null) {
            return "";
        }
        return city;
    }
    public void setCity(String city) {
        contactInfo.put("city", city);
    }
    public String getState() {
        String state;
        if((state = contactInfo.get("state")) == null) {
            return "";
        }
        return state;
    }
    public void setState(String state) {
        contactInfo.put("state", state);
    }
    public String getZip() {
        String zip;
        if((zip = contactInfo.get("zip")) == null) {
            return "";
        }
        return zip;
    }
    public void setZip(String zip) {
        contactInfo.put("zip", zip);
    }
}