package android.accessguarded.guarded365.co.uk.guardedguard.guards;

public class Guard {

    private String FirstName;
    private String LastName;

    // Required empty constructor for deserialization
    @SuppressWarnings("unused")
    public Guard() {
    }

    public Guard(String name) {
        FirstName = name;
    }

    public Guard(String firstName, String lastName) {
        FirstName = firstName;
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        if (LastName == null) {
            return "";
        }
        return LastName;
    }

    public String getInitials() {
        return FirstName.substring(0, 1) + " " + LastName.substring(0, 1);
    }
}