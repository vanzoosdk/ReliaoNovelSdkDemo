package com.vanzoo.novel

import android.app.Application
import android.content.Context
import android.provider.Settings
import com.bytedance.msdk.api.UserInfoForSegment
import com.bytedance.msdk.api.v2.*
import com.bytedance.msdk.api.v2.GMAdConstant.ADULT_STATE
import com.vanzoo.novelsdk.NovelSdk
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initGroMoreAd()
    }

    private var sInit = false
    private fun initGroMoreAd() {
        if (!sInit) {
            GMMediationAdSdk.initialize(this, buildV2Config(this))
            sInit = true
        }
        //NovelSdk初始化
        val channel = 1 //api请求中的的channel参数，表示渠道，1:飞觅浏览器, 2:搜搜, 3:最美天气
        NovelSdk.init(this, channel, "NOVEL_SDK_Setting.json")
    }
    private fun buildV2Config(context: Context): GMAdConfig {
            /**
         * GMConfigUserInfoForSegment设置流量分组的信息
         * 注意：
         * 1、请每次都传入新的info对象
         * 2、字符串类型的值只能是大小写字母，数字，下划线，连字符，字符个数100以内 ( [A-Za-z0-9-_]{1,100} ) ，不符合规则的信息将被过滤掉，不起作用。
             */
            /**
         * GMConfigUserInfoForSegment设置流量分组的信息
         * 注意：
         * 1、请每次都传入新的info对象
         * 2、字符串类型的值只能是大小写字母，数字，下划线，连字符，字符个数100以内 ( [A-Za-z0-9-_]{1,100} ) ，不符合规则的信息将被过滤掉，不起作用。
             */
        val userInfo = GMConfigUserInfoForSegment()
        userInfo.userId = "msdk-demo"
        userInfo.gender = UserInfoForSegment.GENDER_MALE
        userInfo.channel = "msdk-channel"
        userInfo.subChannel = "msdk-sub-channel"
        userInfo.age = 999
        userInfo.userValueGroup = "msdk-demo-user-value-group"
        val customInfos: MutableMap<String, String> = HashMap()
        customInfos["aaaa"] = "test111"
        customInfos["bbbb"] = "test222"
        userInfo.customInfos = customInfos
        var jsonObject: JSONObject? = null
        //读取json文件，本地缓存的配置
        //读取json文件，本地缓存的配置
        try {
            jsonObject = JSONObject(getJson("androidlocalconfig.json", context))
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val initConfig: MutableMap<String, Any> = HashMap()
        initConfig["1111"] = "22222"
        initConfig["22222"] = "33333"
        initConfig["44444"] = "5555"
        return GMAdConfig.Builder()
            .setAppId("填写在gromore中申请的appid")
            .setAppName(context.resources.getString(R.string.app_name))
            .setDebug(true) //默认false，测试阶段打开，可以通过日志排查问题
            .setPublisherDid(getAndroidId(context)!!)
            .setOpenAdnTest(false) //开启第三方ADN测试时需要设置为true，会每次重新拉去最新配置，release 包情况下必须关闭.默认false
            .setConfigUserInfoForSegment(userInfo)
            .setPangleOption(
                GMPangleOption.Builder()
                    .setIsPaid(false)
                    .setTitleBarTheme(GMAdConstant.TITLE_BAR_THEME_DARK)
                    .setAllowShowNotify(true)
                    .setAllowShowPageWhenScreenLock(true)
                    .setDirectDownloadNetworkType(
                        GMAdConstant.NETWORK_STATE_WIFI,
                        GMAdConstant.NETWORK_STATE_3G
                    )
                    .setIsUseTextureView(true)
                    .setNeedClearTaskReset()
                    .setKeywords("")
                    .build()
            )
            .setGdtOption(
                GMGdtOption.Builder()
                    .setWxInstalled(false)
                    .setOpensdkVer(null)
                    .setSupportH265(false)
                    .setSupportSplashZoomout(false)
                    .build()
            )
        /**
             * 隐私协议设置，详见GMPrivacyConfig
         */
            .setPrivacyConfig(object : GMPrivacyConfig() {
                // 重写相应的函数，设置需要设置的权限开关，不重写的将采用默认值
                // 例如，重写isCanUsePhoneState函数返回true，表示允许使用ReadPhoneState权限。
                override fun isCanUsePhoneState(): Boolean {
                    return true
                }
                //当isCanUseWifiState=false时，可传入Mac地址信息，穿山甲sdk使用您传入的Mac地址信息
                override fun getMacAddress(): String {
                    return ""
                }
                // 设置青少年合规，默认值GMAdConstant.ADULT_STATE.AGE_ADULT为成年人
                override fun getAgeGroup(): ADULT_STATE {
                    return ADULT_STATE.AGE_ADULT
                }
            })
            .setLocalExtra(initConfig)
            .setCustomLocalConfig(jsonObject)
            .build()


    }
    private fun getAndroidId(context: Context): String? {
        var androidId: String? = null
        try {
            androidId =
                Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return androidId
    }
    private fun getJson(fileName: String?, context: Context): String? {
        val stringBuilder = StringBuilder()
        try {
            val `is` = context.assets.open(fileName!!)
            val bufferedReader = BufferedReader(InputStreamReader(`is`))
            var line: String?
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}