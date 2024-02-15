module com.example.demofinalproject {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.demofinalproject to javafx.fxml;
    exports com.example.demofinalproject;
}