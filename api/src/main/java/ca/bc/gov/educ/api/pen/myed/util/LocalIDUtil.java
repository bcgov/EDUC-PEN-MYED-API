package ca.bc.gov.educ.api.pen.myed.util;


import ca.bc.gov.educ.api.pen.myed.constants.BadLocalID;


public final class LocalIDUtil {

    /**
     * Instantiates a new local id util.
     */
    private LocalIDUtil() {
    }

    // if localID is one of the BadLocalID enum value, change it to null
    public static String changeBadLocalID(String localID){
        for (BadLocalID info : BadLocalID.values()) {
            if(info.getLabel().equals(localID)) {
                return null;
            }
        }
        return localID;

    }
}
