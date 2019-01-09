package com.baidu.iov.dueros.waimai.ui;

import android.Manifest;
import android.app.StatusBarsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.interfacedef.Ui;
import com.baidu.iov.dueros.waimai.presenter.Presenter;
import com.baidu.iov.dueros.waimai.utils.AtyContainer;
import com.baidu.iov.dueros.waimai.utils.CommonUtils;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.LocationManager;
import com.baidu.iov.dueros.waimai.utils.ToastUtils;
import com.baidu.location.BDLocation;

public abstract class BaseActivity<T extends Presenter<U>, U extends Ui> extends AppCompatActivity implements LocationManager.LocationCallBack {

    private T mPresenter;
    private int mBDLocationLocType;
    private AlertDialog dialog;

    abstract T createPresenter();

    abstract U getUi();

    protected BaseActivity() {
        mPresenter = createPresenter();
    }

    protected BDLocation mBDLocation;

    public T getPresenter() {
        return mPresenter;
    }

    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainer.getInstance().addActivity(this);
        setStatusBar(false, ContextCompat.getColor(this, R.color.base_color));
        mPresenter.onUiReady(getUi());
        initLocationCity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().registerCmd(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPresenter().unregisterCmd(this);
//        StatusBarsManager.exitApp(this, "com.baidu.iov.dueros.waimai");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null)
            dialog = null;
        AtyContainer.getInstance().removeActivity(this);
        LocationManager.getInstance(this).stopLocation();
        mPresenter.onUiDestroy(getUi());
    }

    protected void initLocationCity() {
        LocationManager instance = LocationManager.getInstance(this);
        instance.initLocationClient(null, null, 0, true);
        LocationManager.getInstance(this).setLocationCallBack(this);
        instance.startLocation();
        BDLocation lastKnownLocation = instance.getLastKnownLocation();
        if (lastKnownLocation != null) {
            mBDLocation = lastKnownLocation;
            Constant.LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
            Constant.LONGITUDE = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
        }
    }

    public void setStatusBar(boolean translucent, @ColorInt int color) {
        CommonUtils.setTranslucentStatusBar(this, translucent);
        if (color != 0) {
            CommonUtils.setStatusBarColor(this, color);
        }
    }

    @Override
    public void locationCallBack(boolean isSuccess, BDLocation bdLocation) {
        if (isSuccess) {
            mBDLocation = bdLocation;
            Constant.LATITUDE = (int) (mBDLocation.getLatitude() * LocationManager.SPAN);
            Constant.LONGITUDE = (int) (mBDLocation.getLongitude() * LocationManager.SPAN);
        } else {
            mBDLocationLocType = bdLocation.getLocType();
            LocationManager.getInstance(this).requestLocation();
        }
    }

    public void requestPermission() {
        if (isNoRequest)return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionDialog();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_ACCESS_COARSE_LOCATION);
                }


            }

        }
    }

    private void showPermissionDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.permission_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(layout);
        Button btn_sure = layout.findViewById(R.id.to_setting);
        Button btn_cancel = layout.findViewById(R.id.i_know);
        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setLayout((int) getResources().getDimension(R.dimen.px1000dp), (int) getResources().getDimension(R.dimen.px400dp));
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void startPrivacyActivity() {
        Intent intent = new Intent();
        intent.putExtra("com.baidu.bodyguard-Query.Location", "map.query");
        intent.putExtra("query_action", "Location");
        intent.setComponent(new ComponentName("com.baidu.bodyguard", "com.baidu.bodyguard.ui.activity.PrivacyDetailsActivity"));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted, yay! Do the contacts-related task you need to do.
                initLocationCity();
            } else {
                //permission denied, boo! Disable the functionality that depends on this permission.
                showPermissionDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected void doSearchAddress(boolean isEditModle) {
        Intent intent = new Intent(this, AddressSuggestionActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                intent.putExtra(Constant.ADDRESS_EDIT_INTENT_EXTRE_CITY, Constant.GET_CITY_ERROR);
                intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, isEditModle);
                startActivityForResult(intent, Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE);
                return;
            }

        }
        if (mBDLocation != null) {
            intent.putExtra(Constant.ADDRESS_EDIT_INTENT_EXTRE_CITY, mBDLocation.getCity());
            intent.putExtra(Constant.ADDRESS_SELECT_INTENT_EXTRE_ADD_OR_EDIT, isEditModle);
            startActivityForResult(intent, Constant.ADDRESS_SEARCH_ACTIVITY_RESULT_CODE);
        } else {
            ToastUtils.show(this, getApplicationContext().getResources().getString(R.string.location_error_toast),Toast.LENGTH_SHORT);
            LocationManager.getInstance(this).requestLocation();
        }
    }

    private boolean isNoRequest = false;

    public void setNoRequestForPermissions(boolean isNoRequest){
        this.isNoRequest=isNoRequest;
    }
}
