/*
 *  Copyright Pieter van den Hombergh 2010/.
 *  Fontys Hogeschool voor Techniek en logistiek Venlo Netherlands.
 *  Software Engineering. Website: http://www.fontysvenlo.org
 *  This file may be used distributed under GPL License V2.
 */

package nl.fontys.sevenlo.netio;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sets up a connection as a client.
 * This subclass does a simple <pre>new Socket( host,port) </pre>.
 * @author Pieter van den Hombergh (p.vandenhombergh at fontys dot nl )
 */
public class NetIOConnectorClient extends NetIOConnector {

    /**
     * Pass connection info to super.
     * @param netAdress server address
     * @param port to use
     */
    public NetIOConnectorClient(String netAdress, int port) {
        super(netAdress,port);
    }

    /**
     * Simply create a socket with the passed information provided.
     * @return connected socket.
     */
    @Override
    public Socket connect() {
        Socket socket=null;
        try {
            socket = new Socket(this.netAddress, this.port);
        } catch (UnknownHostException ex) {
            Logger.getLogger(NetIOConnectorClient.class.getName())
                    .log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NetIOConnectorClient.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return socket;
    }
}
