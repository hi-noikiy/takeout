package com.baidu.iov.dueros.waimai.net.entity.response;

import java.util.List;

public class SearchSuggestResponse {

	private MeituanBean meituan;
	private IovBean iov;

	public MeituanBean getMeituan() {
		return meituan;
	}

	public void setMeituan(MeituanBean meituan) {
		this.meituan = meituan;
	}

	public IovBean getIov() {
		return iov;
	}

	public void setIov(IovBean iov) {
		this.iov = iov;
	}

	@Override
	public String toString() {
		return "SearchSuggestResponse{" +
				"meituan=" + meituan +
				", iov=" + iov +
				'}';
	}

	public static class MeituanBean {

		private int code;
		private String msg;
		private Object errorInfo;
		private DataBean data;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getErrorInfo() {
			return errorInfo;
		}

		public void setErrorInfo(Object errorInfo) {
			this.errorInfo = errorInfo;
		}

		public DataBean getData() {
			return data;
		}

		public void setData(DataBean data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return "MeituanBean{" +
					"code=" + code +
					", msg='" + msg + '\'' +
					", errorInfo=" + errorInfo +
					", data=" + data +
					'}';
		}

		public static class DataBean {
			private List<String> terms;
			private List<SuggestBean> suggest;

			public List<String> getTerms() {
				return terms;
			}

			public void setTerms(List<String> terms) {
				this.terms = terms;
			}

			public List<SuggestBean> getSuggest() {
				return suggest;
			}

			public void setSuggest(List<SuggestBean> suggest) {
				this.suggest = suggest;
			}

			@Override
			public String toString() {
				return "DataBean{" +
						"terms=" + terms +
						", suggest=" + suggest +
						'}';
			}

			public static class SuggestBean {

				private String suggest_query;
				private int type;
				private PoiAdditionInfoBean poi_addition_info;

				public String getSuggest_query() {
					return suggest_query;
				}

				public void setSuggest_query(String suggest_query) {
					this.suggest_query = suggest_query;
				}

				public int getType() {
					return type;
				}

				public void setType(int type) {
					this.type = type;
				}

				public PoiAdditionInfoBean getPoi_addition_info() {
					return poi_addition_info;
				}

				public void setPoi_addition_info(PoiAdditionInfoBean poi_addition_info) {
					this.poi_addition_info = poi_addition_info;
				}

				@Override
				public String toString() {
					return "SuggestBean{" +
							"suggest_query='" + suggest_query + '\'' +
							", type=" + type +
							", poi_addition_info=" + poi_addition_info +
							'}';
				}

				public static class PoiAdditionInfoBean {

					private long wm_poi_id;
					private int status;
					private String status_desc;
					private String name;
					private String pic_url;
					private double shipping_fee;
					private double min_price;
					private double wm_poi_score;
					private int avg_delivery_time;
					private String poi_type_icon;
					private String distance;
					private int latitude;
					private int longitude;
					private String address;
					private int month_sale_num;
					private int delivery_type;
					private int invoice_support;
					private int invoice_min_price;
					private String average_price_tip;
					private List<?> product_list;
					private List<?> discounts;
					private List<CategoryInfoListBean> categoryInfoList;

					public long getWm_poi_id() {
						return wm_poi_id;
					}

					public void setWm_poi_id(long wm_poi_id) {
						this.wm_poi_id = wm_poi_id;
					}

					public int getStatus() {
						return status;
					}

					public void setStatus(int status) {
						this.status = status;
					}

					public String getStatus_desc() {
						return status_desc;
					}

					public void setStatus_desc(String status_desc) {
						this.status_desc = status_desc;
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}

					public String getPic_url() {
						return pic_url;
					}

					public void setPic_url(String pic_url) {
						this.pic_url = pic_url;
					}

					public double getShipping_fee() {
						return shipping_fee;
					}

					public void setShipping_fee(double shipping_fee) {
						this.shipping_fee = shipping_fee;
					}

					public double getMin_price() {
						return min_price;
					}

					public void setMin_price(double min_price) {
						this.min_price = min_price;
					}

					public double getWm_poi_score() {
						return wm_poi_score;
					}

