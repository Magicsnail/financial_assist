package com.magic.snail.widget.alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.magic.snail.assist.R;
import com.magic.snail.widget.SnailMaxHeightScrollView;
import com.magic.snail.widget.SUI;


public class SnailContextMenu extends Dialog implements View.OnClickListener{

    private static final int TITLE_COLOR = Color.parseColor("#999999");
    private static final int TITLE_TEXT_SIZE = 12;//SP

    private static final int ITEM_COLOR = Color.parseColor("#3D4145");
    private static final int ITEM_TEXT_SIZE = 16;//SP

    private SelectMenuListener listener;
    private SelectMenuTagListener listener2;

    private Object[] tags;


    /**
     * 菜单点击事件
     */
    public interface SelectMenuListener {
        void onSelectMenu(int id);
    }

    public interface SelectMenuTagListener {
        void onSelectMenu(int id, Object tag);
    }

    SnailContextMenu(Context context) {
        super(context);
    }


    @Override
    public void onClick(View v) {
        int index = (int) v.getTag();
        if (listener != null) {
            listener.onSelectMenu(index);
        } else if (listener2 != null) {
            if (index >= 0 && index < tags.length) {
                listener2.onSelectMenu(index, tags[index]);
            } else {
                listener2.onSelectMenu(index, null);
            }
        }
        dismiss();
    }


    /**
     * 用于构建Menu的builder
     */
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {

        private CharSequence title;
        private CharSequence[] items;
        private Object[] tags;
        private SelectMenuListener listener;
        private SelectMenuTagListener tagListener;

        private Builder() {
        }

        public Builder title(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder items(CharSequence[] items) {
            this.items = items;
            return this;
        }

        public Builder items(CharSequence[] items, Object[] tags) {
            if (items.length != tags.length) {
                Log.e("CoContextMenu", "items and tags size must be the same.");
                return this;
            }
            this.items = items;
            this.tags = tags;
            return this;
        }

        public Builder listener(SelectMenuListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder listener(SelectMenuTagListener listener) {
            this.tagListener = listener;
            return this;
        }

        public SnailContextMenu build(Context context) {
            if (context == null) {
                return null;
            }
            SnailContextMenu menu = new SnailContextMenu(context);
            menu.listener = this.listener;
            menu.listener2 = this.tagListener;
            menu.tags = tags;

            int maxH = 0;
            Point p = SUI.getScreenSize(context);
            if (p != null) {
                maxH = (int)(p.y * 0.8);
            }
            View view = setupMenuView(context, title, items, menu, maxH);

            // 有些手机dialog默认有title，
            View parentTitle = menu.findViewById(android.R.id.title);
            if (parentTitle != null) {
                parentTitle.setVisibility(View.GONE);
            }
            menu.setContentView(view);

            return menu;
        }

        static View setupMenuView(Context context, CharSequence title, CharSequence[] items, View.OnClickListener listener, int maxHeight) {
            View view = LayoutInflater.from(context).inflate(R.layout.qui_context_menu, null);
            SnailMaxHeightScrollView scrollView =  view.findViewById(R.id.root_scroll);
            LinearLayout rootView = (LinearLayout) view.findViewById(R.id.root);
            TextView titleView = (TextView) view.findViewById(R.id.title_view);

            if (maxHeight > 100) {
                scrollView.setMaxHeight(maxHeight);
            }

            if (title != null && title.length() > 0) {
                titleView.setVisibility(View.VISIBLE);
                titleView.setText(title);
            }

//            int menuPadding = (int)context.getResources().getDimension(R.dimen.qw_context_menu_item_left_padding);
//            // title
//            if (title != null && title.length() > 0) {
//                TextView menuTitle = new TextView(context);
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                menuTitle.setLayoutParams(lp);
//                menuTitle.setPadding(menuPadding, menuPadding, 0, 0);
//                menuTitle.setText(title);
//                menuTitle.setTextSize(TITLE_TEXT_SIZE);
//                menuTitle.setTextColor(TITLE_COLOR);
//                rootView.addView(menuTitle);
//            }

            buildNormalMenuViews(items, listener, rootView);
            return view;
        }

        static void buildNormalMenuViews(CharSequence[] items, View.OnClickListener listener, LinearLayout root) {
            if (root == null || items == null || items.length == 0) {
                return;
            }

            Context context = root.getContext();

            int menuItemHeight = (int) context.getResources().getDimension(R.dimen.qw_context_menu_item_height);
            int menuPadding = (int)context.getResources().getDimension(R.dimen.qw_context_menu_item_left_padding);

            for (int i=0; i<items.length; ++i) {
                if (items[i] == null) {
                    return;
                }

                // 创建divider
                if (i > 0) {
                    View div = new View(context);
                    div.setBackgroundColor(context.getResources().getColor(R.color.context_menu_divider));
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    div.setLayoutParams(lp);
                    root.addView(div);
                }

                // 添加Menu
                TextView menuItem = new TextView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, menuItemHeight);
                menuItem.setLayoutParams(lp);
                menuItem.setMinWidth(SUI.dp2px(context, 240));
                menuItem.setPadding(menuPadding, 0, menuPadding, 0);
                menuItem.setGravity(Gravity.CENTER_VERTICAL);
                menuItem.setText(items[i]);
                menuItem.setTextSize(ITEM_TEXT_SIZE);
                menuItem.setTextColor(ITEM_COLOR);
                menuItem.setMaxLines(1);
                menuItem.setTag(i);
                menuItem.setOnClickListener(listener);
                root.addView(menuItem);
            }
        }

    }
}
