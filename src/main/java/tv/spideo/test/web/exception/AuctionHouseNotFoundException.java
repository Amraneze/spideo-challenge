package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AuctionHouseNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuctionHouseNotFoundException() {
        super(ErrorConstants.getAuctionHouseNotFound(), "Auction House doesn't exist", Status.NOT_FOUND);
    }

}
