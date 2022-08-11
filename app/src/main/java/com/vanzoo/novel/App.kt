package com.vanzoo.novel

import android.app.Application
import com.adroi.polyunion.view.AdConfig
import com.adroi.polyunion.view.AdView
import com.adroi.polyunion.view.InitSDKConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.vanzoo.novelsdk.NovelSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initAdroiAd()
    }

    private fun initAdroiAd() {
        //聚合SDK初始化
        val builder = InitSDKConfig.Builder()
            .AppId("adroi广告appid") //在ADroi后台注册的应用id
            .TTAppName(resources.getString(R.string.app_name)) //在穿山甲SDK后台注册的应用名称
            .setHwAppName(resources.getString(R.string.app_name))
            .setClientId("test_client")
            .setChannel("test_channel")
            .setOaidProvider { "test_oaid" }
            /**
             * 若接入激励视频广告，可调用此方法设置横竖屏(默认竖屏)
             * 参数：
             * AdConfig.REWARD_VIDEO_SCREEN_VERTICAL    竖屏
             * AdConfig.REWARD_VIDEO_SCREEN_HORIZONTAL    横屏
             */
            .RewardVideoScreenDirection(AdConfig.REWARD_VIDEO_SCREEN_VERTICAL) //设置穿山甲SDK落地页风格
            .TTAdLoadingPageTheme(TTAdConstant.TITLE_BAR_THEME_LIGHT)
            /**
             * 若接入头条网盟广告，可调用以下方法(接收不定参数)设置允许直接下载的网络状态集合
             * 聚合SDK默认直接下载的网络为WIFI，接入应用根据需要，设置多种网络类型
             * 参数:
             * TTAdConstant.NETWORK_STATE_MOBILE 流量环境下
             * TTAdConstant.NETWORK_STATE_2G    2G网络环境
             * TTAdConstant.NETWORK_STATE_3G    3G网络环境
             * TTAdConstant.NETWORK_STATE_4G    4G网络环境
             * TTAdConstant.NETWORK_STATE_WIFI  Wifi网络环境
             */
            .TTAllowDownloadNetworkTypes(TTAdConstant.NETWORK_STATE_WIFI)
            .setAPIDirectDownloadNetworkTypes(AdConfig.NETWORK_TYPE_WIFI)
            .debug(true) // ADroi SDK 测试开关，服务器判定是否使用测试id


        /**
         * 测试时，再行打开，避免聚合最终使用了测试id进行发布
         */
        val initSDKConfig = builder.build()
        AdView.initSDK(this, initSDKConfig)
        //热料小说sdk初始化
        val channel = 1 //api请求中的的channel参数，表示渠道，1:飞觅浏览器, 2:搜搜, 3:最美天气
        NovelSdk.init(this, channel, "NOVEL_SDK_Setting.json")
    }
}