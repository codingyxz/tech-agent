《bytebuddy核心教程》-学习笔记

视频教程: https://www.bilibili.com/video/BV1G24y1a7bd/
ByteBuddy github项目: https://github.com/raphw/byte-buddy
ByteBuddy官方教程: https://bytebuddy.net/#/tutorial-cn

ByteBuddy是基于ASM (ow2.io)实现的字节码操作类库。比起ASM，ByteBuddy的API更加简单易用。
开发者无需了解class file format知识，也可通过ByteBuddy完成字节码编辑。
ByteBuddy使用java5实现，并且支持生成JDK6及以上版本的字节码(由于jdk6和jdk7使用未加密的HTTP类库, 
作者建议至少使用jdk8版本.和其他字节码操作类库一样，ByteBuddy支持生成类和修改现存类
与与静态编译器类似，需要在快速生成代码和生成快速的代码之间作出平衡，ByteBuddy主要关注以最少的运行时间生成代码