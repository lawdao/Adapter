package example.fussen.adapter.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import example.fussen.adapter.App;

/**
 * Created by Fussen on 2016/11/23.
 */

public class OneHolder extends BaseHolder<String> {

    private TextView textView;
    @Override
    public View initView() {
        textView = new TextView(App.getContext());
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(20);
        textView.setPadding(10,10,10,10);
        return textView;
    }

    @Override
    public void refreshView() {
        textView.setText(mData);
    }
}
