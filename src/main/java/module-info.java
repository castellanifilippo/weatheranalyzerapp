module it.unipi.weatheranalyzerapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.logging.log4j;

    opens it.unipi.weatheranalyzerapp to javafx.fxml;
    exports it.unipi.weatheranalyzerapp;
    exports it.unipi.weatheranalyzerapp.utility;
}
