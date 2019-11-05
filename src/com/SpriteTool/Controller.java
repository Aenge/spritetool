package com.SpriteTool;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXDrawersStack;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXHamburger;
import javafx.scene.input.MouseEvent;

public class Controller implements Initializable {

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //--------- HAMBURGER
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            drawer.toggle();
        });
        //--------- DRAWER
    }

}
