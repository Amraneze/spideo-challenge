package tv.spideo.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionBidder;
import tv.spideo.test.domain.AuctionHouse;
import tv.spideo.test.repository.AuctionHouseRepository;
import tv.spideo.test.util.CommonUtils;
import tv.spideo.test.web.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The service which communicates with the controller and the repository (Database)
 *
 * @author Amrane Ait Zeouay
 * @since 0.0.1
 */
@Service
public class AuctionHouseService {

    private final AuctionHouseRepository auctionHouseRepository;

    /**
     * Create a new {@link AuctionHouseService} instance with the
     * repository that will be injected to it.
     *
     * @param auctionHouseRepository an instance of {@link AuctionHouseRepository}
     */
    @Autowired
    public AuctionHouseService(AuctionHouseRepository auctionHouseRepository) {
        this.auctionHouseRepository = auctionHouseRepository;
    }

    /**
     * A function that will create an auction house
     * from a valid "AuctionHouse" model. It checks
     * if the given name of the auction house is
     * already used then we should throw an exception
     * otherwise, save it in the database.
     **
     * @param auctionHouse the auction house that needs to be created
     * @throws AuctionHouseAlreadyExistException if the auction house's name exist
     * @throws GeneralException if there is an error occurred while saving the auction house
     * @return a response entity of the same auction house but with an ID
     */
    public AuctionHouse createAuctionHouse(AuctionHouse auctionHouse) {
        auctionHouseRepository.findAuctionHouseByName(auctionHouse.getName())
                .ifPresent((existedAuctionHouse) -> { throw new AuctionHouseAlreadyExistException(); });
        return auctionHouseRepository.saveAuctionHouse(auctionHouse)
                .orElseThrow(GeneralException::new);
    }

    /**
     * Get all auction houses in the actual database.
     *
     * @return an empty list or actual list of the auction houses
     */
    public List<AuctionHouse> getAllAuctionHouses() {
        return auctionHouseRepository.findAllAuctionHouses();
    }

    /**
     * Get auctions by the id of their creator.
     *
     * @return an empty list or actual list of the auction houses
     */
    public List<AuctionHouse> getAuctionHousesByCreatorId(String creatorId) {
        return auctionHouseRepository.findAllAuctionHousesByCreatorId(creatorId);
    }

    /**
     * Delete an auction house by id. It check if the auction house
     * does exist before deleting it.
     *
     * @param auctionHouseId the auction house id that needs to be deleted
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return A boolean, true if the auction house is deleted, false otherwise
     */
    public boolean deleteAuctionHouse(String auctionHouseId) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        return auctionHouseRepository.deleteAuctionHouse(auctionHouse)
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    /**
     * Delete all auction houses to clear the db for test purpose.
     */
    public void deleteAllAuctionHouse() {
        auctionHouseRepository.deleteAllAuctionHouses();
    }

