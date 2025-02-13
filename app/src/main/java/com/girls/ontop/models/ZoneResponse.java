package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ZoneResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ZoneData data;

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

    public ZoneData getData() {
        return data;
    }

    public void setData(ZoneData data) {
        this.data = data;
    }

    public static class ZoneData {

        @SerializedName("data")
        private List<Zone> data;

        // Getter and Setter
        public List<Zone> getData() {
            return data;
        }

        public void setData(List<Zone> data) {
            this.data = data;
        }
    }

    public static class Zone {

        @SerializedName("zone_id")
        private int zoneId;

        @SerializedName("zone_name")
        private String zoneName;

        // Getters and Setters
        public int getZoneId() {
            return zoneId;
        }

        public void setZoneId(int zoneId) {
            this.zoneId = zoneId;
        }

        public String getZoneName() {
            return zoneName;
        }

        public void setZoneName(String zoneName) {
            this.zoneName = zoneName;
        }
    }
}
