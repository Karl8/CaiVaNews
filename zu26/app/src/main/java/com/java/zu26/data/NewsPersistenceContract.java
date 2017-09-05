package com.java.zu26.data;

import android.provider.BaseColumns;

/**
 * Created by kaer on 2017/9/2.
 */

public final class NewsPersistenceContract {
    private NewsPersistenceContract() {}

    /* Inner class that defines the table contents */
    public static abstract class NewsEntry implements BaseColumns {
        // list item
        public static final String TABLE_NAME = "news";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CLASS_TAG = "classTag";
        public static final String COLUMN_NAME_PICTURES = "pictures";
        public static final String COLUMN_NAME_SOURCE = "source";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_INTRO = "intro";
        public static final String COLUMN_NAME_READ = "read";
        //--------------------------------------------------------------
        // detail
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_FAVORITE = "favorite";
    }
}
/*
last list:
"lang_Type": "zh-CN",
        "newsClassTag": "文化",
        "news_Author": "来源：北京晚报",
        "news_ID": "201609130413093b4f3cd78f4803b8f9895dcf7ed9f6",
        "news_Pictures": "",
        "news_Source": "其他",
        "news_Time": "20160912000000",
        "news_Title": "江波摘全球华语科幻星云奖大奖 自称“乌龟选手”",
        "news_URL": "http://www.chinanews.com/cul/2016/09-12/8001779.shtml",
        "news_Video": "",
        "news_Intro": "　　本报讯（记者陈
*/

/*
news detail:
{
    "seggedTitle":"...",<!--新闻分词后标题结果-->
    "seggedPListOfContent":[<!--新闻正文的分词结果-->
        "..."
    ],
    "persons":[  <!-- 人物列表 -->
        {
        "word":"弗拉基米尔",
        "count":2
        }
    ] ,
 "locations":[<!-- 地点列表 -->
    {
    "word":"里约",
    "count":2
    }
],"organizations":[<!--组织机构列表-->
],"Keywords":[ <!--  新闻中关键词列表 -->
    {
    "word":"柔道",
    "score":284.757301873237
    }
],
"wordCountOfTitle":11,
"wordCountOfContent":226,
"bagOfWords":[
    {
    "word":"参赛",
    "score":1.0
    }
],
"newsClassTag":"体育", <!--新闻所属的分类-->
"news_ID":"201608090432c815a85453c34d8ca43a591258701e9b",<!-- 新闻ID -->
"news_Category":"首页 > 新闻 > 环球扫描 > 正文", <!-- 新闻的类别 -->
"news_Source":"其他", <!-- 来源 -->
"news_Title":"德媒：俄柔道运动员里约夺金与普京密切相关", <!-- 标题 -->
"inborn_KeyWords":"", <!--新闻关键词-->
"news_Time":"Aug 8, 2016 12:00:00 AM",<!-- 时间 -->
"news_URL":"http://news.21cn.com/world/guojisaomiao/a/2016/0808/15/31396661.shtml",<!-- 新闻的URL链接 -->
"news_Author":"环球网|",<!--新闻的作者-->
"news_Content":"...",<!-- 新闻正文 -->
"lang_Type":"zh-CN",<!--新闻的语言类型-->
"crawl_Source":"news.21cn.com",<!-- 爬取来源 -->
"news_Journal":"翟潞曼",<!--记者列表-->
"crawl_Time":"Aug 9, 2016 3:43:17 AM",<!-- 爬取时间 -->
"news_Pictures":"http://img003.21cnimg.com/photos/album/20160808/m600/A3B78A702DF9BF0EE02ADFD5D4F53D54.jpeg",<!--新闻的图片路径-->
"news_Video":"",
"repeat_ID":"0"<!--与该条新闻重复的新闻ID-->
}
 */