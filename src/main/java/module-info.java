module com.example.cm2601_cw_javafx {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires java.desktop;
    requires java.net.http;
    requires org.jetbrains.annotations;
    requires librec.core;


    exports com.example.cm2601_cw_javafx.app;
    opens com.example.cm2601_cw_javafx.app to javafx.fxml;
    exports com.example.cm2601_cw_javafx.model;
    opens com.example.cm2601_cw_javafx.model to javafx.fxml;
    exports com.example.cm2601_cw_javafx.service;
    opens com.example.cm2601_cw_javafx.service to javafx.fxml;
    exports com.example.cm2601_cw_javafx.db;
    opens com.example.cm2601_cw_javafx.db to javafx.fxml;

}