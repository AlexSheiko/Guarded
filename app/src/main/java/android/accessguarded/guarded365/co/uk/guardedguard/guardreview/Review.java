package android.accessguarded.guarded365.co.uk.guardedguard.guardreview;

/**
 * Immutable model class for a Review.
 */
public class Review {

    // personal data
    private int guardId;
    private int siteId;
    private int reviewerId;

    // review points
    private int uniformRating;
    private int attendanceRating;
    private int attitudeRating;
    private int communicationRating;
    private int performanceRating;

    // comment
    private String comments;

    // Required empty constructor for deserialization
    public Review() {
    }
}
