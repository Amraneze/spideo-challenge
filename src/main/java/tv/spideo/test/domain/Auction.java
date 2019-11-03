package tv.spideo.test.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.Instant;
import java.util.HashMap;

@Data
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(builder = Auction.AuctionBuilder.class)
public class Auction implements Base {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("auctions")
    private String creatorId;

    @JsonProperty("description")
    private String description;

    @Builder.Default
    @JsonProperty("startingTime")
    private final Instant startingTime = Instant.now();

    @JsonProperty("endTime")
    private Instant endTime;

    @JsonProperty("maxBidders")
    private int maxBidders;

    @Builder.Default
    @JsonProperty("status")
    private AuctionStatus status = AuctionStatus.NOT_STARTED;

    @JsonProperty("initialPrice")
    private double initialPrice;

    @JsonProperty("currentPrice")
    private double currentPrice;

    @Builder.Default
    @JsonProperty("bidders")
    private final HashMap<String, AuctionBidder> bidders = new HashMap<>();

    @Builder.Default
    @JsonProperty("bidding")
    private final HashMap<String, Double> bidding = new HashMap<>();

    @JsonIgnore
    public Auction addBid(AuctionBidder bidder) {
        this.bidders.put(bidder.getId(), bidder);
        this.bidding.put(bidder.getId(), bidder.getPrice());
        return this;
    }

    public void setCurrentPriceIfZero() {
        this.currentPrice = Double.compare(this.currentPrice, 0.0) == 0 ? this.initialPrice : this.currentPrice;
    }

    public enum AuctionStatus {
        NOT_STARTED,
        RUNNING,
        TERMINATED,
        DELETED,
        NOT_FOUND;

        @JsonCreator
        public static AuctionStatus toAuctionStatus(String value) {
            return AuctionStatus.valueOf(value);
        }

        @JsonValue
        public String toString() {
            return this.name();
        }
    }

    public boolean isFiltered(AuctionStatus status) {
        return this.status == status;
    }

}
