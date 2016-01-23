package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import info.androidhive.materialdesign.R;

public class PersonalInfoAct extends AppCompatActivity {

    private EditText  name_text;
    private EditText  mobile_text;

    private ImageView name_edit;
    private ImageView mobile_edit;
    private Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"black\">" + "Personal Info" + "</font>"));
        getSupportActionBar().show();

        mobile_text =(EditText)findViewById(R.id.mobile_text);
        name_text = (EditText)findViewById(R.id.name_text);
        name_edit = (ImageView)findViewById(R.id.name_edit_icon);
        name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_text.setInputType(InputType.TYPE_CLASS_TEXT);
                name_text.setEnabled(true);
                name_text.requestFocus();
                name_text.setSelection(name_text.getText().length());
                name_text.setCursorVisible(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        mobile_edit = (ImageView)findViewById(R.id.mobile_edit_icon);
        mobile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_text.setEnabled(true);
                mobile_text.requestFocus();
                mobile_text.setSelection(mobile_text.getText().length());
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        save_btn = (Button)findViewById(R.id.save);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
