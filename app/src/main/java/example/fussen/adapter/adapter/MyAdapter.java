package example.fussen.adapter.adapter;

import java.util.ArrayList;

/**
 * Created by Fussen on 2016/11/23.
 */

public class MyAdapter extends MyBaseAdapter<String> {
    public MyAdapter(ArrayList<String> data) {
        super(data);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new OneHolder();
    }


    /**
     * 如果需要第二种布局就重写此方法，否则不需要重写
     * @param position
     * @return
     */
    @Override
    public BaseHolder getOtherHolder(int position) {
        return new TwoHolder();
    }

    /**
     * 如果需要第二种布局就重写此方法，否则不需要重写
     * @return
     */
    @Override
    public int getItemCount() {
        return 2;
    }

    /**
     * 如果需要第二种布局就重写此方法，否则不需要重写
     * @param position
     * @return
     */
    @Override
    public int getItemType(int position) {
        if (position == 30) {
            return MyBaseAdapter.TYPE_TWO;
        } else {
            return MyBaseAdapter.TYPE_ONE;
        }
    }
}
