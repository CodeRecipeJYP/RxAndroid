package com.asuscomm.yangyinetwork.todos.presenter;

import android.util.Log;

import com.asuscomm.yangyinetwork.todos.MainActivity;
import com.asuscomm.yangyinetwork.todos.view.MainView;
import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by jaeyoung on 7/24/17.
 */

public class MainPresenter {
    private static final String TAG = "MainPresenter";
    private MainView mMainView;
    private Observable<CharSequence> mEmailObservable;
    private Observable<CharSequence> mPwObservable;

    public MainPresenter(MainView view) {
        Log.d(TAG, "MainPresenter: ");
        this.mMainView = view;
    }

    public void setEmailObservable(InitialValueObservable<CharSequence> emailObservable) {
        Log.d(TAG, "setEmailObservable: ");
        mEmailObservable = emailObservable;
    }

    public void setPwObservable(InitialValueObservable<CharSequence> pwObservable) {
        Log.d(TAG, "setPwObservable: ");
        mPwObservable = pwObservable;
    }

    public void enableSubscriptions() {
        Log.d(TAG, "enableSubscriptions: ");
        mEmailObservable.map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {
                        Log.d(TAG, "apply() called with: charSequence = [" + charSequence + "]");;
                        String result = charSequence.toString();
                        return result;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "accept: email = " + s);
                        String msg = "가능?불가";
                        mMainView.setEmailTvText(msg);
                    }
                });

        mPwObservable.map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {
                        Log.d(TAG, "apply() called with: charSequence = [" + charSequence + "]");
                        String result = charSequence.toString();
                        return result;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "accept: pw = " + s);
                        String msg = "가능?불가";
                        mMainView.setPwTvText(msg);
                    }
                });
    }
}
