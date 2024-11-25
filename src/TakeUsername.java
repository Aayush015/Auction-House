import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

public class TakeUsername extends VBox {
    Agent agent;

    public TakeUsername(Agent agent){
        drawDialog();
        this.agent = agent;
    }

    private void drawDialog() {
        setSpacing(20);
        setStyle("-fx-padding: 20 40 20 40");
        Label prompt = new Label("Create an account");
        prompt.setStyle("-fx-font: 18 futura; -fx-font-weight: bold");
        addChildren(prompt);

        TextField userName = new TextField();
        userName.setPromptText("\uD83D\uDC64 Enter your full name");
        styleText(userName);
        addChildren(userName);

        TextField userBalance = new TextField();
        userBalance.setPromptText("\uD83D\uDCB2 Amount");
        TextFormatter<String> formatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            } else {
                return null;
            }
        });

        userBalance.setTextFormatter(formatter);
        styleText(userBalance);
        addChildren(userBalance);

        Button submitButton = new Button("Submit");
        submitButton.setPrefSize(400, 20);
        submitButton.setFocusTraversable(false);
        submitButton.setStyle("-fx-base: #46b920; -fx-text-fill: white");
        submitButton.setDisable(userBalance.getText().equals("") && userName.getText().matches("^[0-9]+$"));
        submitButton.setOnAction(event -> {
            if (!userBalance.getText().equals("") && !userName.getText().matches("^[0-9]+$")){
                agent.setTotalBalance(Double.parseDouble(userBalance.getText()));
                agent.setAvailableBalance(Double.parseDouble(userBalance.getText()));
                AgentGUI.changeScenes();
            }

            //TODO: Add event handler for the submit button, which will basically send the stored values to the agent
        });
        addChildren(submitButton);
    }

    private void styleText(TextField field) {
        field.setStyle("-fx-base: #35c1c4; -fx-text-fill: black");
        field.setPrefHeight(20);
        field.setFocusTraversable(false);
    }

    private void addChildren(Node node) {
        getChildren().add(node);
    }

}
