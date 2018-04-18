package com.qx.interactive.answer.ui.selfView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.qx.interactive.answer.R;
import com.qx.interactive.answer.model.Point;
import com.qx.interactive.answer.utils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYingXin on 2017/2/16.
 */
public class LockView extends View {

    private Bitmap mNormalBitmap;
    private Bitmap mPressBitmap;
    private Bitmap smallNormalBitmap;
    private Bitmap smallPressBitmap;
    private float mPointRadius;
    private float mSmallPointRadius;

    // 手指在屏幕上的位置
    private float mX, mY;

    // 手指在屏幕上第一次按下的位置
    private float mXPress, mYPress;

    // 标记当前是否在绘制状态
    private boolean isDraw = false;

    // 图片绘制画笔和文字绘制画笔
    private Paint mPaint,textPaint,textPaintPress,textPaintInput;

    //文字请输入密码的偏移位置
    private float inputTextX,inputTextY;

    // 4行3列 12-2个点
    private Point[][] mPoints = new Point[4][3];

    //绘制6个小球，代表六个密码
    private Point[] smallPoints = new Point[6];

    //每个点的文字
    private String[] texts = {"1","2","3","4","5","6","7","8","9","0"};

    // 被选中的点
    private List<Point> mSelectedPoints = new ArrayList<>();
    // 绘制正确的点位置（0-9记录下来，验证）
    private List<Integer> mPassPositions = new ArrayList<>();

    private OnDrawFinishedListener mListener;

    public LockView(Context context) {
        this(context, null);
    }

    public LockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制密码数字按钮
        drawPoints(canvas);
        //绘制文字 请输入密码和已输入数量的小球
        drawOther(canvas);
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#265d81"));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.H_DIMEN_40PX));

        textPaintPress = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintPress.setColor(Color.parseColor("#3e7599"));
        textPaintPress.setStyle(Paint.Style.FILL);
        textPaintPress.setTextSize(getResources().getDimensionPixelSize(R.dimen.H_DIMEN_40PX));

        textPaintInput = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintInput.setColor(Color.parseColor("#e2ebf1"));
        textPaintInput.setStyle(Paint.Style.FILL);
        textPaintInput.setTextSize(getResources().getDimensionPixelSize(R.dimen.H_DIMEN_65PX));

