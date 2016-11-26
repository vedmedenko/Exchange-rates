package com.vedmedenko.exchangerates.core.rest.models.current;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "exchangerate")
public class CurrentRate implements Parcelable {

    @Attribute
    private String ccy;

    @Attribute
    private String ccy_name_ru;

    @Attribute
    private String ccy_name_ua;

    @Attribute
    private String ccy_name_en;

    @Attribute
    private String base_ccy;

    @Attribute
    private String date;

    @Attribute
    private Long buy;

    @Attribute
    private Double unit;

    public CurrentRate() {

    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public String getCcy_name_ru() {
        return ccy_name_ru;
    }

    public void setCcy_name_ru(String ccy_name_ru) {
        this.ccy_name_ru = ccy_name_ru;
    }

    public String getCcy_name_ua() {
        return ccy_name_ua;
    }

    public void setCcy_name_ua(String ccy_name_ua) {
        this.ccy_name_ua = ccy_name_ua;
    }

    public String getCcy_name_en() {
        return ccy_name_en;
    }

    public void setCcy_name_en(String ccy_name_en) {
        this.ccy_name_en = ccy_name_en;
    }

    public String getBase_ccy() {
        return base_ccy;
    }

    public void setBase_ccy(String base_ccy) {
        this.base_ccy = base_ccy;
    }

    public Long getBuy() {
        return buy;
    }

    public void setBuy(Long buy) {
        this.buy = buy;
    }

    public Double getUnit() {
        return unit;
    }

    public void setUnit(Double unit) {
        this.unit = unit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CurrentRate(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        ccy = data[0];
        ccy_name_ru = data[1];
        ccy_name_ua = data[2];
        ccy_name_en = data[3];
        base_ccy = data[4];
        date = data[5];

        buy = in.readLong();
        unit = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] { ccy, ccy_name_ru, ccy_name_ua, ccy_name_en, base_ccy, date });
        parcel.writeLong(buy);
        parcel.writeDouble(unit);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CurrentRate createFromParcel(Parcel in) {
            return new CurrentRate(in);
        }

        public CurrentRate[] newArray(int size) {
            return new CurrentRate[size];
        }
    };
}
