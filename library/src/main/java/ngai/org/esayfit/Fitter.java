package ngai.org.esayfit;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.TextView;

/**
 * Created by Ngai on 2016/5/16.
 */
public class Fitter {

    private static final String TAG = "Fitter";

    private static Context mContext;
    private static Ratio mRatio;
    //若开发人员重新设置此监听器,适配失效；
    private static ViewGroup.OnHierarchyChangeListener mHierarchyChangeListener;

    private Fitter() {
    }

    // Application 初始化即可
    public static void init(Context context, int standardWidth, int standardHeight) {
        if (mContext == null) {
            mContext = context;
            mRatio = new Ratio(standardWidth, standardHeight);
            mHierarchyChangeListener = new ViewGroup.OnHierarchyChangeListener() {

                @Override
                public void onChildViewAdded(View parent, View child) {
                    if (parent instanceof AbsListView) {
                        Log.d(TAG, "OnHierarchyChange.AbsListView [parent=" + parent.toString() + " , child=" + child.toString());
                        AbsListView parentLv = (AbsListView) parent;
                        Ratio lvTagRatio = null;
                        Object obj = parentLv.getTag(R.id.absview_tag_fitter_ratio);
                        if (obj != null && (obj instanceof Ratio)) {
                            lvTagRatio = (Ratio) obj;
                        } else {
                            int lvStandardWidth = parentLv.getWidth() - (parentLv.getPaddingLeft() + parentLv.getPaddingRight());
                            int lvStandardHeight = parentLv.getHeight() - (parentLv.getPaddingTop() + parentLv.getPaddingBottom());
                            lvTagRatio = newRatio(lvStandardWidth, lvStandardHeight);
                            parentLv.setTag(R.id.absview_tag_fitter_ratio, lvTagRatio);
                            Log.d(TAG, "OnHierarchyChange.AbsListView.[Ratio=" + lvTagRatio.toString());
                        }
                        adj(child, lvTagRatio);
                    } else {
                        Log.d(TAG, "OnHierarchyChange.View/ViewGroup [parent=" + parent.toString() + " , child=" + child.toString());
                        Object tag = child.getTag(R.id.view_tag_fitter_ratio);
                        if (tag != null && (tag instanceof Ratio))
                            adj(child, (Ratio) tag);
                    }

                }

                private void adj(View child, Ratio tagRatio) {
                    if (child instanceof ViewGroup) {
                        doAdjust((ViewGroup) child, tagRatio);
                    } else {
                        doAdjust(child, tagRatio);
                    }
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {
                    // ignore
                }
            };
        } else {
            // Fitter init yet !
        }

    }

    // 生产一个临时比例,相对于设计稿的尺寸
    public static Ratio newRatio(int displayWidth, int displayHeight) {
        return new Ratio(mRatio.getStandardWidth(), mRatio.getStandardHeight(), displayWidth, displayHeight);
    }

    // 根据临时比例调整当前View
    public static void doAdjust(View view, Ratio ratio) {
        if (null == view) return;
        ViewGroup.LayoutParams vlp = view.getLayoutParams();
        onResizer(view, vlp, ratio);
        view.setLayoutParams(vlp);
    }

    // 根据临时比例调整当前View
    public static void doAdjust(ViewGroup viewGroup, Ratio ratio) {
        if (null == viewGroup) return;

        doAdjust((View) viewGroup, ratio);
        viewGroup.setOnHierarchyChangeListener(mHierarchyChangeListener);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                doAdjust((ViewGroup) childView, ratio);
            } else {
                doAdjust(childView, ratio);
            }
        }
    }

    // 适配当前 Fragment
    public static void doAdjust(Fragment fragment) {
        if (null == fragment) return;
        doAdjust((ViewGroup) fragment.getView());
    }

    // 适配当前Activity
    public static void doAdjust(Activity activity) {
        if (null == activity) return;
        View rootView = activity.findViewById(android.R.id.content);
        Object object = rootView.getTag(R.id.view_tag_fitter_ratio);
        if (object != null && (object instanceof Ratio)) return;

        if (rootView instanceof ViewGroup) {
            doAdjust((ViewGroup) rootView);
        } else {
            doAdjust(rootView);
        }
        rootView.setTag(R.id.view_tag_fitter_ratio, mRatio);
    }

    //  调整当前View 属性
    public static void doAdjust(View view) {
        ViewGroup.LayoutParams vlp = view.getLayoutParams();
        onResizer(view, vlp, mRatio);
        view.setLayoutParams(vlp);
    }

    // 调整当前View 属性（包括多级子View^^）;
    public static void doAdjust(ViewGroup viewGroup) {
        if (null == viewGroup) return;

        long startTimeMillis = System.currentTimeMillis();
//        Log.d(TAG, "Fit it start . 0 ms");

        doAdjust((View) viewGroup);
        viewGroup.setOnHierarchyChangeListener(mHierarchyChangeListener);
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = viewGroup.getChildAt(i);
            if (childView instanceof ViewGroup) {
                doAdjust((ViewGroup) childView);
            } else {
                doAdjust(childView);
            }
        }

        Log.d(TAG, "Resizer finish . " + (System.currentTimeMillis() - startTimeMillis) + "ms");
    }


    // 比例尺寸,定义不同屏幕的比率值
    private static class Ratio {
        // 标准宽高，设计稿原素
        private int mStandardWidth = 1280;
        private int mStandardHeight = 720;
        // 设备宽高，Display
        private int mDisplayWidth = 0;
        private int mDisplayHeight = 0;
        // 比例
        private float mRatioWidth = 1.0f;
        public float mRatioHeight = 1.0f;
        public float mRatioAverage = 1.0f;
        // 是否需要做适配
        public boolean isShouldResizer;

        public int getStandardWidth() {
            return mStandardWidth;
        }

        public int getStandardHeight() {
            return mStandardHeight;
        }

        public int getDisplayWidth() {
            return mDisplayWidth;
        }

        public int getDisplayHeight() {
            return mDisplayHeight;
        }

        public float getRatioWidth() {
            return mRatioWidth;
        }

        public float getRatioHeight() {
            return mRatioHeight;
        }

        public float getRatioAverage() {
            return mRatioAverage;
        }

        public boolean isShouldResizer() {
            return isShouldResizer;
        }

        public Ratio(int standardWidth, int standardHeight, int displayWidth, int displayHeight) {
            if (standardWidth > 0 && standardHeight > 0) {
                mStandardWidth = standardWidth;
                mStandardHeight = standardHeight;
                mDisplayWidth = displayWidth;
                mDisplayHeight = displayHeight;
                mRatioWidth = mDisplayWidth / (float) mStandardWidth;
                mRatioHeight = mDisplayHeight / (float) mStandardHeight;
                mRatioAverage = (mRatioWidth + mRatioHeight) / 2;
                isShouldResizer = !((mRatioWidth == 1.0f) && (mRatioHeight == 1.0f));
                Log.d(TAG, toString());
            } else {
                throw new IllegalArgumentException("Both standard width and height should be positive.");
            }
        }

        public Ratio(int standardWidth, int standardHeight) {
            if (standardWidth > 0 && standardHeight > 0) {
                mStandardWidth = standardWidth;
                mStandardHeight = standardHeight;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    Point point = new Point();
                    display.getRealSize(point);
                    mDisplayWidth = point.x;
                    mDisplayHeight = point.y;
                } else {
                    DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
                    Resources.getSystem().getConfiguration();
                    mDisplayWidth = metrics.widthPixels;
                    mDisplayHeight = metrics.heightPixels;
                }
                mRatioWidth = mDisplayWidth / (float) mStandardWidth;
                mRatioHeight = mDisplayHeight / (float) mStandardHeight;
                mRatioAverage = (mRatioWidth + mRatioHeight) / 2;
                isShouldResizer = !((mRatioWidth == 1.0f) && (mRatioHeight == 1.0f));
                Log.d(TAG, toString());
            } else {
                throw new IllegalArgumentException("Both standard width and height should be positive.");
            }
        }

        @Override
        public String toString() {
            return "Ratio{" +
                    "mStandardWidth=" + mStandardWidth +
                    ", mStandardHeight=" + mStandardHeight +
                    ", mDisplayWidth=" + mDisplayWidth +
                    ", mDisplayHeight=" + mDisplayHeight +
                    ", mRatioWidth=" + mRatioWidth +
                    ", mRatioHeight=" + mRatioHeight +
                    ", mRatioAverage=" + mRatioAverage +
                    ", isShouldResizer=" + isShouldResizer +
                    '}';
        }
    }

    /**
     * Integer.MIN_VALUE is default margin value
     **/
    private static final int UNDEFINED_MARGIN = Integer.MIN_VALUE;

    private static void onResizer(View child, ViewGroup.LayoutParams params, Ratio ratio) {

        if(mContext == null){
            throw  new RuntimeException("Please init Fitter by application !");
        }

        if (child != null && params != null) {
            float ratioW = ratio.getRatioWidth();
            float ratioH = ratio.getRatioHeight();
            child.setTag(R.id.view_tag_fitter_ratio, ratio);

            /** width and height **/
            params.width = params.width > 0 ? ceil(params.width, ratioW) : params.width;
            params.height = params.height > 0 ? ceil(params.height, ratioH) : params.height;

            /** margin **/
            if (params instanceof ViewGroup.MarginLayoutParams) {
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) params;

                /** left, top, right, bottom **/
                int left = p.leftMargin;
                int top = p.topMargin;
                int right = p.rightMargin;
                int bottom = p.bottomMargin;
                p.leftMargin = (left == UNDEFINED_MARGIN ? left : ceil(left, ratioW));
                p.topMargin = (top == UNDEFINED_MARGIN ? top : ceil(top, ratioH));
                p.rightMargin = (right == UNDEFINED_MARGIN ? right : ceil(right, ratioW));
                p.bottomMargin = (bottom == UNDEFINED_MARGIN ? bottom : ceil(bottom, ratioH));

                /** start, end **/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    int direction = child.getLayoutDirection();
                    int lm = p.leftMargin;
                    int rm = p.rightMargin;
                    int start = p.getMarginStart();
                    int end = p.getMarginEnd();
                    switch (direction) {
                        case View.LAYOUT_DIRECTION_LTR:
                            if (lm == UNDEFINED_MARGIN) {
                                p.leftMargin = (start == UNDEFINED_MARGIN ? lm : ceil(start, ratioW));
                            }
                            if (rm == UNDEFINED_MARGIN) {
                                p.rightMargin = (end == UNDEFINED_MARGIN ? rm : ceil(end, ratioW));
                            }
                            break;
                        case View.LAYOUT_DIRECTION_RTL:
                            if (lm == UNDEFINED_MARGIN) {
                                p.leftMargin = (end == UNDEFINED_MARGIN ? lm : ceil(end, ratioW));
                            }
                            if (rm == UNDEFINED_MARGIN) {
                                p.rightMargin = (start == UNDEFINED_MARGIN ? rm : ceil(start, ratioW));
                            }
                            break;
                        default:// ignored
                            break;
                    }
                }
            }

            /** x and y **/
            if (params instanceof AbsoluteLayout.LayoutParams) {
                AbsoluteLayout.LayoutParams p = (AbsoluteLayout.LayoutParams) params;
                p.x = ceil(p.x, ratioW);
                p.y = ceil(p.y, ratioH);
            }

            /** padding **/
            int paddingLeft = ceil(child.getPaddingLeft(), ratioW);
            int paddingTop = ceil(child.getPaddingTop(), ratioH);
            int paddingRight = ceil(child.getPaddingRight(), ratioW);
            int paddingBottom = ceil(child.getPaddingBottom(), ratioH);
            child.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

            /** font size and others **/
            if (child instanceof TextView) {
                TextView tv = (TextView) child;
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ceil((int) tv.getTextSize(), (ratioW + ratioH) / 2.0F));
                tv.setCompoundDrawablePadding(ceil(tv.getCompoundDrawablePadding(), (ratioW + ratioH) / 2.0F));
            }
        }
    }

    private static int ceil(int value, float ratio) {
        return (int) Math.ceil(value * ratio);
    }

}
