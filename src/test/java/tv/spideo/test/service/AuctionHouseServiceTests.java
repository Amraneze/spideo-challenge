package tv.spideo.test.service;

import org.junit.jupiter.api.*;
import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionBidder;
import tv.spideo.test.domain.AuctionHouse;
import tv.spideo.test.repository.AuctionHouseRepositoryImpl;
import tv.spideo.test.util.CommonUtils;
import tv.spideo.test.util.TestCommonUtils;
import tv.spideo.test.web.exception.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class AuctionHouseServiceTests {

    private static AuctionHouseService auctionHouseService;
    private static AuctionHouse mockedAuctionHouse;
    private static Auction mockedAuction;
    private static AuctionBidder mockedBidder;

    @BeforeAll
    static void setUp() {
        auctionHouseService = new AuctionHouseService(new AuctionHouseRepositoryImpl());
    }

    @BeforeEach
    void init() {
        mockedAuctionHouse = TestCommonUtils.generateRandomAuctionHouse(false, 0);
        mockedBidder = TestCommonUtils.generateRandomBidder();
    }

    @AfterEach
    void clearAll() {
        auctionHouseService.deleteAllAuctionHouse();
    }

    @Test
    @DisplayName("It should create a new Auction house with a new name")
    void itShouldAddAnAuctionHouse() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        Assertions.assertEquals(mockedAuctionHouse.getName(), auctionHouse.getName());
        Assertions.assertEquals(mockedAuctionHouse.getCreatorName(), auctionHouse.getCreatorName());
        Assertions.assertNotNull(auctionHouse.getId());
    }

    @Test
    @DisplayName("It should throw an auction already exist exception")
    void itShouldThrowAuctionHouseExistException() {
        auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        Assertions.assertThrows(AuctionHouseAlreadyExistException.class, () ->
            auctionHouseService.createAuctionHouse(mockedAuctionHouse)
        );
    }

    @Test
    @DisplayName("It should list all auction houses that are inserted in the db")
    void itShouldListAllAuctionHouses() {
        List<AuctionHouse> mockedAuctionHouses = TestCommonUtils.generateListOfRandomAuctionHouse()
                .stream()
                .map((auctionHouse) -> auctionHouseService.createAuctionHouse(auctionHouse))
                .collect(Collectors.toList());
        List<AuctionHouse> auctionHouses = auctionHouseService.getAllAuctionHouses();
        Assertions.assertEquals(mockedAuctionHouses.size(), auctionHouses.size());

        List<AuctionHouse> sortedMockedAuctionHouses = mockedAuctionHouses
                .stream()
                .sorted(Comparator.comparing(AuctionHouse::getName))
                .collect(Collectors.toList());

        List<AuctionHouse> sortedAuctionHouses = auctionHouses
                .stream()
                .sorted(Comparator.comparing(AuctionHouse::getName))
                .collect(Collectors.toList());

        IntStream.range(0, sortedMockedAuctionHouses.size()).forEach((index) -> {
            Assertions.assertEquals(sortedMockedAuctionHouses.get(index).getCreatorName(),
                    sortedAuctionHouses.get(index).getCreatorName());
            Assertions.assertEquals(sortedMockedAuctionHouses.get(index).getName(),
                    sortedAuctionHouses.get(index).getName());
        });
    }

    @Test
    @DisplayName("It should delete an auction house based on its id")
    void itShouldDeleteAnAuctionHouse() {
        List<AuctionHouse> mockedAuctionHouses = TestCommonUtils.generateListOfRandomAuctionHouse()
                .stream()
                .map((auctionHouse) -> auctionHouseService.createAuctionHouse(auctionHouse))
                .collect(Collectors.toList());
        AuctionHouse randomAuctionHouse = mockedAuctionHouses.get(2);
        auctionHouseService.deleteAuctionHouse(randomAuctionHouse.getId());

        List<AuctionHouse> auctionHouses = auctionHouseService.getAllAuctionHouses();
        auctionHouses.forEach((auctionHouse) -> Assertions.assertNotEquals(auctionHouse.getId(), randomAuctionHouse.getId()));
    }

    @Test
    @DisplayName("It should add an auction in a specific auction house")
    void itShouldAddAnAuction() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(auctionHouse, false, 0);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);
        Assertions.assertEquals(mockedAuction.getName(), auction.getName());
        Assertions.assertEquals(mockedAuction.getStatus(), auction.getStatus());
        Assertions.assertEquals(mockedAuction.getCreatorId(), auction.getCreatorId());
        Assertions.assertNotNull(auction.getId());
    }

    @Test
    @DisplayName("It should throw an auction not found exception")
    void itShouldThrowAuctionNotFoundException() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        Assertions.assertThrows(AuctionNotFoundException.class, () ->
                auctionHouseService.updateAuctionStatus(auctionHouse.getId(),
                        CommonUtils.generateUUID(), Auction.AuctionStatus.RUNNING)
        );

        mockedAuction = TestCommonUtils.generateRandomAuction(auctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.NOT_FOUND);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);
        Assertions.assertThrows(AuctionNotFoundException.class, () ->
                auctionHouseService.updateAuctionStatus(auctionHouse.getId(),
                        auction.getId(), Auction.AuctionStatus.RUNNING)
        );
    }

    @Test
    @DisplayName("It should list all auctions of a specific auction house")
    void itShouldListAllAuctionsOfAnAuctionHouse() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        List<Auction> mockedAuctions = TestCommonUtils.generateListOfRandomAuction(auctionHouse)
                .stream()
                .map((auction) -> auctionHouseService.createAuction(auctionHouse.getId(), auction))
                .collect(Collectors.toList());
        List<Auction> auctions = auctionHouseService.getAuctionsByAuctionHouseId(auctionHouse.getId());

        List<Auction> sortedMockedAuctions = mockedAuctions
                .parallelStream()
                .sorted(Comparator.comparing(Auction::getId))
                .collect(Collectors.toList());

        List<Auction> sortedAuctions = auctions
                .parallelStream()
                .sorted(Comparator.comparing(Auction::getId))
                .collect(Collectors.toList());

        IntStream.range(0, sortedMockedAuctions.size()).forEach((index) -> {
            Assertions.assertEquals(sortedMockedAuctions.get(index).getCreatorId(),
                    sortedAuctions.get(index).getCreatorId());
            Assertions.assertEquals(sortedMockedAuctions.get(index).getName(),
                    sortedAuctions.get(index).getName());
            Assertions.assertEquals(sortedMockedAuctions.get(index).getStatus(),
                    sortedAuctions.get(index).getStatus());
            Assertions.assertEquals(sortedMockedAuctions.get(index).getMaxBidders(),
                    sortedAuctions.get(index).getMaxBidders());
            Assertions.assertEquals(sortedMockedAuctions.get(index).getDescription(),
                    sortedAuctions.get(index).getDescription());
            Assertions.assertEquals(sortedMockedAuctions.get(index).getInitialPrice(),
                    sortedAuctions.get(index).getInitialPrice());
            Assertions.assertEquals(sortedMockedAuctions.get(index).getStartingTime(),
                    sortedAuctions.get(index).getStartingTime());
        });
    }

    @Test
    @DisplayName("It should throw an auction already terminated exception")
    void itShouldThrowAuctionFinishedException() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(auctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.TERMINATED);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);
        Assertions.assertThrows(AuctionFinishedException.class, () ->
                auctionHouseService.updateAuctionStatus(auctionHouse.getId(),
                        auction.getId(), Auction.AuctionStatus.RUNNING)
        );
    }

    @Test
    @DisplayName("It should delete an auction from a specific auction house based on its id")
    void itShouldDeleteAnAuction() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(auctionHouse, false, 0);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        auctionHouseService.deleteAuction(auctionHouse.getId(), auction.getId());

        auctionHouseService.getAuctionsByAuctionHouseId(auctionHouse.getId())
                .parallelStream()
                .forEach((_auction) -> Assertions.assertNotEquals(auction.getId(), _auction.getId()));
    }

    @Test
    @DisplayName("It should display auctions by status in a specific auction house")
    void itShouldDisplayAuctionsByStatus() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        TestCommonUtils.generateListOfRandomAuction(auctionHouse)
                .forEach((auction) -> auctionHouseService.createAuction(auctionHouse.getId(), auction));
        auctionHouseService.getAuctionsByStatus(auctionHouse.getId(), Auction.AuctionStatus.RUNNING)
                .parallelStream()
                .forEach((auction) -> Assertions.assertEquals(Auction.AuctionStatus.RUNNING, auction.getStatus()));
        auctionHouseService.getAuctionsByStatus(auctionHouse.getId(), Auction.AuctionStatus.DELETED)
                .parallelStream()
                .forEach((auction) -> Assertions.assertEquals(Auction.AuctionStatus.DELETED, auction.getStatus()));
        auctionHouseService.getAuctionsByStatus(auctionHouse.getId(), Auction.AuctionStatus.NOT_FOUND)
                .parallelStream()
                .forEach((auction) -> Assertions.assertEquals(Auction.AuctionStatus.NOT_FOUND, auction.getStatus()));
        auctionHouseService.getAuctionsByStatus(auctionHouse.getId(), Auction.AuctionStatus.TERMINATED)
                .parallelStream()
                .forEach((auction) -> Assertions.assertEquals(Auction.AuctionStatus.TERMINATED, auction.getStatus()));
        auctionHouseService.getAuctionsByStatus(auctionHouse.getId(), Auction.AuctionStatus.NOT_STARTED)
                .parallelStream()
                .forEach((auction) -> Assertions.assertEquals(Auction.AuctionStatus.NOT_STARTED, auction.getStatus()));
    }

    @Test
    @DisplayName("It should bid on a specific auction")
    void itShouldBidOnAnAuction() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(auctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        // FIXED one bug from the list in the issue #1
        // We should always set the price of the bidder to be
        // bigger than the current price of the auction
        mockedBidder.setPrice(auction.getInitialPrice() + 100d);

        AuctionBidder bidder = auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        Assertions.assertEquals(mockedBidder.getId(), bidder.getId());
        Assertions.assertEquals(mockedBidder.getName(), bidder.getName());
        Assertions.assertEquals(mockedBidder.getPrice(), bidder.getPrice());
        Assertions.assertEquals(mockedBidder.getBiddingTime(), bidder.getBiddingTime());
    }

    @Test
    @DisplayName("It should list all bidding of a bidder in specific auction")
    void itShouldListAllBiddingOfABidder() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        List<Double> biddingPrices = TestCommonUtils.generateListOfBiddingPrices(auction.getInitialPrice());

        mockedBidder.setPrice(biddingPrices.get(0));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        AuctionBidder newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 5d);

        mockedBidder.setPrice(biddingPrices.get(1));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 30d);

        mockedBidder.setPrice(biddingPrices.get(2));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 1600d);

        mockedBidder.setPrice(biddingPrices.get(3));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        auctionHouseService.getAuctionsByAuctionHouseId(auctionHouse.getId())
                .stream()
                .filter((_auction) -> _auction.getId().contentEquals(auction.getId()))
                .flatMap((_auction) -> _auction.getBidding().values().stream())
                .forEach((price) -> Assertions.assertTrue(biddingPrices.contains(price)));
    }

    @Test
    @DisplayName("It should throw an auction not started exception if a bidder want to bid")
    void itShouldThrowAuctionNotStartedException() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.TERMINATED);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);
        Assertions.assertThrows(AuctionNotStartedException.class, () ->
                auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder)
        );
    }

    @Test
    @DisplayName("It should throw an bidding's price is low when the bidder's price is lower than the auction current price")
    void itShouldThrowBiddingPriceLowException() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);
        mockedBidder.setPrice(auction.getInitialPrice() - 1);
        Assertions.assertThrows(BiddingPriceLowException.class, () ->
                auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder)
        );
    }

    @Test
    @DisplayName("It should display the winner of a finished auction of a specific auction house")
    void itShouldDisplayTheWinnerOfaFinishedAuction() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        List<Double> biddingPrices = TestCommonUtils.generateListOfBiddingPrices(auction.getInitialPrice());

        mockedBidder.setPrice(biddingPrices.get(0));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        AuctionBidder newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 5d);

        mockedBidder.setPrice(biddingPrices.get(1));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 30d);

        mockedBidder.setPrice(biddingPrices.get(2));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 1600d);

        mockedBidder.setPrice(biddingPrices.get(3));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        auctionHouseService.updateAuctionStatus(auctionHouse.getId(), auction.getId(), Auction.AuctionStatus.TERMINATED);

        AuctionBidder finishedAuction = auctionHouseService
                .getAuctionWinner(auctionHouse.getId(), auction.getId());

        Assertions.assertEquals(biddingPrices.get(3), finishedAuction.getPrice());
    }

    @Test
    @DisplayName("It should throw an auction not terminated exception when we want to get the winner of the auction")
    void itShouldThrowAuctionNotFinishedException() {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        List<Double> biddingPrices = TestCommonUtils.generateListOfBiddingPrices(auction.getInitialPrice());

        mockedBidder.setPrice(biddingPrices.get(0));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        AuctionBidder newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 5d);

        mockedBidder.setPrice(biddingPrices.get(1));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 30d);

        mockedBidder.setPrice(biddingPrices.get(2));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(auction.getCurrentPrice() + 1600d);

        mockedBidder.setPrice(biddingPrices.get(3));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), auction.getId(), mockedBidder);

        Assertions.assertThrows(AuctionNotFinishedException.class, () ->
                auctionHouseService
                        .getAuctionWinner(auctionHouse.getId(), auction.getId())
        );
    }
}
