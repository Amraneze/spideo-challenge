package tv.spideo.test.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(builder = AuctionHouse.AuctionHouseBuilder.class)
public class AuctionHouse implements Base {

    @JsonProperty("id")
    private String id;

    @NotNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("creatorName")
    private String creatorName;

    @Builder.Default
    @JsonProperty("auctions")
    private final HashMap<String, Auction> auctions = new HashMap<>();

    @JsonIgnore
    public void addAuction(Auction auction) {
        auctions.put(auction.getId(), auction);
    }

}
