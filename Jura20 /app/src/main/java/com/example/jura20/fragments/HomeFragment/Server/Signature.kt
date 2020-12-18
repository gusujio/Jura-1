package com.example.jura20.fragments.HomeFragment.Server

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.Throws


class Signature {
    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(UnsupportedEncodingException::class)
    fun signWithHmacSha1(sk: String, canonicalString: String): String? {
        try {
            val signingKey =
                SecretKeySpec(sk.toByteArray(charset("UTF-8")), "HmacSHA1")
            val mac: Mac = Mac.getInstance("HmacSHA1")
            mac.init(signingKey)
            return Base64.getEncoder()
                .encodeToString(mac.doFinal(canonicalString.toByteArray(charset("UTF-8"))))
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return null
    }
}