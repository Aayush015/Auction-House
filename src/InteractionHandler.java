import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InteractionHandler extends VBox {
    Label agentBid = new Label();

    public InteractionHandler(Slider bidSlider, Button placeBid, Button back, Button next){
        // Navigation between items, back/next buttons
        HBox navigate = new HBox();
        navigate.setStyle("-fx-padding: 0 20 15 20; -fx-background-color: white");
        navigate.setSpacing(20);
        navigate.getChildren().add(back);
        navigate.getChildren().add(next);

        // Holds bid selection, slider, and submit button
        HBox bid = new HBox();
        bid.setStyle("-fx-padding: 10 20 10 20");
        bid.setSpacing(20);
        bid.getChildren().add(agentBid);
        bid.getChildren().add(placeBid);

        // Bid slider display
        agentBid.setAlignment(Pos.CENTER);
        agentBid.setText("$0.00");
        agentBid.setStyle("-fx-background-color: #d1d1d1;" +
                "-fx-font: 16 sansserif; -fx-font-weight: bold");
        agentBid.setPrefSize(190,28);

        bidSlider.setStyle("-fx-padding: 0 20 15 20");

        Separator separator = new Separator();
        Separator separator1 = new Separator();

        addToStage(navigate);
        addToStage(separator);
        addToStage(bid);
        addToStage(bidSlider);
        addToStage(separator1);
    }

    public void updateBidDisplay(double bid) {
        agentBid.setText("$"+String.format("%.2f",bid));
    }

    public void addToStage(Node node) {
        getChildren().add(node);
    }

}
