module com.group14 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.web;
    requires com.google.gson;
    opens com.group14 to javafx.fxml, com.google.gson;
    requires com.github.oshi;
    requires org.slf4j;
    exports com.group14;
}
