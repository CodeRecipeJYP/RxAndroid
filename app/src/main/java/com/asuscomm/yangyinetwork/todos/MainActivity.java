package com.asuscomm.yangyinetwork.todos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.asuscomm.yangyinetwork.todos.presenter.MainPresenter;
import com.asuscomm.yangyinetwork.todos.view.MainView;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements MainView {
    private static final String TAG = "MainActivity";
    
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_email_isvalid)
    TextView tvEmailIsvalid;
    @BindView(R.id.et_pw)
    EditText etPw;
    @BindView(R.id.tv_pw_isvalid)
    TextView tvPwIsvalid;
    @BindView(R.id.btn_signin)
    Button btnSignin;

    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initPresenter();
    }

    private void initPresenter() {
        Log.d(TAG, "initPresenter: ");
        mPresenter = new MainPresenter(this);

        InitialValueObservable<CharSequence> emailObservable = RxTextView.textChanges(etEmail);
        InitialValueObservable<CharSequence> pwObserbable = RxTextView.textChanges(etPw);
        mPresenter.setEmailObservable(emailObservable);
        mPresenter.setPwObservable(pwObserbable);
        mPresenter.enableSubscriptions();

//        Observable<Object> clicks = RxView.clicks(btnSignin);
//        clicks.subscribe(
//                new Consumer<Object>() {
//                    @Override
//                    public void accept(@NonNull Object o) throws Exception {
//                        Log.d(TAG, "accept() called with: o = [" + o + "]");
        mPresenter.enableValidations();
//                    }
//                }
//        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void setEmailTvText(String msg) {
        Log.d(TAG, "setEmailTvText() called with: msg = [" + msg + "]");
        tvEmailIsvalid.setText(msg);
    }

    @Override
    public void activeBtnSignin(boolean b) {
        btnSignin.setEnabled(b);
    }

    @Override
    public void setPwTvText(String msg) {
        Log.d(TAG, "setPwTvText() called with: msg = [" + msg + "]");
        tvPwIsvalid.setText(msg);
    }
}
