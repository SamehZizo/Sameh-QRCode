package com.sameh.qrcode.QRCreate;

import android.app.Activity;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.sameh.qrcode.R;

public class QRCreator {

    private Activity activity;
    private final int QRCodeSize = 500;

    public QRCreator(Activity activity) {
        this.activity = activity;
    }

    public void Create(String text , Completed completed){
        try {
            Bitmap bitmap = TextToQRCodeEncode(text);
            completed.onComplete(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private Bitmap TextToQRCodeEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRCodeSize, QRCodeSize, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        activity.getResources().getColor(R.color.black):activity.getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    public interface Completed{
        void onComplete(Bitmap bitmap);
    }

}
