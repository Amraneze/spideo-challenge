package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class BiddingPriceLowException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public BiddingPriceLowException() {
        super(ErrorConstants.getBiddingPriceLow(), "Bidding price is low", Status.BAD_REQUEST);
    }

}
