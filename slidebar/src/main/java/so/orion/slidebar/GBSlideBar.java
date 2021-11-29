package so.orion.slidebar;

import ohos.agp.animation.Animator;
import ohos.agp.animation.AnimatorValue;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentState;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.StateElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.Rect;
import ohos.agp.utils.RectFloat;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.multimodalinput.event.MmiPoint;
import ohos.multimodalinput.event.TouchEvent;


public class GBSlideBar<mIsStartAnimation> extends Component implements Component.DrawTask, Component.TouchEventListener{

    private static final HiLogLabel LABEL_LOG = new HiLogLabel(0, 0, "GBSlideBar.class");
    private RectFloat mBackgroundPaddingRect;
    private Element mBackgroundDrawable;
    private boolean mFirstDraw = true;
    private GBSlideBarAdapter mAdapter;
    private int[][] mAnchor;
    private boolean mModIsHorizontal = true;
    private int mCurrentX;
    private int mPivotX;
    private int mPivotY;
    private boolean mSlide = false;

    private static final int[] STATE_NORMAL = new int[]{ComponentState.COMPONENT_STATE_EMPTY};
    private static final int[] STATE_SELECTED = new int[]{ComponentState.COMPONENT_STATE_SELECTED};
    private static final int[] STATE_PRESS = new int[]{ComponentState.COMPONENT_STATE_PRESSED};
    private int[] mState = STATE_SELECTED;
    private int mCurrentItem;

    private int mAnchorWidth;
    private int mAnchorHeight;

    private int mPlaceHolderWidth;
    private int mPlaceHolderHeight;
    private int mTextMargin;

    private Paint mPaint;
    private int mTextSize;
    private Color mTextColor;

    private int mLastX;
    private int mSlideX;

    private int mAbsoluteY;

    private boolean mIsStartAnimation = false;
    private boolean mIsEndAnimation = false;
    private boolean mIsFirstSelect = true;
    private boolean mCanSelect = true;

    private GBSlideBarListener gbSlideBarListener;

    //Attributes
    private static final String ATTR_GBS_PADDING_LEFT = "gbs_paddingLeft";
    private static final String ATTR_GBS_PADDING_TOP = "gbs_paddingTop";
    private static final String ATTR_GBS_PADDING_RIGHT = "gbs_paddingRight";
    private static final String ATTR_GBS_PADDING_BOTTOM = "gbs_paddingBottom";

    private static final String ATTR_GBS_ANCHOR_WIDTH = "gbs_anchor_width";
    private static final String ATTR_GBS_ANCHOR_HEIGHT = "gbs_anchor_height";

    private static final String ATTR_GBS_PLACEHOLDER_WIDTH = "gbs_placeholder_width";
    private static final String ATTR_GBS_PLACEHOLDER_HEIGHT = "gbs_placeholder_height";

    private static final String ATTR_GBS_BACKGROUND = "gbs_background";

    private static final String ATTR_GBS_TEXT_SIZE = "gbs_textSize";
    private static final String ATTR_GBS_TEXT_COLOR = "gbs_textColor";

    private static final String ATTR_GBS_TEXT_MARGIN = "gbs_text_margin";


    public GBSlideBar(Context context) {
        super(context);
        initAttrSet(null);
    }

    public GBSlideBar(Context context, AttrSet attrSet) {
        super(context, attrSet);
        initAttrSet(attrSet);
    }

    public GBSlideBar(Context context, AttrSet attrSet, String styleName) {
        super(context, attrSet, styleName);
        initAttrSet(attrSet);
    }

