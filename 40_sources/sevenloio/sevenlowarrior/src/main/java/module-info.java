/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module nl.fontys.sevenlo.sevenlowarrior {
    requires javafx.baseEmpty;
    requires javafx.base;
    requires javafx.controlsEmpty;
    requires javafx.controls;
    requires javafx.graphicsEmpty;
    requires javafx.graphics;
    requires iowarrior;
    requires nl.fontys.sebivenlo.utils;
    requires nl.fontys.sebivenlo.hwio;
    exports nl.fontys.sevenlo.iowarrior;
}
