package com.asuscomm.yangyinetwork.todos;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.asuscomm.yangyinetwork.todos.presenter.MainPresenter;
import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class MainActivity extends AppCompatActivity {
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
//        subscriptions.add(
//                Observable.combineLatest(
//                        RxAdapterView.itemSelections(spinner).skip(1),
//                        list.asObservable(),
//                        new BiFunction<Integer, TodoList, List<Todo>>() {
//                            @Override
//                            public List<Todo> apply(@NonNull Integer integer, @NonNull TodoList todoList) throws Exception {
//                                switch (integer) {
//                                    case FilterPositions.INCOMPLETE:
//                                        return list.getIncomplete();
//                                    case FilterPositions.COMPLETE:
//                                        return list.getComplete();
//                                    default:
//                                        return list.getAll();
//                                }
//                            }
//                        }
//                ).subscribe(adapter)
//        );
//
//        subscriptions.add(
//                RxView.clicks(findViewById(R.id.btn_add_todo))
//                        .map(new Function<Object, String>() {
//                            @Override
//                            public String apply(@NonNull Object o) throws Exception {
//                                return addInput.getText().toString();
//                            }
//                        })
//                        .filter(new Predicate<String>() {
//                            @Override
//                            public boolean test(@NonNull String s) throws Exception {
//                                return !TextUtils.isEmpty(s);
//                            }
//                        })
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(@NonNull String s) throws Exception {
//                                list.add(new Todo(s, false));
//
//                                // reset the input box, move focus, and dismiss keyboard
//                                addInput.setText("");
//                                findViewById(R.id.add_todo_container).requestFocus();
//                                dismissKeyboard();
//                            }
//                        }));
    }

    private void initPresenter() {
        mPresenter = new MainPresenter();

        InitialValueObservable<CharSequence> emailObservable = RxTextView.textChanges(etEmail);
        InitialValueObservable<CharSequence> pwObserbable = RxTextView.textChanges(etPw);
        mPresenter.setEmailObservable(emailObservable);
        mPresenter.setPwObservable(pwObserbable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        subscriptions.dispose();
    }
}
