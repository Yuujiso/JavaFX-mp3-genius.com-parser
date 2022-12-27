package com.example.geniusparser;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

import static com.example.geniusparser.SceneController.switchToScene1;

public class Mp3Parser {

    public TextArea lyrics;
    File file;
    public Button processButton;
    public Button clearButton;
    public Button prevPage;
    public TextField title;
    public TextField artist;
    public TextField genre;
    public TextField path;

    public void Mp3FileParserFromPath(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3file = new Mp3File(file);
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            title.setText(id3v2Tag.getTitle());
            artist.setText(id3v2Tag.getArtist());
            genre.setText(id3v2Tag.getGenreDescription());
            lyrics.setText(id3v2Tag.getLyrics());
        }
    }

    public void onSaveButtonClick() {
    }

    public void onClearButtonClick() {
        title.setText("");
        artist.setText("");
        genre.setText("");
        lyrics.setText("");
    }

    public void onPrevPageClick(ActionEvent e) throws IOException {
        switchToScene1(e);
    }

    public void onBrowseClick() {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            path.setText(String.valueOf(file));
            try {
                Mp3FileParserFromPath(file);
            } catch (InvalidDataException | UnsupportedTagException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
