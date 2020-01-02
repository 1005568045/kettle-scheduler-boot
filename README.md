
#### 安装教程

1.  安装过程中需要根据mysql数据库的版本设置数据库连接的driver-class-name，5.x版本的mysql数据库使用com.mysql.jdbc.Driver，6.0以上的使用com.mysql.cj.jdbc.Driver

#### 使用说明
1.  执行kettle-scheduler-starter下面docs下面的kettle-scheduler-调度平台脚本.sql脚本创建表

2.  生成环境执行时修改application-prod.yml中的数据库连接配置，开发环境修改application-dev.yml中的数据库配置，如果需要修改端口就在application.yml中修改

3.  修改application-kettle.yml配置，设置日志存储路径、kettle脚本保存路径、kettle-home路径（如果没有指定home路径，那么.kettle文件夹就在当前用户根路径下）

4.  如果需要自定义变量在kettle.properties中编写，并把kettle.properties文件拷贝到kettle-home路径下面的.kettle文件夹下

5.  启动项目使用调度平台

6.  如果生成环境要使用【文件资源库】需要单独把你的文件资源库拷贝到生成环境中，并在管理页面配置好文件资源库



###docker容器跑应用
1.多模块在父模块使用命令打包即可。
2.编写Dockerfile。项目中已写好dockerfile  
3.制作镜像   docker build -t 镜像名称  .   （dockerfile在此目录下）   
4.跑镜像    docker run -d -p 8088:8080 --name   镜像名称  容器名称                                                        


