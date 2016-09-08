package android.accessguarded.guarded365.co.uk.guardedguard.guardreview;

/**
 * Immutable model class for a Review.
 */
public class Review {

    // personal data
    private int guardId;
    private int siteId;
    private int reviewerId;
    private String comments;

    // review points
    private int uniformRating;
    private int attendanceRating;
    private int attitudeRating;
    private int communicationRating;
    private int performanceRating;

    // Required empty constructor for deserialization
    public Review() {
    }

    public Review(int guardId, int siteId, int reviewerId, int uniformRating, int attendanceRating, int attitudeRating, int communicationRating, int performanceRating) {
        this.guardId = guardId;
        this.siteId = siteId;
        this.reviewerId = reviewerId;
        this.uniformRating = uniformRating;
        this.attendanceRating = attendanceRating;
        this.attitudeRating = attitudeRating;
        this.communicationRating = communicationRating;
        this.performanceRating = performanceRating;
    }

    public void setComment(String comment) {
        this.comments = comment;
    }
}