    /**
     * Create an auction in a specific auction house, we should check
     * if the auction house exist so we can create the auction inside
     * of it otherwise we need to throw an exception.
     *
     * @param auctionHouseId the auction house id of the auction
     * @param auction a valid {@link Auction} model
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return The actual auction with a generated ID
     */
    public Auction createAuction(String auctionHouseId, Auction auction) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> {
                    auction.setId(CommonUtils.generateUUID());
                    // We need to set the current price to the initial price if it's null
                    auction.setCurrentPriceIfZero();
                    auctionHouse.addAuction(auction);
                    auctionHouseRepository.saveAuctionHouse(auctionHouse);
                    return auction;
                })
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    /**
     * Get all auctions of a specific auction house or throw
     * the an exception if the auction house is not found
     *
     * @param auctionHouseId the auction house id that contains the auction
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return An empty list or a list of auctions of that auction house
     */
    public List<Auction> getAuctionsByAuctionHouseId(String auctionHouseId) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> new ArrayList<>(auctionHouse.getAuctions().values()))
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    /**
     * Delete an auction by id of an auction house or throw
     * the an exception if the auction house or the auction
     * is not found
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that needs to be deleted
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @throws AuctionNotFoundException if the auction is not found in the list of auctions
     * @return A boolean, true if the auction house is deleted, false otherwise
     */
    public boolean deleteAuction(String auctionHouseId, String auctionId) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        Auction auction = Optional.ofNullable(auctionHouse.getAuctions().get(auctionId))
                .orElseThrow(AuctionNotFoundException::new);
        return auctionHouseRepository.deleteAuction(auctionHouse, auction).isPresent();
    }

    /**
     * Get auctions of an auction house by status or
     * throw exception if the auction house is not
     * found.
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionStatus the status of the auction {@link Auction.AuctionStatus}
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return An empty list or a list of filtered auctions with the given status
     */
    public List<Auction> getAuctionsByStatus(String auctionHouseId, Auction.AuctionStatus auctionStatus) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> auctionHouse.getAuctions().values()
                            .stream()
                            .filter((auction) -> auction.isFiltered(auctionStatus))
                            .collect(Collectors.toList())
                )
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    /**
     * Update an auction's status of an auction house or
     * throw an exception if the auction is not finished,
     * the auction or the auction house is not found.
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction to update
     * @param auctionStatus the new status of the auction
     * @throws AuctionFinishedException if the auction already finished
     * @throws AuctionNotFoundException if the auction is not found
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return The auction with the updated status
     */
    public Auction updateAuctionStatus(String auctionHouseId, String auctionId, Auction.AuctionStatus auctionStatus) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        Auction auction = auctionHouse.getAuctions()
                .values()
                .stream()
                .filter(v -> v.getId().contentEquals(auctionId))
                .findFirst()
                .orElseThrow(AuctionNotFoundException::new);
        if (auction.getStatus() == Auction.AuctionStatus.NOT_FOUND) {
            throw new AuctionNotFoundException();
        } else if (auction.getStatus() == Auction.AuctionStatus.TERMINATED) {
            throw new AuctionFinishedException();
        }
        auction.setStatus(auctionStatus);
        auctionHouse.getAuctions().replace(auctionId, auction);
        // In case of the auction house was deleted by someone else
        auctionHouseRepository.saveAuctionHouse(auctionHouse)
                .orElseThrow(AuctionHouseNotFoundException::new);
        return auction;
    }

    /**
     * Bid on an auction in an auction house or throw
     * an exception if the auction did not start yet,
     * the bidding's price is lower than the current
     * auction's price, the auction or the auction
     * house is not found.
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction to bid on
     * @param bidder a valid {@link AuctionBidder} that wants to bid
     * @throws AuctionNotStartedException if the auction did not start yet
     * @throws BiddingPriceLowException if the the bidder's price is lower than the current auction price
     * @throws AuctionNotFoundException if the auction was not found in the list of the auctions
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return The actual bidder with a generated id
     */
    public AuctionBidder bidOnAuction(String auctionHouseId, String auctionId, AuctionBidder bidder) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> {
                    Auction auction = Optional.of(auctionHouse.getAuctions()
                            .get(auctionId))
                            .orElseThrow(AuctionNotFoundException::new);
                    if (auction.getStatus() != Auction.AuctionStatus.RUNNING) {
                        throw new AuctionNotStartedException();
                    }
                    if (bidder.getPrice() > auction.getCurrentPrice() && bidder.getPrice() > auction.getInitialPrice())
                        auction.setCurrentPrice(bidder.getPrice());
                    else
                        throw new BiddingPriceLowException();
                    bidder.setId(CommonUtils.generateUUID());
                    auctionHouse.getAuctions().replace(auctionId, auction.addBid(bidder));
                    auctionHouseRepository.saveAuctionHouse(auctionHouse);
                    return bidder;
                })
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    /**
     * Get all bidding of an auction in an auction house
     * or throw an exception if the auction house or the
     * auction is not found.
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction
     * @throws AuctionNotFoundException if the auction is not found in the list of auctions
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return Map of id of bidders and prices that they used to bid
     */
    public Map<String, Double> getAllBidding(String auctionHouseId, String auctionId) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        Auction auction = auctionHouse.getAuctions()
                .values()
                .stream()
                .filter(v -> v.getId().contentEquals(auctionId))
                .findFirst()
                .orElseThrow(AuctionNotFoundException::new);
        return auction.getBidding()
                .entrySet()
                .stream()
                .collect(Collectors.toMap((entry) -> auction.getBidders().get(entry.getKey()).getName(),
                        Map.Entry::getValue));
    }

    /**
     * Get the auction's winner of an auction house or throw
     * an exception if there was no bidding, the auction did
     * not finish yet, the auction or auction house is not
     * found.
     *
     * @param auctionHouseId the auction house id that we should to get the auctions
     * @param auctionId the auction id that we should use to get the auction
     * @throws AuctionNotFoundException if the auction is not found in the list of auctions
     * @throws NoBiddingFoundException if there is no bidding, maybe we should throw GeneralException
     * @throws AuctionNotFinishedException if the auction is not found in the list of auctions
     * @throws AuctionHouseNotFoundException if the auction house is not found in db
     * @return The winner of the auction {@link AuctionBidder}
     */
    public AuctionBidder getAuctionWinner(String auctionHouseId, String auctionId) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        Auction auction = auctionHouse.getAuctions()
                .values()
                .stream()
                .filter(v -> v.getId().contentEquals(auctionId))
                .findFirst()
                .orElseThrow(AuctionNotFoundException::new);
        if (auction.getStatus() != Auction.AuctionStatus.TERMINATED) {
            throw new AuctionNotFinishedException();
        }
        return auction.getBidders()
            .get(auction
                .getBidding()
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(NoBiddingFoundException::new)
                .getKey());
    }
}