    public GBSlideBar(Context context, AttrSet attrSet, int resId) {
        super(context, attrSet, resId);
        initAttrSet(attrSet);
    }
    private void initAttrSet(AttrSet attrSet) {

        if (attrSet == null) return;
        mBackgroundPaddingRect = new RectFloat();
        mBackgroundPaddingRect.left=0;
        mBackgroundPaddingRect.top=0;
        mBackgroundPaddingRect.right=0;
        mBackgroundPaddingRect.bottom=0;
        mAnchorWidth=50;
        mAnchorHeight=50;
        mPlaceHolderWidth=20;
        mPlaceHolderHeight=20;

        if(attrSet.getAttr(ATTR_GBS_PADDING_LEFT).isPresent()){
            mBackgroundPaddingRect.left =attrSet.getAttr(ATTR_GBS_PADDING_LEFT).get().getDimensionValue();
        }
        if(attrSet.getAttr(ATTR_GBS_PADDING_TOP).isPresent()){
            mBackgroundPaddingRect.top =attrSet.getAttr(ATTR_GBS_PADDING_TOP).get().getDimensionValue();
        }
        if(attrSet.getAttr(ATTR_GBS_PADDING_RIGHT).isPresent()){
            mBackgroundPaddingRect.right =attrSet.getAttr(ATTR_GBS_PADDING_RIGHT).get().getDimensionValue();
        }
        if(attrSet.getAttr(ATTR_GBS_PADDING_BOTTOM).isPresent()){
            mBackgroundPaddingRect.bottom =attrSet.getAttr(ATTR_GBS_PADDING_BOTTOM).get().getDimensionValue();
        }

        if(attrSet.getAttr(ATTR_GBS_ANCHOR_WIDTH).isPresent()){
            mAnchorWidth = attrSet.getAttr(ATTR_GBS_ANCHOR_WIDTH).get().getDimensionValue();
        }
        if(attrSet.getAttr(ATTR_GBS_ANCHOR_HEIGHT).isPresent()){
            mAnchorHeight = attrSet.getAttr(ATTR_GBS_ANCHOR_HEIGHT).get().getDimensionValue();
        }

        if(attrSet.getAttr(ATTR_GBS_PLACEHOLDER_WIDTH).isPresent()){
            mPlaceHolderWidth = attrSet.getAttr(ATTR_GBS_PLACEHOLDER_WIDTH).get().getDimensionValue();
        }
        if(attrSet.getAttr(ATTR_GBS_PLACEHOLDER_HEIGHT).isPresent()){
            mPlaceHolderHeight = attrSet.getAttr(ATTR_GBS_PLACEHOLDER_HEIGHT).get().getDimensionValue();
        }


        mBackgroundDrawable =attrSet.getAttr(ATTR_GBS_BACKGROUND).get().getElement();

        mTextSize=28;
        if(attrSet.getAttr(ATTR_GBS_TEXT_SIZE).isPresent()){
            mTextSize = attrSet.getAttr(ATTR_GBS_TEXT_SIZE).get().getDimensionValue();
        }
        mTextColor= Color.BLACK;
        if(attrSet.getAttr(ATTR_GBS_TEXT_COLOR).isPresent()){
            mTextColor =attrSet.getAttr(ATTR_GBS_TEXT_COLOR).get().getColorValue();
        }
        mTextMargin=0;
        if(attrSet.getAttr(ATTR_GBS_TEXT_MARGIN).isPresent()){
            mTextMargin =attrSet.getAttr(ATTR_GBS_TEXT_MARGIN).get().getDimensionValue();
        }
        HiLog.debug(LABEL_LOG, "Attr end", "");
        setTouchEventListener(this::onTouchEvent);
    }

    public void registerDrawTask(){
        HiLog.debug(LABEL_LOG, "Reg OnDraw", "");
        addDrawTask(this::onDraw);
    }


