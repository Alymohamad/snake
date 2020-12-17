package game;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

public class Score {
    private int scoreValue = 0;

    private String scoreOutputText = "Score: ";

    Label score = new Label(scoreOutputText + scoreValue);

    public Score(Group group) {
        score.setFont(new Font("Arial", 50));
        score.setText(scoreOutputText + scoreValue);
        group.getChildren().add(score);

    }

    public void scoreRespawn(Group group) {
        scoreValue = 0;
        score.setFont(new Font("Arial", 50));
        score.setText(scoreOutputText + scoreValue);
        group.getChildren().add(score);

    }

    public void upScoreValue() {
        scoreValue++;
        score.setText(scoreOutputText + scoreValue);
    }

    public int getScore() {
        return scoreValue;
    }


}
