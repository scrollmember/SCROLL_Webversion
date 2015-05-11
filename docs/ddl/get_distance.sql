CREATE FUNCTION `get_distance`(`lat1` FLOAT, `long1` FLOAT, `lat2` FLOAT, `long2` FLOAT) RETURNS float
    DETERMINISTIC
BEGIN
DECLARE distance FLOAT DEFAULT -1;
DECLARE earthRadius FLOAT DEFAULT 6371.009;
-- 3958.761 --miles
-- 6371.009 --km
DECLARE axis FLOAT;

IF ((lat1 IS NOT NULL) AND (long1 IS NOT NULL) AND (lat2 IS NOT NULL) AND (long2 IS NOT NULL)) THEN -- bit of protection against bad data

  SET axis = (SIN(RADIANS(lat2-lat1)/2) * SIN(RADIANS(lat2-lat1)/2) + COS(RADIANS(lat1)) * COS(RADIANS(lat2)) * SIN(RADIANS(long2-long1)/2) * SIN(RADIANS(long2-long1)/2));
  SET distance = earthRadius * (2 * ATAN2(SQRT(axis), SQRT(1-axis)));

END IF;

RETURN distance;
END