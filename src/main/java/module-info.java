module com.kensoftph.jfxpasswordgenerator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kensoftph.jfxpasswordgenerator to javafx.fxml;
    exports com.kensoftph.jfxpasswordgenerator;
}