/*
 *  Copyright Pieter van den Hombergh 2010/.
 *  Fontys Hogeschool voor Techniek en logistiek Venlo Netherlands.
 *  Software Engineering. Website: http://www.fontysvenlo.org
 *  This file may be used distributed under GPL License V2.
 */
package nl.fontys.sevenlo.netio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.fontys.sevenlo.hwio.IO;

/**
 * The common part for server and client of a network IO implementation.
 * In a working application this could be used
 * to forward the io bits on one machine (e.g. the server) to a client
 * which could then control these bits.
 * @author hom Pieter van den Hombergh (p.vandenhombergh at fontys dot nl )
 */
public abstract class NetIOConnector implements IO {

    private int shadow;
    /** The address of the server. */
    protected final String netAddress;
    /** The default port to use. */
    public static final int DEFAULT_PORT = 12345;
    /** the actual port used. */
    protected int port;
    private final BlockingQueue<Integer> outQ;
    private final BlockingQueue<Integer> inQ;
    /** Backlog in the queue. */
    public static final int QUEUE_CAPACITY = 10;
    private Socket connection;
    private Integer lastValue;

    /**
     * Create a connector.
     * @param netAddress the address to use
     * @param port the port to use.
     */
    public NetIOConnector(String netAddress, int port) {
        this.netAddress = netAddress;
        this.port = port;
        this.outQ = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        this.inQ = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
    }

    /**
     * A network connector using "localhost" as address and the default port.
     */
    public NetIOConnector() {
        this("localhost", DEFAULT_PORT);
    }

    /*
     * See nl.fontys.sevenlo.hwio.Output.
     */
    @Override
    public void writeMasked(int mask, int value) {
        int ms = shadow & ~mask;
        int mv = value & mask;
        shadow = ms | mv;
        outQ.add(shadow);
    }

    /*
     * See nl.fontys.sevenlo.hwio.Output.
     */
    @Override
    public int lastWritten() {
        return shadow;
    }

    /*
     * See nl.fontys.sevenlo.hwio.Output.
     */
    @Override
    public int read() {
        try {
            this.lastValue = inQ.take();
        } catch (InterruptedException ex) {
            Logger.getLogger(NetIOConnector.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return this.lastValue;
    }

    /**
     * A server and client have a connected socket in common.
     *
     * @return a (connected) socket.
     */
    public abstract Socket connect();

    /**
     * Set up the connector.
     * Post: the this.connection is connected.
     */
    public void start() {
        OutputStream out = null;
        InputStream in = null;
        ObjectOutputStream objOut = null;
        ObjectInputStream objIn = null;
        try {
            this.connection = connect();
            objOut = new ObjectOutputStream(connection.getOutputStream());
            objIn = new ObjectInputStream(connection.getInputStream());
            Thread outThread = new Thread(new QueueToNet(objOut, outQ));
            outThread.start();
            Thread inThread = new Thread(new QueueFromNet(objIn, inQ));
            inThread.start();
        } catch (IOException ex) {
            Logger.getLogger(NetIOConnector.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * QueueToNet dequeues data from a queue and outputs it to the
     * ObjectOutputStream (the socket).
     * @author Pieter van den Hombergh
     *
     */
    static class QueueToNet implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final ObjectOutputStream out;

        /**
         * Connect an output stream to a socket by dequeuing.
         * @param out typically the socket
         * @param q the queue
         */
        QueueToNet(ObjectOutputStream out, BlockingQueue<Integer> q) {
            this.queue = q;
            this.out = out;
        }

        /**
         * Does the dequeuing and writing.
         */
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int v = queue.take();
                    out.writeInt(v);
                    out.flush();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(NetIOConnector.class.getName())
                        .log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NetIOConnector.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Reads from socket and puts in queue.
     * @author Pieter van den Hombergh
     */
    static class QueueFromNet implements Runnable {

        private final BlockingQueue<Integer> queue;
        private final ObjectInputStream in;

        /**
         * Connect socket to queue.
         * @param in source stream
         * @param q the queue to put to.
         */
        QueueFromNet(ObjectInputStream in, BlockingQueue<Integer> q) {
            this.queue = q;
            this.in = in;
        }

        /**
         * The reading and dequeuing takes place here.
         */
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    int v = in.readInt();
                    queue.offer(v);
                }
            } catch (IOException ex) {
                Logger.getLogger(NetIOConnector.class.getName())
                        .log(Level.SEVERE, null, ex);
                Runtime.getRuntime().exit(0);
            }
        }
    }
}
