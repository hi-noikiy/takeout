package com.baidu.iov.dueros.waimai.model;


import android.util.Log;

import com.baidu.iov.dueros.waimai.interfacedef.RequestCallback;
import com.baidu.iov.dueros.waimai.net.ApiCallBack;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressEditReq;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressEditBean;
import com.baidu.iov.dueros.waimai.utils.ApiUtils;

import java.util.List;

public class AddressEditModel implements IAddressEditModel {

    @Override
    public void requestAdressList(AddressEditReq addressEditReq, RequestCallback callback) {
        if (callback == null) {
            return;
        }

        addressEditReq.setName("Stanford Zhang");
        addressEditReq.setGender(1);
        addressEditReq.setPhone("17638916218");
        addressEditReq.setAddress("WuHan JiangXia");
        addressEditReq.setHouseNumber("18");
        addressEditReq.setLatitude(40002102);
        addressEditReq.setLongitude(116491116);
        addressEditReq.setBindType(11);
        addressEditReq.setAddressRangeTip("");
        Log.d("hhr", addressEditReq.toString());
        ApiUtils.addAddress(addressEditReq, new ApiCallBack<AddressEditBean>() {
            @Override
            public void onSuccess(AddressEditBean data) {
                Log.d("hhr", "AddressEditBean" + data.toString());
            }

            @Override
            public void onFailed(String msg) {
                Log.d("hhr", "Address erro:--" + msg);
            }
        });
    }


    @Override
    public void onReady() {

    }

    @Override
    public void onDestroy() {

    }
}
