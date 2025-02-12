package com.example.pokemonultimate.ui.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object QrCodeUtils {

    fun generateQrCode(data: String, size: Int, logo: Bitmap? = null): Bitmap {
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, size, size)

        val qrBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)

        for (x in 0 until size) {
            for (y in 0 until size) {
                qrBitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        // Ajouter un logo au centre du QR Code si fourni
        return logo?.let { addLogoToQrCode(qrBitmap, it) } ?: qrBitmap
    }


    private fun addLogoToQrCode(qrBitmap: Bitmap, logo: Bitmap): Bitmap {
        val combinedBitmap = Bitmap.createBitmap(qrBitmap.width, qrBitmap.height, qrBitmap.config)
        val canvas = Canvas(combinedBitmap)

        canvas.drawBitmap(qrBitmap, 0f, 0f, null)

        val logoSize = qrBitmap.width / 5
        val scaledLogo = Bitmap.createScaledBitmap(logo, logoSize, logoSize, true)

        val left = (qrBitmap.width - logoSize) / 2f
        val top = (qrBitmap.height - logoSize) / 2f
        canvas.drawBitmap(scaledLogo, left, top, Paint(Paint.ANTI_ALIAS_FLAG))

        return combinedBitmap
    }
}
