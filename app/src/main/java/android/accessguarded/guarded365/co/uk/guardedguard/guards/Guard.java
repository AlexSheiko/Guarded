package android.accessguarded.guarded365.co.uk.guardedguard.guards;

import android.accessguarded.guarded365.co.uk.guardedguard.R;
import android.content.Context;

import java.io.Serializable;

/**
 * Immutable model class for a Guard.
 */
public class Guard implements Serializable {

    private String FirstName;
    private String LastName;
    private String ProfilePicUrl;
    private String SiteName;
    private int TaskCount;
    private int IncidentCount;

    // Required empty constructor for deserialization
    @SuppressWarnings("unused")
    public Guard() {
    }

    Guard(String name) {
        FirstName = name;
    }

    Guard(String firstName, String lastName) {
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

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getInitials() {
        String initials = FirstName.substring(0, 1);
        if (LastName != null && !LastName.isEmpty()) {
            initials += " " + LastName.substring(0, 1);
        }
        return initials;
    }

    public String getPhotoUrl() {
        return ProfilePicUrl;
    }

    public String getSiteName() {
        if (SiteName == null) {
            return "Site not specified";
        }
        return SiteName;
    }

    public String getTaskCount(Context context) {
        if (TaskCount == 0) {
            return "No tasks";
        }
        return String.format(context.getResources().getQuantityString(
                R.plurals.tasks_count_label, TaskCount), TaskCount);
    }

    public String getIncidentCount(Context context) {
        if (IncidentCount == 0) {
            return "No incidents";
        }
        return String.format(context.getResources().getQuantityString(
                R.plurals.incidents_count_label, IncidentCount), IncidentCount);
    }
}