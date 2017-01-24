package de.flo_aumeier.popularmoviesstage1;

/**
 * Created by Society on 22.01.2017.
 */

public enum PictureSizes {
      w92("w92"), w154("w154"), w185("w185")
    , w342("w342"), w500("w500"), w780("w780")
    , original("original");

    private String mSize;

    PictureSizes(String size) {
        mSize = size;
    }
}
