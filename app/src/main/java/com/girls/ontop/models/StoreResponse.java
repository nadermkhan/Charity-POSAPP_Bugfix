package com.girls.ontop.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StoreResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private StoreData data;

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

    public StoreData getData() {
        return data;
    }

    public void setData(StoreData data) {
        this.data = data;
    }

    public static class StoreData {

        @SerializedName("data")
        private List<Store> data;

        @SerializedName("total")
        private int total;

        @SerializedName("current_page")
        private int currentPage;

        @SerializedName("per_page")
        private int perPage;

        @SerializedName("total_in_page")
        private int totalInPage;

        @SerializedName("last_page")
        private int lastPage;

        @SerializedName("path")
        private String path;

        @SerializedName("to")
        private int to;

        @SerializedName("from")
        private int from;

        @SerializedName("last_page_url")
        private String lastPageUrl;

        @SerializedName("first_page_url")
        private String firstPageUrl;

        // Getters and Setters
        public List<Store> getData() {
            return data;
        }

        public void setData(List<Store> data) {
            this.data = data;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }

        public int getTotalInPage() {
            return totalInPage;
        }

        public void setTotalInPage(int totalInPage) {
            this.totalInPage = totalInPage;
        }

        public int getLastPage() {
            return lastPage;
        }

        public void setLastPage(int lastPage) {
            this.lastPage = lastPage;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getTo() {
            return to;
        }

        public void setTo(int to) {
            this.to = to;
        }

        public int getFrom() {
            return from;
        }

        public void setFrom(int from) {
            this.from = from;
        }

        public String getLastPageUrl() {
            return lastPageUrl;
        }

        public void setLastPageUrl(String lastPageUrl) {
            this.lastPageUrl = lastPageUrl;
        }

        public String getFirstPageUrl() {
            return firstPageUrl;
        }

        public void setFirstPageUrl(String firstPageUrl) {
            this.firstPageUrl = firstPageUrl;
        }
    }

    public static class Store {

        @SerializedName("store_id")
        private String storeId;

        @SerializedName("store_name")
        private String storeName;

        @SerializedName("store_address")
        private String storeAddress;

        @SerializedName("is_active")
        private int isActive;

        @SerializedName("city_id")
        private String cityId;

        @SerializedName("zone_id")
        private String zoneId;

        @SerializedName("hub_id")
        private String hubId;

        @SerializedName("is_default_store")
        private boolean isDefaultStore;

        @SerializedName("is_default_return_store")
        private boolean isDefaultReturnStore;

        // Getters and Setters
        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreAddress() {
            return storeAddress;
        }

        public void setStoreAddress(String storeAddress) {
            this.storeAddress = storeAddress;
        }

        public int getIsActive() {
            return isActive;
        }

        public void setIsActive(int isActive) {
            this.isActive = isActive;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getZoneId() {
            return zoneId;
        }

        public void setZoneId(String zoneId) {
            this.zoneId = zoneId;
        }

        public String getHubId() {
            return hubId;
        }

        public void setHubId(String hubId) {
            this.hubId = hubId;
        }

        public boolean isDefaultStore() {
            return isDefaultStore;
        }

        public void setDefaultStore(boolean defaultStore) {
            isDefaultStore = defaultStore;
        }

        public boolean isDefaultReturnStore() {
            return isDefaultReturnStore;
        }

        public void setDefaultReturnStore(boolean defaultReturnStore) {
            isDefaultReturnStore = defaultReturnStore;
        }
    }
}
