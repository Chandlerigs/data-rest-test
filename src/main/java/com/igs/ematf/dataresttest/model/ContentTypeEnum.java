package com.igs.ematf.dataresttest.model;

/**
 * 内容的类型
 *
 * @author Samuel
 * @since 2025/4/21 11:43
 */
public enum ContentTypeEnum {
    /*
    current：正式版本，没有perversion，可能有，可能没有pageParent, 有space
            历史版本，有perversion，没有pageParent, 没有space
    draft:未发布的，没有perversion，有pageParent, 有space
            已发布的，有perversion，没有pageParent, 有space
     */
    PAGE, // 页面

    /*
    current：正式版本，没有perversion，没有pageParent, 有space
            历史版本，有perversion，没有pageParent, 没有space
    draft:未发布的，没有perversion，没有pageParent, 有space
            已发布的，有perversion，没有pageParent, 有space
     */
    BLOGPOST, // 博文

    /*
    current：正式版本，没有perversion，没有pageParent, 有contentParent， 可能有，可能没有commentParent，没有space
            历史版本，有perversion，没有pageParent, 没有contentParent， 没有commentParent，没有space
    draft: 没有
     */
    COMMENT, // 评论
    CUSTOM, // 日程表

    /*
    current：正式版本，没有perversion 有contentParent， 有space
            历史版本，有perversion，有contentParent，有space
    draft: 没有
     */
    ATTACHMENT, // 附件
    USERINFO, // 用户的信息
    SPACE_DESCRIPTION, // 空间描述
    /*
    类似page
     */
    FOLDER, // 文件夹
    BLANK, // 白板
    LINK, // 智能链接
    DATABASE, // 数据库

    ;
}
