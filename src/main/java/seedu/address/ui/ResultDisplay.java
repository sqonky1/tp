package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";
    private static final double LINE_HEIGHT = 20.0;
    private static final double MIN_HEIGHT = 100.0;
    private static final double MAX_HEIGHT = 200.0;
    private static final double PADDING = 20.0;

    @FXML
    private TextArea resultDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
        resultDisplay.setScrollTop(0);
        int lines = feedbackToUser.split("\n", -1).length;
        double newHeight = Math.min(MAX_HEIGHT, Math.max(MIN_HEIGHT, lines * LINE_HEIGHT + PADDING));
        resultDisplay.setPrefHeight(newHeight);
    }

}
