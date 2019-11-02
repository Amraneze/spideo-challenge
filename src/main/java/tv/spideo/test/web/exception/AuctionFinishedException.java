package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AuctionFinishedException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuctionFinishedException() {
        super(ErrorConstants.getAuctionFinished(), "Auction already finished", Status.BAD_REQUEST);
    }

}
