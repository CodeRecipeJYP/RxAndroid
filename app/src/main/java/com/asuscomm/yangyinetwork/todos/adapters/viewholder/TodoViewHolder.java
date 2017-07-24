package com.asuscomm.yangyinetwork.todos.adapters.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import org.reactivestreams.Subscription;

import io.reactivex.disposables.Disposable;

/**
 * Created by jaeyoung on 7/24/17.
 */

public class TodoViewHolder extends RecyclerView.ViewHolder {

    public CheckBox checkbox;
    public Disposable disposableSubscription;

    public TodoViewHolder(View itemView) {
        super(itemView);
        checkbox = (CheckBox) itemView;
    }
}
