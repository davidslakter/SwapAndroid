package com.swap.models;


import android.content.Context;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.swap.utilities.AmazonClientManager;

public class SwapUserHistory {

    private String swap;
    private String swapped;
    private long currentTime = System.currentTimeMillis();


    public SwapUserHistory(String swap, String swapped) {
        this.swap = swap;
        this.swapped = swapped;
    }

    void didShare(Context context, SwapHistory swapHistory) {
        swapHistory.setSwap(swap);
        swapHistory.setSwapped(swapped);
        swapHistory.setTime(currentTime);

        AmazonDynamoDBClient ddb = AmazonClientManager.getInstance(context).ddb();
        if (ddb != null) {
            DynamoDBMapper mapper = new DynamoDBMapper(ddb);
            try {
                mapper.save(swapHistory, new DynamoDBMapperConfig(
                        DynamoDBMapperConfig.SaveBehavior.UPDATE_SKIP_NULL_ATTRIBUTES));
            } catch (AmazonServiceException ex) {
                /*UserPreferenceDemoActivity.clientManager
                        .wipeCredentialsOnAuthError(ex);*/
            }
        }
    }
}
