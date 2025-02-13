package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CityResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private CityData data;

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public CityData getData() {
        return data;
    }

    public void setData(CityData data) {
        this.data = data;
    }

    public static class CityData {

        @SerializedName("data")
        private List<City> data;

        // Getter and Setter
        public List<City> getData() {
            return data;
        }

        public void setData(List<City> data) {
            this.data = data;
        }
    }

    public static class City {

        @SerializedName("city_id")
        private int cityId;

        @SerializedName("city_name")
        private String cityName;

        // Getters and Setters
        public int getCityId() {
            return cityId;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
    }
}
