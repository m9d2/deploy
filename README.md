# 简单的构建服务

## 启动

默认端口号: 4000

```shell
nohup java -server -Xms60m -Xmx100m -jar deploy-1.0.jar >/dev/null &2>&1 &
```
自定义服务端口
```shell
nohup java -server -Xms60m -Xmx100m -jar deploy-1.0.jar --server.port={port} >/dev/null &2>&1 &
```
> port替换成你自定义的端口号
## 构建
浏览器打开地址：
```
http://127.0.0.1:4000/deploy?path={shell path}
```
或通过命令：
```
curl http://127.0.0.1:4000/deploy?path={shell path}
```
> shell path替换成要执行的shell脚本绝对路径