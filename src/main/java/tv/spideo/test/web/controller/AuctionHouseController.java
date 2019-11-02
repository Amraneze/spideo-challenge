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

@RestController
@RequestMapping("/auction/house/")
public class AuctionHouseController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getCanonicalName());

    private final AuctionHouseService auctionHouseService;

    @Autowired
    public AuctionHouseController(AuctionHouseService auctionHouseService) {
        this.auctionHouseService = auctionHouseService;
    }

    @PostMapping
    public ResponseEntity<AuctionHouse> createAuctionHouse(@Valid @RequestBody AuctionHouse auctionHouse) {
        logger.debug("Create a new Auction House {}", auctionHouse.toString());
        return ResponseWrapper
                .wrapResponse(auctionHouseService.createAuctionHouse(auctionHouse));
    }

    /**
     * Get all auction houses in the mocked db
     * @return a list of the auction houses
     */
    @GetMapping
    public ResponseEntity<List<AuctionHouse>> getAllAuctionHouses() {
        logger.debug("Get all Auction Houses");
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAllAuctionHouses());
    }

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
     * Auction
     * @param auctionHouseId the auction id
     * @return ResponseEntity if everything went fine, otherwise an exception will be thrown
     */
    @DeleteMapping("{auctionHouseId}")
    public ResponseEntity<Boolean> deleteAuctionHouse(@PathVariable String auctionHouseId) {
        logger.debug("Delete the Auction House {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.deleteAuctionHouse(auctionHouseId));
    }


    @PostMapping(value = "{auctionHouseId}/create", consumes = {"application/json"})
    public ResponseEntity<Auction> createAuction(@PathVariable String auctionHouseId,
                                                 @Valid @RequestBody Auction auction) {
        logger.debug("Create a new Auction {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.createAuction(auctionHouseId, auction));
    }

    @GetMapping("{auctionHouseId}")
    public ResponseEntity<List<Auction>> getAuctionsByAuctionHouseId(@PathVariable String auctionHouseId) {
        logger.debug("Get all auctions of the auction house {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionsByAuctionHouseId(auctionHouseId));
    }

    /**
     * Delete an auction house by id, we should also check if the
     * person who wants to delete the auction house has the rights
     * to do it, only the user who created the auction house can
     * delete it, so in the deleteAuctionHouse we should check
     * if the person who wants to delete is the same as in
     * Auction
     * @param auctionHouseId the auction id
     * @return ResponseEntity if everything went fine, otherwise an exception will be thrown
     */
    @DeleteMapping("{auctionHouseId}/{auctionId}")
    public ResponseEntity<Boolean> deleteAuction(@PathVariable String auctionHouseId,
                                                 @PathVariable String auctionId) {
        logger.debug("Delete the Auction {} from the Auction house {}", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse( auctionHouseService.deleteAuction(auctionHouseId, auctionId));
    }

    @GetMapping("{auctionHouseId}/{status}")
    public ResponseEntity<List<Auction>> getAuctionsByStatus(@PathVariable String auctionHouseId,
                                                             @PathVariable Auction.AuctionStatus status) {
        logger.debug("Get auctions with status {} of the auction house {}", status, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionsByStatus(auctionHouseId, status));
    }

    @PutMapping(value = "{auctionHouseId}/{auctionId}/status/{status}", consumes = {"application/json"})
    public ResponseEntity<Auction> updateAuctionStatus(@PathVariable String auctionHouseId,
                                                       @PathVariable String auctionId,
                                                       @PathVariable Auction.AuctionStatus status) {
        logger.debug("Change the status of the auction {} in the auction house {} ", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.updateAuctionStatus(auctionHouseId, auctionId, status));
    }

    @PostMapping(value = "{auctionHouseId}/{auctionId}/bid", consumes = {"application/json"})
    public ResponseEntity<AuctionBidder> bidOnAuction(@PathVariable String auctionHouseId,
                                                      @PathVariable String auctionId,
                                                      @Valid @RequestBody AuctionBidder bidder) {
        logger.debug("Bid on the auction {}", auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.bidOnAuction(auctionHouseId, auctionId, bidder));
    }

    @GetMapping("{auctionHouseId}/{auctionId}/bid")
    public ResponseEntity<Map<String, Double>> getAllBiddingOfAuction(@PathVariable String auctionHouseId,
                                                                      @PathVariable String auctionId) {
        logger.debug("Get All bidding of the auction {} in the auction house {} ", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAllBidding(auctionHouseId, auctionId));
    }

    @GetMapping("{auctionHouseId}/{auctionId}/winner")
    public ResponseEntity<AuctionBidder> getAuctionWinner(@PathVariable String auctionHouseId,
                                                          @PathVariable String auctionId) {
        logger.debug("Get the winner of the auction {} in the auction house {}", auctionId, auctionHouseId);
        return ResponseWrapper
                .wrapResponse(auctionHouseService.getAuctionWinner(auctionHouseId, auctionId));
    }
}
