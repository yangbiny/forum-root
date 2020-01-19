package cn.pzhu.forum.content;

/**
 * Redis存放Key值
 */
public class RedisKeyConstant {

    /**
     * 所有分类信息（不用给任何参数）
     */
    public static String SORT_LIST = "sort_list";

    /**
     * 指定用户的用户信息（需要给出用户ID）
     */
    public static String USER_USERINFO = "userInfo_";

    /**
     * 所有用户的用户信息（不用给参数）
     */
    public static String USERINFO_LIST = "userInfo_list";

    /**
     * 对应分类下的博客（需要给出分类的ID）
     */
    public static String SORT_ARTICLE_LIST = "sort_article_list_";

    /**
     * 全部博客（不用给参数）
     */
    public static String ARTICLE_LIST = "article_list";

    /**
     * 对应用户的博客（需要给出用户名字（唯一的））
     */
    public static String USER_ARTICLE_LIST = "user_article_list_";

    /**
     * 置顶文章列表（不用给参数）
     */
    public static String ARTICLE_TOP_LIST = "article_top_list";

    /**
     * 对应文章的讨论列表（需要给出文章的ID）
     */
    public static String ARTICLE_REPLY_LIST = "article_reply_list_";

    /**
     * ArticleType对下对应的分类信息（需要给出ArticleType的type的值）
     */
    public static String SORT_ARTICLETYPE_LIST = "sort_articleType_list_";

    /**
     * 指定关键字下的博客信息（给出一个关键字）
     */
    public static String ARTICLE_LIST_KEY = "article_list_key_";

    /**
     * 指定文章关键字下的回复的数量
     */
    public static String ARTICLE_RECORD_LIST_COUNT = "article_record_list_count_";

    /**
     * 用户点赞标志（需要给出用户名和博客的主要关键字）
     */
    public static String USER_ARTICLE_RECORD_FLAG = "user_article_record_flag_";

    /**
     * 查询与指定标题类似的文章（需要给出文章标题）
     */
    public static String SIMILAR_ARTICLE_LIST = "similar_article_list_";

    /**
     * 查询指定学院下的专业信息（需要给出学院的ID）
     */
    public static String MAJOR_LIST = "majorList_";

    /**
     * 列出所有的专业信息
     */
    public static String MAJORS = "major";
}
