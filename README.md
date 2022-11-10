# 热料小说sdk接入文档

|  版本号 | 日期 | 说明 |
| ---- | ---- | --- |
| 1.1.1 | 2022-8-10 | 适配 adroi-sdk:10.0.0.33 |
| 1.1.2 | 2022-8-12 | 优化UI，修复闪退bug |
| 1.1.4 | 2022-9-7 | 适配 adroi-sdk:10.0.0.51，增加个性化功能 |
| 1.1.9 | 2022-10-25 | 修复内存溢出问题,增加获取书架中书籍列表功能 |
| 1.2.2 | 2022-10-27 | 增加获取热度排行榜数据并进入对应书籍详情页功能 |
| 1.2.8 | 2022-11-09 | 修改广告展示相关bug |

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
// 热料小说sdk,xxx部分请填入对应的版本号
implementation 'io.github.vanzoosdk:ReliaoNovelSdk:xxx'
```

### 二、配置广告位
在项目的`assets`目录下新增`NOVEL_SDK_Setting.json`配置文件，配置文件中填入小说sdk对应的广告位id
```
{
  "detail_code_id": "书籍详情页广告位id",
  "chapter_code_id": "书籍阅读页章节中广告位id",
  "bottom_banner_code_id": "书籍阅读页底部banner广告位id",
  "reward_code_id": "书籍阅读页章节尾激励视频广告位id"
}
```

### 三、SDK初始化
在Application中的`onCreate`添加
```
//热料小说Sdk初始化
val channel = 1 //api请求中的的channel参数，表示渠道，1:飞觅浏览器, 2:搜搜, 3:最美天气
NovelSdk.init(this, channel, "NOVEL_SDK_Setting.json")
```

### 四、使用
创建NovelFragment
```
val novelFragment = NovelSdk.getNovelFragment()
```

#### 个性化功能
小说SDK为开发者提供了若干接口，以支持个性化功能开发。
##### 1、获取用户阅读记录
获取用户的阅读记录以及打开对应的小说，进入小说阅读页
```
//获取用户的阅读记录列表，返回List<BookRecordBean>
NovelSdk.getNovelRecord(context)

//打开阅读记录中对应的小说，进入小说阅读页
NovelSdk.openNovelReader(context,bookRecordBean)

```
##### 2、获取用户的书架以及打开对应的小说，进入小说阅读页
```
//获取用户的书架书籍列表，返回List<CollBookBean>
NovelSdk.getBookShelf(context)

//打开书架中中对应的小说，进入小说阅读页
NovelSdk.openNovelReader(context,collBookBean)

```

##### 3、获取/释放热度排行榜数据，进入书籍详情页
```
//获取热度排行榜数据,回调中返回List<Book>
NovelSdk.getRankData(object : RankDataCallBck<List<Book>> {
    override fun result(data: List<Book>) {
       //do something
    }
})

//打开热度排行中的书籍，进入书籍详情页
NovelSdk.openBookInfo(context,book)

```

### 五、其他
1.关于混淆 混淆规则已打包在aar包里




