package tv.spideo.test.web.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class AuctionHouseAlreadyExistException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuctionHouseAlreadyExistException() {
        super(ErrorConstants.getAuctionHouseAlreadyExist(), "Auction House already exist", Status.BAD_REQUEST);
    }

}
