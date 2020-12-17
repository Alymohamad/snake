package game;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Score {
    private int scoreValue = 0;

    private String scoreOutputText = "Score: ";

    Label scoreLabel = new Label(scoreOutputText + scoreValue);

    public Score(Group group) {
        scoreLabel.setFont(new Font("Arial", 50));
        scoreLabel.setText(scoreOutputText + scoreValue);
        group.getChildren().add(scoreLabel);

    }

    public void scoreRespawn(Group group) {
        scoreValue = 0;
        scoreLabel.setFont(new Font("Arial", 50));
        scoreLabel.setText(scoreOutputText + scoreValue);
        group.getChildren().add(scoreLabel);

    }

    public void upScoreValue() {
        scoreValue++;
        scoreLabel.setText(scoreOutputText + scoreValue);
    }

    public int getScoreLabel() {
        return scoreValue;
    }


}