    private void drawBackground() {
        HiLog.debug(LABEL_LOG, "drawBack", "");
        Rect rect = new Rect((int) mBackgroundPaddingRect.left + mAnchorWidth,
                (int) mBackgroundPaddingRect.top,
                (int) (getWidth() - mBackgroundPaddingRect.right - mAnchorWidth),
                (int) (getHeight() - mBackgroundPaddingRect.bottom));
        mBackgroundDrawable.setBounds(rect);

        mAbsoluteY = (int) (mBackgroundPaddingRect.top - mBackgroundPaddingRect.bottom);

        mCurrentX = mPivotX = getWidth() / 2;
        mPivotY = getHeight() / 2;

        int widthBase = rect.getWidth() / getCount();
        int widthHalf = widthBase / 2;


        mAnchor = new int[getCount()][2];

        for (int i = 0, j = 1; i < getCount(); i++, j++) {

            if (i == 0) {
                mAnchor[i][0] = rect.left;
            } else if (i == getCount() - 1) {
                mAnchor[i][0] = rect.right;
            } else {
                mAnchor[i][0] =widthBase * j - widthHalf + rect.left;
            }
            mAnchor[i][1] = mPivotY + mAbsoluteY / 2;

        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mPaint.setTextAlign(TextAlignment.CENTER);

    }

    @Override
    public void onDraw(Component component, Canvas canvas) {
        HiLog.debug(LABEL_LOG, "onDraw", "");
        if (mFirstDraw) drawBackground();
        if (mBackgroundDrawable != null) mBackgroundDrawable.drawToCanvas(canvas);

        Element itemDefault;
        Element itemSlide;
        StateElement stateListDrawable;

        if (!mSlide) {
            HiLog.debug(LABEL_LOG, "inside not mslide", "");
            int distance;
            int minIndex = 0;
            int minDistance = Integer.MAX_VALUE;
            for (int i = 0; i < getCount(); i++) {
                distance = Math.abs(mAnchor[i][0] - mCurrentX);
                if (minDistance > distance) {
                    minIndex = i;
                    minDistance = distance;
                }
            }

            setCurrentItem(minIndex);
            stateListDrawable = mAdapter.getItem(minIndex);


        }
        else {
            HiLog.debug(LABEL_LOG, "INside else", "");
            mSlide = false;
            mCurrentX = mAnchor[mCurrentItem][0];
            if (mFirstDraw) {
                mSlideX = mLastX = mCurrentX;
            }
            stateListDrawable = mAdapter.getItem(mCurrentItem);
            mIsFirstSelect = true;


        }
        int selectedIndex = stateListDrawable.findStateElementIndex(mState);
        HiLog.debug(LABEL_LOG, "Selected Index"+selectedIndex, "");
        itemDefault = stateListDrawable.getStateElement(selectedIndex);


        for (int i = 0; i < getCount(); i++) {

            if (i == mCurrentItem) {
                mPaint.setColor(mAdapter.getTextColor(mCurrentItem));
                mPaint.setColor(mAdapter.getTextColor(mCurrentItem));
                canvas.drawText(mPaint, mAdapter.getText(i), mAnchor[i][0], mAnchor[i][1] + mAnchorHeight * 3 / 2 + mTextMargin);
            }else {
                mPaint.setColor(mTextColor);
                canvas.drawText(mPaint, mAdapter.getText(i), mAnchor[i][0], mAnchor[i][1] + mAnchorHeight * 3 / 2 + mTextMargin);
            }
            stateListDrawable = mAdapter.getItem(i);

            int normalIndex = stateListDrawable.findStateElementIndex(STATE_NORMAL);
            itemSlide = stateListDrawable.getStateElement(normalIndex);
            itemSlide.setBounds(
                    mAnchor[i][0] - mPlaceHolderWidth,
                    mAnchor[i][1] - mPlaceHolderHeight,
                    mAnchor[i][0] + mPlaceHolderWidth,
                    mAnchor[i][1] + mPlaceHolderHeight
            );
            itemSlide.drawToCanvas(canvas);

        }


        itemDefault.setBounds(
                mSlideX - mAnchorWidth,
                mPivotY + mAbsoluteY / 2 - mAnchorHeight,
                mSlideX + mAnchorWidth,
                mPivotY + mAbsoluteY / 2 + mAnchorHeight
        );

        itemDefault.drawToCanvas(canvas);

        setFirstDraw(false);
    }

    private void endSlide() {
        HiLog.debug(LABEL_LOG, "endSlide", "");
        if (!mIsEndAnimation && mSlide) {
            mIsEndAnimation = true;
            ValueAnimator mEndAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            mEndAnim.setDuration(200);
            mEndAnim.setCurveType(Animator.CurveType.LINEAR);
            mEndAnim.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
                @Override
                public void onUpdate(AnimatorValue animatorValue, float v) {
                    mSlideX = (int) ((mCurrentX - mLastX) * v+ mLastX);
                    invalidate();
                }
            });

            mEndAnim.setStateChangedListener(new Animator.StateChangedListener() {
                @Override
                public void onStart(Animator animator) {
                    //onstart

                }

                @Override
                public void onStop(Animator animator) {
                    //onStop

                }

                @Override
                public void onCancel(Animator animator) {
                    //onCancel

                }

                @Override
                public void onEnd(Animator animator) {
                    mIsStartAnimation = false;
                    mLastX = mCurrentX;
                    mIsEndAnimation = false;
                    mCanSelect = true;
                    invalidate();
                }

                @Override
                public void onPause(Animator animator) {
                    //onPause

                }

                @Override
                public void onResume(Animator animator) {
                    //onResume

                }
            });

            mEndAnim.start();
        } else {

            mLastX = mCurrentX;
            mSlideX = mCurrentX;
            invalidate();
        }
    }

    private void startSlide() {
        HiLog.debug(LABEL_LOG, "startSlide", "");
        if (!mIsStartAnimation && !mSlide && mCanSelect) {

            mIsStartAnimation = true;
            ValueAnimator mStartAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            mStartAnim.setDuration(200);
            HiLog.debug(LABEL_LOG, "animator value + " +Animator.CurveType.LINEAR, "");
            mStartAnim.setCurveType(Animator.CurveType.LINEAR);
            mStartAnim.setValueUpdateListener(new AnimatorValue.ValueUpdateListener() {
                @Override
                public void onUpdate(AnimatorValue animatorValue, float v) {
                    mSlideX = (int) ((mCurrentX - mLastX) * v + mLastX);

                    invalidate();
                }
            });
            mStartAnim.setStateChangedListener(new Animator.StateChangedListener() {
                @Override
                public void onStart(Animator animator) {
                    //onStart

                }

                @Override
                public void onStop(Animator animator) {
                    //onStop

                }

                @Override
                public void onCancel(Animator animator) {
                    //onCancel

                }

                @Override
                public void onEnd(Animator animator) {
                    mLastX = mCurrentX;
                    mIsStartAnimation = false;
                    mCanSelect = true;
                    invalidate();
                }

                @Override
                public void onPause(Animator animator) {
                    //onPause

                }

                @Override
                public void onResume(Animator animator) {
                    //onResume

                }
            });
            mStartAnim.start();
        }
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        HiLog.debug(LABEL_LOG, "onTouchEvent", "");
        if (mCanSelect) {
            int action = touchEvent.getAction();
            int index=touchEvent.getIndex();
            MmiPoint point1=touchEvent.getPointerPosition(index);
            //获取当前坐标
            mCurrentX = mModIsHorizontal ? getNormalizedX(point1) : mPivotX;
            mSlide = action == TouchEvent.PRIMARY_POINT_UP;

            if (!mSlide && mIsFirstSelect) {
                HiLog.debug(LABEL_LOG, "before start Slide", "");
                startSlide();
                mIsFirstSelect = false;

            } else if (!mIsStartAnimation && !mIsEndAnimation) {
                endSlide();
            }


            mState = action == TouchEvent.PRIMARY_POINT_UP || action == TouchEvent.CANCEL ? STATE_SELECTED : STATE_PRESS;

            switch (action) {
                case TouchEvent.POINT_MOVE:
                    return true;
                case TouchEvent.PRIMARY_POINT_DOWN:
                    return true;
                case TouchEvent.PRIMARY_POINT_UP:
                    mCanSelect = false;
                    invalidate();
                    return true;
                default:
                    return false;
            }
        }
        return onTouchEvent(component,touchEvent);
    }



    private int getNormalizedX(MmiPoint point1) {
        return Math.min(Math.max((int) point1.getX(), mAnchorWidth), getWidth() - mAnchorWidth);
    }

    private void setFirstDraw(boolean firstDraw) {
        mFirstDraw = firstDraw;
    }



    private int getCount() {
        return  mAdapter.getCount() ;
    }


    private void setCurrentItem(int currentItem) {
        if (mCurrentItem != currentItem && gbSlideBarListener != null) {
            gbSlideBarListener.onPositionSelected(currentItem);
        }
        mCurrentItem = currentItem;
    }
    public void setAdapter(GBSlideBarAdapter adapter) {
        HiLog.debug(LABEL_LOG, "fazil setAdapter called"+adapter, "");
        mAdapter = adapter;
    }

    public void setPosition(int position) {
        position = position < 0 ? 0 : position;
        position = position > mAdapter.getCount() ? mAdapter.getCount() - 1 : position;
        mCurrentItem = position;
        mSlide = true;
        invalidate();
    }

    public void setOnGbSlideBarListener(GBSlideBarListener listener) {
        HiLog.debug(LABEL_LOG, "listener", "");
        this.gbSlideBarListener = listener;
    }



}
