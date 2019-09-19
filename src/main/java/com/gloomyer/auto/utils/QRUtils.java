package com.gloomyer.auto.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/**
 * 二维码生成工具类
 */
public class QRUtils {


    /**
     * 生成二维码
     *
     * @param width   宽
     * @param height  高
     * @param content 内容
     * @throws IOException 异常
     */
    public static void createQrImg(
            int width, int height, String content,
            File saveFile) throws IOException {
        FileOutputStream fos = null;
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);// 纠错等级L,M,Q,H
            hints.put(EncodeHintType.MARGIN, 0); // 边距
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, height, width, hints);
            fos = new FileOutputStream(saveFile);
            MatrixToImageWriter.writeToStream(bitMatrix, "jpg", fos);
            fos.flush();
        } catch (WriterException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
