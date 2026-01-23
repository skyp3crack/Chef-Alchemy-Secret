//Ratingrequest for rating

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class RatingRequest {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer score;

    public RatingRequest() {
    }

    public RatingRequest(Integer score) {
        this.score = score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

}