package elevatorparts;

/**
 *
 * @author Pieter van den Hombergh {@code <p.vandenhombergh@fontys.nl>}
 */
public enum  Direction {
    STOP(0),
    DOWN(0b01),
    UP(0b10);

    private Direction( int bitPattern ) {
        this.bitPattern = bitPattern;
    }
    
    final int bitPattern;

    public int getBitPattern() {
        return bitPattern;
    }
    
}
