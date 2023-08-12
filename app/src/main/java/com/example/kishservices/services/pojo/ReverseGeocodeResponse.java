package com.example.kishservices.services.pojo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ReverseGeocodeResponse {

        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("neighbourhood")
        @Expose
        public String neighbourhood;
        @SerializedName("municipality_zone")
        @Expose
        public String municipalityZone;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("in_traffic_zone")
        @Expose
        public Boolean inTrafficZone;
        @SerializedName("in_odd_even_zone")
        @Expose
        public Boolean inOddEvenZone;
        @SerializedName("route_name")
        @Expose
        public String routeName;
        @SerializedName("route_type")
        @Expose
        public String routeType;
        @SerializedName("place")
        @Expose
        public Object place;
        @SerializedName("district")
        @Expose
        public String district;
        @SerializedName("formatted_address")
        @Expose
        public String formattedAddress;
        @SerializedName("village")
        @Expose
        public Object village;
        @SerializedName("county")
        @Expose
        public String county;

}