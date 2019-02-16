package com.example.user.zhtx.tools;

public class GetGeoCoderResult {

    private String address;
    private String addressSematic;

    private static GetGeoCoderResult getGeoCoderResult;

    public static GetGeoCoderResult newInstance() {
        if (getGeoCoderResult == null) {
            getGeoCoderResult = new GetGeoCoderResult();
        }
        return getGeoCoderResult;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressSematic() {
        return addressSematic;
    }

    public void setAddressSematic(String addressSematic) {
        this.addressSematic = addressSematic;
    }
}
