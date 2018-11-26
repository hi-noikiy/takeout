package com.baidu.iov.dueros.waimai.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.AddressSelectAdapter;
import com.baidu.iov.dueros.waimai.bean.MyApplicationAddressBean;
import com.baidu.iov.dueros.waimai.net.entity.request.AddressListReqBean;
import com.baidu.iov.dueros.waimai.net.entity.response.AddressListBean;
import com.baidu.iov.dueros.waimai.presenter.AddressSelectPresenter;
import com.baidu.iov.dueros.waimai.utils.CacheUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.Encryption;
import com.baidu.iov.faceos.client.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class AddressSelectActivity extends BaseActivity<AddressSelectPresenter, AddressSelectPresenter.AddressSelectUi> implements AddressSelectPresenter.AddressSelectUi, View.OnClickListener {
    private List<AddressListBean.IovBean.DataBean> mDataList;
    private RecyclerView mRecyclerView;
    private LinearLayout mNoAddress;
    private AddressSelectAdapter mAdapter;
    private final long SIX_HOUR = 6 * 60 * 60 * 1000;
    private AddressSelectPresenter.MReceiver mReceiver;
    private View addBtnView;

    @Override
    AddressSelectPresenter createPresenter() {
        return new AddressSelectPresenter();
    }

    @Override
    AddressSelectPresenter.AddressSelectUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_select);
        getPresenter().initDesBeans();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendBroadcast(new Intent("com.baidu.iov.dueros.waimai.requestNaviDes"));
        initData();
    }

    private void initData() {
        mDataList = new ArrayList<>();
        getPresenter().requestData(new AddressListReqBean());
        mAdapter = new AddressSelectAdapter(mDataList, this){
            @Override
            public void addAddress() {
                doSearchAddress(false);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        initListener();
    }

    private void initListener() {
        mAdapter.setOnItemClickListerner(new AddressSelectAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View v, AddressListBean.IovBean.DataBean dataBean) {
                switch (v.getId()) {
                    case R.id.address_select_details_container:
                        String databeanStr = GsonUtil.toJson(dataBean);
                        CacheUtils.saveAddressBean(databeanStr);
                        if (CacheUtils.getAddrTime() == 0 || (System.currentTimeMillis() - CacheUtils.getAddrTime() > SIX_HOUR)) {
                            CacheUtils.saveAddrTime(System.currentTimeMillis());
                        }
                        Intent homeintent = new Intent(AddressSelectActivity.this, HomeActivity.class);
                        try {
                            String address = Encryption.desEncrypt(dataBean.getAddress());
                            CacheUtils.saveAddress(address);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(homeintent);
                        finish();
                        break;
                    case R.id.address_select_edit:
                        startEditActivity(dataBean);
                        break;
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();//获取LayoutManager
                if (manager instanceof LinearLayoutManager) {
                    int position =  ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                    if (position==mDataList.size()){
                        addBtnView.setVisibility(View.GONE);
                    }else{
                        addBtnView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void startEditActivity(AddressListBean.IovBean.DataBean dataBean) {
        Intent intent = new Intent(AddressSelectActivity.this, AddressEditActivity.class);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, true);
        intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_EDIT_ADDRESS, dataBean);
        AddressSelectActivity.this.startActivity(intent);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.address_select_lv);
        mNoAddress = findViewById(R.id.address_none);
        RecyclerView.LayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layout);
        findViewById(R.id.address_back).setOnClickListener(this);
        findViewById(R.id.address_select_add).setOnClickListener(this);
        findViewById(R.id.add_no_address).setOnClickListener(this);
        addBtnView = findViewById(R.id.address_select_btn_layout);
    }


    @Override
    public void onSuccess(List<AddressListBean.IovBean.DataBean> data) {
        if (data.size() == 0) {
            mNoAddress.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mNoAddress.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mDataList.clear();
            mDataList.addAll(data);
            mAdapter.setAddressList(mDataList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(String msg) {

    }

    @Override
    public void onRegisterReceiver(AddressSelectPresenter.MReceiver receiver, IntentFilter intentFilter) {
        this.registerReceiver(receiver, intentFilter);
        this.mReceiver = receiver;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.address_back:
                finish();
                break;
            case R.id.add_no_address:
            case R.id.address_select_add:
                doSearchAddress(false);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }
}