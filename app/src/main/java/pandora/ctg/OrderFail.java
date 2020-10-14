package pandora.ctg;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import fragment.Payment_fragment;
import pandora.ctg.R;

public class OrderFail extends AppCompatActivity {
    RelativeLayout oredr_fail;
    private Payment_fragment mItemsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_fail);
        mItemsFragment = new Payment_fragment();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Order Failed");
        oredr_fail = (RelativeLayout) findViewById(R.id.retry_order);
        oredr_fail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(OrderFail.this, MainActivity.class);
              startActivity(intent);
              finish();
            }
        });


    }


}
