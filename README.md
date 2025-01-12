# MindustryTomlParser ![](assets/icon.png)

### 这是一个mindustry的Toml模组文件解析器

### 外部库:tomlj
# 如何使用?

TomlParsers 本身作为模组前置库加载

请在你的

mod.json添加 "dependencies":["toml-parser"],

mod.hjson添加 dependencies:["toml-parser"]

新建文件夹 **contentToml** 替换掉 **content** 对,直接删

contentToml文件夹同样支持json和hjson的解析

然后你就可以快乐的编写toml了

### toml文件放在哪里？

contentToml文件夹中

contentToml和content的结构一样 例如

contentToml/blocks 和 content/blocks 是等价的

本质上就是content换了个名字

### content 还可以编写json吗？

理论上可以,但content是由原版进行解析,这可能会导致content加载的时候,contentToml还没有进行加载,从而导致内容加载报错,但是contentToml也支持json和hjson的加载,所以我们不建议在往content文件夹中编写文件

### 文件迁移

content有之前写过的json？没有关系,直接将content内的文件全部移动到contentToml即可

### 不会编写toml？ toml是什么?

#### 【比 JSON、YAML 更好的配置文件语言？】https://www.bilibili.com/video/BV1sw4m1v7Bj?vd_source=076506b59e7d7c3d253d427f2e529169