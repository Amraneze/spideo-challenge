package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NoBiddingFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NoBiddingFoundException() {
        super(ErrorConstants.getBiddingNotFound(), "No bidding found in the auction", Status.NOT_FOUND);
    }

}
