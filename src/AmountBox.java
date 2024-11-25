import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AmountBox extends HBox {
    private final String title;
    private Double amount;
    private Label amountLabel;

    public AmountBox(String title, Double amount) {
        this.title = title;
        this.amount = amount;
        setupBox();
    }

    private void setupBox() {
        setStyle("-fx-border-color: #bababa;");
        Label titleLabel = new Label("  "+title);
        titleLabel.setStyle("-fx-font: 16 sansserif; -fx-font-weight: bold;" +
                "-fx-background-color: #f5f5f5");
        titleLabel.setPrefSize(170,30);

        amountLabel = new Label("  $"+String.format("%.2f",amount)+"  ");
        amountLabel.setAlignment(Pos.CENTER_RIGHT);
        amountLabel.setStyle("-fx-font: 16 sansserif;-fx-font-weight: bold;" +
                "-fx-background-color: #d1d1d1");
        amountLabel.setPrefSize(130,30);

        getChildren().add(titleLabel);
        getChildren().add(amountLabel);
    }

    public void setColor(String color) {
        amountLabel.setStyle("-fx-font: 16 sansserif;-fx-font-weight: bold;" +
                "-fx-background-color: "+color);
    }

    public void updateAmount(double amount) {
        this.amount = amount;
        amountLabel.setText("  $"+String.format("%.2f",amount)+"  ");
    }
}
