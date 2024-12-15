module com.example.employee {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.commons;
    requires de.jensd.fx.glyphs.materialdesignicons;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;



    exports com.example.employee;
    exports com.example.employee.Controllers;
    opens com.example.employee.Controllers to javafx.fxml;
    exports com.example.employee.Utils;
}