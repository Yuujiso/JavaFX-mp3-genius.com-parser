module com.example.geniusparser {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires htmlunit;
    requires mp3agic;
    requires java.datatransfer;
    requires java.desktop;


    opens com.example.geniusparser to javafx.fxml;
    exports com.example.geniusparser;
}