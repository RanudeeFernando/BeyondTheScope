module com.example.cm2601_cw_javafx {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires java.desktop;


    opens com.example.cm2601_cw_javafx to javafx.fxml;
    exports com.example.cm2601_cw_javafx;
}