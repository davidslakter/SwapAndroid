package com.swap.models;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBNativeBoolean;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;
import com.swap.utilities.Constants;

@DynamoDBTable(tableName = Constants.TABLE_SWAP_REQUEST)
public class SwapRequest {

    private String sender;
    private String requested;
    private boolean sender_confirmed_acceptance;
    private double sent_at;
    private boolean status;
    private boolean requested_user_has_responded_to_request;

    @DynamoDBHashKey(attributeName = "sender")
    public String getSender() {
        return sender;
    }

    @DynamoDBRangeKey(attributeName = "requested")
    public String getRequested() {
        return requested;
    }

    @DynamoDBAttribute(attributeName = "sender_confirmed_acceptance")
    @DynamoDBNativeBoolean
    public boolean isSender_confirmed_acceptance() {
        return sender_confirmed_acceptance;
    }

    @DynamoDBAttribute(attributeName = "sent_at")
    public double getSent_at() {
        return sent_at;
    }

    @DynamoDBAttribute(attributeName = "status")
    @DynamoDBNativeBoolean
    public boolean isStatus() {
        return status;
    }

    @DynamoDBAttribute(attributeName = "requested_user_has_responded_to_request")
    @DynamoDBNativeBoolean
    public boolean isRequested_user_has_responded_to_request() {
        return requested_user_has_responded_to_request;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRequested(String requested) {
        this.requested = requested;
    }

    public void setSender_confirmed_acceptance(boolean sender_confirmed_acceptance) {
        this.sender_confirmed_acceptance = sender_confirmed_acceptance;
    }

    public void setSent_at(double sent_at) {
        this.sent_at = sent_at;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setRequested_user_has_responded_to_request(boolean requested_user_has_responded_to_request) {
        this.requested_user_has_responded_to_request = requested_user_has_responded_to_request;
    }
}
