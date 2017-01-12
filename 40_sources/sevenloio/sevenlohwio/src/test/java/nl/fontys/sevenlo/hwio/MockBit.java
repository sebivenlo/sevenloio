/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.fontys.sevenlo.hwio;

/**
 *
 * @author lhom
 */
public class MockBit extends Bit {

    private boolean value = false;
    private static int serialNr = 0;
    final int id = serialNr++;

    @Override
    public void set(boolean b) {
        value = b;
        updateListeners(this, value);
    }

    @Override
    public boolean isSet() {
        return value;
    }
}
