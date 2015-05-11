package jp.ac.tokushima_u.is.ll.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vstar
 */
@Service
public class GoogleMapService {

    @Value("${google.map.api}")
    private String googleMapApi;

    public String getGoogleMapApi() {
        return googleMapApi;
    }
}
