package com.prank.sexygirlcallprank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.prank.sexygirlcallprank.R;
import com.prank.sexygirlcallprank.model.ChatOne;

import java.util.List;


public class ChatAdapter extends ArrayAdapter<ChatOne> {

    public class C3895a implements OnClickListener {
        C3895a(ChatAdapter aVar) {
        }

        public void onClick(View view) {
        }
    }

    public ChatAdapter(Context context, List<ChatOne> list) {
        super(context, R.layout.item_mine_message, list);
    }

    public int getItemViewType(int i) {
        ChatOne aVar = getItem(i);
        if (aVar != null && aVar.f16641b && !aVar.f16640a) {
            return 0;
        }
        if (aVar != null && (aVar.f16641b || aVar.f16640a)) {
            return !aVar.f16641b ? 3 : 2;
        }
        return 1;
    }

    @NonNull
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_mine_message, viewGroup, false);
            ((TextView) view.findViewById(R.id.text)).setText(getItem(i).f16642c);
        } else if (itemViewType == 1) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_other_message, viewGroup, false);
            ((TextView) view.findViewById(R.id.text)).setText(getItem(i).f16642c);
        }
        view.findViewById(R.id.chatMessageView).setOnClickListener(new C3895a(this));
        return view;
    }

    public int getViewTypeCount() {
        return 4;
    }
}
