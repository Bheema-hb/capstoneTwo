package com.sakhatech.parkme.Activity.signup;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sakhatech.parkme.Activity.BaseActivity;
import com.sakhatech.parkme.util.ParkMeSharedPreference;
import com.sakhatech.parkme.util.Utility;
import com.sakhatech.parkme.Activity.signup.crop.GOTOConstants;
import com.sakhatech.parkme.Activity.signup.crop.ImageCropActivity;
import com.sakhatech.parkme.Activity.signup.crop.PicModeSelectDialogFragment;
import com.sakhatech.spotizen.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Bheema.
 * Company Techjini
 */
public class SignUpScreen extends BaseActivity implements PicModeSelectDialogFragment.IPicModeSelectListener {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int REQUEST_CODE_UPDATE_PIC = 0x1;
    EditText mUserName, mPassword, mConfirmPassword, mMobile, mVehicle;
    AutoCompleteTextView mEmail;
    TextView mTermsConditions;
    Button mSignUp;
    String mFbToken;
    RelativeLayout mLoadingLayout;
    TextView mLoadingText;
    CircleImageView mProfileImage;
    private Bitmap myBitmap;
    private String encodedImage;
    HashMap<String, String> params = new HashMap<>();
    RelativeLayout mSignUpButtonLayout;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
        mFbToken = getIntent().getStringExtra("FB_TOKEN");
        mEmail = (AutoCompleteTextView) findViewById(R.id.user_email);
        mUserName = (EditText) findViewById(R.id.user_name);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        mMobile = (EditText) findViewById(R.id.mobile);
        mVehicle = (EditText) findViewById(R.id.vehicle);
        mTermsConditions = (TextView) findViewById(R.id.terms_and_conditions);
        mSignUp = (Button) findViewById(R.id.sign_up);
        mLoadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);
        mLoadingText = (TextView) findViewById(R.id.loading_text);
        mProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        mSignUpButtonLayout = (RelativeLayout) findViewById(R.id.sign_up_button_layout);
        mScrollView = (ScrollView) findViewById(R.id.sign_up_scrollview);

        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showFileChooser();
                showAddProfilePicDialog();
            }
        });
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSignUpRequest();
            }
        });
        mTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO show terms and conditions
            }
        });

        String email = getIntent().getStringExtra("EMAIL");
        String userName = getIntent().getStringExtra("USER_NAME");
        if (email == null) {
            Account[] accounts = AccountManager.get(this).getAccounts();
            Set<String> emailSet = new HashSet<String>();
            for (Account account : accounts) {
                if (Utility.isValidEmail(account.name)) {
                    emailSet.add(account.name);
                }
            }
            ArrayList<String> emails = new ArrayList<String>(emailSet);
            mEmail.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emails));
            if (emails.size() > 0) {
                email = emails.get(0);
            }
        }
        mEmail.setText(email);
        mUserName.setText(userName);
        if (email == null) {
            mEmail.requestFocus();
        } else if (userName == null) {
            mUserName.requestFocus();
        } else {
            mPassword.requestFocus();
        }

        checkPermissions();
//        hideKeyboard();
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final View activityRootView = findViewById(R.id.signup_fields_layout);
        ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {


            private boolean wasOpened;

            private final int DefaultKeyboardDP = 100;

            // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
            private final int EstimatedKeyboardDP = DefaultKeyboardDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);

            private final Rect r = new Rect();

            @Override
            public void onGlobalLayout() {
                // Convert the dp to pixels.
                int estimatedKeyboardHeight = (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, activityRootView.getResources().getDisplayMetrics());

                // Conclude whether the keyboard is shown or not.
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == wasOpened) {
                    return;
                }

                wasOpened = isShown;
                if (wasOpened) {
                    mSignUpButtonLayout.setVisibility(View.GONE);

                } else {
                    mSignUpButtonLayout.setVisibility(View.VISIBLE);
                }
                mScrollView.smoothScrollTo(0, findViewById(R.id.user_email).getTop());


            }
        };

        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
    }

    @SuppressLint("InlinedApi")
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12);
        }
    }

    private void sendSignUpRequest() {
        if (isInputValid()) {
            if (Utility.isOnline(SignUpScreen.this)) {

                //Launch Login screen
                ParkMeSharedPreference.getInstance(SignUpScreen.this).setEmail(mEmail.getText().toString().trim());

                ParkMeSharedPreference.getInstance(SignUpScreen.this).setUsername(mUserName.getText().toString().trim());

                ParkMeSharedPreference.getInstance(SignUpScreen.this).setVehicle(mVehicle.getText().toString().trim());


                showDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissDailog();
                        Intent loginIntent = new Intent(SignUpScreen.this, LoginScreen.class);
                        startActivity(loginIntent);
                    }
                }, getString(R.string.ok), getString(R.string.success), getString(R.string.signup_successful), false);


            } else {
                showDialog(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissDailog();
                    }
                }, getString(R.string.ok), getString(R.string.network_issue), getString(R.string.please_check_your_internet), false);
            }


        }
    }

    private boolean isInputValid() {

        if (TextUtils.isEmpty(mEmail.getText().toString().trim()) || !Utility.isValidEmail(mEmail.getText().toString().trim())) {
            mEmail.setError(getString(R.string.enter_valid_email));
            mEmail.requestFocus();
            return false;
        }
        if (mUserName.getText().toString().trim().length() == 0) {
            mUserName.setError(getString(R.string.enter_user_name));
            mUserName.requestFocus();
            return false;
        }
        if (mPassword.getText().toString().trim().length() == 0) {
            mPassword.setError(getString(R.string.enter_password));
            mPassword.requestFocus();
            return false;
        }
        if (mConfirmPassword.getText().toString().trim().length() == 0) {
            mConfirmPassword.setError(getString(R.string.enter_valid_password));
            mConfirmPassword.requestFocus();
            return false;
        }
        if (!mPassword.getText().toString().trim().equalsIgnoreCase(mConfirmPassword.getText().toString().trim())) {
            mConfirmPassword.setError(getString(R.string.password_missmatch));
            mConfirmPassword.requestFocus();
            return false;
        }

        if (mMobile.getText().toString().trim().length() != 10) {
            mMobile.setError(getString(R.string.enter_valid_mobile_number));
            mMobile.requestFocus();
            return false;
        }
        if (mVehicle.getText().toString().trim().length() == 0) {
            mVehicle.setError(getString(R.string.enter_vehicle_number));
            mVehicle.requestFocus();
            return false;
        }
        return true;
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == RESULT_OK) {
                String imagePath = data.getStringExtra(GOTOConstants.IntentExtras.IMAGE_PATH);
                showCroppedImage(imagePath);
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = data.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showAddProfilePicDialog() {
        PicModeSelectDialogFragment dialogFragment = new PicModeSelectDialogFragment();
        dialogFragment.setiPicModeSelectListener(SignUpScreen.this);
        dialogFragment.show(getFragmentManager(), "picModeSelector");
    }

    private void showCroppedImage(String mImagePath) {
        if (mImagePath != null) {
            myBitmap = BitmapFactory.decodeFile(mImagePath);
            mProfileImage.setImageBitmap(myBitmap);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] byteArrayImage = baos.toByteArray();
            encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
            //TODO set file string


        }
    }

    @Override
    public void onPicModeSelected(String mode) {
        String action = mode.equalsIgnoreCase(GOTOConstants.PicModes.CAMERA) ? GOTOConstants.IntentExtras.ACTION_CAMERA : GOTOConstants.IntentExtras.ACTION_GALLERY;
        actionProfilePic(action);
    }

    private void actionProfilePic(String action) {
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("ACTION", action);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
