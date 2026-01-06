package com.example.md3.data.preferences;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class SharedPrefs {
    private static final String PREF_NAME = "app_preference";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String OTP_TOKEN = "otp_token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String IS_OWNER = "is_owner";
    private static final String NAME = "company_name";


    private static final String WORK_STATUS = "work_status";
    private static final String JOURNEY_STATUS = "journey_status";
    private static final String ORGANISATION_ID = "organisation_id";
    private static final String ORGANISATION_USER_ID = "organisation_user_id";

    private static final String ORGANISATION_USER_NAME = "organisation_user_name";
    private static final String ORGANISATION_CONTACT_NUMBER = "organisation_contact_number";


    private static final String LAT_LONG_LIST = "lat_long_list";
    private static final String LOGO = "logo";
    private static final String EMAIL = "email";
    private static final String PHONE = "phone";
    private static final String SECTOR = "sector";
    private static final String BRAND = "brand";
    private static final String INFORMATION = "information";
    private static final String OWNER = "owner";

    private static final String JOURNEY_ROUTE_ID = "journey_route_id";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void clearPrefs() {
        editor.clear().apply();
    }

    public String getAccessToken() {
        return pref.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        editor.putString(ACCESS_TOKEN, accessToken).apply();
    }

    public String getRefreshToken() {
        return pref.getString(REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String refreshToken) {
        editor.putString(REFRESH_TOKEN, refreshToken).apply();
    }

    public String getOtpToken() {
        return pref.getString(OTP_TOKEN, "");
    }

    public void setOtpToken(String otpToken) {
        editor.putString(OTP_TOKEN, otpToken).apply();
    }


    public String getOrganisationId() {
        return pref.getString(ORGANISATION_ID, "");
    }

    public void setOrganisationId(String organisationId) {
        editor.putString(ORGANISATION_ID, organisationId).apply();
    }

    public String getOrganisationUserId() {
        return pref.getString(ORGANISATION_USER_ID, "");
    }

    public void setOrganisationUserId(String organisationUserId) {
        editor.putString(ORGANISATION_USER_ID, organisationUserId).apply();
    }


    public String getOrganisationUserName() {
        return pref.getString(ORGANISATION_USER_NAME, "");
    }

    public void setOrganisationUserName(String organisationUserName) {
        editor.putString(ORGANISATION_USER_NAME, organisationUserName).apply();
    }


    public String getOrganisationUserContact() {
        return pref.getString(ORGANISATION_CONTACT_NUMBER, "");
    }

    public void setOrganisationUserContact(String phone) {
        editor.putString(ORGANISATION_CONTACT_NUMBER, phone).apply();
    }



    public String getLatLongList() {
        return pref.getString(LAT_LONG_LIST, null);
    }

    public void setLatLongList(String latLongList) {
        editor.putString(LAT_LONG_LIST, latLongList).apply();
    }


    public boolean getIsOwner() {
        return pref.getBoolean(IS_OWNER, false);
    }

    public void setIsOwner(boolean isOwner) {
        editor.putBoolean(IS_OWNER, isOwner).apply();
    }

    public String getName() {
        return pref.getString(NAME, "");
    }

    public void setName(String name) {
        editor.putString(NAME, name).apply();
    }

    public String getLogo() {
        return pref.getString(LOGO, "");
    }

    public void setLogo(String logo) {
        editor.putString(LOGO, logo).apply();
    }

    public String getEmail() {
        return pref.getString(EMAIL, "");
    }

    public void setEmail(String email) {
        editor.putString(EMAIL, email).apply();
    }

    public String getPhone() {
        return pref.getString(PHONE, "");
    }

    public void setPhone(String phone) {
        editor.putString(PHONE, phone).apply();
    }

    public String getSector() {
        return pref.getString(SECTOR, "");
    }

    public void setSector(String sector) {
        editor.putString(SECTOR, sector).apply();
    }

    public String getBrand() {
        return pref.getString(BRAND, "");
    }

    public void setBrand(String brand) {
        editor.putString(BRAND, brand).apply();
    }

    public String getInformation() {
        return pref.getString(INFORMATION, "");
    }

    public void setInformation(String information) {
        editor.putString(INFORMATION, information).apply();
    }

    public String getOwner() {
        return pref.getString(OWNER, "");
    }

    public void setOwner(String owner) {
        editor.putString(OWNER, owner).apply();
    }


    public HashMap<String, String> getWorkStatusHashMap() {
        String json = pref.getString(WORK_STATUS, "");
        return new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
        }.getType());
    }

    public void saveWorkStatusHashMap(HashMap<String, String> hashMap) {
        String json = new Gson().toJson(hashMap);
        editor.putString(WORK_STATUS, json).apply();
    }


    public HashMap<String, String> getJourneyStatusHashMap() {
        String json = pref.getString(JOURNEY_STATUS, "");
        return new Gson().fromJson(json, new TypeToken<HashMap<String, String>>() {
        }.getType());
    }

    public void saveJourneyStatusHashMap(HashMap<String, String> hashMap) {
        String json = new Gson().toJson(hashMap);
        editor.putString(JOURNEY_STATUS, json).apply();
    }




    public void clearSavedWorkStatusHashMap() {
        editor.remove(WORK_STATUS).apply();
    }


    public void clearSavedJourneyStatusHashMap() {
        editor.remove(WORK_STATUS).apply();
    }


}
