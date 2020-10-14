package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Config.BaseURL;
import Model.SubCategory;
import pandora.ctg.R;

public class CategoriesViewAdapter extends RecyclerView.Adapter<CategoriesViewAdapter.MyCategoryViewHolder> {
    private Context context;
    private List<SubCategory> categories;
    public CategoriesViewAdapter(Context context, List<SubCategory> categories) {
        this.context = context;
        this.categories = categories;
    }
    @Override
    public MyCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_my_category,parent,false);
        return new MyCategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyCategoryViewHolder holder, int position) {
        //Log.d("TESTING",BaseURL.IMG_PRODUCT_URL+categories.get(position).getImage2());
        holder.categoryTitle.setText(categories.get(position).getTitle());
        Picasso.with(context).load(BaseURL.IMG_CATEGORY_URL+categories.get(position).getImage2()).into(holder.categoryPreview);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class MyCategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryPreview;
        TextView categoryTitle;
        public MyCategoryViewHolder(View itemView) {
            super(itemView);
            categoryPreview = itemView.findViewById(R.id.category_preview);
            categoryTitle = itemView.findViewById(R.id.category_title);
        }
    }
}
