package nl.fontys.sevenlo.utils;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.ImageIcon;

/**
 * Utility class to retrieve and cache resources (icons and sounds) via the
 * class loader. The utilities allow loading resources through the class loader,
 * allowing the resources to come from a jar. The resources are cached, using
 * the filename as key. <br>
 * <p>
 * <b>NOTE FOR NETBEANS USERS</b>: To make this work, you must run the
 * application from its jar file, that is, doe not right-click-run but instead
 * run with the run button (right pointing green arrow) from the tool bar. You
 * may need to work with more run configurations.</p>
 *
 * @version $Id$
 * @author Pieter van den Hombergh (P dot vandenHombergh at fontys dot nl)
 */
public final class ResourceUtils {

    /**
     * Cache for icons.
     */
    private static Map<String, ImageIcon> iconMap
            = new HashMap<String, ImageIcon>();
    /**
     * Cache for sounds.
     */
    private static Map<String, AudioClip> soundMap
            = new HashMap<String, AudioClip>();

    /**
     * Prevent construction and keep check-style happy.
     */
    private ResourceUtils() {
    }

    /**
     * Gets a resource through class loader.
     *
     * @param iconFilename file to use.
     * @return ImageIcon for this filename.
     * @throws RuntimeException if resource cannot be found.
     */
    public static ImageIcon getIconResource( final String iconFilename ) {
        ImageIcon result = null;
        if ( !iconMap.containsKey( iconFilename ) ) {
            String urlString = iconFilename;
            File f = new File( iconFilename );
            URL url = getResourceUrl( urlString );
            if ( f.exists() ) {
                result = new ImageIcon( iconFilename );
            } else if ( url != null ) {
                result = new ImageIcon( url );
            } else {
                throw new RuntimeException( "resource " + iconFilename
                        + " not found" );
            }
            iconMap.put( iconFilename, result );
        } else {
            result = iconMap.get( iconFilename );
        }
        return result;
    }

    /**
     * Gets a resource through classloader.
     *
     * @param soundFilename file to use
     * @return ImageIcon for this filename
     * @throws RuntimeException if resource cannot be found.
     */
    public static AudioClip getAudioClipResource( final String soundFilename ) {
        AudioClip result = null;
        if ( !iconMap.containsKey( soundFilename ) ) {
            String urlString = soundFilename;
            URL url = getResourceUrl( urlString );
            if ( url != null ) {
                result = Applet.newAudioClip( url );
                soundMap.put( soundFilename, result );
            } else {
                throw new RuntimeException( "resource "
                        + '"' + soundFilename + '"' + " not found" );
            }
        } else {
            result = soundMap.get( soundFilename );
        }
        return result;
    }

    /**
     * Use the classloader of this class to convert a string to an url.
     *
     * @param urlString string to convert.
     * @return the url.
     */
    public static URL getResourceUrl( String urlString ) {
        ClassLoader cl = ResourceUtils.class.getClassLoader();
        URL url = cl.getResource( urlString );
        if ( url == null ) {
            System.err.println( "Null url on class path for " + urlString );
        }
        return url;
    }

    /**
     * Add the properties from a file to properties, possibly in a jar. The
     * method tries the passed string as a file system path first, then attempts
     * to load the file through the class loader and then gives up if failing.
     *
     * @param p the properties to fill
     * @param fileName the filename with the properties to add
     * @return the properties, completed
     */
    public static Properties loadPropertiesFormFile( final Properties p,
            final String fileName ) {
        try {
            File f = new File( fileName );
            InputStream is;
            if ( f.exists() ) {
                is = new FileInputStream( f );
            } else {
                is = ResourceUtils.class.getClassLoader().getResourceAsStream(
                        fileName );
            }
            if ( is != null ) {
                p.load( is );
            }
        } catch ( Exception e ) {
            System.out.println( "Cannot read properties " + fileName );
        }
        return p;
    }

    /**
     * Parses a property as long. A prefix of the kind "0x" is interpreted
     * correctly, but not required.
     *
     * @param props the dictionary
     * @param propName the name of the property
     * @param defaultValue the default string rep
     * @return the parsed property.
     * @throws NumberFormatException when the string contains non hex chars.
     */
    public static long parseHexProperty( Properties props, String propName,
            String defaultValue ) throws NumberFormatException {
        long im;
        String stringVal = props.getProperty( propName,
                defaultValue );
        int radix = 10;
        if ( stringVal.startsWith( "0x" ) ) {
            stringVal = stringVal.substring( 2 );
            radix = 16;
        }
        im = ( int ) Long.parseLong( stringVal, radix );
        return im;
    }

    /**
     * Parses properties and searches for property names with pattern, deriving
     * an inputMask from the names.
     *
     * The returned value is an integer with a one for each input found at the
     * start of the property value. The pins are named pin
     * <tt>"^pin\\d+$"</tt> and are searched by this name. The pin is recognized
     * as input when its name starts with "in".
     *
     * @param props properties to search.
     * @param pinCount the number of pins
     * @return 0 or the input mask computed from the in pins found.
     */
    public static int getInputMaskFromProperties( Properties props,
            int pinCount ) {
        int im = 0;
        int mask = 1;
        for ( int i = 0; i < pinCount; i++ ) {
            String pinName = "pin" + i;
            String propValue = props.getProperty( pinName );
            if ( null != propValue && propValue.startsWith( "in" ) ) {
                im |= mask;
            }
            mask <<= 1;
        }
        return im;
    }
}
