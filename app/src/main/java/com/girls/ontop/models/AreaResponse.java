package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AreaResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private AreaData data;

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

    public AreaData getData() {
        return data;
    }

    public void setData(AreaData data) {
        this.data = data;
    }

    public static class AreaData {

        @SerializedName("data")
        private List<Area> data;

        // Getter and Setter
        public List<Area> getData() {
            return data;
        }

        public void setData(List<Area> data) {
            this.data = data;
        }
    }

    public static class Area {

        @SerializedName("area_id")
        private int areaId;

        @SerializedName("area_name")
        private String areaName;

        @SerializedName("home_delivery_available")
        private boolean homeDeliveryAvailable;

        @SerializedName("pickup_available")
        private boolean pickupAvailable;

        // Getters and Setters
        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public boolean isHomeDeliveryAvailable() {
            return homeDeliveryAvailable;
        }

        public void setHomeDeliveryAvailable(boolean homeDeliveryAvailable) {
            this.homeDeliveryAvailable = homeDeliveryAvailable;
        }

        public boolean isPickupAvailable() {
            return pickupAvailable;
        }

        public void setPickupAvailable(boolean pickupAvailable) {
            this.pickupAvailable = pickupAvailable;
        }
    }
}
