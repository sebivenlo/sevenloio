package elevatorparts;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public enum  DoorDirection {
    STOP(0),
    OPEN(0b01),
    CLOSE(0b10);

    private DoorDirection( int bitPattern ) {
        this.bitPattern = bitPattern;
    }
    
    final int bitPattern;

    public int getBitPattern() {
        return bitPattern;
    }
}
