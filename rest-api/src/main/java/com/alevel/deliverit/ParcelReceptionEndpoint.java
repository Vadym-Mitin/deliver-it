package com.alevel.deliverit;

import com.alevel.deliverit.customers.ParcelReceipt;
import com.alevel.deliverit.customers.ParcelReception;

/**
 * @author Vitalii Usatyi
 */
public class ParcelReceptionEndpoint {

    ParcelReceipt accept(String jsonString) {
        ParcelReceptionRequest request = ParcelReceptionRequest.parser().parse(jsonString);
        ParcelReception parcelReception =
                ParcelReception
                        .builder()
                        .setSender(request.getSender())
                        .setParcel(request.getParcel())
                        .build();
        return parcelReception.accept();
    }
}
