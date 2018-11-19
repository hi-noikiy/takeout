package com.baidu.iov.dueros.waimai.ui;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.adapter.PoifoodSpusListAdapter;
import com.baidu.iov.dueros.waimai.adapter.PoifoodSpusTagsAdapter;
import com.baidu.iov.dueros.waimai.adapter.ShoppingCartAdapter;
import com.baidu.iov.dueros.waimai.interfacedef.IShoppingCartToDetailListener;
import com.baidu.iov.dueros.waimai.net.entity.response.PoidetailinfoBean;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.presenter.PoifoodListPresenter;
import com.baidu.iov.dueros.waimai.utils.Constant;
import com.baidu.iov.dueros.waimai.utils.DoubleUtil;
import com.baidu.iov.dueros.waimai.utils.GlideApp;
import com.baidu.iov.dueros.waimai.utils.Lg;
import com.baidu.iov.dueros.waimai.view.PoifoodListPinnedHeaderListView;
import com.domain.multipltextview.MultiplTextView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.baidu.iov.dueros.waimai.ui.PaymentActivity.TO_SHOW_SHOP_CART;

public class FoodListActivity extends BaseActivity<PoifoodListPresenter, PoifoodListPresenter.PoifoodListUi> implements PoifoodListPresenter.PoifoodListUi, View.OnClickListener, PoifoodSpusListAdapter.onCallBackListener, IShoppingCartToDetailListener {
    private static final String TAG = FoodListActivity.class.getSimpleName();
    public static final String POI_INFO = "poi_info";
    public static final String PRODUCT_LIST_BEAN = "product_list_bean";
    public static final String DISCOUNT = "discount";

