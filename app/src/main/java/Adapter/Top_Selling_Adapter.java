package Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.HashMap;
import java.util.List;

import Config.BaseURL;
import Model.Top_Selling_model;
import pandora.ctg.R;
import pandora.ctg.MainActivity;
import util.DatabaseHandler;
import util.MyListener;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Top_Selling_Adapter extends RecyclerView.Adapter<Top_Selling_Adapter.MyViewHolder> {

    private List<Top_Selling_model> modelList;
    private Context context;

    private DatabaseHandler dbcart;
    private MyListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae, product_prize;
        public ImageView image;

        TextView itemCount;
        Button addItem;
        ImageButton btnMinus,btnPlus;

        public MyViewHolder(View view) {
            super(view);
            product_nmae = (TextView) view.findViewById(R.id.product_name);
            product_prize = (TextView) view.findViewById(R.id.product_prize);
            image = (ImageView) view.findViewById(R.id.iv_icon);


            itemCount = view.findViewById(R.id.tv_subcat_contetiy);
            addItem = view.findViewById(R.id.tv_subcat_add);
            btnMinus = view.findViewById(R.id.iv_subcat_minus);
            btnPlus = view.findViewById(R.id.iv_subcat_plus);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = Integer.valueOf(itemCount.getText().toString());
                    qty = qty + 1;

                    itemCount.setText(String.valueOf(qty));
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = 0;
                    if (!itemCount.getText().toString().equalsIgnoreCase(""))
                        qty = Integer.valueOf(itemCount.getText().toString());

                    if (qty > 0) {
                        qty = qty - 1;
                        itemCount.setText(String.valueOf(qty));
                    }
                }
            });

            addItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = view.getId();
                    int position = getAdapterPosition();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("product_id",String.valueOf(position));
                    map.put("product_name", modelList.get(position).getProduct_name());
                    map.put("category_id", String.valueOf(position));
                    //map.put("product_description", modelList.get(position).getProduct_description());
                    /*map.put("deal_price", modelList.get(position).getDeal_price());
                    map.put("start_date", modelList.get(position).getStart_date());
                    map.put("start_time", modelList.get(position).getStart_time());
                    map.put("end_date", modelList.get(position).getEnd_date());
                    map.put("end_time", modelList.get(position).getEnd_time());*/
                    map.put("price", modelList.get(position).getPrice());
                    map.put("product_image", modelList.get(position).getProduct_image());
                    map.put("unit_value", String.valueOf(position));
                    map.put("unit", String.valueOf(position));
                    map.put("status", String.valueOf(position));
                    map.put("in_stock", String.valueOf(position));


                    map.put("increament",String.valueOf(position));
                    map.put("rewards", modelList.get(position).getPrice());
                    map.put("stock", String.valueOf(position));
                    map.put("title", modelList.get(position).getProduct_name());


                    if (!itemCount.getText().toString().equalsIgnoreCase("0")) {
                        if (dbcart.isInCart(map.get("product_id"))) {
                            dbcart.setCart(map, Float.valueOf(itemCount.getText().toString()));
                            addItem.setText(context.getResources().getString(R.string.tv_pro_update));
                        } else {
                            dbcart.setCart(map, Float.valueOf(itemCount.getText().toString()));
                            addItem.setText(context.getResources().getString(R.string.tv_pro_update));
                        }
                    } else {
                        dbcart.removeItemFromCart(map.get("product_id"));
                        addItem.setText(context.getResources().getString(R.string.tv_pro_add));
                    }
                    ((MainActivity) context).setCartCounter("" + dbcart.getCartCount());
                }
            });

        }
    }

    public Top_Selling_Adapter(List<Top_Selling_model> modelList,Activity activity, MyListener listener) {
        this.modelList = modelList;

        dbcart = new DatabaseHandler(activity);
        this.listener = listener;
    }

    @Override
    public Top_Selling_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_top_selling, parent, false);
        context = parent.getContext();
        return new Top_Selling_Adapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(Top_Selling_Adapter.MyViewHolder holder, int position) {
        Top_Selling_model mList = modelList.get(position);
        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.product_nmae.setText(mList.getProduct_name());
        holder.product_prize.setText(" Price : " + mList.getPrice());

        final View v = holder.itemView;
        final int POSITION = position;
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMyClick(v,POSITION,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

