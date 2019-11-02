package tv.spideo.test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(builder = AuctionBidder.AuctionBidderBuilder.class)
public class AuctionBidder implements Base {

    @JsonProperty("id")
    private String id;

    /* it can be an anonymous name,
    so we don't use firstName and lastName */
    @JsonProperty("name")
    private String name;

    @Builder.Default
    @JsonProperty("biddingTime")
    private final Instant biddingTime = Instant.now();

    @JsonProperty("price")
    private double price;

}