    private boolean isScroll = true;
    private ListView mFoodSpuTagsList;
    private PoifoodListPinnedHeaderListView mSpusList;
    private PoifoodSpusListAdapter mPoifoodSpusListAdapter;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;
    private MultiplTextView shoppingPrise;
    private MultiplTextView shoppingNum;
    private Button settlement;
    private LinearLayout cardShopLayout;
    private ImageView shopping_cart;
    private int AnimationDuration = 500;
    private int number = 0;
    private boolean isClean = false;
    private FrameLayout animation_viewGroup;
    private MultiplTextView defaultText;
    private List<String> foodSpuTagsBeanName;
    private RelativeLayout parentLayout;
    private MultiplTextView noData;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> foodSpuTagsBeans = new ArrayList<>();
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spusBeanList;
    private ListView shoppingListView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private ArrayMap<String, String> map;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        animation_viewGroup.removeAllViews();
                    } catch (Exception e) {

                    }
                    isClean = false;

                    break;
                default:
                    break;
            }
        }
    };
    private PoifoodSpusTagsAdapter mFoodSpuTagsListAdapter;
    private RelativeLayout mStoreDetails;
    private MultiplTextView mClearshopCart;
    private PoidetailinfoBean mPoidetailinfoBean;
    private PoifoodListBean.MeituanBean.DataBean.PoiInfoBean mPoiInfoBean;
    private ImageView mFinish;
    private MultiplTextView mShopTitle;
    private MultiplTextView mDelivery;
    private MultiplTextView mBulletin;
    private MultiplTextView mDiscounts;
    private ImageView mShopPicture;
    private List<Double> listFull;
    private List<Double> listReduce;
    private MultiplTextView mDiscount;
    private Integer discount;
    private double mDiscountNumber;
    private MultiplTextView mDetailsNotice;
    private MultiplTextView mDetailsDistribution;
    private MultiplTextView mDetailsDiscount;
    private MultiplTextView mDetailsShopName;
    private Dialog mBottomDialog;
    private MultiplTextView mShopCartNum;
    private MultiplTextView mCartDistributionFee;
    private MultiplTextView mCartShoppingPrise;
    private Button mCartSettlement;
    private MultiplTextView mCartDiscount;
    private MultiplTextView mCartClose;
    private MultiplTextView mDistributionFee;
    private RelativeLayout mRlDiscount;
    private List<Map<Integer, Integer>> foodSpuTagsChoiceNum;
    private int rightSection;
    private MultiplTextView mNoProduct;
    private RelativeLayout mLlPrice;

    @Override
    PoifoodListPresenter createPresenter() {
        return new PoifoodListPresenter();
    }

    @Override
    PoifoodListPresenter.PoifoodListUi getUi() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        map = new ArrayMap<>();
        initView();
        initData();
    }

    private void initView() {
        animation_viewGroup = createAnimLayout();
        noData = (MultiplTextView) findViewById(R.id.noData);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        shoppingPrise = (MultiplTextView) findViewById(R.id.shoppingPrise);
        shoppingNum = (MultiplTextView) findViewById(R.id.shoppingNum);
        settlement = (Button) findViewById(R.id.settlement);
        mFoodSpuTagsList = (ListView) findViewById(R.id.classify_mainlist);
        mSpusList = (PoifoodListPinnedHeaderListView) findViewById(R.id.classify_morelist);
        shopping_cart = (ImageView) findViewById(R.id.shopping_cart);
        mStoreDetails = (RelativeLayout) findViewById(R.id.rl_store_details);
        mFinish = (ImageView) findViewById(R.id.iv_finish);
        mShopTitle = (MultiplTextView) findViewById(R.id.tv_shop_title);
        mDelivery = (MultiplTextView) findViewById(R.id.tv_delivery);
        mBulletin = (MultiplTextView) findViewById(R.id.tv_bulletin);
        mDiscounts = (MultiplTextView) findViewById(R.id.tv_discounts);
        mShopPicture = (ImageView) findViewById(R.id.iv_shop);
        mDiscount = (MultiplTextView) findViewById(R.id.tv_discount);
        mDistributionFee = (MultiplTextView) findViewById(R.id.tv_distribution_fee);
        mRlDiscount = (RelativeLayout) findViewById(R.id.rl_discount);
        mNoProduct = (MultiplTextView) findViewById(R.id.tv_no_product);
        mLlPrice = (RelativeLayout) findViewById(R.id.ll_price);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent() != null) {
            boolean IsShowShopCart = getIntent().getBooleanExtra(TO_SHOW_SHOP_CART, false);
            if (IsShowShopCart) {
                showShopCartDialog();
            }
        }
    }

    public void initData() {
        productList = new ArrayList<>();
        foodSpuTagsBeanName = new ArrayList<>();
        foodSpuTagsChoiceNum = new ArrayList<>();
        mPoifoodSpusListAdapter = new PoifoodSpusListAdapter(this, productList, foodSpuTagsBeans, FoodListActivity.this);
        mPoifoodSpusListAdapter.SetOnSetHolderClickListener(new PoifoodSpusListAdapter.HolderClickListener() {
            @Override
            public void onHolderClick(Drawable drawable, int[] start_location) {
                doAnim(drawable, start_location);
            }
        });

        mSpusList.setAdapter(mPoifoodSpusListAdapter);
        mPoifoodSpusListAdapter.setCallBackListener(this);
        mFoodSpuTagsListAdapter = new PoifoodSpusTagsAdapter(this, foodSpuTagsBeanName, foodSpuTagsChoiceNum);
        mFoodSpuTagsList.setAdapter(mFoodSpuTagsListAdapter);
        shoppingCartAdapter = new ShoppingCartAdapter(this, productList);
        mFoodSpuTagsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                isScroll = false;
                Lg.getInstance().d(TAG, "mFoodSpuTagsList.getChildCount() = " + mFoodSpuTagsList.getChildCount());
                for (int i = 0; i < mFoodSpuTagsList.getChildCount(); i++) {
                    if (i == position) {
                        mFoodSpuTagsList.getChildAt(i).setBackgroundResource(R.color.list_bg);
                    } else {
                        mFoodSpuTagsList.getChildAt(i).setBackgroundColor(
                                Color.TRANSPARENT);
                    }
                }
                for (int i = 0; i < position; i++) {
                    rightSection += mPoifoodSpusListAdapter.getCountForSection(i) + 1;
                }
                mSpusList.setSelection(rightSection);
            }

        });


        mSpusList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isScroll) {
                    for (int i = 0; i < mFoodSpuTagsList.getChildCount(); i++) {
                        if (i == mPoifoodSpusListAdapter.getSectionForPosition(firstVisibleItem)) {
                            mFoodSpuTagsList.getChildAt(i).setBackgroundResource(R.color.list_bg);
                        } else {
                            mFoodSpuTagsList.getChildAt(i).setBackgroundColor(
                                    Color.TRANSPARENT);
                        }
                    }
                } else {
                    isScroll = true;
                }
            }
        });

        settlement.setOnClickListener(this);
        shopping_cart.setOnClickListener(this);

        mFinish.setOnClickListener(this);
        mStoreDetails.setOnClickListener(this);

        long wmPoiId = (long) getIntent().getExtras().get(Constant.STORE_ID);
        map.put(Constant.STORE_ID, String.valueOf(wmPoiId));
        getPresenter().requestData(map);
    }

    public void showFoodListActivityDialog(View view, View contentView, final PopupWindow window) {
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAtLocation(view, Gravity.TOP, 0, 0);
        backgroundAlpha(0.5f);
        ImageView dismiss = (ImageView) contentView.findViewById(R.id.iv_dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
    }

    public View getPopView(int layoutId) {
        return LayoutInflater.from(this).inflate(layoutId, null, false);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    public void updateProduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, String tag, int selection) {
        /*for (int i = 0; i < foodSpuTagsBeanName.size(); i++) {TODO
            if (foodSpuTagsChoiceNum.get(i).containsKey(selection)) {
                Integer choiceNum = foodSpuTagsChoiceNum.get(selection).get(selection);
                if (i == selection) {
                    choiceNum++;
                    Map map = new HashMap();
                    map.put(i, choiceNum);
                    foodSpuTagsChoiceNum.add(map);
                }
            }
        }
        mFoodSpuTagsListAdapter.notifyDataSetChanged();*/
        String spusBeanTag = spusBean.getTag();
        boolean inList = false;
        Lg.getInstance().d(TAG, "updateProduct tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            Lg.getInstance().d(TAG, "productList.contains(spusBean) = " + productList.contains(spusBean));
            if (productList.contains(spusBean)) {
                if (spusBean.getNumber() == 0) {
                    productList.remove(spusBean);
                } else {
                    for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                        Lg.getInstance().d(TAG, "shopProduct.getId() = " + shopProduct.getId() + "; spusBean.getId() = " + spusBean.getId());
                        if (spusBean.getId() == shopProduct.getId()) {
                            Lg.getInstance().d(TAG, "shopProduct.getNumber() = " + shopProduct.getNumber());
                            if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                                for (int i = 0; i < shopProduct.getAttrs().size(); i++) {
                                    List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = shopProduct.getAttrs().get(i).getChoiceAttrs();
                                    long id = choiceAttrs.get(0).getId();
                                    if (id == spusBean.getAttrs().get(i).getChoiceAttrs().get(0).getId()) {

                                        if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                                            int skusId = shopProduct.getChoiceSkus().get(0).getId();
                                            if (skusId == spusBean.getChoiceSkus().get(0).getId()) {
                                                shopProduct.setNumber(spusBean.getNumber());
                                                inList = true;
                                                break;
                                            }
                                        } else {
                                            shopProduct.setNumber(spusBean.getNumber());
                                            inList = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (inList) {
                                    break;
                                }
                            } else {
                                if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                                    for (int i = 0; i < shopProduct.getSkus().size(); i++) {
                                        int id = shopProduct.getChoiceSkus().get(0).getId();
                                        if (id == spusBean.getChoiceSkus().get(i).getId()) {
                                            int num = spusBean.getNumber();
                                            shopProduct.setNumber(num);
                                            inList = true;
                                            break;
                                        }
                                    }
                                } else {
                                    int num = spusBean.getNumber();
                                    shopProduct.setNumber(num);
                                    inList = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            } else {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBeanNew = null;
                try {
                    spusBeanNew = (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean) spusBean.deepClone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Lg.getInstance().d(TAG, "shopProduct else");
                productList.add(spusBeanNew);
                inList = false;
            }
        }
        shoppingCartAdapter.notifyDataSetChanged();
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        setPrise();
    }

    @Override
    public void onUpdateDetailList(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean, String tag) {
        String spusBeanTag = spusBean.getTag();
        Lg.getInstance().d(TAG, "onUpdateDetailList tag = " + tag + "; spusBeanTag = " + spusBeanTag);
        if (tag.equals(spusBeanTag)) {
            for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                spusBeanList = foodSpuTagsBeans.get(i).getSpus();
                for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                    if (spusBean.getId() == shopProduct.getId()) {
                        shopProduct.setNumber(spusBean.getNumber());
                    }
                }
            }
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        setPrise();
    }

    @Override
    public void onRemovePriduct(PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean product) {
        for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
            spusBeanList = foodSpuTagsBeans.get(i).getSpus();
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : spusBeanList) {
                if (product.getId() == shopProduct.getId()) {
                    productList.remove(product);
                    shoppingCartAdapter.notifyDataSetChanged();
                    shopProduct.setNumber(shopProduct.getNumber());
                }
            }
        }
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        shoppingCartAdapter.notifyDataSetChanged();
        if (mBottomDialog != null && mBottomDialog.isShowing()) {
            setDialogHeight(mBottomDialog);
        }
        setPrise();
    }

    public void setPrise() {
        double sum = 0;
        int shopNum = 0;
        for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean pro : productList) {
            sum = DoubleUtil.sum(sum, DoubleUtil.mul((double) pro.getNumber(), Double.parseDouble("" + pro.getMin_price())));
            shopNum = shopNum + pro.getNumber();
        }
        if (shopNum > 0) {
            shoppingNum.setVisibility(View.VISIBLE);
            if (mShopCartNum != null) {
                mShopCartNum.setVisibility(View.VISIBLE);
            }
        } else {
            shoppingNum.setVisibility(View.GONE);
            if (mShopCartNum != null) {
                mShopCartNum.setVisibility(View.GONE);
            }
        }
        if (sum > 0) {
            mLlPrice.setVisibility(View.VISIBLE);
            mNoProduct.setVisibility(View.GONE);
            shoppingPrise.setVisibility(View.VISIBLE);
            if (mCartShoppingPrise != null) {
                mCartShoppingPrise.setVisibility(View.VISIBLE);
            }
        } else {
            mLlPrice.setVisibility(View.GONE);
            mNoProduct.setVisibility(View.VISIBLE);
            shoppingPrise.setVisibility(View.GONE);
            if (mCartShoppingPrise != null) {
                mCartShoppingPrise.setVisibility(View.GONE);
            }
        }
        shoppingPrise.setText("¥" + " " + (new DecimalFormat("0.00")).format(sum));
        if (mCartShoppingPrise != null) {
            mCartShoppingPrise.setText("¥" + " " + (new DecimalFormat("0.00")).format(sum));
        }
        shoppingNum.setText(String.valueOf(shopNum));
        if (mShopCartNum != null) {
            mShopCartNum.setText(String.valueOf(shopNum));
        }

        if (listFull != null && listReduce != null) {
            if (listFull.size() > 0 && sum > listFull.get(listFull.size() - 1)) {
                mDiscount.setText(getString(R.string.already_reduced) + listReduce.get(listReduce.size() - 1) + getString(R.string.element));
                if (mCartDiscount != null) {
                    mCartDiscount.setText(getString(R.string.already_reduced) + listReduce.get(listReduce.size() - 1) + getString(R.string.element));
                }
                mDiscountNumber = listReduce.get(listReduce.size() - 1);
                return;
            }
            for (int i = 0; i < listFull.size(); i++) {
                Lg.getInstance().d(TAG, "listFull.get(0) = " + listFull.get(0));
                if (listFull.get(i) > sum) {
                    double v = listFull.get(i) - sum;
                    java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                    String str = myformat.format(v);
                    mDiscount.setVisibility(View.VISIBLE);
                    mRlDiscount.setVisibility(View.VISIBLE);
                    if (i == 0) {
                        mDiscount.setText(getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                        if (mCartDiscount != null) {
                            mCartDiscount.setText(getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                        }
                    } else {
                        mDiscount.setText(getString(R.string.already_reduced) + listReduce.get(i - 1) + getString(R.string.element) + "，" +
                                getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                        if (mCartDiscount != null) {
                            mCartDiscount.setText(getString(R.string.already_reduced) + listReduce.get(i - 1) + getString(R.string.element) + "，" +
                                    getString(R.string.buy_again) + str + getString(R.string.reduce) + listReduce.get(i) + getString(R.string.element));
                        }
                        mDiscountNumber = listReduce.get(i - 1);
                    }
                    break;
                }
            }
        }
        if (productList == null || productList.size() == 0) {
            mDiscount.setVisibility(View.GONE);
            mRlDiscount.setVisibility(View.GONE);
        }
        double min_price = mPoidetailinfoBean.getMeituan().getData().getMin_price();
        if (sum > 0) {
            if (sum < mPoidetailinfoBean.getMeituan().getData().getMin_price()) {
                double v = min_price - sum;
                java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                String str = myformat.format(v);
                settlement.setText(String.format(getString(R.string.not_distribution), "" + str));
                settlement.setBackgroundResource(R.drawable.btn_grey);
                settlement.setEnabled(false);
            } else {
                settlement.setText(R.string.confirmation_of_the_order);
                settlement.setBackgroundResource(R.drawable.btn_bg);
                settlement.setEnabled(true);
            }
            if (mCartSettlement != null) {
                if (sum < min_price) {
                    double v = min_price - sum;
                    java.text.DecimalFormat myformat = new java.text.DecimalFormat("0.0");
                    String str = myformat.format(v);
                    mCartSettlement.setText(String.format(getString(R.string.not_distribution), "" + str));
                    mCartSettlement.setBackgroundResource(R.drawable.btn_grey);
                    mCartSettlement.setEnabled(false);
                } else {
                    mCartSettlement.setText(R.string.confirmation_of_the_order);
                    mCartSettlement.setBackgroundResource(R.drawable.btn_bg);
                    mCartSettlement.setEnabled(true);
                }
            }
        } else {
            settlement.setText(String.format(getString(R.string.can_not_order), "" + mPoidetailinfoBean.getMeituan().getData().getMin_price()));
            settlement.setBackgroundResource(R.drawable.btn_grey);
            settlement.setEnabled(false);
        }
    }

    private void showShopCartDialog() {
        mBottomDialog = new Dialog(this, R.style.DialogTheme);
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_shop_cart, null);
        mBottomDialog.setContentView(v);
        ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        v.setLayoutParams(layoutParams);
        mBottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomDialog.show();
        setDialogHeight(mBottomDialog);
        cardShopLayout = (LinearLayout) v.findViewById(R.id.cardShopLayout);
        mClearshopCart = (MultiplTextView) v.findViewById(R.id.tv_clear);
        shoppingListView = (ListView) v.findViewById(R.id.shopproductListView);
        mShopCartNum = (MultiplTextView) v.findViewById(R.id.shoppingNum);
        mCartDistributionFee = (MultiplTextView) v.findViewById(R.id.distribution_fee);
        mCartShoppingPrise = (MultiplTextView) v.findViewById(R.id.shoppingPrise);
        mCartClose = (MultiplTextView) v.findViewById(R.id.tv_close_cart);
        mCartDiscount = (MultiplTextView) v.findViewById(R.id.tv_discount);
        mCartSettlement = (Button) v.findViewById(R.id.settlement);
        mCartSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomDialog.dismiss();
                Intent intent = new Intent(FoodListActivity.this, SubmitOrderActivity.class);
                intent.putExtra(POI_INFO, mPoiInfoBean);
                intent.putExtra(PRODUCT_LIST_BEAN, (Serializable) productList);
                intent.putExtra(DISCOUNT, mDiscountNumber);
                startActivity(intent);
            }
        });
        mCartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomDialog.dismiss();
            }
        });
        shoppingListView.setAdapter(shoppingCartAdapter);
        shoppingCartAdapter.setShopToDetailListener(this);
        mClearshopCart.setOnClickListener(this);
        mCartDistributionFee.setText(String.format(getString(R.string.distribution_fee), "" + mPoidetailinfoBean.getMeituan().getData().getShipping_fee()));
        setPrise();
    }

    private void setDialogHeight(Dialog dialog) {
        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes();
        if (productList.size() > 4) p.height = (int) (display.getHeight() * 0.4);
        if (productList.size() <= 4) p.height = ListView.LayoutParams.WRAP_CONTENT;
        if (productList.size() == 0) dialog.dismiss();
        dialog.getWindow().setAttributes(p);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopping_cart:
                if (productList == null || productList.size() == 0) {
                    return;
                }
                showShopCartDialog();
                break;

            case R.id.settlement:
                if (productList == null || productList.size() == 0) {
//                    Toast.makeText(this, getString(R.string.please_add_to_the_shopping_cart_first), Toast.LENGTH_SHORT).show();
                    settlement.setEnabled(false);
                    return;
                }
                Intent intent = new Intent(this, SubmitOrderActivity.class);
                intent.putExtra(POI_INFO, mPoiInfoBean);
                intent.putExtra(PRODUCT_LIST_BEAN, (Serializable) productList);
                intent.putExtra(DISCOUNT, mDiscountNumber);
                startActivity(intent);
                break;

//            case R.id.bg_layout:
//                cardLayout.setVisibility(View.GONE);
//                bg_layout.setVisibility(View.GONE);
//                cardShopLayout.setVisibility(View.GONE);
//                break;

            case R.id.tv_clear:
                if (productList != null && productList.size() > 0) {
                    productList.clear();
                    defaultText.setVisibility(View.VISIBLE);
                    for (int i = 0; i < foodSpuTagsBeans.size(); i++) {
                        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spus = foodSpuTagsBeans.get(i).getSpus();
                        for (int j = 0; j < spus.size(); j++) {
                            spus.get(j).setNumber(0);
                        }
                    }
                    mPoifoodSpusListAdapter.notifyDataSetChanged();
                    shoppingCartAdapter.notifyDataSetChanged();
                    if (mBottomDialog != null && mBottomDialog.isShowing()) {
                        setDialogHeight(mBottomDialog);
                    }
                    setPrise();
                }
                if (mBottomDialog != null && mBottomDialog.isShowing()) {
                    mBottomDialog.dismiss();
                }
                break;
            case R.id.iv_finish:
                finish();
                break;
            case R.id.rl_store_details:
                View popView = getPopView(R.layout.dialog_shop_details);
                mDetailsNotice = (MultiplTextView) popView.findViewById(R.id.tv_notice);
                mDetailsShopName = (MultiplTextView) popView.findViewById(R.id.tv_shop_name);
                mDetailsDistribution = (MultiplTextView) popView.findViewById(R.id.tv_details_distribution);
                mDetailsDiscount = (MultiplTextView) popView.findViewById(R.id.tv_discount);

                mDetailsDiscount.setText(mPoidetailinfoBean.getMeituan().getData().getDiscounts().get(0).getInfo());
                mDetailsShopName.setText(mPoidetailinfoBean.getMeituan().getData().getName());
                mDetailsDistribution.setText(getString(R.string.distribution_situation, "" + mPoidetailinfoBean.getMeituan().getData().getMin_price(),
                        "" + mPoidetailinfoBean.getMeituan().getData().getShipping_fee(), "" + mPoidetailinfoBean.getMeituan().getData().getAvg_delivery_time()));
                mDetailsNotice.setText(getString(R.string.notice, mPoiInfoBean.getBulletin()));

                final PopupWindow window = new PopupWindow(popView,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT, true);
                showFoodListActivityDialog(v, popView, window);
                break;
        }
    }

    private FrameLayout createAnimLayout() {
        ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
        FrameLayout animLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;

    }

    private void doAnim(Drawable drawable, int[] start_location) {
        if (!isClean) {
            setAnim(drawable, start_location);
        } else {
            try {
                animation_viewGroup.removeAllViews();
                isClean = false;
                setAnim(drawable, start_location);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isClean = true;
            }
        }
    }

    @SuppressLint("NewApi")
    private void setAnim(Drawable drawable, int[] start_location) {
        Animation mScaleAnimation = new ScaleAnimation(1.2f, 0.6f, 1.2f, 0.6f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mScaleAnimation.setFillAfter(true);

        final ImageView iview = new ImageView(this);
        iview.setImageDrawable(drawable);
        final View view = addViewToAnimLayout(animation_viewGroup, iview,
                start_location);

        view.setAlpha(0.6f);

        int[] end_location = new int[2];
        settlement.getLocationInWindow(end_location);

        int endX = 0 - start_location[0] + 40;
        int endY = end_location[1] - start_location[1];
        TranslateAnimation translateAnimationX = new TranslateAnimation(0,
                endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0,
                0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);
        translateAnimationX.setFillAfter(true);


        Animation mRotateAnimation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.3f, Animation.RELATIVE_TO_SELF,
                0.3f);
        mRotateAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(mRotateAnimation);
        set.addAnimation(mScaleAnimation);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(500);
        view.startAnimation(set);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                number++;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                number--;
                if (number == 0) {
                    isClean = true;
                    myHandler.sendEmptyMessage(0);
                }
                ObjectAnimator.ofFloat(shopping_cart, "translationY", 0, 4, -2, 0).setDuration(400).start();
                ObjectAnimator.ofFloat(shoppingNum, "translationY", 0, 4, -2, 0).setDuration(400).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private View addViewToAnimLayout(ViewGroup vg, View view, int[] location) {
        int x = location[0];
        int y = location[1];
        vg.addView(view);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setPadding(5, 5, 5, 5);
        view.setLayoutParams(lp);
        return view;
    }

    @Override
    public void onLowMemory() {
        isClean = true;
        try {
            animation_viewGroup.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        isClean = false;
        super.onLowMemory();
    }

    @Override
    public void onPoifoodListSuccess(PoifoodListBean data) {
        foodSpuTagsBeans.clear();
        foodSpuTagsBeanName.clear();
        mPoiInfoBean = data.getMeituan().getData().getPoi_info();
        mShopTitle.setText(mPoiInfoBean.getName());
        mDelivery.setText(getString(R.string.distribution_situation, "" + mPoiInfoBean.getMin_price(),
                "" + mPoiInfoBean.getShipping_fee(), "" + mPoiInfoBean.getAvg_delivery_time()));
        mBulletin.setText(getString(R.string.notice, mPoiInfoBean.getBulletin()));
        GlideApp.with(this)
                .load(mPoiInfoBean.getPic_url())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(mShopPicture);
        List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean> food_spu_tags = data.getMeituan().getData().getFood_spu_tags();
        for (int i = 0; i < food_spu_tags.size(); i++) {
            PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean foodSpuTagsBean = food_spu_tags.get(i);
            String foodSpuTagsName = foodSpuTagsBean.getName();
            foodSpuTagsBeanName.add(foodSpuTagsName);
            Lg.getInstance().d(TAG, "foodSpuTagsBeanName = " + foodSpuTagsName);
            spusBeanList = new ArrayList<>();
            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> spus = foodSpuTagsBean.getSpus();
            for (int j = 0; j < spus.size(); j++) {
                PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean = spus.get(j);
                String spusName = spusBean.getName();
                spusBeanList.add(spusBean);
                Lg.getInstance().d(TAG, "spusBeanName = " + spusName);
            }
            Lg.getInstance().d(TAG, "spusBeanList = " + spusBeanList.size());
            foodSpuTagsBean.setSpus(spusBeanList);
            foodSpuTagsBeans.add(foodSpuTagsBean);
        }
        Lg.getInstance().d(TAG, "foodSpuTagsBeanName = " + foodSpuTagsBeanName.toString());
        Lg.getInstance().d(TAG, "spusBeanName = " + spusBeanList.toString());
        mPoifoodSpusListAdapter.notifyDataSetChanged();
        mFoodSpuTagsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPoifoodListError(String error) {
        Lg.getInstance().d(TAG, "error = " + error);
    }

    @Override
    public void onPoidetailinfoSuccess(PoidetailinfoBean data) {
        mPoidetailinfoBean = data;
        List<PoidetailinfoBean.MeituanBean.DataBean.DiscountsBean> discounts = mPoidetailinfoBean.getMeituan().getData().getDiscounts();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < data.getMeituan().getData().getDiscounts().size(); i++) {
            String info = data.getMeituan().getData().getDiscounts().get(i).getInfo();
            stringBuffer.append(info + "   ");
        }
        mDiscounts.setText(stringBuffer);
        settlement.setText(String.format(getString(R.string.can_not_order), "" + mPoidetailinfoBean.getMeituan().getData().getMin_price()));
        mDistributionFee.setText(String.format(getString(R.string.distribution_fee), "" + mPoidetailinfoBean.getMeituan().getData().getShipping_fee()));
        for (int i = 0; i < discounts.size(); i++) {
            String info = discounts.get(i).getInfo();
            if (info.contains(getString(R.string.full)) && info.contains(getString(R.string.reduce))) {
                if (info.startsWith(getString(R.string.full))) {
                    String[] split = info.split(";");
                    listFull = new ArrayList();
                    listReduce = new ArrayList();
                    Lg.getInstance().d(TAG, "split.length = " + split.length);
                    for (int j = 0; j < split.length; j++) {
                        String splitString = split[j];
                        int y = 0;
                        for (int k = 0; k < splitString.length(); k++) {
                            String s = splitString.charAt(k) + "";
                            if (s.equals(getString(R.string.reduce))) {
                                y = k;
                            }
                        }
                        String substring = splitString.substring(1, y);
                        Lg.getInstance().d(TAG, "substring = " + substring);
                        String lastString = splitString.substring(y + 1, splitString.length());
                        Lg.getInstance().d(TAG, "lastString = " + lastString);
                        listFull.add(Double.parseDouble(substring));
                        listReduce.add(Double.parseDouble(lastString));
                    }
                }
            }
        }
        Lg.getInstance().d(TAG, "onPoidetailinfoSuccess data = " + data);
    }

    @Override
    public void onPoidetailinfoError(String error) {
        Lg.getInstance().d(TAG, "onPoidetailinfoError = " + error);
    }
}
