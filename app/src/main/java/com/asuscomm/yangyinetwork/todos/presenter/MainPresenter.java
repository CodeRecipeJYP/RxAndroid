package com.asuscomm.yangyinetwork.todos.presenter;

import android.util.Log;

import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by jaeyoung on 7/24/17.
 */

public class MainPresenter {
    private static final String TAG = "MainPresenter";

    public void setEmailObservable(InitialValueObservable<CharSequence> emailObservable) {
        emailObservable
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {
                        String result = charSequence.toString();
                        return result;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "accept: email = " + s);
                    }
                });
    }

    public void setPwObservable(InitialValueObservable<CharSequence> pwObservable) {
        pwObservable
                .map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {
                        String result = charSequence.toString();
                        return result;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "accept: pw = " + s);
                    }
                });
    }
}
