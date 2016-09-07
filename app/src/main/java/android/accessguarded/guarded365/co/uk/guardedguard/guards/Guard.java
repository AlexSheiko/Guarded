package android.accessguarded.guarded365.co.uk.guardedguard.guards;

public class Guard {

    private String FirstName;
    private String LastName;
    private int TaskCount;

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
        } else if (LastName.length() > 8) {
            return "\n" + LastName;
        }
        return LastName;
    }

    public String getInitials() {
        String initials = FirstName.substring(0, 1);
        if (LastName != null && !LastName.isEmpty()) {
            initials += " " + LastName.substring(0, 1);
        }
        return initials;
    }

    public int getTaskCount() {
        return TaskCount;
    }
}