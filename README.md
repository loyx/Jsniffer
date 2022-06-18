# Jsniffer 是什么 [![License](https://img.shields.io/github/license/loyx/Jsniffer)](https://www.apache.org/licenses/LICENSE-2.0) [![size](https://img.shields.io/github/repo-size/loyx/Jsniffer)](https://github.com/loyx/Jsniffer) [![lang](https://img.shields.io/github/languages/top/loyx/Jsniffer)](https://github.com/loyx/Jsniffer) ![status](https://img.shields.io/github/checks-status/loyx/Jsniffer/master)
![image](https://user-images.githubusercontent.com/30404367/170512376-079781b2-ed4f-4345-841d-03121b8caba4.png)

JSniffer 基于Jnetpcap的跨平台的抓包工具
![mian](https://user-images.githubusercontent.com/30404367/174430752-053ccd35-59c8-4480-9f85-048f1c2cf1d1.gif)

# 项目结构
```text
Jsniffer:.
├─.idea
│  └─artifacts
└─src
    └─main
        ├─java
        │  └─cn/loyx/Jsniffer
        │      ├─capture
        │      │  └─Protocols
        │      │      ├─application
        │      │      ├─lan
        │      │      ├─network
        │      │      ├─tcpip
        │      │      └─wan
        │      ├─service
        │      └─ui
        └─resources
            ├─icons
            ├─lib
            └─META-INF
```
# 功能
## 1. 网卡选择
![网卡选择](https://user-images.githubusercontent.com/30404367/173275354-26854580-f7ff-4ae9-b32d-5d8141561322.gif)
## 2. packet解析
![packet解析](https://user-images.githubusercontent.com/30404367/173275367-32d7fb3e-5835-4770-8a5f-8efacef447fc.gif)
## 3. packet过滤
![伯克利过滤器](https://user-images.githubusercontent.com/30404367/173275416-409cf165-5680-4c2a-b5d2-610a29f206fe.gif)
## 4. 其他功能
![其他功能](https://user-images.githubusercontent.com/30404367/173275432-3a5618e7-de70-49fe-b88c-291af4deb792.gif)
## License
The Jsniffer is released under version 2.0 of [Apache License](https://www.apache.org/licenses/LICENSE-2.0).
