/*
 * Copyright Pieter van den Hombergh 2010/. Fontys Hogeschool voor Techniek en
 * logistiek Venlo Netherlands. Software Engineering. Website:
 * http://www.fontysvenlo.org This file may be used distributed under GPL
 * License
 * V2.
 */
package nl.fontys.sevenlo.netio;

import java.util.Properties;
import nl.fontys.sevenlo.hwio.BitAggregate;
import nl.fontys.sevenlo.hwio.SimpleBitAggregate;
import nl.fontys.sevenlo.hwio.SimplePoller;
import nl.fontys.sevenlo.hwio.PollThreads;
import nl.fontys.sevenlo.utils.ResourceUtils;
import nl.fontys.sevenlo.widgets.IOGUIPanel;

/**
 * Main application to show how an IO could b forwarded over a network. This
 * implementation is not very sophisticated. It supports only one server with
 * one client. Error (exception) handling is minimal.
 *
 * @author Pieter van den Hombergh (p.vandenhombergh at fontys dot nl )
 */
public final class IO {

    /**
     * The program startup.
     *
     * @param args tested to start as server (arg[0].equals("-s")).
     */
    public static void main(String[] args) {
        Properties prop = new Properties();
        NetIOConnector connector;
        BitAggregate ba = null;
        String label = null;
        int inputMask = 0xffff;
        String server = "";
        for (String arg : args) {
            if (arg.equalsIgnoreCase("-s")) {
                server = "-s";
            }
            if (arg.endsWith(".properties")) {
                prop = ResourceUtils.loadPropertiesFormFile(prop,arg);
            }
        }

        String listenAddress= prop.getProperty("serverAddress", "0.0.0.0");
        int port = Integer.parseInt(prop.getProperty("port",
                Integer.toString(NetIOConnector.DEFAULT_PORT)));
        String netAddress = prop.getProperty("netAddress", "localhost");
        if (server.equals("-s")) {
            connector = new NetIOConnectorServer(listenAddress,
                    port);
            ba = new SimpleBitAggregate(connector, connector, inputMask);
            label = "server";
        } else {
            connector = new NetIOConnectorClient(netAddress,
                    port);
            ba = new SimpleBitAggregate(connector, connector, ~inputMask);
            label = "client";
        }


        SimplePoller poller = new SimplePoller(ba);
        IOGUIPanel panel = new IOGUIPanel(label, ba, prop);
        connector.start();
        panel.pack();
        panel.setVisible(true);
        panel.startTheShow();
        PollThreads.createPollThread(poller).start();
    }

    /* keep checkstyle happy. */
    private IO() {
    }
}
