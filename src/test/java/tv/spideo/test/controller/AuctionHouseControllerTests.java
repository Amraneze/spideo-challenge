package tv.spideo.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tv.spideo.test.domain.Auction;
import tv.spideo.test.domain.AuctionBidder;
import tv.spideo.test.domain.AuctionHouse;
import tv.spideo.test.repository.AuctionHouseRepositoryImpl;
import tv.spideo.test.service.AuctionHouseService;
import tv.spideo.test.util.TestCommonUtils;
import tv.spideo.test.web.controller.AuctionHouseController;
import tv.spideo.test.web.exception.AuctionNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(AuctionHouseController.class)
@ContextConfiguration(classes = {AuctionHouseController.class, AuctionHouseService.class, AuctionHouseRepositoryImpl.class})
class AuctionHouseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuctionHouseService auctionHouseService;

    private static AuctionHouse mockedAuctionHouse;
    private static Auction mockedAuction;
    private static AuctionBidder mockedBidder;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        mockedAuctionHouse = TestCommonUtils.generateRandomAuctionHouse(false, 0);
        mockedAuction = TestCommonUtils.generateRandomAuction();
        mockedBidder = TestCommonUtils.generateRandomBidder();
    }

    @AfterEach
    void clearAll() {
        auctionHouseService.deleteAllAuctionHouse();
    }

    @Test
    @DisplayName("It should create an auction house")
    void itShouldCreateAnAuctionHouse() throws Exception {
        MvcResult result = mockMvc.perform(post("/auction/house/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(mockedAuctionHouse))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        AuctionHouse auctionHouse = objectMapper.readValue(result.getResponse().getContentAsString(), AuctionHouse.class);
        Assertions.assertEquals(mockedAuctionHouse.getName(), auctionHouse.getName());
        Assertions.assertEquals(mockedAuctionHouse.getCreatorName(), auctionHouse.getCreatorName());
        Assertions.assertNotNull(auctionHouse.getId());
    }

    @Test
    @DisplayName("It should return an empty list of auction houses")
    void itShouldGetEmptyListOfAuctionHouses() throws Exception {
        MvcResult result = mockMvc.perform(get("/auction/house/")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String auctionHouses = result.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(new ArrayList<>()), auctionHouses);
    }

    @Test
    @DisplayName("It should get all auction houses stored in db")
    void itShouldGetAllAuctionHouses() throws Exception {
        TestCommonUtils.generateListOfRandomAuctionHouse()
                .forEach((auctionHouse) -> auctionHouseService.createAuctionHouse(auctionHouse));
        List<AuctionHouse> savedAuctionHouses = auctionHouseService.getAllAuctionHouses();

        MvcResult result = mockMvc.perform(get("/auction/house/")
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String auctionHouses = result.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(savedAuctionHouses), auctionHouses);
    }

    @Test
    @DisplayName("it should get all auction houses by creator id")
    void itShouldGetAuctionHouseByCreatorId() throws Exception {
        TestCommonUtils.generateListOfRandomAuctionHouse()
                .forEach((auctionHouse) -> auctionHouseService.createAuctionHouse(auctionHouse));

        List<AuctionHouse> savedAuctionHouses = auctionHouseService.getAllAuctionHouses();
        String randomCreatorName = savedAuctionHouses.get(0).getCreatorName();
        List<AuctionHouse> filteredAuctionHouses = savedAuctionHouses
                .stream()
                .filter((auctionHouse) -> auctionHouse.getCreatorName().contentEquals(randomCreatorName))
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/auction/house/creator/{creatorName}", randomCreatorName)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String auctionHousesByCreatorName = result.getResponse().getContentAsString();
        Assertions.assertEquals(objectMapper.writeValueAsString(filteredAuctionHouses), auctionHousesByCreatorName);
    }

    @Test
    @DisplayName("it should delete an auction house")
    void itShouldDeleteAnAuctionHouse() throws Exception {
        TestCommonUtils.generateListOfRandomAuctionHouse()
                .forEach((auctionHouse) -> auctionHouseService.createAuctionHouse(auctionHouse));
        AuctionHouse savedAuctionHouse = auctionHouseService.getAllAuctionHouses().get(0);

        mockMvc.perform(delete("/auction/house/{auctionHouseId}", savedAuctionHouse.getId())
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        auctionHouseService.getAllAuctionHouses()
                .forEach((auctionHouse) -> Assertions.assertFalse(savedAuctionHouse.getId().contentEquals(auctionHouse.getId())));
    }

    @Test
    @DisplayName("it should create an auction of a specific auction house")
    void itShouldCreateAnAuction() throws Exception {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);

        MvcResult result = mockMvc.perform(post("/auction/house/{auctionHouseId}/create", auctionHouse.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(mockedAuction))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Auction auction = objectMapper.readValue(result.getResponse().getContentAsString(), Auction.class);

        Assertions.assertEquals(mockedAuction.getName(), auction.getName());
        Assertions.assertEquals(mockedAuction.getInitialPrice(), auction.getInitialPrice());
        Assertions.assertEquals(mockedAuction.getMaxBidders(), auction.getMaxBidders());
        Assertions.assertEquals(auction.getStatus(), Auction.AuctionStatus.NOT_STARTED);
        Assertions.assertNotNull(auction.getId());
    }

    @Test
    @DisplayName("It should get all auction of a specific auction house")
    void itShouldGetAllAuctionOfAuctionHouse() throws Exception {
        TestCommonUtils.generateListOfRandomAuctionHouse()
                .forEach((auctionHouse) -> auctionHouseService.createAuctionHouse(auctionHouse));
        String auctionHouseId = auctionHouseService.getAllAuctionHouses().get(0).getId();
        List<Auction> savedAuctions = auctionHouseService.getAuctionsByAuctionHouseId(auctionHouseId);

        MvcResult result = mockMvc.perform(get("/auction/house/{auctionHouseId}", auctionHouseId)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String auctions = result.getResponse().getContentAsString();
        // Maybe we can convert them to list just in case that the list is not ordered
        Assertions.assertEquals(objectMapper.writeValueAsString(savedAuctions), auctions);
    }

    @Test
    @DisplayName("it should delete an auction of a specific auction house")
    void itShouldDeleteAnAuction() throws Exception {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        Auction auction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        mockMvc.perform(delete("/auction/house/{auctionHouseId}/{auctionId}", auctionHouse.getId(), auction.getId())
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        auctionHouseService.getAuctionsByAuctionHouseId(auctionHouse.getId())
                .forEach((savedAuction) -> Assertions.assertFalse(savedAuction.getId().contentEquals(auctionHouse.getId())));
    }

    @Test
    @DisplayName("It should get all auctions by a specific status of an auction house")
    void itShouldGetAuctionHousesByStatus() throws Exception {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        List<Auction> runningAuctions = TestCommonUtils.generateListOfRandomAuction(auctionHouse)
                .stream()
                .map((auction) -> auctionHouseService.createAuction(auctionHouse.getId(), auction))
                .filter((auction) -> auction.getStatus() == Auction.AuctionStatus.RUNNING)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/auction/house/{auctionHouseId}/{status}",
                    auctionHouse.getId(), Auction.AuctionStatus.RUNNING)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String auctions = result.getResponse().getContentAsString();
        // Maybe we can convert them to list just in case that the list is not ordered
        Assertions.assertEquals(objectMapper.writeValueAsString(runningAuctions), auctions);
    }

    @Test
    @DisplayName("It should update an auction's status of an auction house")
    void itShouldUpdateAnAuctionStatus() throws Exception {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        Auction savedAuction = TestCommonUtils.generateListOfRandomAuction(auctionHouse)
                .stream()
                .map((_auction) -> auctionHouseService.createAuction(auctionHouse.getId(), _auction))
                .findFirst()
                .orElseThrow(AuctionNotFoundException::new);

        MvcResult result = mockMvc.perform(put("/auction/house/{auctionHouseId}/{auctionId}/status/{status}",
                auctionHouse.getId(), savedAuction.getId(), Auction.AuctionStatus.RUNNING)
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Auction auction = objectMapper.readValue(result.getResponse().getContentAsString(), Auction.class);

        Assertions.assertEquals(savedAuction.getId(), auction.getId());
        Assertions.assertEquals(savedAuction.getName(), auction.getName());
        Assertions.assertEquals(savedAuction.getInitialPrice(), auction.getInitialPrice());
        Assertions.assertEquals(savedAuction.getMaxBidders(), auction.getMaxBidders());
        Assertions.assertEquals(Auction.AuctionStatus.RUNNING, auction.getStatus());
    }

    @Test
    @DisplayName("It should bid on an auction of an auction house")
    void itShouldBidOnAnAuction() throws Exception {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction savedAuction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        mockedBidder.setPrice(savedAuction.getInitialPrice() + 100d);

        MvcResult result = mockMvc.perform(post("/auction/house/{auctionHouseId}/{auctionId}/bid",
                    auctionHouse.getId(), savedAuction.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(mockedBidder))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AuctionBidder bidder = objectMapper.readValue(result.getResponse().getContentAsString(), AuctionBidder.class);
        Assertions.assertEquals(mockedBidder.getName(), bidder.getName());
        Assertions.assertEquals(mockedBidder.getPrice(), bidder.getPrice());
        Assertions.assertNotNull(bidder.getId());
    }

    @Test
    @DisplayName("It should get the actual winner of a specific auction")
    void itShouldGetTheWinnerOfAnAuction() throws Exception {
        AuctionHouse auctionHouse = auctionHouseService.createAuctionHouse(mockedAuctionHouse);
        mockedAuction = TestCommonUtils.generateRandomAuction(mockedAuctionHouse, false, 0);
        mockedAuction.setStatus(Auction.AuctionStatus.RUNNING);
        Auction savedAuction = auctionHouseService.createAuction(auctionHouse.getId(), mockedAuction);

        List<Double> biddingPrices = TestCommonUtils.generateListOfBiddingPrices(savedAuction.getInitialPrice());

        mockedBidder.setPrice(biddingPrices.get(0));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), savedAuction.getId(), mockedBidder);

        AuctionBidder newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(savedAuction.getCurrentPrice() + 5d);

        mockedBidder.setPrice(biddingPrices.get(1));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), savedAuction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(savedAuction.getCurrentPrice() + 30d);

        mockedBidder.setPrice(biddingPrices.get(2));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), savedAuction.getId(), mockedBidder);

        newBidder = TestCommonUtils.generateRandomBidder();
        newBidder.setPrice(savedAuction.getCurrentPrice() + 1600d);

        mockedBidder.setPrice(biddingPrices.get(3));
        auctionHouseService.bidOnAuction(auctionHouse.getId(), savedAuction.getId(), mockedBidder);

        auctionHouseService.updateAuctionStatus(auctionHouse.getId(), savedAuction.getId(), Auction.AuctionStatus.TERMINATED);

        MvcResult result = mockMvc.perform(get("/auction/house/{auctionHouseId}/{auctionId}/winner",
                auctionHouse.getId(), savedAuction.getId())
                .contentType("application/json")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AuctionBidder winner = objectMapper.readValue(result.getResponse().getContentAsString(), AuctionBidder.class);
        Assertions.assertEquals(biddingPrices.get(3), winner.getPrice());
        Assertions.assertEquals(mockedBidder.getName(), winner.getName());
    }

}
