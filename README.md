# AudioPlayer-on-net
* 网络流媒体传输实验
* 作者：王子旗
* CUG

## 1. 简介
### 1.1 基础功能
这是个用于学习实践的流媒体开发项目，目的是开发一个简单的基于UDP的应用层流媒体协议。
### 1.2 未来目标
设计一套系统，可以截取系统声音，并将声音通过流媒体的形式转播出去。目标是实现一个可以共享声音的APP。
    
## 2. API 
### ACoNProtocol.java:ACoN控制协议类
#### CMD_AUDIOFORMAT　传输音频流格式的命令代码  
#### CMD_COMMON　通用自定义命令代码  
#### cmdListener 命令回调函数接口  
#### ACoNProtocol(int , int , InetAddress , cmdListener) 构造函数
##### 参数：发送端口，接收端口，目的地址，命令处理监听器
#### void sendCMD_AudioFormat(AudioFormat AF) 发送命令：音频格式
##### 参数；发送的音频格式
#### void startWorking() 初始化后组件开始工作 
### ASoNProtocol.java:ASoN音频流媒体传输协议类
#### HEADLENGTH 报头长度
#### ASoNProtocol(int, int, InetAddress) 构造函数
##### 参数：发送端口，接收端口，目的地址
#### void startWorking() 初始化后组件开始工作
#### void sendData(byte[]) 发送数据报
##### 参数：数据报字节数组
#### ASoNPacket getData() 接收数据报
##### 返回值：ASoN数据报
