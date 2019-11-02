package tv.spideo.test.repository;

import org.springframework.stereotype.Component;
import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionHouse;
import tv.spideo.test.util.CommonUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AuctionHouseRepositoryImpl implements AuctionHouseRepository {

    private static HashMap<String, AuctionHouse> auctionHouses = new HashMap<>();

    @Override
    public Optional<AuctionHouse> findAuctionHouseByName(String auctionHouseName) {
        return auctionHouses.values()
                .stream()
                .filter((auctionHouse) -> auctionHouse.getName().contentEquals(auctionHouseName))
                .findFirst();
    }

    @Override
    public Optional<AuctionHouse> findAuctionHouseById(String auctionHouseId) {
        return auctionHouses.entrySet()
                .stream()
                .filter((entry) -> entry.getKey().contentEquals(auctionHouseId))
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public Optional<AuctionHouse> saveAuctionHouse(AuctionHouse auctionHouse) {
        if (auctionHouse.getId() == null)
            auctionHouse.setId(CommonUtils.generateUUID());
        auctionHouses.put(auctionHouse.getId(), auctionHouse);
        return Optional.of(auctionHouse);
    }

    @Override
    public List<AuctionHouse> findAllAuctionHouses() {
        return new ArrayList<>(auctionHouses.values());
    }

    @Override
    public List<AuctionHouse> findAllAuctionHousesByCreatorId(String auctionHouseCreator) {
        return auctionHouses.values()
                .stream()
                .filter((auction) -> auction.getCreatorName().contentEquals(auctionHouseCreator))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Boolean> deleteAuctionHouse(AuctionHouse auctionHouse) {
        return Optional.of(auctionHouses.remove(auctionHouse.getId()) != null);
    }

    @Override
    public Optional<Boolean> deleteAuction(AuctionHouse auctionHouse, Auction auction) {
        return Optional.of(auctionHouses.get(auctionHouse.getId())
                .getAuctions()
                .remove(auction.getId()) != null);
    }

    @Override
    public void deleteAllAuctionHouses() {
        auctionHouses.clear();
    }

    @Override
    public Optional<Auction> findAuctionByHouseIdAndAuctionId(String auctionHouseId, String auctionId) {
        return Optional.of(auctionHouses.get(auctionHouseId)
                .getAuctions()
                .get(auctionId));
    }

}
