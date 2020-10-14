package Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
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
import Model.Deal_Of_Day_model;
import pandora.ctg.R;
import pandora.ctg.MainActivity;
import util.DatabaseHandler;
import util.MyListener;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class Deal_OfDay_Adapter extends RecyclerView.Adapter<Deal_OfDay_Adapter.MyViewHolder> {

    private List<Deal_Of_Day_model> modelList;
    private Context context;
    private DatabaseHandler dbcart;
    private MyListener listener;
    public int counter;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_nmae, product_prize, offer_product_prize, start_time, end_time,offer_textview;
        public ImageView image;

        TextView itemCount;
        Button addItem;
        ImageButton btnMinus,btnPlus;

        public MyViewHolder(View view) {
            super(view);
            product_nmae = (TextView) view.findViewById(R.id.product_name);
            product_prize = (TextView) view.findViewById(R.id.product_prize);
            offer_product_prize = (TextView) view.findViewById(R.id.offer_product_prize);
            start_time = (TextView) view.findViewById(R.id.start_time);
            end_time = (TextView) view.findViewById(R.id.end_time);
            offer_textview = (TextView) view.findViewById(R.id.ofer_textview);

            product_prize.setPaintFlags(product_prize.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
                    map.put("deal_price", modelList.get(position).getDeal_price());
                    map.put("start_date", modelList.get(position).getStart_date());
                    map.put("start_time", modelList.get(position).getStart_time());
                    map.put("end_date", modelList.get(position).getEnd_date());
                    map.put("end_time", modelList.get(position).getEnd_time());
                    map.put("price", modelList.get(position).getPrice());
                    map.put("product_image", modelList.get(position).getProduct_image());
                    map.put("status", modelList.get(position).getStatus());
                    map.put("in_stock", modelList.get(position).getIn_stock());
                    map.put("unit_value", modelList.get(position).getUnit_value());
                    map.put("unit", modelList.get(position).getUnit());
                    map.put("increament", modelList.get(position).getIncreament());
                    map.put("rewards", modelList.get(position).getRewards());
                    map.put("stock", String.valueOf(position));
                    map.put("title", modelList.get(position).getTitle());


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

    public Deal_OfDay_Adapter(List<Deal_Of_Day_model> modelList, Activity activity, MyListener listener) {
        this.modelList = modelList;
        dbcart = new DatabaseHandler(activity);
        this.listener = listener;
    }

    @Override
    public Deal_OfDay_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_deal_of_the_day, parent, false);

        context = parent.getContext();

        return new Deal_OfDay_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Deal_OfDay_Adapter.MyViewHolder holder, int position) {
        Deal_Of_Day_model mList = modelList.get(position);
        if (mList.getStatus().equals("1")) {
            holder.offer_product_prize.setText(context.getResources().getString(R.string.currency) + mList.getDeal_price());
            holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.green));
            holder.offer_textview.setTextColor(context.getResources().getColor(R.color.green));
            holder.end_time.setText(mList.getEnd_time());
            holder.end_time.setTextColor(context.getResources().getColor(R.color.green));
        } else if (mList.getStatus().equals("0")) {
            holder.offer_product_prize.setText("Expired");
            holder.offer_product_prize.setTextColor(context.getResources().getColor(R.color.color_3));
            holder.offer_textview.setTextColor(context.getResources().getColor(R.color.color_3));
            holder.end_time.setText("Expired");
            holder.end_time.setTextColor(context.getResources().getColor(R.color.color_3));
        }

        Glide.with(context)
                .load(BaseURL.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);
        holder.product_prize.setText("Price: ৳" + mList.getPrice());
        holder.product_nmae.setText(mList.getProduct_name());
        holder.start_time.setText(mList.getStart_time());
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