        // 加载两种状态图片
        Bitmap nBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lock_point_normal);
        Bitmap pBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lock_point_press);
        mNormalBitmap = BitmapUtils.zoomImg(nBitmap,
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_100PX),
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_100PX));
        mPressBitmap = BitmapUtils.zoomImg(pBitmap,
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_100PX),
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_100PX));
        mPointRadius = mNormalBitmap.getWidth() / 2;

        //加载几个小球
        Bitmap smallBitmapNormal = BitmapFactory.decodeResource(getResources(), R.drawable.lock_pnormal_small);
        Bitmap smallBitmapPress = BitmapFactory.decodeResource(getResources(), R.drawable.lock_press_small);
        smallNormalBitmap = BitmapUtils.zoomImg(smallBitmapNormal,
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_40PX),
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_40PX));
        smallPressBitmap = BitmapUtils.zoomImg(smallBitmapPress,
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_40PX),
                getResources().getDimensionPixelSize(R.dimen.H_DIMEN_40PX));
        mSmallPointRadius = smallNormalBitmap.getWidth() /2 ;

        // 当前视图的大小
        int width = getWidth();
        int height = getHeight();
        // 九宫格点的偏移量
        int offSet = (int) (Math.abs(width - height) / 1.5);
        // x、y轴上的偏移量
        int offSetX = 0, offSetY = 0;
        int pointItemWidth = 0; // 每个点所占用方格的宽度
        if (width > height){ // 默认为横屏，设置左右偏移范围
            offSetX = offSet;
            offSetY = getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_100PX);
            pointItemWidth = height / 8;
            inputTextX = (float) (width/2-((getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_65PX))*1.5));
            inputTextY = getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_50PX);
        }
        // 初始化十个点
        mPoints[0][0] = new Point(offSetX + pointItemWidth, offSetY + pointItemWidth);
        mPoints[0][1] = new Point((float) (offSetX + pointItemWidth * 2.8), offSetY + pointItemWidth);
        mPoints[0][2] = new Point((float) (offSetX + pointItemWidth * 4.6), offSetY + pointItemWidth);

        mPoints[1][0] = new Point(offSetX + pointItemWidth, (float) (offSetY + pointItemWidth * 2.6));
        mPoints[1][1] = new Point((float) (offSetX + pointItemWidth * 2.8), (float) (offSetY + pointItemWidth * 2.6));
        mPoints[1][2] = new Point((float) (offSetX + pointItemWidth * 4.6), (float) (offSetY + pointItemWidth * 2.6));

        mPoints[2][0] = new Point(offSetX + pointItemWidth, (float) (offSetY + pointItemWidth * 4.2));
        mPoints[2][1] = new Point((float) (offSetX + pointItemWidth * 2.8), (float) (offSetY + pointItemWidth * 4.2));
        mPoints[2][2] = new Point((float) (offSetX + pointItemWidth * 4.6), (float) (offSetY + pointItemWidth * 4.2));

        mPoints[3][0] = new Point(offSetX + pointItemWidth, (float) (offSetY + pointItemWidth * 5.8));
        mPoints[3][1] = new Point((float) (offSetX + pointItemWidth * 2.8), (float) (offSetY + pointItemWidth * 5.8));
        mPoints[3][2] = new Point((float) (offSetX + pointItemWidth * 4.6), (float) (offSetY + pointItemWidth * 5.8));

        //初始化六个密码点(点一是屏幕一半减去三个球大小加2个半间隔，点二加一个球加一个间隔，以此类推)
        float smallWidth = getResources().getDimensionPixelOffset(R.dimen.W_DIMEN_24PX);
        float offSetcenter = (float) (width/2-(6*mSmallPointRadius+smallWidth*2.5));
        smallPoints[0] = new Point(offSetcenter,
                offSetY-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_18PX));
        smallPoints[1] = new Point((offSetcenter + smallWidth)+
                +mSmallPointRadius*2,
                offSetY-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_18PX));
        smallPoints[2] = new Point((offSetcenter + smallWidth * 2)+
                +mSmallPointRadius*4,
                offSetY-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_18PX));
        smallPoints[3] = new Point((offSetcenter + smallWidth * 3)+
                +mSmallPointRadius*6,
                offSetY-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_18PX));
        smallPoints[4] = new Point((offSetcenter + smallWidth * 4)+
                +mSmallPointRadius*8,
                offSetY-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_18PX));
        smallPoints[5] = new Point((offSetcenter + smallWidth * 5)+
                +mSmallPointRadius*10,
                offSetY-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_18PX));
        // 重置所有的点
        resetPoints();
    }

    /**
     * 绘制所有的点
     * @param canvas
     */
    private void drawPoints(Canvas canvas){
        int index = -1;
        for (int i = 0; i < mPoints.length; i++){
            for (int j = 0; j < mPoints[i].length; j++){
                if(i==3&&j!=1)//这0和3两个点不要
                    continue;
                ++index;
                Point point = mPoints[i][j];
                // 不同状态绘制点
                switch (point.state){
                    case Point.STATE_NORMAL:
                        canvas.drawBitmap(mNormalBitmap, point.x - mPointRadius, point.y - mPointRadius, mPaint);
                        canvas.drawText(texts[index],(point.x-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_12PX)),
                                (point.y+getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_17PX)), textPaint);
                        break;
                    case Point.STATE_PRESS:
                        canvas.drawBitmap(mPressBitmap, point.x - mPointRadius, point.y - mPointRadius, mPaint);
                        canvas.drawText(texts[index],(point.x-getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_12PX)),
                                (point.y+getResources().getDimensionPixelOffset(R.dimen.H_DIMEN_17PX)), textPaintPress);
                        break;
                }
            }
        }
    }

    /**
     * 绘制文字和小球
     * @return
     */
    private void drawOther(Canvas canvas){
        canvas.drawText("请输入密码",inputTextX, inputTextY, textPaintPress);
        for (int i = 0; i < smallPoints.length; i++) {
            Point point = smallPoints[i];
            // 不同状态绘制点
            switch (point.state) {
                case Point.STATE_NORMAL:
                    canvas.drawBitmap(smallNormalBitmap, point.x, point.y, mPaint);
                    break;
                case Point.STATE_PRESS:
                    canvas.drawBitmap(smallPressBitmap, point.x, point.y, mPaint);
                    break;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mX = event.getX();
        mY = event.getY();
        int[] position;
        int i, j;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mXPress = event.getX();
                mYPress = event.getY();
                // 获取选择的点的位置
                position = getSelectedPointPosition();
                if (position != null){
                    isDraw = true; // 标记为绘制状态
                    i = position[0];
                    j = position[1];
                    mPoints[i][j].state = Point.STATE_PRESS;
                    // 被选择的点存入一个集合中
                    mSelectedPoints.add(mPoints[i][j]);
                    mPassPositions.add((i * 3 + j+1)==11?0:(i * 3 + j+1)); // 把选中的点的路径转换成一位数组存储起来
                }
                for(int k=0;k<mPassPositions.size();k++){
                    smallPoints[k].state = Point.STATE_PRESS;
                }
                break;
            case MotionEvent.ACTION_MOVE:
//                LogUtils.e("Tag","x位置:"+mX+"y位置:"+mY+"定点位置:"+mXPress+"---"+mYPress);
                //手指移动位置大于60认为已经离开当前坐标点
                if(Math.abs(mX-mXPress)>60||Math.abs(mY-mYPress)>60) {
                    resetPointsPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mListener != null && isDraw){
                    // 每执行一次，这里就往回调传一次，如果发现密码位数已满那就判断  准确/不正确
                    mListener.onDrawFinished(mPassPositions);
                }
                isDraw = false;
                // 获取选择的点的位置
                position = getSelectedPointPosition();
                if (position != null){
                    isDraw = true; // 标记为绘制状态
                    i = position[0];
                    j = position[1];
                    mPoints[i][j].state = Point.STATE_NORMAL;
                }
                break;
        }
        invalidate();
        return true;
    }

    /**
     * 获取选择的点的位置
     */
    @Nullable
    protected int[] getSelectedPointPosition(){
        Point point = new Point(mX, mY);
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                // 判断触摸的点和遍历的当前点的距离是否小于当个点的半径
                if(mPoints[i][j].getInstance(point) < mPointRadius){
                    // 小于则获取作为被选中，并返回选中点的位置
                    int[] result = new int[2];
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 重置所有的点
     */
    public void resetPoints(){
        mPassPositions.clear();
        mSelectedPoints.clear();
        for (Point[] mPoint : mPoints) {
            for (Point aMPoint : mPoint) {
                aMPoint.state = Point.STATE_NORMAL;
            }
        }
        invalidate();
    }

    /**
     * 重置绘图
     */
    public void resetPointsPosition(){
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                mPoints[i][j].state = Point.STATE_NORMAL;
            }
        }
        invalidate();
    }

    public interface OnDrawFinishedListener{
        void onDrawFinished(List<Integer> passPositions);
    }

    /**
     * 设置点击完成监听接口
     * @param listener
     */
    public void setOnDrawFinishedListener(OnDrawFinishedListener listener){
        this.mListener = listener;
    }
}