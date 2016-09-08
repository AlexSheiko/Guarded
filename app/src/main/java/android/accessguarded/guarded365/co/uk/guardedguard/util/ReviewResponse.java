package android.accessguarded.guarded365.co.uk.guardedguard.util;

/**
 * Immutable model class for a Review response.
 */
public class ReviewResponse {

    private boolean HasBeenSubmitted;
    private String Message;

    // Required empty constructor for deserialization
    public ReviewResponse() {
    }

    public boolean isSuccessful() {
        return HasBeenSubmitted;
    }

    public String getMessage() {
        return Message;
    }
}
