package fragment;

import android.Manifest;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.Product_adapter;
import Adapter.Deal_OfDay_Adapter;
import Adapter.Home_adapter;
import Adapter.Home_Icon_Adapter;
import Adapter.MyBannerOneAdapter;
import Adapter.Top_Selling_Adapter;
import Config.BaseURL;
import Model.Category_model;
import Model.Deal_Of_Day_model;
import Model.Home_Icon_model;
import Model.Product_model;
import Model.SubCategory;
import Model.Top_Selling_model;
import pandora.ctg.R;
import pandora.ctg.AppController;
import pandora.ctg.CustomSlider;
import pandora.ctg.MainActivity;
import util.ConnectivityReceiver;
import util.CustomVolleyJsonRequest;
import util.EqualSpacingItemDecoration;
import util.MyGridItemDecoration;
import util.MyListener;
import util.RecyclerTouchListener;
import Adapter.CategoriesViewAdapter;


public class Home_fragment extends Fragment implements MyListener {
    private static String TAG = Home_fragment.class.getSimpleName();
    private SliderLayout imgSlider, banner_slider, featuredslider;
    private ViewPager imageSlider,bannerSlider;
    private RecyclerView rv_items, rv_top_selling, rv_deal_of_day, rv_headre_icons;
    private List<Category_model> category_modelList = new ArrayList<>();
    private Home_adapter adapter;
    private boolean isSubcat = false;
    private RecyclerView categoriesView;
    private List<SubCategory> myCategories = null;
    LinearLayout Search_layout;
    String getid;
    String getcat_title;
    ScrollView scrollView;

    //Home Icons
    private Home_Icon_Adapter menu_adapter;
    private List<Home_Icon_model> menu_models = new ArrayList<>();


    //Deal O Day
    private Deal_OfDay_Adapter deal_ofDay_adapter;
    private List<Deal_Of_Day_model> deal_of_day_models = new ArrayList<>();
    LinearLayout Deal_Linear_layout;
    FrameLayout Deal_Frame_layout, Deal_Frame_layout1;


    //Top Selling Products
    private Top_Selling_Adapter top_selling_adapter;
    private List<Top_Selling_model> top_selling_models = new ArrayList<>();


    private ImageView iv_Call, iv_Whatspp, iv_reviews, iv_share_via;
    private TextView timer;
    Button View_all_deals, View_all_TopSell;

    private ImageView Top_Selling_Poster, Deal_Of_Day_poster;

    View view;

