package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AuctionNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuctionNotFoundException() {
        super(ErrorConstants.getAuctionNotFound(), "Auction doesn't exist", Status.NOT_FOUND);
    }

}
