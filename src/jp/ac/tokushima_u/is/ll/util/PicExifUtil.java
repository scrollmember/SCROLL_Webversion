/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.tokushima_u.is.ll.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.GpsDirectory;
import java.io.BufferedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author houbin
 */
public class PicExifUtil {

    private static final Logger logger = LoggerFactory.getLogger(PicExifUtil.class);

    public static double[] readGps(BufferedInputStream inputStream) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(inputStream);
            Directory dir = metadata.getDirectory(GpsDirectory.class);
            Rational[] latArr = dir.getRationalArray(GpsDirectory.TAG_GPS_LATITUDE);
            Rational[] lngArr = dir.getRationalArray(GpsDirectory.TAG_GPS_LONGITUDE);
            double lat = latArr[0].doubleValue() + latArr[1].doubleValue() / 60 + latArr[2].doubleValue() / 3600;
            double lng = lngArr[0].doubleValue() + lngArr[1].doubleValue() / 60 + latArr[2].doubleValue() / 3600;
            if ("S".equals(dir.getString(GpsDirectory.TAG_GPS_LATITUDE_REF))) {
                lat *= -1;
            }
            if ("W".equals(dir.getString(GpsDirectory.TAG_GPS_LONGITUDE_REF))) {
                lng *= -1;
            }
            return new double[]{lat, lng};
        } catch (ImageProcessingException ex) {
            logger.debug("Image not exist or error occurred when reading", ex);
            return null;
        } catch (MetadataException ex) {
            logger.debug("Metadata not exist or error format", ex);
            return null;
        } catch (Exception ex){
        	logger.debug("Error when read GPS from image", ex);
            return null;
        }
    }
}
