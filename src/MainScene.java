import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class MainScene extends VBox {
    TabPane auctionHouses = new TabPane();
    LinkedList<AuctionHouseGUI> tabs = new LinkedList<>();
    private int AHNumber = 1;

    public MainScene(AmountPane wallet, InteractionHandler interactionHandler){
        auctionHouses.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        auctionHouses.setTabMinWidth(60);
        auctionHouses.setStyle("-fx-base: white; -fx-font: 13 sansserif;" +
                "-fx-font-weight: bold");

        addToStage(auctionHouses);
        addToStage(interactionHandler);
        addToStage(wallet);
    }

    public void addAuctionHouse(AuctionHouseGUI auctionPane) {
        auctionHouses.getTabs().add(new Tab("AH"+AHNumber,auctionPane));
        tabs.add(auctionPane);
        AHNumber++;
    }

    // Navigates to next AH item
    public void navigateNext() {
        if (!tabs.isEmpty()) {
            tabs.get(getCurrentAH()).getNextItem();
        }
    }

    // Navigates to previous AH item
    public void navigatePrev() {
        if (!tabs.isEmpty()) {
            tabs.get(getCurrentAH()).getPrevItem();
        }
    }

    // gets current AH index
    public int getCurrentAH() {
        return auctionHouses.getSelectionModel().getSelectedIndex();
    }

    public double getCurrentBid() {
        return tabs.get(getCurrentAH()).getCurrentBid();
    }

    public String getCurrentItem() {
        return tabs.get(getCurrentAH()).getCurrentItem();
    }

    public void addToStage(Node node) {
        getChildren().add(node);
    }
}
