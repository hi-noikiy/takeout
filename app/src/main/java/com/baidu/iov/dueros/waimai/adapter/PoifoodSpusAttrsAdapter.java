package com.baidu.iov.dueros.waimai.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.iov.dueros.waimai.R;
import com.baidu.iov.dueros.waimai.net.entity.response.PoifoodListBean;
import com.baidu.iov.dueros.waimai.view.GoodsViewGroup;

import java.util.List;

/**
 * Created by ubuntu on 18-11-3.
 */

public class PoifoodSpusAttrsAdapter extends BaseAdapter {
    private final Context context;
    private final LayoutInflater mInflater;
    private final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrsBeans;
    private final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans;
    private final PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs;
    private SetPriceListener setPriceListener;
    private List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList;

    public PoifoodSpusAttrsAdapter(Context context, List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean> attrsBeans,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> skusBeans,
                                   PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean spusBean,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.SkusBean> choiceSkus,
                                   List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean> productList) {
        this.context = context;
        this.attrsBeans = attrsBeans;
        this.skusBeans = skusBeans;
        this.spusBean = spusBean;
        this.choiceAttrs = choiceAttrs;
        this.choiceSkus = choiceSkus;
        this.productList = productList;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (attrsBeans.size() > 0) {
            if (skusBeans.size() > 1) {
                return attrsBeans.size() + 1;
            } else {
                return attrsBeans.size();
            }
        } else {
            if (skusBeans.size() > 1) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spus_attrs, null);
            viewHolder = new ViewHolder();
            viewHolder.attrsName = (TextView) convertView.findViewById(R.id.tv_attrs_name);
            viewHolder.radioGroup = (GoodsViewGroup) convertView.findViewById(R.id.rg_group);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.radioGroup.removeAllViews();

        if (attrsBeans.size() > 0 && position != attrsBeans.size()) {
            viewHolder.attrsName.setText(attrsBeans.get(position).getName());
            final List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> values = attrsBeans.get(position).getValues();
            for (int i = 0; i < values.size(); i++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(values.get(i).getValue());
                radioButton.setId((int) values.get(i).getId());
                viewHolder.radioGroup.addView(radioButton);
            }
            viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int id) {
                    for (int k = 0; k < values.size(); k++) {
                        if (id == values.get(k).getId()) {
                            choiceAttrs.clear();
                            choiceAttrs.add(values.get(k));
                            spusBean.getAttrs().get(position).setChoiceAttrs(choiceAttrs);
                        }
                    }
                    inProductList();
                }
            });
        } else {
            if (skusBeans.size() > 1) {
                viewHolder.attrsName.setText(context.getString(R.string.specifications));
                for (int i = 0; i < skusBeans.size(); i++) {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setText(skusBeans.get(i).getSpec());
                    radioButton.setId(skusBeans.get(i).getId());
                    viewHolder.radioGroup.addView(radioButton);
                }
                viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int id) {
                        for (int i = 0; i < skusBeans.size(); i++) {
                            if (id == skusBeans.get(i).getId()) {
                                choiceSkus.clear();
                                choiceSkus.add(skusBeans.get(i));
                                spusBean.setChoiceSkus(choiceSkus);
                                double price = skusBeans.get(i).getPrice();
                                if (setPriceListener != null) {
                                    setPriceListener.setPrice("" + price);
                                }
                            }
                        }
                        inProductList();
                    }
                });
            }
        }
        return convertView;
    }

    private void inProductList() {
        if (productList.contains(spusBean)) {
            for (PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean shopProduct : productList) {
                if (spusBean.getId() == shopProduct.getId()) {
                    if (shopProduct.getAttrs() != null && shopProduct.getAttrs().size() > 0) {
                        for (int i = 0; i < shopProduct.getAttrs().size(); i++) {
                            List<PoifoodListBean.MeituanBean.DataBean.FoodSpuTagsBean.SpusBean.AttrsBean.ValuesBean> choiceAttrs = shopProduct.getAttrs().get(i).getChoiceAttrs();
                            long id = choiceAttrs.get(0).getId();
                            if (id == spusBean.getAttrs().get(i).getChoiceAttrs().get(0).getId()) {
                                if (setPriceListener != null) {
                                    setPriceListener.setNumber(shopProduct.getNumber());
                                }
                            }
                        }
                    } else {
                        if (shopProduct.getSkus() != null && shopProduct.getSkus().size() > 1) {
                            for (int i = 0; i < shopProduct.getSkus().size(); i++) {
                                int id = shopProduct.getChoiceSkus().get(0).getId();
                                if (id == spusBean.getSkus().get(i).getId()) {
                                    if (setPriceListener != null) {
                                        setPriceListener.setNumber(shopProduct.getNumber());
                                    }
                                }
                            }

                        }
                    }
                }
            }
        } else {
            if (setPriceListener != null) {
                setPriceListener.setNumber(0);
            }
        }
    }

    class ViewHolder {
        public TextView attrsName;
        public GoodsViewGroup radioGroup;
    }

    public void setPriceListener(SetPriceListener setPriceListener) {
        this.setPriceListener = setPriceListener;
    }

    public interface SetPriceListener {
        void setPrice(String price);

        void setNumber(int number);
    }
}
