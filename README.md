# AudioPlayer-on-net
* 网络流媒体传输实验
* 作者：王子旗
* CUG

## 1. 简介
### 1.1 基础功能
这是个用于学习实践的流媒体开发项目，目的是开发一个简单的基于UDP的应用层流媒体协议。
### 1.2 未来目标
设计一套系统，可以截取系统声音，并将声音通过流媒体的形式转播出去。目标是实现一个可以共享声音的APP。
    
## 2. 成果
  * ASoN协议 AudioStreamOnNet协议 网络音频流传输协议
    * 基于UDP协议，具有ASoN协议头，包含包序列号。
    * ASoNPacket是ASoN协议的协议包。
  * ACoN协议 AudioControlOnNet协议 网络音频控制协议
    * 开发中...