# Websocket-sockjs

- 主流websocket通信，当浏览器不支持websocket使用sockjs替代方案。
- 使用邮箱注册，用了部分加密方式。
- 禁言模块方便管理。
- 消息系统多样性，使用约定型消息格式。
- 使用二级缓存增加性能。

4.10更新：
  - 前端修复一些意外的显示效果。
  - 全面修改EnCache的缓存方式，大部分数据都进行缓存，保证通信的及时效率，只有在修改禁言权限时才进行一次二级缓存同步，保证系统的高效运行。另外优化部分代码结构，提高运行效率。（上一版本的EnCache应用的非常差，看着闹心）

采用技术：前端bootstrap+Jquery
后端 SSH2加一点点SpringMVC
采用tomcat数据源（懒）。。

导入项目进入eclipse，右键Properties--》Chatroom/WebContent/WEB-INF/classes即可。
不要忘记设置context的数据源配置
