package tv.spideo.test.util;

import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionBidder;
import tv.spideo.test.domain.AuctionHouse;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TestCommonUtils {

    private final static int MAX_BIDDERS = 40;
    private final static int MAX_AUCTION_HOUSES = 5;

    private static final List<String> names = Arrays
            .asList("Spideo", "Test", "Data", "Java", "AuctionHouse", "Halloween");
    private static final List<String> creatorNames = Arrays
            .asList("Elliott", "Emilie", "Jonathan", "Sonia", "Frederic", "Sarah");
    private static final String description = "A random description of a random auction house which we don't know";
    private static final List<Double> initialPrices = Arrays
            .asList(15.15, 150d, 187.98, 2005d, 10185.95, 500d);
    private static final List<Auction.AuctionStatus> auctionStatus = Arrays
            .asList(Auction.AuctionStatus.NOT_STARTED, Auction.AuctionStatus.RUNNING, Auction.AuctionStatus.NOT_FOUND,
                    Auction.AuctionStatus.TERMINATED, Auction.AuctionStatus.DELETED);

    private static int getRandomInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static double getRandomDoubleInRange(double min, double max) {
        return Double.valueOf(new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.ENGLISH))
                .format(ThreadLocalRandom.current().nextDouble(min, max + 1)));
    }

    public static AuctionHouse generateRandomAuctionHouse(boolean useIndex, int index) {
        int random = useIndex ? index : getRandomInRange(0, MAX_AUCTION_HOUSES);
        return AuctionHouse.builder()
                .creatorName(creatorNames.get(random))
                .name(names.get(random))
                .build();
    }

    public static Auction generateRandomAuction(AuctionHouse auctionHouse, boolean useIndex, int index) {
        int random = useIndex ? index : getRandomInRange(0, MAX_AUCTION_HOUSES);
        return Auction.builder()
                .creatorId(auctionHouse.getId())
                .description(description)
                .initialPrice(initialPrices.get(random))
                .endTime(Instant.now().plusSeconds(60 * 60))
                .maxBidders(getRandomInRange(0, MAX_BIDDERS))
                .status(auctionStatus.get(useIndex ? index : getRandomInRange(0, 4)))
                .name(names.get(random))
                .build();
    }

    public static Auction generateRandomAuction() {
        int random = getRandomInRange(0, MAX_AUCTION_HOUSES);
        return Auction.builder()
                .description(description)
                .initialPrice(initialPrices.get(random))
                .endTime(Instant.now().plusSeconds(60 * 60))
                .maxBidders(getRandomInRange(0, MAX_BIDDERS))
                .name(names.get(random))
                .build();
    }

    public static AuctionBidder generateRandomBidder() {
        return AuctionBidder.builder()
                .price(getRandomDoubleInRange(0, 30000))
                .name(new StringJoiner("-")
                        .add("Anonymous")
                        .add(String.valueOf(getRandomInRange(0, MAX_BIDDERS)))
                        .toString()
                )
                .build();
    }

    public static List<AuctionHouse> generateListOfRandomAuctionHouse() {
        List<AuctionHouse> auctionHouses = new ArrayList<>();
        for (int idx = 0; idx < MAX_AUCTION_HOUSES; idx++) {
            auctionHouses.add(generateRandomAuctionHouse(true, idx));
        }
        return auctionHouses;
    }

    public static List<Auction> generateListOfRandomAuction(AuctionHouse auctionHouse) {
        List<Auction> auctions = new ArrayList<>();
        for (int idx = 0; idx < MAX_AUCTION_HOUSES; idx++) {
            auctions.add(generateRandomAuction(auctionHouse, true, idx));
        }
        return auctions;
    }

    public static List<Double> generateListOfBiddingPrices(double initialPrice) {
        return Arrays.asList(initialPrice + 1.02, initialPrice + 26d, initialPrice + 1574.98d, initialPrice + 198562.15d);
    }
}
