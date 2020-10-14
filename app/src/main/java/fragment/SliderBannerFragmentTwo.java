package fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import pandora.ctg.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SliderBannerFragmentTwo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SliderBannerFragmentTwo extends Fragment {
    private static final String ARG_PARAM = "param";

    private String mParam;
    private ImageView imageView;
    private Context context;


    public SliderBannerFragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static SliderBannerFragmentTwo newInstance(String param) {
        SliderBannerFragmentTwo fragment = new SliderBannerFragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_slider_banner, container, false);
        imageView = v.findViewById(R.id.image_view);
        Picasso.with(context).load(mParam).into(imageView);
        return v;
    }

}
