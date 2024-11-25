import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ItemGUI extends VBox{
    private String auctionItem;
    private String auctionItemName;
    private String auctionItemImage;
    private double currentBid;
    Label statusLabel;
    private VBox bidStatus;
    private AmountBox amountBox;

    public ItemGUI(String auctionItem){
        setSpacing(15);
        this.auctionItem = auctionItem;
        auctionItemImage = auctionItem + ".jpg";
        auctionItemName = auctionItem.replace("-"," ");
        setup();
    }

    private void setup() {
        HBox hBox = new HBox();
        Label itemTitle = new Label();
        itemTitle.setText(auctionItemName);
        itemTitle.setStyle("-fx-font: 18 sansserif; -fx-font-weight: bold");

        Label timerLabel = new Label();
        timerLabel.setStyle("-fx-font: 18 futura; -fx-font-weight: bold");
        timerLabel.setText("30:00");

        hBox.getChildren().add(itemTitle);
        hBox.getChildren().add(timerLabel);

        Image image = new Image(this.auctionItemImage);
        ImageView itemImage = new ImageView();
        itemImage.setImage(image);
        itemImage.setFitHeight(300);
        itemImage.setFitWidth(300);

        amountBox = new AmountBox("Current Bid", 0.00);
        amountBox.setColor("#ebbfb5");

        // Bid status update
        bidStatus = new VBox();
        bidStatus.setStyle("-fx-border-color: #bababa");
        Label statusTitle = new Label(" Bid Status");
        statusTitle.setPrefWidth(300);
        statusTitle.setStyle("-fx-font: 14 sansserif; -fx-font-weight: bold;" +
                "-fx-background-color: #d6d6d6");
        statusLabel = new Label();
        statusLabel.setStyle("-fx-font: 18 sansserif; -fx-font-weight: bold;" +
                "-fx-background-color: #ffffff");
        statusLabel.setPrefSize(300, 30);
        statusLabel.setAlignment(Pos.CENTER);

        bidStatus.getChildren().add(statusTitle);
        bidStatus.getChildren().add(statusLabel);

        addToStage(itemTitle);
        addToStage(itemImage);
        addToStage(amountBox);
        addToStage(bidStatus);
    }

    public String getItem() { return auctionItem; }

    public void setCurrentBid(double bid, boolean otherBid) {
        currentBid = bid;
        amountBox.updateAmount(currentBid);
        if (otherBid) amountBox.setColor("#c6d9bd");
        else amountBox.setColor("#ebbfb5");
    }

    public void updateStatus(String status) {
        statusLabel.setText(status);
    }

    public double getCurrentBid() {
        return currentBid;
    }

    private void addToStage(Node node) {
        getChildren().add(node);
    }


}
