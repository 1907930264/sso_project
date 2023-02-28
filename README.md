# sso_project
sso-server配置：

sso-server为独立启动的springboot项目，主要集成了springsecurity实现认证功能。启动项目之前需要先配置redis和mysql

db配置：

第一：建立mysql库：sso_server
第二：执行sql语句：
           DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` bigint(20) NOT NULL,
  `login_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `phone_number` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `id_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `del_flag` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;



sso-client配置：

第一：代码拉下来后进入sso-client文件夹执行maven命令: mvn clean install -Dmaven.test.skip=true 生成本地jar包

第二：新建其他springboot项目A,并且引入maven依赖：

        <dependency>
            <groupId>com.yxj</groupId>
            <artifactId>sso-client</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        
第三：在项目A里配置关于sso-client的一些yml配置：

sso-config:
  #ssoServerUrl 为ssoServer登陆页地址
  ssoServerUrl: http://${sso服务地址}/sso/loginPage.html
  #filterGlobalSwitch 全局开关，开启sso单点登陆拦截，需配置为true
  filterGlobalSwitch: true
  #customAuthenticationSwitch 是否开启业务系统自定义认证判断，如果为true，则需要实现CustomAuthenticationInterface接口并重写认证判断逻辑
  customAuthenticationSwitch: false
  #urlPattens 过滤器过滤，配置/*说明对所有请求进行拦截
  urlPattens:
    - /*
#    - /testCookie/*
  #callbackUrl 业务系统的回调地址，在ssoserver认证通过后回调回业务系统
  callbackUrl: http://localhost:${server.port}/testCookie/a
  #analysisTokenUrl 调用ssoServer服务，用于解析token，获取用户数据接口
  analysisTokenUrl: http://${sso服务地址}/sso/claims



3、cd sso_client 进入目录

4、执行maven命令： mvn clean install -Dmaven.test.skip=true
