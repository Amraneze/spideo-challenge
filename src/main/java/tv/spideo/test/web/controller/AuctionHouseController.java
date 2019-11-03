package tv.spideo.test.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionBidder;
import tv.spideo.test.domain.AuctionHouse;
import tv.spideo.test.service.AuctionHouseService;
import tv.spideo.test.web.util.ResponseWrapper;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * A REST controller which handles all HTTP requests starting with "/auctio/house/".
 *
 * @author Amrane Ait Zeouay
 * @since 0.0.1
 */
@RestController
@RequestMapping("/auction/house/")
public class AuctionHouseController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getCanonicalName());

    private final AuctionHouseService auctionHouseService;

    /**
     * Create a new {@link AuctionHouseController} instance with the service injected to it.
     *
     * @param auctionHouseService an instance of {@link AuctionHouseService}
     */
    @Autowired
    public AuctionHouseController(AuctionHouseService auctionHouseService) {
        this.auctionHouseService = auctionHouseService;
    }


    /**
     * A function that will create an auction house
     * from a valid "AuctionHouse" model.
     *
     * <pre><code>Endpoint: POST /auction/house/</code></pre>
     *
     * @param auctionHouse the auction house that needs
     * to be created
     * @return a response entity of the same auction
     * house but with an ID
     */
    @PostMapping
    public ResponseEntity<AuctionHouse> createAuctionHouse(@Valid @RequestBody AuctionHouse auctionHouse) {
        logger.debug("Create a new Auction House {}", auctionHouse.toString());
        return ResponseWrapper
                .wrapResponse(auctionHouseService.createAuctionHouse(auctionHouse));
    }

    /**
     * Get all auction houses in the actual database.
     *
     * <pre><code>Endpoint: GET /auction/house/</code></pre>
     *
     * @return an empty list or actual list of the auction houses
     */
    @GetMapping
    public ResponseEntity<List<AuctionHouse>> getAllAuctionHouses() {
        logger.debug("Get all Auction Houses");
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAllAuctionHouses());
    }

    /**
     * Get auctions by the id of their creator.
     *
     * <pre><code>Endpoint: GET /auction/house/:creatorId</code></pre>
     *
     * @return an empty list or actual list of the auction houses
     */
    @GetMapping("creator/{creatorId}")
    public ResponseEntity<List<AuctionHouse>> getAuctionHousesByCreatorId(@PathVariable String creatorId) {
        logger.debug("Get all Auction Houses by creator id {}", creatorId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionHousesByCreatorId(creatorId));
    }

    /**
     * Delete an auction house by id, we should also check if the
     * person who wants to delete the auction house has the rights
     * to do it, only the user who created the auction house can
     * delete it, so in the deleteAuctionHouse we should check
     * if the person who wants to delete is the same as in
     * Auction.
     *
     * <pre><code>Endpoint: DELETE /auction/house/:auctionHouseId</code></pre>
     *
     * @param auctionHouseId the auction house id that needs to be deleted
     * @return ResponseEntity of a boolean if the auction house is deleted
     */
    @DeleteMapping("{auctionHouseId}")
    public ResponseEntity<Boolean> deleteAuctionHouse(@PathVariable String auctionHouseId) {
        logger.debug("Delete the Auction House {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.deleteAuctionHouse(auctionHouseId));
    }

    /**
     * Create an auction in a specific auction house, we should check
     * if the auction house exist so we can create the auction inside
     * of it otherwise we need to throw an exception.
     *
     * <pre><code>Endpoint: POST /auction/house/:auctionHouseId/create</code></pre>
     *
     * @param auctionHouseId the auction house id of the auction
     * @param auction a valid {@link Auction} model
     * @return ResponseEntity of the actual auction with a generated ID
     */
    @PostMapping("{auctionHouseId}/create")
    public ResponseEntity<Auction> createAuction(@PathVariable String auctionHouseId,
                                                 @Valid @RequestBody Auction auction) {
        logger.debug("Create a new Auction {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.createAuction(auctionHouseId, auction));
    }

    /**
     * Get all auctions of a specific auction house.
     *
     * <pre><code>Endpoint: GET /auction/house/:auctionHouseId</code></pre>
     *
     * @param auctionHouseId the auction house id that contains the auction
     * @return An empty list or a list of auctions of that auction house
     */
    @GetMapping("{auctionHouseId}")
    public ResponseEntity<List<Auction>> getAuctionsByAuctionHouseId(@PathVariable String auctionHouseId) {
        logger.debug("Get all auctions of the auction house {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionsByAuctionHouseId(auctionHouseId));
    }

    /**
     * Delete an auction by id of an auction house.
     *
     * <pre><code>Endpoint: DELETE /auction/house/:auctionHouseId/:auctionId</code></pre>
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that needs to be deleted
     * @return ResponseEntity of a boolean if the auction is deleted
     */
    @DeleteMapping("{auctionHouseId}/{auctionId}")
    public ResponseEntity<Boolean> deleteAuction(@PathVariable String auctionHouseId,
                                                 @PathVariable String auctionId) {
        logger.debug("Delete the Auction {} from the Auction house {}", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse( auctionHouseService.deleteAuction(auctionHouseId, auctionId));
    }

    /**
     * Get auctions of an auction house by status.
     *
     * <pre><code>Endpoint: GET /auction/house/:auctionHouseId/:status</code></pre>
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param status the status of the auction {@link Auction.AuctionStatus}
     * @return An empty list or a list of filtered auctions with the given status
     */
    @GetMapping("{auctionHouseId}/{status}")
    public ResponseEntity<List<Auction>> getAuctionsByStatus(@PathVariable String auctionHouseId,
                                                             @PathVariable Auction.AuctionStatus status) {
        logger.debug("Get auctions with status {} of the auction house {}", status, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionsByStatus(auctionHouseId, status));
    }

    /**
     * Update an auction's status of an auction house.
     *
     * <pre><code>Endpoint: PUT /auction/house/:auctionHouseId/:auctionId/:status</code></pre>
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction to update
     * @param status the new status of the auction
     * @return The auction with the updated status
     */
    @PutMapping("{auctionHouseId}/{auctionId}/status/{status}")
    public ResponseEntity<Auction> updateAuctionStatus(@PathVariable String auctionHouseId,
                                                       @PathVariable String auctionId,
                                                       @PathVariable Auction.AuctionStatus status) {
        logger.debug("Change the status of the auction {} in the auction house {} ", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.updateAuctionStatus(auctionHouseId, auctionId, status));
    }

    /**
     * Bid on an auction in an auction house.
     *
     * <pre><code>Endpoint: POST /auction/house/:auctionHouseId/:auctionId/bid</code></pre>
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction to bid on
     * @param bidder a valid {@link AuctionBidder} that wants to bid
     * @return The actual bidder with a generated id
     */
    @PostMapping("{auctionHouseId}/{auctionId}/bid")
    public ResponseEntity<AuctionBidder> bidOnAuction(@PathVariable String auctionHouseId,
                                                      @PathVariable String auctionId,
                                                      @Valid @RequestBody AuctionBidder bidder) {
        logger.debug("Bid on the auction {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.bidOnAuction(auctionHouseId, auctionId, bidder));
    }

    /**
     * Get all bidding of an auction in an auction house.
     *
     * <pre><code>Endpoint: GET /auction/house/:auctionHouseId/:auctionId/bid</code></pre>
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction
     * @return Map of id of bidders and prices that they used to bid
     */
    @GetMapping("{auctionHouseId}/{auctionId}/bid")
    public ResponseEntity<Map<String, Double>> getAllBiddingOfAuction(@PathVariable String auctionHouseId,
                                                                      @PathVariable String auctionId) {
        logger.debug("Get All bidding of the auction {} in the auction house {} ", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAllBidding(auctionHouseId, auctionId));
    }

    /**
     * Get the auction's winner of an auction house.
     *
     * <pre><code>Endpoint: GET /auction/house/:auctionHouseId/:auctionId/winner</code></pre>
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction
     * @return The winner of the auction {@link AuctionBidder}
     */
    @GetMapping("{auctionHouseId}/{auctionId}/winner")
    public ResponseEntity<AuctionBidder> getAuctionWinner(@PathVariable String auctionHouseId,
                                                          @PathVariable String auctionId) {
        logger.debug("Get the winner of the auction {} in the auction house {}", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionWinner(auctionHouseId, auctionId));
    }
}