    public Home_fragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.app_name));
        ((MainActivity) getActivity()).updateHeader();
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    ((MainActivity) getActivity()).finish();
                    return true;
                }
                return false;
            }
        });


        timer = (TextView) view.findViewById(R.id.timer);
        View_all_deals = (Button) view.findViewById(R.id.view_all_deals);
        View_all_TopSell = (Button) view.findViewById(R.id.view_all_topselling);
        Deal_Frame_layout = (FrameLayout) view.findViewById(R.id.deal_frame_layout);
        Deal_Frame_layout1 = (FrameLayout) view.findViewById(R.id.deal_frame_layout1);
        Deal_Linear_layout = (LinearLayout) view.findViewById(R.id.deal_linear_layout);


        //Top Selling Poster
        Top_Selling_Poster = (ImageView) view.findViewById(R.id.top_selling_imageview);

        //Deal Of Day Poster
        Deal_Of_Day_poster = (ImageView) view.findViewById(R.id.deal_of_day_imageview);


        //Scroll View
        scrollView = (ScrollView) view.findViewById(R.id.scroll_view);
        scrollView.setSmoothScrollingEnabled(true);

        //Search
        Search_layout = (LinearLayout) view.findViewById(R.id.search_layout);
        Search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_fragment fm = new Search_fragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });
        //Slider
        imgSlider = (SliderLayout) view.findViewById(R.id.home_img_slider);
        imageSlider = view.findViewById(R.id.home_img_pager);
        imageSlider.setOffscreenPageLimit(2);
        banner_slider = (SliderLayout) view.findViewById(R.id.relative_banner);
        bannerSlider = view.findViewById(R.id.relative_banner_pager);
        featuredslider = (SliderLayout) view.findViewById(R.id.featured_img_slider);


        //Catogary Icons
        categoriesView = view.findViewById(R.id.categories_view);
        categoriesView.setLayoutManager(new GridLayoutManager(getContext(),2));
        categoriesView.addItemDecoration(new MyGridItemDecoration());

        rv_items = (RecyclerView) view.findViewById(R.id.rv_home);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rv_items.setLayoutManager(gridLayoutManager);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        rv_items.setNestedScrollingEnabled(false);

        //DealOf the Day
        rv_deal_of_day = (RecyclerView) view.findViewById(R.id.rv_deal);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 2);
        rv_deal_of_day.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rv_deal_of_day.setItemAnimator(new DefaultItemAnimator());
        rv_deal_of_day.setNestedScrollingEnabled(false);
        rv_deal_of_day.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.HORIZONTAL));


        //Top Selling Products
        rv_top_selling = (RecyclerView) view.findViewById(R.id.top_selling_recycler);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
        rv_top_selling.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        rv_top_selling.setItemAnimator(new DefaultItemAnimator());
        rv_top_selling.setNestedScrollingEnabled(false);
        rv_top_selling.addItemDecoration(new EqualSpacingItemDecoration(16,EqualSpacingItemDecoration.HORIZONTAL));


        //make_menu_items Icons
        rv_headre_icons = (RecyclerView) view.findViewById(R.id.collapsing_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()) {

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                    private static final float SPEED = 2000f;// Change this value (default=25f)

                    @Override
                    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                        return SPEED / displayMetrics.densityDpi;
                    }
                };
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_headre_icons.setLayoutManager(layoutManager);
        rv_headre_icons.setHasFixedSize(true);
        rv_headre_icons.setItemViewCacheSize(10);
        rv_headre_icons.setDrawingCacheEnabled(true);
        rv_headre_icons.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);


        //Call And Whatsapp
        iv_Call = view.findViewById(R.id.iv_call);
        iv_Whatspp = view.findViewById(R.id.iv_whatsapp);
        iv_reviews =  view.findViewById(R.id.reviews);
        iv_share_via =  view.findViewById(R.id.share_via);

        iv_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + "0469811986"));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                getActivity().startActivity(callIntent);

            }
        });
        iv_Whatspp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String smsNumber = "+610469811986";
                Uri uri = Uri.parse("smsto:" + smsNumber);
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.putExtra("Test", "Tanvir");
                i.setPackage("com.whatsapp");
                startActivity(i);

            }
        });
        iv_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewOnApp();
            }
        });
        iv_share_via.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();

            }
        });


        //Recycler View Shop By Catogary
        rv_items.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_items, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = category_modelList.get(position).getId();
                getcat_title = category_modelList.get(position).getTitle();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        categoriesView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), categoriesView, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = myCategories.get(position).getId();
                getcat_title = myCategories.get(position).getTitle();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                args.putString("cat_title", getcat_title);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        //Recycler View Menu Products
        rv_headre_icons.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_headre_icons, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = menu_models.get(position).getId();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_id", getid);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        //Recycler View Deal Of Day
        /*rv_deal_of_day.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_deal_of_day, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = deal_of_day_models.get(position).getId();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_deal", "2");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
        View_all_deals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_deal", "2");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });


        //REcyclerview Top Selling
        /*rv_top_selling.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), rv_top_selling, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getid = top_selling_models.get(position).getProduct_id();
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_top_selling", "2");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));*/
        View_all_TopSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Fragment fm = new Product_fragment();
                args.putString("cat_top_selling", "2");
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                        .addToBackStack(null).commit();

            }
        });

        //Check Internet Connection
        if (ConnectivityReceiver.isConnected()) {
            getCategories();
            makeGetSliderRequest();
            makeGetBannerSliderRequest();
            makeGetCategoryRequest();
            makeGetFeaturedSlider();
            make_menu_items();
            //make_deal_od_the_day();
            //make_top_selling();

            //Deals of the day
            makedealIconProductRequest("2");
            //Top Selling Products
            maketopsaleProductRequest("2");
        }


        return view;
    }


    private void makeGetSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            List<String> images = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                                images.add(BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                            }
                            MyBannerOneAdapter bannerAdapter = new MyBannerOneAdapter(getChildFragmentManager(),images);
                            imageSlider.setAdapter(bannerAdapter);
                            imageSlider.setPageMargin(20);
                            imageSlider.setCurrentItem(1);
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                imgSlider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();
                                        Fragment fm = new Product_fragment();
                                        args.putString("id", sub_cat);
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }

    private void makeGetBannerSliderRequest() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_BANNER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            List<String> images = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                                images.add(BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                banner_slider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();
                                        Fragment fm = new Product_fragment();
                                        args.putString("id", sub_cat);
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }


    private void makeGetFeaturedSlider() {
        JsonArrayRequest req = new JsonArrayRequest(BaseURL.GET_FEAATURED_SLIDER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            ArrayList<HashMap<String, String>> listarray = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                HashMap<String, String> url_maps = new HashMap<String, String>();
                                url_maps.put("slider_title", jsonObject.getString("slider_title"));
                                url_maps.put("sub_cat", jsonObject.getString("sub_cat"));
                                url_maps.put("slider_image", BaseURL.IMG_SLIDER_URL + jsonObject.getString("slider_image"));
                                listarray.add(url_maps);
                            }
                            for (HashMap<String, String> name : listarray) {
                                CustomSlider textSliderView = new CustomSlider(getActivity());
                                textSliderView.description(name.get("")).image(name.get("slider_image")).setScaleType(BaseSliderView.ScaleType.Fit);
                                textSliderView.bundle(new Bundle());
                                //  textSliderView.getBundle().putString("extra", name.get("slider_title"));
                                textSliderView.getBundle().putString("extra", name.get("sub_cat"));
                                featuredslider.addSlider(textSliderView);
                                final String sub_cat = (String) textSliderView.getBundle().get("extra");
                                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                    @Override
                                    public void onSliderClick(BaseSliderView slider) {
                                        //   Toast.makeText(getActivity(), "" + sub_cat, Toast.LENGTH_SHORT).show();
                                        Bundle args = new Bundle();
                                        Fragment fm = new Product_fragment();
                                        args.putString("id", sub_cat);
                                        fm.setArguments(args);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                                                .addToBackStack(null).commit();
                                    }
                                });

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(req);

    }


    private void makeGetCategoryRequest() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Category_model>>() {
                            }.getType();
                            category_modelList = gson.fromJson(response.getString("data"), listType);
                            adapter = new Home_adapter(category_modelList);
                            rv_items.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }



    private void make_deal_od_the_day() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.GET_DEAL_OF_DAY_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        Gson gson = new Gson();
                        if (status) {
                            Type listType = new TypeToken<List<Deal_Of_Day_model>>() {
                            }.getType();
                            deal_of_day_models = gson.fromJson(response.getString("Deal_of_the_day"), listType);
                            deal_ofDay_adapter = new Deal_OfDay_Adapter(deal_of_day_models, getActivity(),Home_fragment.this);
                            rv_deal_of_day.setAdapter(deal_ofDay_adapter);
                            deal_ofDay_adapter.notifyDataSetChanged();
                            if (getActivity() != null) {
                                if (deal_of_day_models.isEmpty()) {
                                    //  Toast.makeText(getActivity(), "No Deal For Day", Toast.LENGTH_SHORT).show();
                                    rv_deal_of_day.setVisibility(View.GONE);
                                    Deal_Frame_layout.setVisibility(View.GONE);
                                    Deal_Frame_layout1.setVisibility(View.GONE);
                                    Deal_Linear_layout.setVisibility(View.GONE);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Response", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void make_top_selling() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Top_Selling_model>>() {
                            }.getType();
                            top_selling_models = gson.fromJson(response.getString("top_selling_product"), listType);
                            top_selling_adapter = new Top_Selling_Adapter(top_selling_models,getActivity(),Home_fragment.this);
                            //rv_top_selling.setAdapter(top_selling_adapter);
                            top_selling_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }


    private void make_menu_items() {
        String tag_json_obj = "json_category_req";
        isSubcat = false;
        Map<String, String> params = new HashMap<String, String>();
        params.put("parent", "");
        isSubcat = true;
       /* if (parent_id != null && parent_id != "") {
        }*/

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_CATEGORY_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    if (response != null && response.length() > 0) {
                        Boolean status = response.getBoolean("responce");
                        if (status) {
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<Home_Icon_model>>() {
                            }.getType();
                            menu_models = gson.fromJson(response.getString("data"), listType);
                            menu_adapter = new Home_Icon_Adapter(menu_models);
                            rv_headre_icons.setAdapter(menu_adapter);
                            menu_adapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    @Override
    public void onMyClick(View v, int position, int type) {
        if (type == 1) {
            getid = deal_of_day_models.get(position).getId();
            Bundle args = new Bundle();
            Fragment fm = new Product_fragment();
            args.putString("cat_deal", "2");
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        } else {
            getid = top_selling_models.get(position).getProduct_id();
            Bundle args = new Bundle();
            Fragment fm = new Product_fragment();
            args.putString("cat_top_selling", "2");
            fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
                    .addToBackStack(null).commit();
        }

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public void reviewOnApp() {
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }

    public void shareApp() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + getActivity().getPackageName() + " APP");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    //ToDo: Working below

    private List<Product_model> product_modelListDOD = new ArrayList<>();
    private Adapter.Product_adapter adapter_productDOD;

    private List<Product_model> product_modelListTSP = new ArrayList<>();
    private Adapter.Product_adapter adapter_productTSP;

    //Deals of the day
    private void makedealIconProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("dealproduct", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_DEAL_OF_DAY_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TESTING", response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelListDOD = gson.fromJson(response.getString("Deal_of_the_day"), listType);
                        adapter_productDOD = new Product_adapter(product_modelListDOD, getActivity(),"dod");
                        rv_deal_of_day.setAdapter(adapter_productDOD);
                        adapter_productDOD.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelListDOD.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    //Get Top Sale Products
    private void maketopsaleProductRequest(String cat_id) {
        String tag_json_obj = "json_product_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("top_selling_product", cat_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.GET_ALL_TOP_SELLING_PRODUCTS, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Product_model>>() {
                        }.getType();
                        product_modelListTSP = gson.fromJson(response.getString("top_selling_product"), listType);
                        adapter_productTSP = new Product_adapter(product_modelListTSP, getActivity(),"tsp");
                        rv_top_selling.setAdapter(adapter_productTSP);
                        adapter_productTSP.notifyDataSetChanged();
                        if (getActivity() != null) {
                            if (product_modelListTSP.isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void getCategories() {
        StringRequest request = new StringRequest(Request.Method.GET, BaseURL.SUB_CATEGORIES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        myCategories = Arrays.asList(new Gson().fromJson(new JSONObject(response).getJSONArray("data").toString(), SubCategory[].class));
                        CategoriesViewAdapter adapter = new CategoriesViewAdapter(getContext(),myCategories);
                        categoriesView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Error: ",error);
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    /*private void prepareCategories() {
        List<MyCategory> myCategories = new ArrayList<>();
        MyCategory category1 = new MyCategory();
        category1.setCategoryName("Ice Cream");
        category1.setPreview(R.drawable.i1);
        MyCategory category2 = new MyCategory();
        category2.setCategoryName("Household Needs");
        category2.setPreview(R.drawable.i2);
        MyCategory category3 = new MyCategory();
        category3.setCategoryName("Home Furnishing");
        category3.setPreview(R.drawable.i3);
        MyCategory category4 = new MyCategory();
        category4.setCategoryName("Beverages");
        category4.setPreview(R.drawable.i4);
        MyCategory category5 = new MyCategory();
        category5.setCategoryName("Breakfast Dairy");
        category5.setPreview(R.drawable.i5);
        MyCategory category6 = new MyCategory();
        category6.setCategoryName("Coffee");
        category6.setPreview(R.drawable.i6);
        myCategories.add(category1);
        myCategories.add(category2);
        myCategories.add(category3);
        myCategories.add(category4);
        myCategories.add(category5);
        myCategories.add(category6);
        CategoriesViewAdapter adapter = new CategoriesViewAdapter(getContext(),myCategories);
        categoriesView.setAdapter(adapter);
    }*/

}
