/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.fontys.sevenlo.hwio;

/**
 * This Mock reader can be preset with the value to be read.
 * @author lhom
 */
public class MockIntReader implements Input {
    /**
     * The value is intentionally writable from outside.
     */
    int value = 0;
    public int read() {
        return value;
    }

}
