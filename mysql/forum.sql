create schema if not exists forum collate utf8mb4_0900_ai_ci;

create table if not exists files
(
    id           int auto_increment
        primary key,
    userId       varchar(50)   not null,
    path         varchar(50)   not null comment '文件的路径',
    time         datetime      not null comment '上传时间',
    size         char(10)      not null comment '文件大小，保存为KB',
    title        varchar(20)   not null comment '标题',
    introduction varchar(20)   not null comment '简介',
    integral_num int           not null comment '积分数',
    down_num     int default 0 not null comment '下载次数'
)
    comment '文件上传信息表';

create table if not exists integral
(
    id     int auto_increment
        primary key,
    userId varchar(50) not null,
    num    int         not null comment '积分总数'
)
    comment '积分总表，记录用户的总积分';

create table if not exists integral_item
(
    id            int auto_increment
        primary key,
    userId        varchar(50) not null,
    integral_type varchar(10) not null comment '积分的类型，例如文件上传获得等',
    num           int         not null comment '积分数',
    time          datetime    not null,
    type          varchar(10) not null comment '积分的类型，消费或者是赚取'
)
    comment '积分来源信息表';

create table if not exists login
(
    id     int auto_increment
        primary key,
    userId varchar(50) not null comment '用户ID',
    time   datetime    not null comment '登录的时间'
)
    comment '登录信息表';

create table if not exists record
(
    id               int auto_increment
        primary key,
    articlePrincipal varchar(20) null,
    userName         varchar(20) not null
)
    charset = utf8;

create table if not exists school
(
    id   int(2) auto_increment
        primary key,
    name varchar(16) not null comment '学院名称'
)
    charset = utf8;

create table if not exists major
(
    id       int(2) auto_increment
        primary key,
    name     varchar(16) not null comment '专业名称',
    schoolId int(2)      null,
    constraint major_school_id
        foreign key (schoolId) references school (id)
)
    charset = utf8;

create table if not exists sort
(
    id     int(2) auto_increment
        primary key,
    name   varchar(16) not null comment '分类名称',
    rely   varchar(50) null,
    number int         null
)
    charset = utf8;

create table if not exists user
(
    id       int auto_increment
        primary key,
    userId   varchar(50)      not null comment '邮箱，用户ID',
    qq       int(13)          null,
    password varchar(32)      not null,
    status   int(2) default 0 not null comment '用户状态0：未激活，1：激活',
    identify int(1) default 0 not null comment '1为管理员。0为普通成员',
    constraint userId
        unique (userId)
)
    charset = utf8;

create table if not exists userinfo
(
    no          int auto_increment
        primary key,
    id          varchar(50)             not null,
    nickName    varchar(16)             null comment '昵称',
    school      int(2)                  null comment '学院',
    major       int(2)                  null comment '专业',
    avatar      varchar(128) default '' not null comment '头像',
    phone       varchar(14)             null comment '电话',
    description varchar(128)            null comment '个人简介',
    constraint id
        unique (id),
    constraint userinfo_major_id
        foreign key (major) references major (id),
    constraint userinfo_school_id
        foreign key (school) references school (id),
    constraint userinfo_user_id
        foreign key (id) references user (userId)
)
    charset = utf8;

create table if not exists article
(
    id         int auto_increment comment '讨论内容ID'
        primary key,
    title      varchar(64)      not null comment '文章标题',
    userId     varchar(50)      not null comment '用户ID',
    time       varchar(20)      not null comment '发布时间',
    sortId     int(2)           not null comment '分类ID',
    context    text             not null comment '正文',
    readNumber int(3) default 0 not null comment '阅读次数',
    top        int(1) default 0 not null comment '置顶（1置顶，0不置顶）',
    userName   varchar(50)      null,
    contextMd  varchar(200)     null,
    principal  varchar(50)      null,
    constraint article_sort_id
        foreign key (sortId) references sort (id),
    constraint article_userinfo_id
        foreign key (userId) references userinfo (id)
)
    charset = utf8;

create table if not exists discuss
(
    id        int          not null comment '评论的ID'
        primary key,
    articleId int          not null comment '被评论的文章',
    userId    varchar(50)  not null comment '评论人',
    time      date         not null comment '评论时间',
    content   varchar(100) not null comment '评论内容',
    constraint discuss_article_id
        foreign key (articleId) references article (id),
    constraint discuss_userInfo_id
        foreign key (userId) references userinfo (id)
)
    charset = utf8;

create table if not exists reply
(
    id        int auto_increment comment '回复ID'
        primary key,
    articleId int          not null comment '被评论的文章',
    userId    varchar(50)  not null comment '用户ID',
    time      varchar(20)  not null comment '回复时间',
    content   varchar(100) not null comment '回复内容',
    replyTo   int          null comment '文章的评论的ID',
    userName  varchar(20)  null,
    constraint reply_artile_id
        foreign key (articleId) references article (id),
    constraint reply_user_id
        foreign key (userId) references userinfo (id)
)
    charset = utf8;

create index reply_discuss_id
    on reply (replyTo);

