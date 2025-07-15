package com.igs.ematf.dataresttest.model;

/**
 * 查询结果的类型
 * <p>
 * 与ContentTypeEnum 基本相同
 */
public enum SearchTypeEnum {
    /**
     * 页面
     */
    PAGE,
    /**
     * 博文
     */
    BLOGPOST,
    /**
     * 评论
     */
    COMMENT,
    /**
     * 日程表
     */
    CUSTOM,
    /**
     * 附件
     */
    ATTACHMENT,
    /**
     * 用户的信息
     */
    USERINFO,
    /**
     * 空间描述
     */
    SPACE_DESCRIPTION,
    /**
     * 文件夹
     */
    FOLDER,
    /**
     * 白板
     */
    BLANK,
    /**
     * 智能链接
     */
    LINK,
    /**
     * 数据库
     */
    DATABASE,

    ;
}
