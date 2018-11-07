/**
 * This package provides a client and server to connect an IO across a network.
 *
 * Typical usage in a hardware io related setting is that the server 'owns'
 * the hardware to be controlled and the (better one) client connects to the
 * server and uses that IO remotely.
 *
 * The main portion of the work is done in NetIOConnector, which extends
 * nl.fontys.sevenlo.hwio.IO. The subclasses xxServer and xxClient take the
 * responsibility for the differences between a network server and ~ client.
 */
package nl.fontys.sevenlo.netio;
