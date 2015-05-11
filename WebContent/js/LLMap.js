/**
 * @author HouBin
 * @param id map id
 * @param options{lat, lng, zoom, onchange}
 * @returns
 */
var LLMap = function(id, options){
    this.mapId = id;
    var lat;
    var lng;
    if(options && options.lat && options.lng){
        this.lat = options.lat;
        this.lng = options.lng;
    }else if(google.loader.ClientLocation && google.loader.ClientLocation.latitude && google.loader.ClientLocation.longitude){
        this.lat = google.loader.ClientLocation.latitude;
        this.lng = google.loader.ClientLocation.longitude;
    }else{
    	this.lat = -1;
    	this.lng = -1;
    }
    
    if(options && options.zoom){
        this.zoom = options.zoom;
    }else{
        this.zoom = 10;
    }
    
    if(options && options.onchange){
        this.onchange = options.onchange;
    }else{
        this.onchange = function(){};
    }
    
    this.map = new google.maps.Map(document.getElementById(this.mapId), {
        zoom: this.zoom,
        center: new google.maps.LatLng(this.lat, this.lng),
        disableDefaultUI: true,
        scaleControl: true,
        navigationControl: true,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });
    
    this.marker = new google.maps.Marker({
          position: this.map.getCenter(), 
          map: this.map,
          draggable: true
    });
    
    var _this = this;
    var latlng = _this.marker.getPosition();
    _this.onchange.call(_this, latlng.lat(), latlng.lng(), _this.map.getZoom());
    
    google.maps.event.addListener(this.map, "bounds_changed", function(){
        if(!_this.map.getBounds().contains(_this.marker.getPosition())){
            _this.marker.setPosition(_this.map.getCenter());
        }
        var latlng = _this.marker.getPosition();
        _this.onchange.call(_this, latlng.lat(), latlng.lng(), _this.map.getZoom());
    });
    
    google.maps.event.addListener(this.marker, "position_changed", function(){
        var latlng = _this.marker.getPosition();
        _this.onchange.call(_this, latlng.lat(), latlng.lng(), _this.map.getZoom());
    });
    
    if((!options || !options.lat || !options.lng) && navigator && navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            if(position && position.coords){
                _this.map.setCenter(new google.maps.LatLng(position.coords.latitude, position.coords.longitude));
                _this.map.setZoom(13);
                _this.marker.setPosition(_this.map.getCenter());
            }
        });
    }
};