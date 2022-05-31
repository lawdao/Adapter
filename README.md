# Adapter
listview和GridView的Adapter的抽取
##闲话中心
本来是要推送RxJava知识的，但是出了点意外，因为我要使用retrofit2和RxJava2.0,本来知识都已经准备的差不多了，最后发现，出意外了，retrofit2中引用的Rxjava的版本不是最新版本，他引用的是1.1.5，还在1.0时代，而我却要引用Rxjava的2.0版本，Android studio就不高兴啦，直接给我报错，不让运行，好吧，我暂时没有找到解决的方案，暂时只能先将RxJava的版本降下去了，所以RxJAVA的推送得暂停一段时间了，今天我要说的就是listview和gridview的适配器adapter，我知道，大家在用这两个的时候都会去写一个适配器，而且适配器中的代码都差不多，什么优化呀，ViewHolder呀，老是让你写，肯定会烦的，所以recyclerview就出来了，说实话，我着实不想用它，用它还得加其他的库，一个字，麻烦！！今天的内容会让你大开眼界，只需要一个adapter，你代码的逻辑都会变得非常清楚，大多数情况下，listview中的条目都是比较复杂的，而且呢，条目中居然有好多地方可以点击，要是按照以前的方法来做，卧槽，那adapter中的代码就显得太复杂了，可以说实在太乱，没人愿意看的，就像你一个activity，里面的代码能上千行，谁能看的过来，而且没有一点的逻辑性，这时候就出现了MVP模式，给我带来的好处就是一个activity中的代码量变得极少，而且逻辑特别的清晰，我们暂时不说mvp模式，今天只说adapter的优化。
##优化的地方
1. getView方法，可能你以后都不会再见到此方法了
2. ViewHolder,以后也不需要考虑什么ViewHolder了
3. getCount，你以后也不用考虑他了，我已经帮你做了
4. getItem，也不用你重写了
5. getItemId，和他也说拜拜


##优化步骤
1、 写一个自己的BaseAdapter

首先写一个自己的adapter继承自BaseAdapter，对类采用了泛型，保证继承自定义的BaseAdapter的子类什么数据都可以添加进来

	

```
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	}
```
	

2、 重写getView方法，采用ViewHolder优化，返回convertView

	   

```
 @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        BaseHolder holder = null;
	        if (convertView == null) {
	            if (getItemViewType(position) == TYPE_ONE) {
	                holder = getHolder(position);
	            } else {
	                holder = getOtherHolder(position);
	            }
	        } else {
	            holder = (BaseHolder) convertView.getTag();
	        }
	        holder.setData(mData.get(position));
	        return holder.convertView;
	    }
```
	    

getView的方法中，做了两件事，一是创建ViewHolder，而是给ViewHolder设置数据，当然，这些数据是你传进来的，这些你暂时不用管，下面我们就把目光投向BaseViewHolder

3、 创建BaseHolder,使用泛型

	
	

```
public abstract class BaseHolder<T> {
	
	    public View convertView;
	    public T mData;
	
	    public BaseHolder() {
	        convertView = initView();
	        convertView.setTag(this);
	    }
	
	    public void setData(T data) {
	        this.mData = data;
	        if (mData != null) {
	            refreshView();
	        }
	    }
	    //初始化布局
	    public abstract View initView();
	
	    //填充数据
	    public abstract void refreshView();
	}
```

这个类里面也是做了两件事，一是加载每个条目中的布局，就是initView方法，已经被抽象了，重写他就可以了，另外就是为条目填充数据了，此方法也是抽象的，是子类必须要重写的。

4、 很简单，已经优化完了，下面就是如何使用了

## 实际使用
1、 创建一个adapter继承刚才自定义的adapter


	

```
public class MyAdapter extends MyBaseAdapter<String> {
	    public MyAdapter(ArrayList<String> data) {
	        super(data);
	    }
	
	    @Override
	    public BaseHolder getHolder(int position) {
	        return new OneHolder();
	    }
	}
```
	
看到没有，adapter里面只写了他的构造方法，还有一个就是getHolder，就是上面咱们写的BaseHolder，写Holder的原因在于，以前的adapter中的代码太多了，就像条目中的监听事件都很难写，现在我们将getView的方法转换成了Holder，这一切交给他处理就好，也就是说，你只需要写一个holder继承自上面写的BaseHolder就行了，你的adapter中不需要太多代码，只需要让他返回一个Holder就行了

2、创建holder，继承BaseHolder，重写initView,refreshView方法


	

```
public class OneHolder extends BaseHolder<String> {
	
	    private TextView textView;
	    @Override
	    public View initView() {
	        textView = new TextView(Uiutils.getContext());
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
```

需要注意的是每创建一个类，都要确定好泛型，initview的方法里就是在加载条目的布局，这里暂时返回的是一个简单的textView，你也可以加载点别的，refreshView方法中是要为条目填充数据的，我们在刚开始为listview或者gridview设置适配器的时候准备的是String类型的数据，用构造方法为adapter传的值，代码没有粘贴出来，那么条目的点击事件，你就可以大胆的写到这个类里面去了，是不是很像activity呢，先是加载布局，加载完了就开始设置监听事件，然后在给布局填充数据，流程就变得很清晰了，在这里你不需要考虑优化的事，都说过了，已经优化过了

##条目的种类问题
这里提一下，目前暂时只支持两种条目，不多支持，具体实现步骤如下：

1、 在你的adapter(继承MyBaseAdapter)中重写几个方法
	
	   

```
 @Override
	    public BaseHolder getOtherHolder(int position) {
	        return new TwoHolder();
	    }
	    @Override
	    public int getItemCount() {
	        return 2;
	    }
	
	    @Override
	    public int getItemType(int position) {
	        if (position == 30) {
	            return MyBaseAdapter.TYPE_TWO;
	        } else {
	            return MyBaseAdapter.TYPE_ONE;
	        }
	    }
```
	    
	    
这里重写了getOtherHolder方法，就是让你创建出另外一个条目的holder(必须要继承上面写的BaseHolder)，还有getItemCount，在这个方法里返回数字2，意思就是你要在listview中显示2中布局，getItemType方法，意思呢，就是说，你要在什么时候显示你的哪种布局，目前支持2中布局，如果你想要显示更多种布局，自己改一改就可以了，我感觉2种就够用了，所以就没有多考虑，懒么

2、 第二种布局的holder和第一种的写法是一样的

##效果演示
我在条目等于30的时候让他显示第二种布局，其他都显示第一种布局

![](http://ww3.sinaimg.cn/large/65e4f1e6gw1fa2830rtbhg207t0e0h1y.gif)

