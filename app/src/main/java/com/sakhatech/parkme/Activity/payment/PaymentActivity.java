package com.sakhatech.parkme.Activity.payment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.sakhatech.parkme.model.PaymentSummary;
import com.sakhatech.parkme.Activity.BaseActivity;
import com.sakhatech.spotizen.R;
import com.sakhatech.spotizen.databinding.ActivityPaymentBinding;


public class PaymentActivity extends BaseActivity implements View.OnClickListener{

    private ActivityPaymentBinding activityPaymentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getIntent().getExtras();
        PaymentSummary paymentSummary = null;
        if (bundle != null) {
            paymentSummary = bundle.getParcelable(IntentConstant.PAYMENT_SUMMARY);
        } else {
            paymentSummary = new PaymentSummary();
            paymentSummary.setmLocation("Testing\n test");
            paymentSummary.setmPrice(10);
            paymentSummary.setmOtherFee(5);
        }

        activityPaymentBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment);
        activityPaymentBinding.setPaymentSummary(paymentSummary);
        activityPaymentBinding.setClickHandler(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.close_payment_summary)
        {
            finish();
        }

    }
}
