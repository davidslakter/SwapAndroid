package com.swap.models;

import java.util.ArrayList;
import java.util.Date;

public class DataModel {

    private String headerTitle;
    private ArrayList<SwapRequest> allItemsInSection;
    private int type;
    private Date msgDate;

    public DataModel() {

    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DataModel(String headerTitle, ArrayList<SwapRequest> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<SwapRequest> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<SwapRequest> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
