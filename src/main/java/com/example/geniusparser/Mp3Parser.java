package com.example.geniusparser;

import com.mpatric.mp3agic.*;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

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
    String parsedTitle, parsedArtist, parsedGenre, parsedLyrics;

    public void Mp3FileParserFromPath(File file) throws InvalidDataException, UnsupportedTagException, IOException {
        Mp3File mp3file = new Mp3File(file);
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            parsedTitle = id3v2Tag.getTitle();
            title.setText(parsedTitle);

            parsedArtist = id3v2Tag.getArtist();
            artist.setText(parsedArtist);

            parsedGenre = id3v2Tag.getGenreDescription();
            genre.setText(parsedGenre);

            parsedLyrics = id3v2Tag.getLyrics();
            lyrics.setText(parsedLyrics);
        }
    }

    public void onSaveButtonClick() throws InvalidDataException, UnsupportedTagException, IOException, NotSupportedException {
        if (path.getText().isEmpty()) {
            path.setText("Choose .mp3 file.");
            return;
        }


        String filePath = path.getText();
        Path realPath = Paths.get(filePath);
        if (Files.exists(realPath)) {
            File file = new File(realPath.toUri());
            Mp3FileMetadataSave(file);
        } else {
            path.setText("Choose .mp3 file.");
        }

    }

    public void Mp3FileMetadataSave(File file) throws InvalidDataException, UnsupportedTagException, IOException, NotSupportedException {
        Mp3File mp3file = new Mp3File(file);
        ID3v2 id3v2Tag;
        int savesCounter = 0;

        if (mp3file.hasId3v2Tag()) {
            id3v2Tag = mp3file.getId3v2Tag();
        } else {
            id3v2Tag = new ID3v24Tag();
            mp3file.setId3v2Tag(id3v2Tag);
        }

        if (!Objects.equals(parsedTitle, title.getText())) {
            id3v2Tag.setTitle(title.getText());
            savesCounter++;
        }

        if (!Objects.equals(parsedArtist, artist.getText())) {
            id3v2Tag.setArtist(artist.getText());
            savesCounter++;
        }

        if (!Objects.equals(parsedGenre, genre.getText())) {
            id3v2Tag.setGenreDescription(genre.getText());
            savesCounter++;
        }

        if (!Objects.equals(parsedLyrics, lyrics.getText())) {
            id3v2Tag.setLyrics(lyrics.getText());
            savesCounter++;
        }

        if (savesCounter > 0) {
            mp3file.save(file.getPath() + "_new.mp3");
        }
    }


    public void onClearButtonClick() {
        path.setText("");
        title.setText("");
        artist.setText("");
        genre.setText("");
        lyrics.setText("");
        parsedArtist = "";
        parsedTitle = "";
        parsedGenre = "";
        parsedLyrics = "";
    }

    public void onPrevPageClick(ActionEvent e) throws IOException {
        switchToScene1(e);
    }

    public void onBrowseClick() {
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "MP3 Files", "mp3");
        fileChooser.setFileFilter(filter);
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
