package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AuctionNotStartedException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuctionNotStartedException() {
        super(ErrorConstants.getAuctionNotStarted(), "Auction didn't started", Status.BAD_REQUEST);
    }

}
