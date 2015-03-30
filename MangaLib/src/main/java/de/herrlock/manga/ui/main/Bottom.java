package de.herrlock.manga.ui.main;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import de.herrlock.javafx.scene.NodeContainer;

class Bottom extends NodeContainer {
    @Override
    protected Node createNode() {
        String btnPre = "bottom.buttons.";
        Button btnDownload = new Button( MDGuiController.i18n.getString( btnPre + "download" ) );
        Button btnJDExport = new Button( MDGuiController.i18n.getString( btnPre + "jdexport" ) );
        Button btnHTML = new Button( MDGuiController.i18n.getString( btnPre + "html" ) );
        Button btnExit = new Button( MDGuiController.i18n.getString( btnPre + "exit" ) );

        btnDownload.setOnAction( MDGuiController.DO_NOTHING_HANDLER );
        btnDownload.setDefaultButton( true );
        btnJDExport.setOnAction( MDGuiController.DO_NOTHING_HANDLER );
        btnHTML.setOnAction( MDGuiController.DO_NOTHING_HANDLER );
        btnExit.setOnAction( MDGuiController.DO_NOTHING_HANDLER );
        btnExit.setCancelButton( true );

        HBox hbox = new HBox( 8 );
        hbox.getStyleClass().add( CCN.PADDING_8 );
        hbox.getChildren().addAll( btnDownload, btnJDExport, btnHTML, btnExit );

        AnchorPane pane = new AnchorPane();
        pane.getChildren().addAll( hbox );
        AnchorPane.setBottomAnchor( hbox, 0.0 );
        AnchorPane.setRightAnchor( hbox, 0.0 );
        pane.getStyleClass().add( CCN.GREEN );
        return pane;
    }
}
