package com.flippey.photopicker.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @ Author      Flippey
 * @ Creat Time  2017/3/20 10:25
 */

public class CommonUtils {
    /**
     * 转换图片成圆形
     *
     * @param sourceBitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap sourceBitmap, int width,
                                       int height, int borderWidth, int borderColor) {
        if (sourceBitmap == null || sourceBitmap.isRecycled()) {
            return null;
        }
        Bitmap bitmap = resizeBitmap(sourceBitmap, width);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (width <= 0) {
            width = bitmapWidth;
        }
        if (height <= 0) {
            height = bitmapHeight;
        }
        if (width > bitmapWidth) {
            width = bitmapWidth;
        }
        if (height > bitmapHeight) {
            height = bitmapHeight;
        }
        if (width > height) {
            width = height;
        } else {
            height = width;
        }
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;

        roundPx = width / 2;
        left = (bitmapWidth - width) / 2;
        top = (bitmapHeight - height) / 2;
        bottom = (bitmapHeight + height) / 2;
        right = (bitmapWidth + width) / 2;

        dst_left = 0;
        dst_top = 0;
        dst_right = width;
        dst_bottom = height;

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        if (borderWidth > 0) {
            final Paint mBorderPaint = new Paint();
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(borderColor);
            mBorderPaint.setStrokeWidth(borderWidth);
            // canvas.drawCircle(width >> 1, height >> 1, (width - borderWidth) >> 1, mBorderPaint);
            canvas.drawCircle(width / 2.0f, height / 2.0f, (width - borderWidth) / 2.0f, mBorderPaint);
        }
        canvas = null;

        return output;
    }



    private static Bitmap resizeBitmap(Bitmap bitmap, int wantSize) {
        if (wantSize <= 0) {
            return bitmap;
        }
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = wantSize;
        int newHeight = wantSize;

        float scale = 1.0f;
        if (width >= height) {
            scale = ((float) newHeight) / width;
        } else {
            scale = ((float) newWidth) / width;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }
}
