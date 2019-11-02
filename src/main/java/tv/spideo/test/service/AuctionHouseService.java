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

@Service
public class AuctionHouseService {

    private final AuctionHouseRepository auctionHouseRepository;

    @Autowired
    public AuctionHouseService(AuctionHouseRepository auctionHouseRepository) {
        this.auctionHouseRepository = auctionHouseRepository;
    }

    public AuctionHouse createAuctionHouse(AuctionHouse auctionHouse) {
        auctionHouseRepository.findAuctionHouseByName(auctionHouse.getName())
                .ifPresent((existedAuctionHouse) -> { throw new AuctionHouseAlreadyExistException(); });
        return auctionHouseRepository.saveAuctionHouse(auctionHouse)
                .orElseThrow(GeneralException::new);
    }

    public List<AuctionHouse> getAllAuctionHouses() {
        return auctionHouseRepository.findAllAuctionHouses();
    }

    public List<AuctionHouse> getAuctionHousesByCreatorId(String creatorId) {
        return auctionHouseRepository.findAllAuctionHousesByCreatorId(creatorId);
    }

    public boolean deleteAuctionHouse(String auctionHouseId) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        return auctionHouseRepository.deleteAuctionHouse(auctionHouse)
                .orElseThrow(AuctionHouseNotFoundException::new);
    }


    public void deleteAllAuctionHouse() {
        auctionHouseRepository.deleteAllAuctionHouses();
    }

    public Auction createAuction(String auctionHouseId, Auction auction) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> {
                    auction.setId(CommonUtils.generateUUID());
                    auctionHouse.addAuction(auction);
                    auctionHouseRepository.saveAuctionHouse(auctionHouse);
                    return auction;
                })
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    public List<Auction> getAuctionsByAuctionHouseId(String auctionHouseId) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> new ArrayList<>(auctionHouse.getAuctions().values()))
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

    public boolean deleteAuction(String auctionHouseId, String auctionId) {
        AuctionHouse auctionHouse = auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .orElseThrow(AuctionHouseNotFoundException::new);
        Auction auction = Optional.ofNullable(auctionHouse.getAuctions().get(auctionId))
                .orElseThrow(AuctionHouseNotFoundException::new);
        return auctionHouseRepository.deleteAuction(auctionHouse, auction).isPresent();
    }

    public List<Auction> getAuctionsByStatus(String auctionHouseId, Auction.AuctionStatus auctionStatus) {
        return auctionHouseRepository.findAuctionHouseById(auctionHouseId)
                .map((auctionHouse) -> auctionHouse.getAuctions().values()
                            .stream()
                            .filter((auction) -> auction.isFiltered(auctionStatus))
                            .collect(Collectors.toList())
                )
                .orElseThrow(AuctionHouseNotFoundException::new);
    }

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

    public AuctionBidder getAuctionWinner(String auctionHouseId, String auctionId) {
        Auction auction = auctionHouseRepository.findAuctionByHouseIdAndAuctionId(auctionHouseId, auctionId)
                .filter((_auction) -> _auction.getStatus() == Auction.AuctionStatus.TERMINATED)
                .orElseThrow(AuctionNotFinishedException::new);
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
