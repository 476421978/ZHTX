package com.example.user.zhtx.Map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import com.baidu.mapapi.map.BitmapDescriptor;

public class BitmapTextView {
    private static final String TAG="BitmapTextView";
    public Bitmap bitmap = null;

    public Bitmap drawBitMapText(String str, BitmapDescriptor bitmapDescriptor){
        int width = bitmapDescriptor.getBitmap().getWidth(),height = bitmapDescriptor.getBitmap().getHeight();//marker的宽和高

        int color = bitmapDescriptor.getBitmap().getPixel(width/2,height/2);
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444); //建立一个空的Bitmap
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        paint.setDither(true);//设定是否使用图像抖动处理，会使绘制出来的图片颜色更加平滑和饱满，图像更加清晰
        paint.setFilterBitmap(true);//函数是用来对位图进行滤波处理。
        paint.setColor(Color.WHITE);
        //paint.setColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
        //paint.setTextSize(25);
        Rect bounds = new Rect();
        paint.getTextBounds(str,0,str.length(),bounds);//获取文字的范围
        Log.e(TAG, "str:" + str.length());
        bitmap = scaleBitmap(bitmap,bounds);

        //文字在marker中展示的位置
        float paddingLeft =(bitmap.getWidth())/2;//在框中间显示
        Log.e(TAG, "paddingLeft:" + paddingLeft  + ";bounds:" + bounds.height() + ";" + bounds.width() + ";" + (bitmap.getWidth() )/2);

        Canvas canvas = new Canvas(bitmap);
        Paint paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintRect.setStyle(Paint.Style.FILL_AND_STROKE);
        paintRect.setColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
        float left = canvas.getHeight();
        Log.e(TAG, "left:" + left + ";" + canvas.getWidth());
        RectF r2=new RectF();                           //RectF对象
        r2.left=paddingLeft-bounds.width();                                 //左边
        r2.top= 0;                               //上边
        r2.right= paddingLeft + bounds.width();                                   //右边
        r2.bottom=bounds.height() +15;                              //下边
        Log.e(TAG, "r2:" +r2.left+";"+r2.top+";"+r2.right+";"+r2.bottom);
        canvas.drawRoundRect(r2, 10, 10, paintRect);        //绘制圆角矩形

        RectF r3 = new RectF();
        r3.left = 0;
        r3.top = 0;
        r3.right = bitmap.getWidth();
        r3.bottom = bitmap.getHeight();
        //canvas.drawRoundRect(r3, 10, 10, paintRect);        //测试画布的实际大小，方便查看图片是否在中点位置
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(20.0f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.WHITE);//采用的颜色
        canvas.drawText(str, r2.left+10 , bounds.height()+8 , textPaint);//绘制上去字，中间参数为坐标点
        Log.e(TAG, "paddingLeft:" + paddingLeft  + ";bounds:" + bounds.height()+10 + ";" + bounds.width() + ";" + (bitmap.getWidth() )/2);
        //合并两个bitmap为一个
        canvas.drawBitmap(bitmapDescriptor.getBitmap(),( bitmap.getWidth() - width) /2, bounds.height() + 15, null);//marker的位置
        return bitmap;

    }

    private Bitmap scaleBitmap(Bitmap bitmap, Rect bounds) {
        if (bounds == null || bitmap == null) {
            return bitmap;
        } else {
            // 记录src的宽高
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int bWidth = bounds.width();
            int bHeight = bounds.height();
            // 计算缩放比例
            int scaleWidth = bWidth;
            int scaleHeight = bHeight + height;

            int scale = bWidth/width;
            // 开始缩放



            return Bitmap.createScaledBitmap(bitmap, (scale + 2)*width, scaleHeight + 15, true);
        }

    }
}
