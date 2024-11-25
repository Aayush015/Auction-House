import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AmountPane extends VBox {
    private AmountBox totalAmount;
    private AmountBox availableAmount;
    private double initialAmount;
    private Label bankIDLabel;

    public AmountPane() {
        setupAmountPane();
    }

    private void setupAmountPane() {
        setSpacing(10);
        setStyle("-fx-padding: 20 20 20 20; -fx-background-color: #e8e8e8;");
        setPrefHeight(170);
        HBox hBox = new HBox();
        Label walletLabel = new Label("Wallet");
        this.bankIDLabel = new Label();
        hBox.getChildren().add(walletLabel);
        hBox.getChildren().add(bankIDLabel);
        walletLabel.setStyle("-fx-font: 20 sansserif; -fx-font-weight: bold;");
        bankIDLabel.setStyle("-fx-font: 12 sansserif; -fx-font-weight: bold;");
        bankIDLabel.setPrefSize(240,30);
        bankIDLabel.setAlignment(Pos.BASELINE_RIGHT);

        availableAmount = new AmountBox("Available Balance",initialAmount);
        totalAmount = new AmountBox("Total Balance", initialAmount);

        getChildren().addAll(hBox,availableAmount,totalAmount);
    }

    public void updateAmount(double total, double available) {
        availableAmount.updateAmount(available);
        totalAmount.updateAmount(total);
    }

    // sets the bankID label with agent bank account ID number
    public void setBankIDLabel(int bankID) {
        bankIDLabel.setText("bankID: "+bankID);
    }
}
