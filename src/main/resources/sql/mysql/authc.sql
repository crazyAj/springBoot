-- 用户表
create table user
(
    id               INT(64) PRIMARY KEY AUTO_INCREMENT,
    user_name        VARCHAR(64) NOT NULL,
    real_name        VARCHAR(64),
    password         VARCHAR(64) NOT NULL,
    salt             VARCHAR(64) NOT NULL,
    phone            VARCHAR(64),
    email            VARCHAR(64),
    sex              INT(1),
    create_time      TIMESTAMP,
    create_by        VARCHAR(64),
    last_update_time TIMESTAMP,
    update_by        VARCHAR(64),
    delete_flag      INT(1) default 0,
    remark           VARCHAR(64)
);


-- 角色表
create table role
(
    id               INT(64) PRIMARY KEY AUTO_INCREMENT,
    role_name        VARCHAR(64) NOT NULL,
    create_time      TIMESTAMP,
    create_by        VARCHAR(64),
    last_update_time TIMESTAMP,
    update_by        VARCHAR(64),
    delete_flag      INT(1),
    remark           VARCHAR(64)
);


-- 权限
create table permission
(
    id               INT(64) PRIMARY KEY AUTO_INCREMENT,
    permission_name  VARCHAR(64) NOT NULL,
    create_time      TIMESTAMP,
    create_by        VARCHAR(64),
    last_update_time TIMESTAMP,
    update_by        VARCHAR(64),
    delete_flag      INT(1),
    remark           VARCHAR(64)
);


-- 用户角色关联表
create table user_role
(
    id               INT(64) PRIMARY KEY AUTO_INCREMENT,
    user_id          VARCHAR(64) NOT NULL,
    role_id          VARCHAR(64) NOT NULL,
    create_time      TIMESTAMP,
    create_by        VARCHAR(64),
    last_update_time TIMESTAMP,
    update_by        VARCHAR(64),
    delete_flag      INT(1),
    remark           VARCHAR(64)
);


-- 角色权限关联表
create table role_permission
(
    id               INT(64) PRIMARY KEY AUTO_INCREMENT,
    role_id          VARCHAR(64) NOT NULL,
    permission_id    VARCHAR(64) NOT NULL,
    create_time      TIMESTAMP,
    create_by        VARCHAR(64),
    last_update_time TIMESTAMP,
    update_by        VARCHAR(64),
    delete_flag      INT(1),
    remark           VARCHAR(64)
);

