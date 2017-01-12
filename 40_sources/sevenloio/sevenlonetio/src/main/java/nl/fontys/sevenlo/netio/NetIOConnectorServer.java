/*
 *  Copyright Pieter van den Hombergh 2010/.
 *  Fontys Hogeschool voor Techniek en logistiek Venlo Netherlands.
 *  Software Engineering. Website: http://www.fontysvenlo.org
 *  This file may be used distributed under GPL License V2.
 */
package nl.fontys.sevenlo.netio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The server sets up a serverSocket and wait for the return of
 * <code>accept();</code>.
 * @author Pieter van den Hombergh (p.vandenhombergh at fontys dot nl )
 */
public class NetIOConnectorServer extends NetIOConnector {

    /**
     * Pass connection information up.
     * @param netAddress server address
     * @param port to use
     */
    public NetIOConnectorServer(String netAddress, int port) {
        super(netAddress, port);
    }

    /**
     * Create a server socket  and return the result of an accept.
     * @return a connected socket.
     */
    public Socket connect() {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(this.port);
            socket = serverSocket.accept();
        } catch (IOException ex) {
            Logger.getLogger(NetIOConnector.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return socket;
    }
}
