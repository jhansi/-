package com.weatherunderground.jhansi.weatherunderground; /**
 * Created by Jhansi Tavva on 5/11/16.
 * Copyright (c) 2016 Jhansi Tavva. All rights reserved.
 */


import android.location.Address;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StateNameAbbreviator {
    private static final String TAG = "StateNameAbbreviator";

    static private Map<String, String> mStateMap = null;

    static public String getStateAbbreviation(Address address) {
        if (address == null) {
            return null;
        }

        populateStates();

        String stateCode = mStateMap.get(address.getAdminArea());
        if (stateCode == null) {
            Log.d(TAG, "State mapping failed, parsing from address");
            stateCode = parseStateCodeFromFullAddress(address);
            if (stateCode == null) {
                Log.d(TAG, "Could not parse state from address");
            }
        } else {
            Log.d(TAG, "Successfully mapped " + address.getAdminArea() + " to " + stateCode);
        }

        return stateCode;
    }

    static private String parseStateCodeFromFullAddress(Address address) {
        if ((address == null) || address.getMaxAddressLineIndex() < 0) {
            return null;
        }

        String fullAddress = "";
        for (int j = 0; j <= address.getMaxAddressLineIndex(); j++) {
            if (address.getAddressLine(j) != null) {
                fullAddress += " " + address.getAddressLine(j);
            }
        }

        Log.d(TAG, "Full address: " + fullAddress);

        Pattern pattern = Pattern.compile("(?<![A-Za-z0-9])([A-Z]{2})(?![A-Za-z0-9])");
        Matcher matcher = pattern.matcher(fullAddress);

        String stateCode = null;
        while (matcher.find()) {
            stateCode = matcher.group().trim();
        }

        Log.d(TAG, "Parsed statecode: " + stateCode);

        return stateCode;
    }

    private static void populateStates() {
        if (mStateMap == null) {
            mStateMap = new HashMap<String, String>();
            mStateMap.put("Alabama", "AL");
            mStateMap.put("Alaska", "AK");
            mStateMap.put("Alberta", "AB");
            mStateMap.put("American Samoa", "AS");
            mStateMap.put("Arizona", "AZ");
            mStateMap.put("Arkansas", "AR");
            mStateMap.put("Armed Forces (AE)", "AE");
            mStateMap.put("Armed Forces Americas", "AA");
            mStateMap.put("Armed Forces Pacific", "AP");
            mStateMap.put("British Columbia", "BC");
            mStateMap.put("California", "CA");
            mStateMap.put("Colorado", "CO");
            mStateMap.put("Connecticut", "CT");
            mStateMap.put("Delaware", "DE");
            mStateMap.put("District Of Columbia", "DC");
            mStateMap.put("Florida", "FL");
            mStateMap.put("Georgia", "GA");
            mStateMap.put("Guam", "GU");
            mStateMap.put("Hawaii", "HI");
            mStateMap.put("Idaho", "ID");
            mStateMap.put("Illinois", "IL");
            mStateMap.put("Indiana", "IN");
            mStateMap.put("Iowa", "IA");
            mStateMap.put("Kansas", "KS");
            mStateMap.put("Kentucky", "KY");
            mStateMap.put("Louisiana", "LA");
            mStateMap.put("Maine", "ME");
            mStateMap.put("Manitoba", "MB");
            mStateMap.put("Maryland", "MD");
            mStateMap.put("Massachusetts", "MA");
            mStateMap.put("Michigan", "MI");
            mStateMap.put("Minnesota", "MN");
            mStateMap.put("Mississippi", "MS");
            mStateMap.put("Missouri", "MO");
            mStateMap.put("Montana", "MT");
            mStateMap.put("Nebraska", "NE");
            mStateMap.put("Nevada", "NV");
            mStateMap.put("New Brunswick", "NB");
            mStateMap.put("New Hampshire", "NH");
            mStateMap.put("New Jersey", "NJ");
            mStateMap.put("New Mexico", "NM");
            mStateMap.put("New York", "NY");
            mStateMap.put("Newfoundland", "NF");
            mStateMap.put("North Carolina", "NC");
            mStateMap.put("North Dakota", "ND");
            mStateMap.put("Northwest Territories", "NT");
            mStateMap.put("Nova Scotia", "NS");
            mStateMap.put("Nunavut", "NU");
            mStateMap.put("Ohio", "OH");
            mStateMap.put("Oklahoma", "OK");
            mStateMap.put("Ontario", "ON");
            mStateMap.put("Oregon", "OR");
            mStateMap.put("Pennsylvania", "PA");
            mStateMap.put("Prince Edward Island", "PE");
            mStateMap.put("Puerto Rico", "PR");
            mStateMap.put("Quebec", "PQ");
            mStateMap.put("Rhode Island", "RI");
            mStateMap.put("Saskatchewan", "SK");
            mStateMap.put("South Carolina", "SC");
            mStateMap.put("South Dakota", "SD");
            mStateMap.put("Tennessee", "TN");
            mStateMap.put("Texas", "TX");
            mStateMap.put("Utah", "UT");
            mStateMap.put("Vermont", "VT");
            mStateMap.put("Virgin Islands", "VI");
            mStateMap.put("Virginia", "VA");
            mStateMap.put("Washington", "WA");
            mStateMap.put("West Virginia", "WV");
            mStateMap.put("Wisconsin", "WI");
            mStateMap.put("Wyoming", "WY");
            mStateMap.put("Yukon Territory", "YT");
        }
    }
}