					public void setWm_poi_score(double wm_poi_score) {
						this.wm_poi_score = wm_poi_score;
					}

					public int getAvg_delivery_time() {
						return avg_delivery_time;
					}

					public void setAvg_delivery_time(int avg_delivery_time) {
						this.avg_delivery_time = avg_delivery_time;
					}

					public String getPoi_type_icon() {
						return poi_type_icon;
					}

					public void setPoi_type_icon(String poi_type_icon) {
						this.poi_type_icon = poi_type_icon;
					}

					public String getDistance() {
						return distance;
					}

					public void setDistance(String distance) {
						this.distance = distance;
					}

					public int getLatitude() {
						return latitude;
					}

					public void setLatitude(int latitude) {
						this.latitude = latitude;
					}

					public int getLongitude() {
						return longitude;
					}

					public void setLongitude(int longitude) {
						this.longitude = longitude;
					}

					public String getAddress() {
						return address;
					}

					public void setAddress(String address) {
						this.address = address;
					}

					public int getMonth_sale_num() {
						return month_sale_num;
					}

					public void setMonth_sale_num(int month_sale_num) {
						this.month_sale_num = month_sale_num;
					}

					public int getDelivery_type() {
						return delivery_type;
					}

					public void setDelivery_type(int delivery_type) {
						this.delivery_type = delivery_type;
					}

					public int getInvoice_support() {
						return invoice_support;
					}

					public void setInvoice_support(int invoice_support) {
						this.invoice_support = invoice_support;
					}

					public int getInvoice_min_price() {
						return invoice_min_price;
					}

					public void setInvoice_min_price(int invoice_min_price) {
						this.invoice_min_price = invoice_min_price;
					}

					public String getAverage_price_tip() {
						return average_price_tip;
					}

					public void setAverage_price_tip(String average_price_tip) {
						this.average_price_tip = average_price_tip;
					}

					public List<?> getProduct_list() {
						return product_list;
					}

					public void setProduct_list(List<?> product_list) {
						this.product_list = product_list;
					}

					public List<?> getDiscounts() {
						return discounts;
					}

					public void setDiscounts(List<?> discounts) {
						this.discounts = discounts;
					}

					public List<CategoryInfoListBean> getCategoryInfoList() {
						return categoryInfoList;
					}

					public void setCategoryInfoList(List<CategoryInfoListBean> categoryInfoList) {
						this.categoryInfoList = categoryInfoList;
					}

					@Override
					public String toString() {
						return "PoiAdditionInfoBean{" +
								"wm_poi_id=" + wm_poi_id +
								", status=" + status +
								", status_desc='" + status_desc + '\'' +
								", name='" + name + '\'' +
								", pic_url='" + pic_url + '\'' +
								", shipping_fee=" + shipping_fee +
								", min_price=" + min_price +
								", wm_poi_score=" + wm_poi_score +
								", avg_delivery_time=" + avg_delivery_time +
								", poi_type_icon='" + poi_type_icon + '\'' +
								", distance='" + distance + '\'' +
								", latitude=" + latitude +
								", longitude=" + longitude +
								", address='" + address + '\'' +
								", month_sale_num=" + month_sale_num +
								", delivery_type=" + delivery_type +
								", invoice_support=" + invoice_support +
								", invoice_min_price=" + invoice_min_price +
								", average_price_tip='" + average_price_tip + '\'' +
								", product_list=" + product_list +
								", discounts=" + discounts +
								", categoryInfoList=" + categoryInfoList +
								'}';
					}

					public static class CategoryInfoListBean {
						/**
						 * name : 甜品
						 * level : 2
						 */

						private String name;
						private int level;

						public String getName() {
							return name;
						}

						public void setName(String name) {
							this.name = name;
						}

						public int getLevel() {
							return level;
						}

						public void setLevel(int level) {
							this.level = level;
						}

						@Override
						public String toString() {
							return "CategoryInfoListBean{" +
									"name='" + name + '\'' +
									", level=" + level +
									'}';
						}
					}
				}
			}
		}
	}

	public static class IovBean {
	}
}
