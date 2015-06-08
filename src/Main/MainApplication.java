package Main;

import java.util.Scanner;

import javax.swing.JFrame;

import Controller.Controller;
import Model.Model;



/**
 * 
 */
public class MainApplication extends JFrame{

    /**
     * 
     */
    public MainApplication()  {
    }

    /**
     * @param args 
     * @return
     */
    public static void main(String[] args) {
        Model smartLoopModel = new Model();
    	Controller smartLoopController = new Controller(smartLoopModel);
    	smartLoopModel.addObserver(smartLoopController);
        smartLoopController.startGui();
    }

}