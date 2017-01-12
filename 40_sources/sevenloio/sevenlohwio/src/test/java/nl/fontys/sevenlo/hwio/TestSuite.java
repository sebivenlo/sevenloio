/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.fontys.sevenlo.hwio;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Pieter van den Hombergh (p.vandenhombergh@fontys.nl)
 */
@RunWith(Suite.class)
@Suite.SuiteClasses( { IOTest.class, InputBitTest.class,
    SimplePollerTest.class,
    SwingPollerTest.class})
public class TestSuite {

}