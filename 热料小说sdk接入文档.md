# 热料小说sdk接入文档

|  版本号 | 日期 | 说明 |
| ---- | ---- | --- |
| 1.0.2 | 2022-8-1 | 适配 adroi-sdk:10.0.0.33 |


## SDK接入前说明
sdk接入了adroi广告，接入sdk前需要申请好相应广告位。

|  广告位名称 | 广告类型 | 展示位置 |
| ---- | ---- | --- |
| detail_code_id | 模板信息流广告 | 书籍详情页中间 |
| chapter_code_id | 信息流模板广告 | 书籍阅读页章中，每隔5页展示一个 |
| bottom_banner_code_id | 轮播banner广告 | 书籍阅读页底部，每隔30s轮播一次 |
| reward_code_id | 激励视频广告 | 书籍阅读页章节尾 |

## SDK接入

### 一、增加依赖
sdk内部用到了adroi广告，接入该sdk前，请务必先接入adroi广告。
以下是adroi广告接入完成后，接入本sdk的步骤。
1.在app工程的`dependencies`添加
```
    // 热料小说sdk
    implementation 'io.github.vanzoosdk:ReliaoNovelSdk:1.0.2'
```

### 二、配置广告位
在项目的`assets`目录下新增`NOVEL_SDK_Setting.json`配置文件，配置文件中填入小说sdk对应的广告位id
```
    {
      "detail_code_id": "xxx",
      "chapter_code_id": "xx
      "bottom_banner_code_id": "xxx",
      "reward_code_id": "xxx"
    }
```

### 三、SDK初始化
在Application中的`onCreate`添加
```
    //小说sdk初始化
    NovelSdk.initAdId(this,"NOVEL_SDK_Setting.json")
```

### 四、使用
创建NovelFragment并显示
```
    private val mFragments = mutableListOf<Fragment>()
    val novelFragment = NovelFragment()
    mFragments.add(novelFragment)
    mViewPager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getCount(): Int {
                return mFragments.size
            }

            override fun getItem(position: Int): Fragment {
                return mFragments[position]
            }

        }
```

### 五、其他
1.关于混淆 混淆规则已打包在aar包里




