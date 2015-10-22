package de.herrlock.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * 
 * http://stackoverflow.com/a/12718117/3680684
 */
@SuppressWarnings( "javadoc" )
public final class Dialogs {

    public static enum Response {
        NO, YES, CANCEL
    }

    private static ImageView icon = new ImageView();

    static final class Dialog extends Stage {

        private Response selectedButton = Response.CANCEL;

        public Dialog( String title, Stage owner, Scene scene, String iconFile ) {
            setTitle( title );
            initStyle( StageStyle.UTILITY );
            initModality( Modality.APPLICATION_MODAL );
            initOwner( owner );
            setResizable( false );
            setScene( scene );
            icon.setImage( new Image( getClass().getResourceAsStream( iconFile ) ) );
        }

        public void showDialog() {
            sizeToScene();
            centerOnScreen();
            showAndWait();
        }

        public Response getSelectedButton() {
            return this.selectedButton;
        }

        public void setSelectedButton( Response selectedButton ) {
            this.selectedButton = selectedButton;
        }
    }

    static final class Message extends Text {
        public Message( String msg ) {
            super( msg );
            setWrappingWidth( 250 );
        }
    }

    public static void showDialog( Stage owner, Node message, String title, String iconFile ) {
        VBox vb = new VBox();
        Scene scene = new Scene( vb );
        final Dialog dial = new Dialog( title, owner, scene, iconFile );
        vb.setPadding( new Insets( 10 ) );
        vb.setSpacing( 10 );
        Button okButton = new Button( "OK" );
        okButton.setAlignment( Pos.CENTER );
        okButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent e ) {
                dial.close();
            }
        } );
        BorderPane bp = new BorderPane();
        bp.setCenter( okButton );
        HBox msg = new HBox();
        msg.setSpacing( 5 );
        msg.getChildren().addAll( icon, message );
        vb.getChildren().addAll( msg, bp );
        dial.showDialog();
    }

    public static Response showConfirmDialog( Stage owner, Node message, String title ) {
        VBox vb = new VBox();
        Scene scene = new Scene( vb );
        final Dialog dial = new Dialog( title, owner, scene, "dialog-confirm.png" );
        vb.setPadding( new Insets( 10 ) );
        vb.setSpacing( 10 );
        Button yesButton = new Button( "Yes" );
        yesButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent e ) {
                dial.close();
                dial.setSelectedButton( Response.YES );
            }
        } );
        Button noButton = new Button( "No" );
        noButton.setOnAction( new EventHandler<ActionEvent>() {
            @Override
            public void handle( ActionEvent e ) {
                dial.close();
                dial.setSelectedButton( Response.NO );
            }
        } );
        BorderPane bp = new BorderPane();
        HBox buttons = new HBox();
        buttons.setAlignment( Pos.CENTER );
        buttons.setSpacing( 10 );
        buttons.getChildren().addAll( yesButton, noButton );
        bp.setCenter( buttons );
        HBox msg = new HBox();
        msg.setSpacing( 5 );
        msg.getChildren().addAll( icon, message );
        vb.getChildren().addAll( msg, bp );
        dial.showDialog();
        return dial.getSelectedButton();
    }

    public static Response showConfirmDialog( Stage owner, String message, String title ) {
        return showConfirmDialog( owner, new Message( message ), title );
    }

    public static void showMessageDialog( Stage owner, String message, String title ) {
        showMessageDialog( owner, new Message( message ), title );
    }

    public static void showMessageDialog( Stage owner, Node message, String title ) {
        showDialog( owner, message, title, "dialog-information.png" );
    }

    public static void showWarningDialog( Stage owner, String message, String title ) {
        showWarningDialog( owner, new Message( message ), title );
    }

    public static void showWarningDialog( Stage owner, Node message, String title ) {
        showDialog( owner, message, title, "dialog-warning.png" );
    }

    public static void showErrorDialog( Stage owner, String message, String title ) {
        showErrorDialog( owner, new Message( message ), title );
    }

    public static void showErrorDialog( Stage owner, Node message, String title ) {
        showDialog( owner, message, title, "dialog-error.png" );
    }

    private Dialogs() {
        // not used
    }

}