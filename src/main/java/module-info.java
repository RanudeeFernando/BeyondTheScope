module com.example.cm2601_cw_javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cm2601_cw_javafx to javafx.fxml;
    exports com.example.cm2601_cw_javafx;
}