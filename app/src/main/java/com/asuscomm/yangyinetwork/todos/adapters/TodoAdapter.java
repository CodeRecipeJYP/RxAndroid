package com.asuscomm.yangyinetwork.todos.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.asuscomm.yangyinetwork.todos.R;
import com.asuscomm.yangyinetwork.todos.adapters.viewholder.TodoViewHolder;
import com.asuscomm.yangyinetwork.todos.models.Todo;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by jaeyoung on 7/24/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> implements Consumer<List<Todo>> {

    LayoutInflater inflater;

    List<Todo> data = Collections.emptyList();

    // the Action to get called for onNext() of the check changed Subscription
    Consumer<Todo> subscriber;

    public TodoAdapter(Activity activity, Consumer<Todo> checkChangedSubscriber) {
        inflater = LayoutInflater.from(activity);
        subscriber = checkChangedSubscriber;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TodoViewHolder(inflater.inflate(R.layout.item_todo, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        final Todo todo = data.get(position);
        holder.checkbox.setText(todo.description);
        holder.checkbox.setChecked(todo.isCompleted);

        /* Subscribe to the changes of the CheckBox. We skip the first one because it gets
            called with the initial value, we only want to take action on changes.
         */

        holder.disposableSubscription = RxCompoundButton.checkedChanges(holder.checkbox)
                .skip(1)
                .map(new Function<Boolean, Todo>() {
                    @Override
                    public Todo apply(@NonNull Boolean aBoolean) throws Exception {
                        return todo;
                    }
                })
                .subscribe(subscriber); // subscribe with each bind
    }

    @Override
    public void onViewDetachedFromWindow(TodoViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // unsubscribe if we are being removed
        holder.disposableSubscription.dispose();
    }

    @Override
    public void accept(List<Todo> list) {
        data = list;
        notifyDataSetChanged();
    }
}
