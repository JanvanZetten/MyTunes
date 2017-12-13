/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes;

import com.sun.javafx.application.LauncherImpl;

/**
 *
 * @author janvanzetten
 */
public class Main {
   public static void main(String[] args) {
      LauncherImpl.launchApplication(MyTunes.class, MyTunesPreloader.class, args);
   }
}

