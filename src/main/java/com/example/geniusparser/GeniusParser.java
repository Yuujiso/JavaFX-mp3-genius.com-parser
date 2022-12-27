package com.example.geniusparser;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.List;

import static com.example.geniusparser.SceneController.switchToScene2;


public class GeniusParser {

    public Button processButton;
    public Button clearButton;
    public Button copyButton;
    public Button bracketsOffButton;
    public TextField url;
    public TextArea text;

    public void onProcessButtonClick() {

        if (!url.getText().isEmpty()) {
            GeniusLyricsParserFromUrl(url.getText());
        } else {
            text.setText("Input URL of Genius lyrics.");
        }
    }

    public void onClearButtonClick() {
        url.setText("");
        text.setText("");
    }

    public void onCopyButtonClick() {
        String temp = text.getText();
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(temp), null);
    }

    public void onBracketsOffButtonClick() {
        String temp = text.getText().replaceAll("\\[.*?\\]", "");
        temp = temp.trim();
        text.setText(temp);
    }

    public void onMp3PageClick(ActionEvent e) throws IOException {
        switchToScene2(e);
    }

    public void GeniusLyricsParserFromUrl(String url) {

        try (final WebClient client = new WebClient()) {

            client.getOptions().setCssEnabled(false);
            client.getOptions().setJavaScriptEnabled(false);

            HtmlPage page = client.getPage(url);
            List<?> elements = page.getByXPath("//div[@data-lyrics-container='true']");
            StringBuilder res = new StringBuilder();
            for (Object element : elements) {
                final HtmlElement text = (HtmlElement) element;
                res.append(text.asNormalizedText());
                res.append("\n");
            }

            text.setText(res.toString());
        } catch (Exception e) {
            System.out.println("Your URL isn't correct or I can't process it.");
            text.setText("Your URL isn't correct or I can't process it.");
        }
    }
}
