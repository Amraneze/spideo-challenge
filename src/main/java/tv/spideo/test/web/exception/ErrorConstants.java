package tv.spideo.test.web.exception;

import java.net.URI;

class ErrorConstants {

    private ErrorConstants() {}

    static URI getDefaultType() {
        return URI.create("exception-occured");
    }

    static URI getAuctionHouseAlreadyExist() {
        return URI.create("auction-house-already-exist");
    }

    static URI getAuctionHouseNotFound() {
        return URI.create("auction-house-not-found");
    }

    static URI getAuctionNotFound() {
        return URI.create("auction-not-found");
    }

    static URI getAuctionFinished() {
        return URI.create("auction-already-finished");
    }

    static URI getBiddingNotFound() {
        return URI.create("bidding-not-found");
    }

    static URI getBiddingPriceLow() {
        return URI.create("bidding-price-is-low");
    }

    static URI getAuctionNotStarted() {
        return URI.create("bidding-not-started");
    }

    static URI getAuctionNotFinished() {
        return URI.create("auction-not-finished");
    }

}
