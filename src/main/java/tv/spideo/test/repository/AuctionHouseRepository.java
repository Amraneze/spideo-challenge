package tv.spideo.test.repository;

import org.springframework.stereotype.Repository;
import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionHouse;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuctionHouseRepository {

    Optional<AuctionHouse> findAuctionHouseByName(String auctionHouseName);
    Optional<AuctionHouse> findAuctionHouseById(String auctionHouseId);
    Optional<AuctionHouse> saveAuctionHouse(AuctionHouse auctionHouse);
    List<AuctionHouse> findAllAuctionHouses();
    List<AuctionHouse> findAllAuctionHousesByCreatorId(String auctionHouseCreator);
    Optional<Boolean> deleteAuctionHouse(AuctionHouse auctionHouse);
    void deleteAllAuctionHouses();

    Optional<Boolean> deleteAuction(AuctionHouse auctionHouse, Auction auction);
    Optional<Auction> findAuctionByHouseIdAndAuctionId(String auctionHouseId, String auctionId);

}
