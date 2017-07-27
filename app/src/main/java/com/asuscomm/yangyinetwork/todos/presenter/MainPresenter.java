package com.asuscomm.yangyinetwork.todos.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.asuscomm.yangyinetwork.todos.OnSuccessListener;
import com.asuscomm.yangyinetwork.todos.view.MainView;
import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
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
    private Disposable mEmailDisposable;
    private Disposable mPwDisposable;
    private CompositeDisposable mCompositeDisposable;
    private Observable<Boolean> mEmailValidObservable;

    public MainPresenter(MainView view) {
        Log.d(TAG, "MainPresenter: ");
        this.mMainView = view;
        mCompositeDisposable = new CompositeDisposable();
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
        Observable<Boolean> emailValidObservable
                = mEmailObservable
                        .map(
                                new Function<CharSequence, String>() {
                                    @Override
                                    public String apply(CharSequence charSequence) throws Exception {
                                        return charSequence.toString();
                                    }
                                }
                        )
                        .map(
                                new Function<String, Boolean>() {
                                    @Override
                                    public Boolean apply(String s) throws Exception {
                                        return emailValid(s);
                                    }
                                }
                    );



        emailValidObservable.subscribe(
                new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.d(TAG, "accept() called with: aBoolean = [" + aBoolean + "]");
                        String msg = aBoolean ? "가능" : "불가능";
                        mMainView.setEmailTvText(msg);
                    }

                }
        );

        Observable<Boolean> pwValidObservable = mPwObservable.map(
                new Function<CharSequence, Boolean>() {
                    @Override
                    public Boolean apply(CharSequence charSequence) throws Exception {
                        String s = charSequence.toString();

                        return s.length() >= 8;
                    }
                }
        );
        Disposable pwValidDisposable = pwValidObservable
                .subscribe(
                        (b) -> {
                            Log.d(TAG, "accept: pw=" + b);
                            String msg = b ? "가능" : "불가";
                            mMainView.setPwTvText(msg);
                        }
                );

//        pwValidDisposable.dispose();

        Disposable emailNpwValidDisposable = Observable.combineLatest(
                emailValidObservable,
                pwValidObservable,
                new BiFunction<Boolean, Boolean, Integer>() {
                    @Override
                    public Integer apply(Boolean aBoolean, Boolean aBoolean2) throws Exception {
                        int b1 = aBoolean ? 2 : 1;
                        int b2 = aBoolean2 ? 2 : 1;

                        return b1+b2;
                    }
                }
        ).map(new Function<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer integer) throws Exception {
                return (integer == 4);
            }
        }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, "accept() called with: aBoolean = [" + aBoolean + "]");
                mMainView.activeBtnSignin(aBoolean);
            }
        });

        mCompositeDisposable.add(pwValidDisposable);
        mCompositeDisposable.add(emailNpwValidDisposable);
//        mCompositeDisposable.dispose();
    }

    public void enableValidations() {
//        unsubscribe();

        mEmailValidObservable = mEmailObservable.map(new Function<CharSequence, String>() {
                    @Override
                    public String apply(@NonNull CharSequence charSequence) throws Exception {
                        return charSequence.toString();
                    }
                })
                .map(
                        (s) -> emailValid(s)
                );

//        mEmailValidObservable.subscribe(new Consumer<Boolean>() {
//            @Override
//            public void accept(@NonNull Boolean aBoolean) throws Exception {
//                Log.d(TAG, "accept() called with: aBoolean = [" + aBoolean + "]");
//                String msg = aBoolean ? "가능" : "불가";
//                mMainView.setEmailTvText(msg);
//            }
//        });
    }

    private Boolean emailValid(String email) {
        return !TextUtils.isEmpty(email)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void unsubscribe() {
        Log.d(TAG, "unsubscribe: ");
        mCompositeDisposable.dispose();

        method2(new OnSuccessListener() {
            @Override
            public void onSuccess(boolean b) {
                if (b) {
                    Log.d(TAG, "onSuccess: ");
                }
            }
        });

        method2(
                (b) -> {
                    if(b) {
                        Log.d(TAG, "unsubscribe: ");
                    }
                }
        );
    }

    private void method2(OnSuccessListener a) {

    }
}
