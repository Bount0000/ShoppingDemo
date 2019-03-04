package com.linkai.shoppingdemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linkai.shoppingdemo.adapter.ShoppingCarAdapter;
import com.linkai.shoppingdemo.bean.ShoppingCarDataBean;
import com.linkai.shoppingdemo.customview.RoundCornerDialog;
import com.linkai.shoppingdemo.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_titlebar_left)
    TextView tvTitlebarLeft;
    @BindView(R.id.tv_titlebar_center)
    TextView tvTitlebarCenter;
    @BindView(R.id.tv_titlebar_right)
    TextView tvTitlebarRight;
    @BindView(R.id.elv_shopping_car)
    ExpandableListView elvShoppingCar;
    @BindView(R.id.iv_select_all)
    ImageView ivSelectAll;
    @BindView(R.id.ll_select_all)
    LinearLayout llSelectAll;
    @BindView(R.id.btn_order)
    Button btnOrder;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.rl_total_price)
    RelativeLayout rlTotalPrice;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.iv_no_contant)
    ImageView ivNoContant;
    @BindView(R.id.rl_no_contant)
    RelativeLayout rlNoContant;
    //模拟的购物车数据（实际开发中使用后台返回的数据）
    private String shoppingCarData = "{\n" +
            "    \"code\": 200,\n" +
            "    \"datas\": [\n" +
            "        {\n" +
            "            \"goods\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"111111\",\n" +
            "                    \"goods_image\": \"http://pic.58pic.com/58pic/15/62/69/34K58PICbmZ_1024.jpg\",\n" +
            "                    \"goods_name\": \"习近平谈治国理政(第二卷)(平装)\",\n" +
            "                    \"goods_num\": \"1\",\n" +
            "                    \"goods_price\": \"18.00\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"store_id\": \"1\",\n" +
            "            \"store_name\": \"一号小书店\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"goods\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"222221\",\n" +
            "                    \"goods_image\": \"http://file06.16sucai.com/2016/0511/9711205e4c003182edeed83355e6f1c7.jpg\",\n" +
            "                    \"goods_name\": \"马克思传\",\n" +
            "                    \"goods_num\": \"2\",\n" +
            "                    \"goods_price\": \"28.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"222222\",\n" +
            "                    \"goods_image\": \"http://img01.taopic.com/150424/240473-1504240U13615.jpg\",\n" +
            "                    \"goods_name\": \"我和霍金的生活\",\n" +
            "                    \"goods_num\": \"2\",\n" +
            "                    \"goods_price\": \"228.00\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"store_id\": \"2\",\n" +
            "            \"store_name\": \"二号中书店\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"goods\": [\n" +
            "                {\n" +
            "                    \"goods_id\": \"333331\",\n" +
            "                    \"goods_image\": \"http://pic22.nipic.com/20120718/8002769_100147127333_2.jpg\",\n" +
            "                    \"goods_name\": \"心的重建\",\n" +
            "                    \"goods_num\": \"3\",\n" +
            "                    \"goods_price\": \"38.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"333332\",\n" +
            "                    \"goods_image\": \"http://pic.58pic.com/58pic/14/71/50/40e58PICy54_1024.jpg\",\n" +
            "                    \"goods_name\": \"福尔摩斯\",\n" +
            "                    \"goods_num\": \"3\",\n" +
            "                    \"goods_price\": \"338.00\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"goods_id\": \"333333\",\n" +
            "                    \"goods_image\": \"http://img01.taopic.com/150518/318750-15051PS40671.jpg\",\n" +
            "                    \"goods_name\": \"书法常识\",\n" +
            "                    \"goods_num\": \"3\",\n" +
            "                    \"goods_price\": \"3.80\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"store_id\": \"3\",\n" +
            "            \"store_name\": \"三号大书店\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private ShoppingCarAdapter shoppingCarAdapter;
    private List<ShoppingCarDataBean.DatasBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initExpandableListView();
        initData();

    }

    @OnClick({R.id.tv_titlebar_left, R.id.tv_titlebar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titlebar_left://刷新数据
                initData();
                break;
            case R.id.tv_titlebar_right://编辑
                String edit = tvTitlebarRight.getText().toString().trim();
                if (edit.equals("编辑")) {
                    tvTitlebarRight.setText("完成");
                    rlTotalPrice.setVisibility(View.GONE);
                    btnOrder.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.VISIBLE);
                } else {
                    tvTitlebarRight.setText("编辑");
                    rlTotalPrice.setVisibility(View.VISIBLE);
                    btnOrder.setVisibility(View.VISIBLE);
                    btnDelete.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 初始化数据
     */
    private void initData() {


        //使用Gson解析购物车数据，
        //ShoppingCarDataBean为bean类，Gson按照bean类的格式解析数据
        /**
         * 实际开发中，通过请求后台接口获取购物车数据并解析
         */
        Gson gson = new Gson();
        ShoppingCarDataBean shoppingCarDataBean = gson.fromJson(shoppingCarData, ShoppingCarDataBean.class);
        datas = shoppingCarDataBean.getDatas();
        initExpandableListViewData(datas);

    }

    private void initExpandableListView() {
        shoppingCarAdapter = new ShoppingCarAdapter(this, llSelectAll, ivSelectAll, btnOrder, btnDelete, rlTotalPrice, tvTotalPrice);
        elvShoppingCar.setAdapter(shoppingCarAdapter);
        //删除按钮操作
        shoppingCarAdapter.setOnDeleteListener(new ShoppingCarAdapter.OnDeleteListener() {
            @Override
            public void onDelete() {
                initDelete();

            }

        });

        shoppingCarAdapter.setOnChangeCountListener(new ShoppingCarAdapter.OnChangeCountListener() {
            @Override
            public void onChangeCount(String goods_id) {
                /**
                 * 实际开发中，在此请求修改商品数量的接口，商品数量修改成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 */
            }
        });

    }

    /**
     * 判断是否要弹出删除的dialog
     * 通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
     * GoodsBean的isSelect属性，判断商品是否被选中，
     */
    private void initDelete() {
        //判断是否有店铺或商品被选中
        //true为有，则需要刷新数据；反之，则不需要；
        boolean hasSelect = false;
        //创建临时的List，用于存储没有被选中的购物车数据
        List<ShoppingCarDataBean.DatasBean> datasTemp = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            List<ShoppingCarDataBean.DatasBean.GoodsBean> goods = datas.get(i).getGoods();
            boolean isSelect_shop = datas.get(i).getIsSelect_shop();
            if (isSelect_shop) {
                hasSelect = true;//若店铺被选中则结束循环
                continue;
            } else {
                datasTemp.add(datas.get(i));
                datasTemp.get(datasTemp.size() - 1).setGoods(new ArrayList<ShoppingCarDataBean.DatasBean.GoodsBean>());//将未选中的商品装进空集合中

            }
            for (int y = 0; y < goods.size(); y++) {
                ShoppingCarDataBean.DatasBean.GoodsBean goodsBean = goods.get(y);
                boolean isSelect = goodsBean.getIsSelect();
                if (isSelect) {
                    hasSelect = true;
                } else {
                    datasTemp.get(datasTemp.size() - 1).getGoods().add(goods.get(y));
                }

            }

        }
        if (hasSelect) {
            showDeleteDialog(datasTemp);
        } else {
            ToastUtil.makeText(this, "请选择删除的商品");
        }
    }


    /**
     * 展示删除的dialog（可以自定义弹窗，不用删除即可）
     *
     * @param datasTemp
     */
    private void showDeleteDialog(final List<ShoppingCarDataBean.DatasBean> datasTemp) {
        View view = View.inflate(this, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(this, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message = (TextView) view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm = (TextView) view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel = (TextView) view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("确定要删除商品吗？");

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas = datasTemp;
                initExpandableListViewData(datas);
                roundCornerDialog.dismiss();
            }
        });
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
            }
        });
    }

    private void initExpandableListViewData(List<ShoppingCarDataBean.DatasBean> datas) {
        if (datas != null && datas.size() > 0) {
            //刷新数据时，保持当前位置
            shoppingCarAdapter.setData(datas);
            //使所有组展开
            for (int i = 0; i < shoppingCarAdapter.getGroupCount(); i++) {
                elvShoppingCar.expandGroup(i);
            }
            //使组点击无效果
            elvShoppingCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });

            tvTitlebarRight.setVisibility(View.VISIBLE);
            tvTitlebarRight.setText("编辑");
            rlNoContant.setVisibility(View.GONE);
            elvShoppingCar.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            rlTotalPrice.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        } else {
            tvTitlebarRight.setVisibility(View.GONE);
            rlNoContant.setVisibility(View.VISIBLE);
            elvShoppingCar.setVisibility(View.GONE);
            rl.setVisibility(View.GONE);
        }
    }


    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };
}
