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
import android.widget.EditText;
import android.widget.Spinner;

import com.asuscomm.yangyinetwork.todos.adapters.TodoAdapter;
import com.asuscomm.yangyinetwork.todos.consts.FilterPositions;
import com.asuscomm.yangyinetwork.todos.models.Todo;
import com.asuscomm.yangyinetwork.todos.models.TodoList;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxAdapterView;

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

    private static final String LIST = "list";
    private static final String FILTER = "filter";

    TodoList list;
    int filterPosition;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.add_todo_input)
    EditText addInput;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    CompositeDisposable subscriptions = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        spinner = (Spinner) findViewById(R.id.spinner);
        setSupportActionBar(toolbar);

        // retrieve the saved list and filter or use defaults
        if (savedInstanceState == null) {
            list = new TodoList();

            // add some sample items
            list.add(new Todo("Sample 1", true));
            list.add(new Todo("Sample 2", false));
            list.add(new Todo("Sample 3", false));

            filterPosition = FilterPositions.ALL;
        } else {
            list = new TodoList(savedInstanceState.getString(LIST));
            filterPosition = savedInstanceState.getInt(FILTER);
        }

        TodoAdapter adapter = new TodoAdapter(this, list);

        // setup the list with the adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        // combine filter and todolist
        subscriptions.add(
                Observable.combineLatest(
                        RxAdapterView.itemSelections(spinner).skip(1),
                        list.asObservable(),
                        new BiFunction<Integer, TodoList, List<Todo>>() {
                            @Override
                            public List<Todo> apply(@NonNull Integer integer, @NonNull TodoList todoList) throws Exception {
                                switch (integer) {
                                    case FilterPositions.INCOMPLETE:
                                        return list.getIncomplete();
                                    case FilterPositions.COMPLETE:
                                        return list.getComplete();
                                    default:
                                        return list.getAll();
                                }
                            }
                        }
                ).subscribe(adapter)
        );

        subscriptions.add(
                RxView.clicks(findViewById(R.id.btn_add_todo))
                        .map(new Function<Object, String>() {
                            @Override
                            public String apply(@NonNull Object o) throws Exception {
                                return addInput.getText().toString();
                            }
                        })
                        .filter(new Predicate<String>() {
                            @Override
                            public boolean test(@NonNull String s) throws Exception {
                                return !TextUtils.isEmpty(s);
                            }
                        })
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(@NonNull String s) throws Exception {
                                list.add(new Todo(s, false));

                                // reset the input box, move focus, and dismiss keyboard
                                addInput.setText("");
                                findViewById(R.id.add_todo_container).requestFocus();
                                dismissKeyboard();
                            }
                        }));

        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"All", "Incomplete", "Completed"}));
        spinner.setSelection(filterPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LIST, list.toString());
        outState.putInt(FILTER, spinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.dispose();
    }

    private void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(addInput.getWindowToken(), 0);
    }

}
