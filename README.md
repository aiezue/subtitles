# 云散月明谁点缀，天容海色本澄清

<h2 class="pm-node nj-subtitle">一个迷你字幕语料库的搭建、检索和使用</h2>

<p class="pm-node nj-authors">信阳农林学院 闫达 高宇</p>

# 总体思路

本项目组的两名成员，来自信阳农林学院，对于语料库有着很高的热情，但是长久以来，我们的热情都存在于空想阶段和兴趣阶段。机缘巧合之下，我们选择开始我们的实践，我们期望我们一方面可以在研究过程中学到更多，另一方面，也想踏踏实实的建设出来能为更多人使用的特色语料库。如下便是我们在项目初期实现的功能和我们的一些思考的展示。

## 现有问题

项目组认为语料库研究和发展中存在如下问题：

1. 语料搜集工作过于缓慢
2. 语料整理工作缺乏高度自动化
3. 语料库相关软件平台局限性太强。
4. 语料库软件严重落伍和缺乏更新
5. 语料库与翻译和教学的结合程度不够高。

## 实现方法构想

项目组拟定使用Clojure程序设计语言，结合JVM平台的优势，实现并发、高速的语料库搜集、整理、查询、存储和与其他领域互通等功能。

最终的成品，应当构架起一套独立的生态系统，源代码开放、跨平台，具有很强的扩展性。

## 创新之处

本项目组拟定在实现过程中实现如下的创新，并解决1.1中提出的全部预设问题。

1. 将一些具有格式上鲜明特色的文本转化为语料库。
2. 提高专业化文本的语料搜集、整理工作。
3. 构建一套独立的、从零开始创造的语料库生态系统。
4. 为语料库提供更快的搜索和查询方式，例如微信小程序等。

## 难度所在

从零开始构建生态系统，需要实现和完善语料库和计算语言学框架下许多不同层面的问题。为了提高效率，也许要结合机器学习和人工智能等最新前沿技术，整体开发难度较大。

# 迷你语料库概览

## 迷你语料库的目标

本项目为项目组长期项目的起点和一个较小的分支项目，本项目的目标为通过英语电影字幕，整理和生成字幕语料库，以便查询和使用，并对语料库进行语言学和统计学的分析，提高语料库的使用效果。

## 取得存放的字幕

整个迷你语料库程序以及所使用的字幕，我都已经发在了github上，如下就是这个目前处于**开放状态**的repo。感兴趣的话，可以随时fork和关注，并提出宝贵的意见。😄️

因为在repo之中已经生成了部分阶段性的展示结果，因此首先将这些阶段性的成果文件删除，以便下面重新生成。

```bash id=107f8a1a-d8e8-4de5-b6b0-95458e75d4f1
ls /subtitles && rm /subtitles/*.dat && rm /subtitles/subtitles_corpus.png && echo "============These are the new files=============" && ls /subtitles
```

   可以看到的是，我们已经将所有的阶段性的成果都删除了。目前只留下了

* clojure语言需要的配置文件 deps.edn（下文可以见到其内容）
* 程序源代码文件夹src
* 字幕存放的文件夹srt
* 语言处理需要的模型目录models

## 字幕基本知识

这里选择的字幕格式是srt。

> **SubRip** is a [free software](https://en.wikipedia.org/wiki/Free_software) program for [Windows](https://en.wikipedia.org/wiki/Microsoft_Windows) which extracts [subtitles](https://en.wikipedia.org/wiki/Subtitle_(captioning)) and their timings from various video formats to a text file. It is released under the [GNU](https://en.wikipedia.org/wiki/GNU) [GPL](https://en.wikipedia.org/wiki/GPL).Its subtitle format's file extension is `.srt` and is widely supported. Each `.srt` file is a [human-readable file format](https://en.wikipedia.org/wiki/Human-readable_format) where the subtitles are stored sequentially along with the timing information. Most subtitles distributed on the Internet are in this format.
>
> \--Wikipedia

如下是一个韩文的字幕文件：

```plaintext no-exec id=d7911731-a0d9-407c-8a65-a8836d780b81
00:03:17,440 --> 00:53:20,375
칠레 한인회에서는  그동안 코로나-19로 인해서 빠트로나또 한인타운가가 5개월 동안 문을 닫은 상황이었는데 오늘(9월7일) 개장하게 되어 코로나-19 확산을 방지하기 위해서 방역 활동을 하였습니다.
정성기 칠레 한인회장님께서 방역 활동을 하고 계십니다.  방역 활동 구역은 만사노, 산타 필로메나, 빠트로나또, 에우세비오 리죠, 안토니아 로페즈 데 베죠 거리를 방역하였습니다.
황성남 한인회이사님께서도 방역 활동에 동참 해 주셨습니다.

2
00:54:20,476 --> 03:16:22,501
까날 트레쎄 13번 칠레 방송국에서 빠트로나또 개장 현황을 취재하던 중에 한인회의 방역 활동을 목격하고 촬영을 하고 있습니다.
촬영이 끝난 후 인터뷰 요청이 있어서 정성기 한인회장님께서 인터뷰를 하게 되었습니다. 
금일 9월7일 월요일 밤 9시 뉴스시간에 촬영 및 인터뷰 내용이 방영 된다고 합니다.
<ref></ref>우리 모두 코로나-19 퇴치 운동에 적극 동참하여 또 다시 자가격리로 되돌아가는 일 없이 자유로운  활동이 계속 이어지도록 모두 협력했으면 합니다.  감사합니다.
```

![03-07-2017-SRT-FileFInal.jpg][nextjournal#file#9dbb8d08-380f-4862-bdea-8df979a55469]

除了文字之外，字幕文件还包括一定的格式。往往借用HTML的tag来实现。

> * Bold – `<b>…</b>` or `{b}…{/b}`
> * Italic – `<i>…</i>` or `{i}…{/i}`
> * Underline – `<u>…</u>` or `{u}…{/u}`
> * Font color – `<font color="color name or #code">…</font>` (as in [HTML](https://en.wikipedia.org/wiki/HTML))

对于语料库的准备而言，这些均属于干扰信息，我们需要将其去除。

## 我们的语料

是时候看看我们的语料是什么样子了。😸️

首先我们来装个软件查看文件，这里使用bat，比系统提供的cat更加强大。😄️

```bash id=6cac1e85-802c-4eae-85b0-971d958a143d
curl -LO https://github.com/sharkdp/bat/releases/download/v0.16.0/bat_0.16.0_amd64.deb
dpkg -i bat_0.16.0_amd64.deb && rm *.deb
```

这里来看看我们的字幕：

为了节约空间，这里只用前300行给大家做一个示范。具体的完整字幕，可以从github的repo获得。不难看出，我们的字幕文件是标准而规范的。

```bash id=579e7de8-2ca8-4a51-a857-7956abc4e9b0
bat -r :300 /subtitles/srt/*.srt
```

# 清理思路

## 问题认知

文本的清理其实是一个很重要的过程。我们再来看一下我们语料中需要清理的部分。

> 73 
>
> 00:05:55,753 --> 00:05:56,903 
>
> **Chen Yi's New Fourth Army**

除了黑体部分的内容，其余的都是无用信息。

上面的数字中的第一行，代表电影的场景编号。

第二行中看起来非常复杂的数字是该行字幕出现和结束的时间，字幕文件正式通过这个起点和终点来使字幕有条不紊的出现在电影中。

虽然我们此处搜集的4个电影的字幕文件都没有使用上面说过的格式tag，但是我们要充分考虑兼容性。如果出现格式tag，也许要处理。包含格式tag的字幕行列如下所示：

> 6 
>
> 00:02:36,040 --> 00:02:40,158 
>
> I'm honoured by my **<i>**comrades'**</i>** nomination...

## 分析问题

既然需要去除这两点冗余信息，那么我们来分析分析它们吧。

### Tag分析

tag的问题非常容易解决，因为tag沿用了HTML的tag格式，所以无论是什么样的格式，tag均由下面的模式构成：

> <X>TEXT</X>

大部分的格式之中，X和后面的/X是一样的，但是其实是否一致根本不重要，对于我们来讲，只需要将这个问题识别为，前后两个由尖括号包围起来的标签，后面一个由/开始即可。

### 数字分析

通过上文，我们发现，所有的数字都是由场景编号+时间轴起止构成的，两者中间有一个空行。

因此我们可以将这个问题识别为：

> 数字\\n两个数字:两个数字：两个数字，三个数字 --> 两个数字：两个数字：两个数字，三个数字\\n

这样来看，这两个构成元素完全可以同时处理，因为所有的场景编号和时间轴起止点都是一起出现的，中间有一个空行。

# 神奇武器登场

问题已经经过了分析，现在是解决的时候了。我们需要使用**正则表达式**来实现我们的目标。

## 正则表达式简介

**正则表达式**又称规则表达式。（英语：Regular Expression，在代码中常简写为regex、regexp或RE），正则表达式通常被用来检索、替换那些符合某个模式（或规则）的文本。

正则表达式是对字符串操作的一种逻辑公式，就是用事先定义好的一些特定字符、及这些特定字符的组合，组成一个**规则字符串**，这个规则字符串用来表达对字符串的一种**过滤逻辑**。

正则表达式具有如下的特点：

1. 灵活性、逻辑性和功能性非常强
2. 可以迅速地用极简单的方式达到字符串的复杂控制
3. 对于刚接触的人来说，比较晦涩难懂

我们要做的事情，就是按照正则表达式，将我们预设需要处理的文本进行匹配。从各种各样的情况中，找出一条条能帮我们全面覆盖所有问题的规则。

![RegEx-语法.jpg][nextjournal#file#3df4c30a-a449-4aa1-9c0a-d2f694eefef0]

## 正则表达式的语法

一个正则表达式通常被称为一个模式（pattern），为用来描述或者匹配一系列匹配某个句法规则的字符串。例如：`Handel`、`Händel` 和 `Haendel` 这三个字符串，都可以由 `H(a|ä|ae)ndel` 这个模式来描述。

### **普通字符**

普通字符包括没有显式指定为元字符的所有可打印和不可打印字符。这包括所有大写和小写字母、所有数字、所有标点符号和一些其他符号。

### **非打印字符**

非打印字符也可以是正则表达式的组成部分。下表列出了表示非打印字符的转义序列：

![1499147415531032865.png][nextjournal#file#fa0e1bb8-a80d-4bc3-b9db-4605d8f7c71b]

### 特殊字符

下表包含了单字符元字符的列表以及它们在正则表达式中的行为。

注意：若要匹配这些特殊字符之一，必须首先转义字符，即，在字符前面加反斜杠字符 (\\)。 例如，若要搜索“+”文本字符，可使用表达式“\\+”。

![1499147415128073223.png][nextjournal#file#3790d426-e6cf-45ad-bab6-6b1f98b20893]

大多数特殊字符在括号表达式内出现时失去它们的意义，并表示普通字符。

### 元字符

![1499147415267019107.png][nextjournal#file#f6160593-2f2f-45d3-b642-5465d0ef4d96]

### 顺序

正则表达式的计算方式与算术表达式非常类似；即从左到右进行计算，并遵循优先级顺序。

字符具有高于替换运算符的优先级，例如，允许“m|food”匹配“m”或“food”。

## 一些具体的例子

### 数字

```clojure no-exec id=fb2c878f-c63f-4216-8a15-6338fe67f6e4
数字：^[0-9]*$
n位的数字：^\d{n}$
至少n位的数字：^\d{n,}$
m-n位的数字：^\d{m,n}$
零和非零开头的数字：^(0|[1-9][0-9]*)$
非零开头的最多带两位小数的数字：^([1-9][0-9]*)+(.[0-9]{1,2})?$
带1-2位小数的正数或负数：^(\-)?\d+(\.\d{1,2})?$
正数、负数、和小数：^(\-|\+)?\d+(\.\d+)?$
有两位小数的正实数：^[0-9]+(.[0-9]{2})?$
有1~3位小数的正实数：^[0-9]+(.[0-9]{1,3})?$
非零的正整数：^[1-9]\d*$ 或 ^([1-9][0-9]*){1,3}$ 或 ^\+?[1-9][0-9]*$
非零的负整数：^\-[1-9][]0-9″*$ 或 ^-[1-9]\d*$
非负整数：^\d+$ 或 ^[1-9]\d*|0$
非正整数：^-[1-9]\d*|0$ 或 ^((-\d+)|(0+))$
非负浮点数：^\d+(\.\d+)?$ 或 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$
非正浮点数：^((-\d+(\.\d+)?)|(0+(\.0+)?))$ 或 ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
正浮点数：^[1-9]\d*\.\d*|0\.\d*[1-9]\d*$ 或 ^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
负浮点数：^-([1-9]\d*\.\d*|0\.\d*[1-9]\d*)$ 或 ^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
浮点数：^(-?\d+)(\.\d+)?$ 或 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
```

### 字符相关

```clojure no-exec id=585d8eeb-d36d-4ab4-8b6b-941400163126
汉字：^[\u4e00-\u9fa5]{0,}$
英文和数字：^[A-Za-z0-9]+$ 或 ^[A-Za-z0-9]{4,40}$
长度为3-20的所有字符：^.{3,20}$
由26个英文字母组成的字符串：^[A-Za-z]+$
由26个大写英文字母组成的字符串：^[A-Z]+$
由26个小写英文字母组成的字符串：^[a-z]+$
由数字和26个英文字母组成的字符串：^[A-Za-z0-9]+$
由数字、26个英文字母或者下划线组成的字符串：^\w+$ 或 ^\w{3,20}$
中文、英文、数字包括下划线：^[\u4E00-\u9FA5A-Za-z0-9_]+$
中文、英文、数字但不包括下划线等符号：^[\u4E00-\u9FA5A-Za-z0-9]+$ 或 ^[\u4E00-\u9FA5A-Za-z0-9]{2,20}$
可以输入含有^%&’,;=?$\”等字符：[^%&’,;=?$\x22]+
禁止输入含有~的字符：[^~\x22]+
```

### 特殊

```clojure no-exec id=ade85264-95a7-4437-a964-b1eafb6cbda7
Email地址：^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
域名：[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?
InternetURL：[a-zA-z]+://[^\s]* 或 ^http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$
手机号码：^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$
电话号码(“XXX-XXXXXXX”、”XXXX-XXXXXXXX”、”XXX-XXXXXXX”、”XXX-XXXXXXXX”、”XXXXXXX”和”XXXXXXXX)：^($$\d{3,4}-)|\d{3.4}-)?\d{7,8}$
国内电话号码(0511-4405222、021-87888822)：\d{3}-\d{8}|\d{4}-\d{7}
身份证号(15位、18位数字)：^\d{15}|\d{18}$
短身份证号码(数字、字母x结尾)：^([0-9]){7,18}(x|X)?$ 或 ^\d{8,18}|[0-9x]{8,18}|[0-9X]{8,18}?$
帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)：^[a-zA-Z][a-zA-Z0-9_]{4,15}$
密码(以字母开头，长度在6~18之间，只能包含字母、数字和下划线)：^[a-zA-Z]\w{5,17}$
强密码(必须包含大小写字母和数字的组合，不能使用特殊字符，长度在8-10之间)：^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$
日期格式：^\d{4}-\d{1,2}-\d{1,2}
一年的12个月(01～09和1～12)：^(0?[1-9]|1[0-2])$
一个月的31天(01～09和1～31)：^((0?[1-9])|((1|2)[0-9])|30|31)$
```

## 我们的问题

有了上面的学习经验，我们的问题其实很容易解决。我们需要做的是把他们替换成空字符，或类似的替换。

### tag问题

```clojure id=d6516a30-b2d5-4223-b217-2bc74505cd8f
"</?.+?>"
```

### 数字问题

```clojure id=8b69857a-eaa4-415b-bcd8-d46a5697e256
"\d+\n\d{2}:\d{2}:\d{2},\d{3} --> \d{2}:\d{2}:\d{2},\d{3}"
```

# 语言选择

## Clojure介绍

Clojure是Lisp编程语言在Java平台上的现代、动态及函数式方言。与其他Lisp一样，Clojure视代码为数据且拥有一套Lisp宏系统。 其开发过程目前由社区驱动。Clojure提倡不可变性（immutability）与持久数据结构（persistent data structures）并鼓励程序员显式地管理标识（identity）及其状态（state）。对利用不可变值（immutable values）及显式时间进展构造（explicit progression-of-time constructs）进行编程的专注旨在促进更加健壮的（尤其是并发）程序的开发。Clojure的类型系统是完全动态的，但人们近期也开始探索其基于渐进类型化（gradual typing）的实现。

## Clojure有什么优点

### **Clojure能够吸引人的很重要一点是它是JVM之上的语言，这个决定非常关键**。

首先，因为根植于JVM之上，并且做到了跟Java语言的相互调用，它能吸引很多成熟的Java开发者。其次，它可以使用Java社区丰富的开源软件，不需要从头去构建一个社区，你可以看到很多Clojure开源代码都是简单地包装Java的开源包，但是通过Clojure高度抽象简单的语法提供更便利的使用的方式； 第三，由于JVM平台本身的高度成熟和优化，clojure的编译器生成的byte code跟Java编译器生成的byte code并无二致（不完全是），它的性能和稳定性也能马上得到保证，这比从头构建一个新平台成本低得多。

构建于JVM之上，Clojure就是一门“严肃”的语言，而非很多人眼中的Lisp“玩具”语言，你学习后可以马上使用并且实践。但是Clojure又是Lisp方言，Lisp的神奇能力它还都保留，这样兼具美学和实用的语言如何让人不爱？我相信很多熟悉Scheme之类方言的童鞋，并且有Java背景的，都会对Clojure有相见

恨晚的感觉。

### **Clojure的设计原则可以概括成5个词汇：简单、专注、实用、一致和清晰**

1. 简单： 鼓励纯函数，极简的语法（少数special form），个人也认为clojure不能算是多范式的语言（有部分OO特性），为了支持多范式引入的复杂度，我们在C++和Scala身上都看到了。 
2. 专注：前缀运算符不需要去考虑优先级，也没有什么菱形继承的问题，动态类型系统（有利有弊），REPL提供的探索式编程方法（告别修改/编译/运行的死循环，所见即所得）。 
3. 实用：前面提到，构建在JVM之上，跟Java语言的互操作非常容易。直接调用Java方法，不去发明一套新的调用语法，努力规避Java语言中繁琐的地方(doto,箭头宏等等）。 
4. 清晰：纯函数（前面提到），immutable var，immutable数据结构，STM避免锁问题。不可变减少了心智的负担，降低了多线程编程的难度，纯函数也更利于测试和调试。 
5. 一致：语法的一致性：例如doseq和for宏类似，都支持destructring,支持相同的guard语句（when,while）。数据结构的一致性：sequence抽象之上的各种高阶函数。

## Hello World

```clojure id=f494fcac-e21e-47a4-b2cc-e1e5421ac4b0
(defn hello ; 函数名
  [& args] ; 参数向量 (`&`表示可变参数)
  (println "Hello, World!")) ; 函数体

(hello)
```

## Clojure环境搭建

我们使用Clojure官方的deps.edn来配置我们的环境，这里只做了两件事，第一件是选择了目前最新的版本的Clojure作为我们的工作语言。第二件是把我们将会使用的包加入到我们的工作环境中。非常简单，因为NextJournal的强大，一切都如此美妙。

```edn no-exec id=ffcf0396-b3f9-40e6-a0c2-654401879781
{:deps 
 {org.clojure/clojure {:mvn/version "1.10.1"}
  compliment {:mvn/version "0.3.9"}
  clojure-opennlp/clojure-opennlp {:mvn/version "RELEASE"}
  com.kennycason/kumo-core {:mvn/version "RELEASE"}
  com.hypirion/clj-xchart {:mvn/version "RELEASE"}}}
```

我们来看看版本对不对。

```clojure id=0fbac8d1-5212-450a-a2c4-1d6789152130
{:hello (clojure-version)} ;; see which of clojure it is running
```

看起来是可以的。那我们就准备开始出发了。

# 代码实现

## 语料库生成

这里我们的任务包括如下两点：

* 首先需要从目录里读取文件，并对每一个文件操作，最后生成一个语料库文件。
* 对每个文件而言，需要完成上述的两点替换工作。
* 因为会经常执行程序，所以一定要保证之前生成的语料库在每次操作之前，都被删除。

```clojure id=be73c457-d1e2-4ec4-bc23-174a77ff3a90
(require '[clojure.string :as string])
(require '[clojure.java.io :as io])

(defn rm
  "delete existing corpus, and create a new one afterwards"
  [& _]
  (io/delete-file "subtitles_corpus.dat" true))

(defn mk
  "clean subtitles and make it a tiny text corpus"
  [& _]
  (let [fs (next (file-seq (io/file "/subtitles/srt")))
        ;; using regexp to clean the subtitles
        ;; time-stamp and scene numbers
        ts #"\d+\n\d{2}:\d{2}:\d{2},\d{3} --> \d{2}:\d{2}:\d{2},\d{3}"
        ;; html tags, for italics and bolds, etc
        ht #"</?.+?>"
        ;; empty lines
        el #"\n+"
        ;; functions to replace them
        ts! #(string/replace % ts "")
        ht! #(string/replace % ht "")
        el! #(string/replace % el "\n")]
    (for [f fs
          ;; slurp it and then clean it one by one
          :let [c (->> (slurp (str f))
                       (ts!)
                       (ht!)
                       (el!))]]
      (spit "/subtitles/subtitles_corpus.dat" c
            ;; append to ensure it will be a single file with all the contents
            :append true))))

(rm)
(mk)
```

下面我们看看到底生成文件没有。

```bash id=4719a2fb-9779-441c-9278-02e5dd34c6ed
ls /subtitles
```

果然已经生成了文件，那么我们就来看看怎么样吧。

```bash id=135ac949-dee4-4174-a777-05db74881f47
bat -r :200 /subtitles/subtitles_corpus.dat
```

不错，正是我们想要的。我们现在就可以把这个文件用我们常用的语料库分析软件进行分析和使用了。这里我们选择Antconc。

## 把语料库变成一朵云

Antconc虽然简明扼要，功能实用，但是抽象的数字结果往往不能满足用户的需要，我们考虑使用Word Cloud技术，将生成的语料变成鲜明的词云，通过云朵的大小来反映在语料库中，具体词汇出现的频率大小。从感官上来讲，具有更强的冲击力，也具有更好的效果。

```clojure id=eda75f01-a865-4d94-96d3-0d7e645d5db4
(import [java.awt Dimension Color])
(import [com.kennycason.kumo WordCloud CollisionMode])
(import [com.kennycason.kumo.bg CircleBackground])
(import [com.kennycason.kumo.palette LinearGradientColorPalette])
(import [com.kennycason.kumo.font.scale SqrtFontScalar])
(import [com.kennycason.kumo.nlp FrequencyAnalyzer])

(defn wc
  "function for word cloud"
  [& _]
  (let [fa (FrequencyAnalyzer.)
        _ (.setWordFrequenciesToReturn fa 600)
        fq (.load fa "subtitles_corpus.dat")
        d (Dimension. 1300 1300)
        sqfs(SqrtFontScalar. 25 90)
        lgcp (LinearGradientColorPalette. Color/MAGENTA
                                          Color/CYAN Color/DARK_GRAY 80 80)
        cbg (CircleBackground. 650)
        wc (WordCloud. d CollisionMode/PIXEL_PERFECT)]
    (doto wc
      (.setPadding 2)
      (.setColorPalette lgcp)
      (.setBackground cbg)
      (.setFontScalar sqfs)
      (.build fq)
      (.writeToFile "subtitles_corpus.png"))))

(wc)
```

Clojure在NextJournal里没有办法直接把图像显示出来，我们这里选择把生成的图上传到网上，再来看。

```bash id=5c8aab26-57bf-4001-b7c7-d88b55487bef
curl -F 'name=@/subtitles/subtitles_corpus.png' https://img.vim-cn.com/
```

不过我也上传了一份目前的图方便大家直接观看。

![subtitles_corpus.png][nextjournal#file#6e8b1070-2030-4514-bc58-0018e5d74565]

看起来虽然比较粗糙，但是已经是一个成型的词云了。未来，我们还会对美观和一些常见词的排除进行优化。但是这不是目前的重点。

## 看看语料的语言学属性

上面两个步骤，我们生成了我们的迷你语料库，用Antconc对它进行分析，同时也使用小程序生成了词云，更好的显示效果。现在我们来对语料库文本进行一些最基本的语言学分析和统计学分析。

在这里，我们希望对文本进行完整的词性标注。其次，将词性标注的结果绘制成为饼状图。我们这里使用了宾夕法尼亚大学的词性标注方式。

```clojure id=432dda56-ef45-4806-8008-19078fe7b3b0
(require '[opennlp.nlp :refer [make-pos-tagger make-tokenizer]])
(require '[com.hypirion.clj-xchart :as c])
(require '[clojure.pprint :refer [pprint]])


(defn pos
  "use opennlp to show pos taggings"
  [& _]
  (let [pos-tag (make-pos-tagger "models/en-pos-maxent.bin")
        tokenize (make-tokenizer "models/en-token.bin")
        s (slurp "subtitles_corpus.dat")
        pos (pos-tag (tokenize s))
        fq (take-last 10 (sort-by val (frequencies (vals (into (sorted-map) pos)))))]
    (pprint pos)
    (pprint fq)
    (c/spit (c/pie-chart fq {:title "POS Pie Chart" :render-style :donut :theme :ggplot2}) "/subtitles/pos.png")))

(pos)
```

如上就是分析的结果，看起来很多很多，我们将其绘制成为了饼状图。因为NextJournal的虚拟环境不包括X11的图形界面，所以我们使用Java Swing生成的图形没有办法直接显示。这里选择保存为PNG图片格式。我们依然首先上传到网络。

```bash id=64b8376b-0d5e-44b1-875f-4c1e4121a172
ls /subtitles
```

```bash id=e22db0ea-b398-4ac2-ab08-941704f91ef6
curl -F 'name=@/subtitles/pos.png' https://img.vim-cn.com/
```

同时也保存了一份目前的状态。供大家参考。

![381359fb7c3f72fb12b2786d4e29a0c6898a9a.png][nextjournal#file#fd0fd6b3-977d-4169-afab-b62d4b90588b]

# 结束语

为了这个短暂的实验，我们花了三四天的时间去思考，花了一天多的时间去实现。尽管只是一个baby project和一个toy corpus，但是我们为我们的计划设定了非常宏大的目标和长远的规划。

当然，我们项目组的两位成员都还处在学习的早期阶段，我们特别希望，作为我们的第一个小项目，这是我们奋斗的起点，更是时刻鞭策我们前进的动力源泉。希望在不久的将来，我们可以把这个baby project做的更大更强。

非常感谢您的时间。也希望您多提宝贵的意见。

> When my dream was near the moon,
>
> The white folds of its gown
>
> Filled with yellow light.
>
> The soles of its feet
>
> Grew red.
>
> Its hair filled
>
> With certain blue crystallizations
>
> From stars,
>
> Not far off.

未来的路还很长，我们希望我们可以一起仰望满天的繁星。✨️

本文档完成于：

```clojure id=faa1be1e-d764-4bf6-a4ba-9b882bd64f0b
(java.time.LocalDateTime/now)
```


[nextjournal#github-repository#3b794f54-e805-4db8-ae6b-7ee17b6e03b6]:
<https://github.com/aiezue/subtitles>

[nextjournal#file#9dbb8d08-380f-4862-bdea-8df979a55469]:
<https://nextjournal.com/data/QmXeco6Ehs2dB7Vig5nnCozCusuPNgmEwPQTrpCB7QFoAc?content-type=image/jpeg&node-id=9dbb8d08-380f-4862-bdea-8df979a55469&filename=03-07-2017-SRT-FileFInal.jpg&node-kind=file> (<p>字幕的构成</p>)

[nextjournal#file#3df4c30a-a449-4aa1-9c0a-d2f694eefef0]:
<https://nextjournal.com/data/QmdmEJaisrHZ2WTcsLzKzBxqimjbDXP4be6RhVjqcGXGvR?content-type=image/jpeg&node-id=3df4c30a-a449-4aa1-9c0a-d2f694eefef0&filename=RegEx-%E8%AF%AD%E6%B3%95.jpg&node-kind=file> (<p>正则表达式的精髓</p>)

[nextjournal#file#fa0e1bb8-a80d-4bc3-b9db-4605d8f7c71b]:
<https://nextjournal.com/data/QmX54VrLyYsJUDSkA3rChDiFpJ7A6sj45NYX2NoDXwZJuF?content-type=image/png&node-id=fa0e1bb8-a80d-4bc3-b9db-4605d8f7c71b&filename=1499147415531032865.png&node-kind=file>

[nextjournal#file#3790d426-e6cf-45ad-bab6-6b1f98b20893]:
<https://nextjournal.com/data/QmXp2ZqmgsMd9ViYDdGWNCTx6CSQbj8fMKDTerhAJnDPyF?content-type=image/png&node-id=3790d426-e6cf-45ad-bab6-6b1f98b20893&filename=1499147415128073223.png&node-kind=file>

[nextjournal#file#f6160593-2f2f-45d3-b642-5465d0ef4d96]:
<https://nextjournal.com/data/QmTvXTzCL3mfC4vbdPDgnhRrXdkyAN8tjqMnpgHM5XdHtt?content-type=image/png&node-id=f6160593-2f2f-45d3-b642-5465d0ef4d96&filename=1499147415267019107.png&node-kind=file> (<p></p>)

[nextjournal#file#6e8b1070-2030-4514-bc58-0018e5d74565]:
<https://nextjournal.com/data/QmQqq8TbZC1pTM974xSkjWYJJ9tvJnbBvW82ZeRuteCp9w?content-type=image/png&node-id=6e8b1070-2030-4514-bc58-0018e5d74565&filename=subtitles_corpus.png&node-kind=file> (<p>最终的词云</p>)

[nextjournal#file#fd0fd6b3-977d-4169-afab-b62d4b90588b]:
<https://nextjournal.com/data/QmPAH922HDo61X9v4stbVsi2E4M1g2JEXdR9ax2eYnQrzG?content-type=image/png&node-id=fd0fd6b3-977d-4169-afab-b62d4b90588b&filename=381359fb7c3f72fb12b2786d4e29a0c6898a9a.png&node-kind=file>

<details id="com.nextjournal.article">
<summary>This notebook was exported from <a href="https://nextjournal.com/a/NC2gfWMiFexEyY8MVqB5Y?change-id=Cp84VGrUP6VYTn1Unbp3r2">https://nextjournal.com/a/NC2gfWMiFexEyY8MVqB5Y?change-id=Cp84VGrUP6VYTn1Unbp3r2</a></summary>

```edn nextjournal-metadata
{:article
 {:settings {:numbered? true},
  :nodes
  {"0fbac8d1-5212-450a-a2c4-1d6789152130"
   {:compute-ref #uuid "379745f9-5c2d-402d-a38a-10471a7b5b75",
    :exec-duration 23,
    :id "0fbac8d1-5212-450a-a2c4-1d6789152130",
    :kind "code",
    :output-log-lines {},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "107f8a1a-d8e8-4de5-b6b0-95458e75d4f1"
   {:compute-ref #uuid "41f8f5c4-87cf-4ccc-885f-da187583c91e",
    :exec-duration 940,
    :id "107f8a1a-d8e8-4de5-b6b0-95458e75d4f1",
    :kind "code",
    :output-log-lines {:stdout 13},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "135ac949-dee4-4174-a777-05db74881f47"
   {:compute-ref #uuid "a8e1cd4c-14b9-4bc9-ab08-4b2821a20fb0",
    :exec-duration 988,
    :id "135ac949-dee4-4174-a777-05db74881f47",
    :kind "code",
    :output-log-lines {:stdout 201},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "3790d426-e6cf-45ad-bab6-6b1f98b20893"
   {:id "3790d426-e6cf-45ad-bab6-6b1f98b20893", :kind "file"},
   "3b794f54-e805-4db8-ae6b-7ee17b6e03b6"
   {:id "3b794f54-e805-4db8-ae6b-7ee17b6e03b6",
    :kind "github-repository"},
   "3df4c30a-a449-4aa1-9c0a-d2f694eefef0"
   {:id "3df4c30a-a449-4aa1-9c0a-d2f694eefef0", :kind "file"},
   "432dda56-ef45-4806-8008-19078fe7b3b0"
   {:compute-ref #uuid "bcd286ba-65fb-4a56-899b-133d1592d547",
    :exec-duration 69148,
    :id "432dda56-ef45-4806-8008-19078fe7b3b0",
    :kind "code",
    :output-log-lines {:stdout 42503},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "4719a2fb-9779-441c-9278-02e5dd34c6ed"
   {:compute-ref #uuid "e866a07f-9fbe-4eff-b6ee-932cd4150a14",
    :exec-duration 1025,
    :id "4719a2fb-9779-441c-9278-02e5dd34c6ed",
    :kind "code",
    :output-log-lines {:stdout 8},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "579e7de8-2ca8-4a51-a857-7956abc4e9b0"
   {:compute-ref #uuid "e91b813a-c8e3-4b49-9b0c-11bf9d4af60c",
    :exec-duration 1207,
    :id "579e7de8-2ca8-4a51-a857-7956abc4e9b0",
    :kind "code",
    :output-log-lines {:stdout 1201},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "585d8eeb-d36d-4ab4-8b6b-941400163126"
   {:id "585d8eeb-d36d-4ab4-8b6b-941400163126", :kind "code-listing"},
   "5c8aab26-57bf-4001-b7c7-d88b55487bef"
   {:compute-ref #uuid "97c5010f-714d-4a59-b76b-ee0bcecfc3e3",
    :exec-duration 2944,
    :id "5c8aab26-57bf-4001-b7c7-d88b55487bef",
    :kind "code",
    :output-log-lines {:stdout 5},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "64b8376b-0d5e-44b1-875f-4c1e4121a172"
   {:compute-ref #uuid "afbbd8a9-8770-429b-abf8-6ed84af82ff5",
    :exec-duration 1198,
    :id "64b8376b-0d5e-44b1-875f-4c1e4121a172",
    :kind "code",
    :output-log-lines {:stdout 9},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "6cac1e85-802c-4eae-85b0-971d958a143d"
   {:compute-ref #uuid "8d410337-7576-49df-9d4c-8cab4a0b3b01",
    :exec-duration 3038,
    :id "6cac1e85-802c-4eae-85b0-971d958a143d",
    :kind "code",
    :output-log-lines {:stdout 10},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"],
    :stdout-collapsed? true},
   "6e8b1070-2030-4514-bc58-0018e5d74565"
   {:id "6e8b1070-2030-4514-bc58-0018e5d74565", :kind "file"},
   "8b69857a-eaa4-415b-bcd8-d46a5697e256"
   {:compute-ref #uuid "d792a770-03db-4093-8e25-2d28ba9a5a00",
    :exec-duration 836,
    :id "8b69857a-eaa4-415b-bcd8-d46a5697e256",
    :kind "code",
    :output-log-lines {},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "9dbb8d08-380f-4862-bdea-8df979a55469"
   {:id "9dbb8d08-380f-4862-bdea-8df979a55469", :kind "file"},
   "ade85264-95a7-4437-a964-b1eafb6cbda7"
   {:id "ade85264-95a7-4437-a964-b1eafb6cbda7", :kind "code-listing"},
   "be73c457-d1e2-4ec4-bc23-174a77ff3a90"
   {:compute-ref #uuid "0e056064-0f96-4289-84ea-d1e78e2c4590",
    :exec-duration 1081,
    :id "be73c457-d1e2-4ec4-bc23-174a77ff3a90",
    :kind "code",
    :output-log-lines {},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"
   {:environment
    [:environment
     {:article/nextjournal.id
      #uuid "5b45eb52-bad4-413d-9d7f-b2b573a25322",
      :change/nextjournal.id
      #uuid "5f045c36-90bd-428b-a26c-b59fa0a2e1db",
      :node/id "0ae15688-6f6a-40e2-a4fa-52d81371f733"}],
    :id "cdff0c2d-a0d2-4a2c-ba89-67461bff8432",
    :kind "runtime",
    :language "clojure",
    :resources {:machine-type "n1-standard-1"},
    :type :prepl,
    :runtime/mounts
    [{:src [:node "3b794f54-e805-4db8-ae6b-7ee17b6e03b6"],
      :dest "/subtitles"}]},
   "d6516a30-b2d5-4223-b217-2bc74505cd8f"
   {:compute-ref #uuid "4872ade1-ad01-4485-97f4-108a15fc390c",
    :exec-duration 46,
    :id "d6516a30-b2d5-4223-b217-2bc74505cd8f",
    :kind "code",
    :output-log-lines {},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "d7911731-a0d9-407c-8a65-a8836d780b81"
   {:id "d7911731-a0d9-407c-8a65-a8836d780b81", :kind "code-listing"},
   "e22db0ea-b398-4ac2-ab08-941704f91ef6"
   {:compute-ref #uuid "22d40163-301d-441d-8991-8ff9e6398e40",
    :exec-duration 1994,
    :id "e22db0ea-b398-4ac2-ab08-941704f91ef6",
    :kind "code",
    :output-log-lines {:stdout 5},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "eda75f01-a865-4d94-96d3-0d7e645d5db4"
   {:compute-ref #uuid "50b2388c-70cc-4a9c-a86a-73b2638c358a",
    :exec-duration 6309,
    :id "eda75f01-a865-4d94-96d3-0d7e645d5db4",
    :kind "code",
    :output-log-lines {},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "f494fcac-e21e-47a4-b2cc-e1e5421ac4b0"
   {:compute-ref #uuid "df913d3d-8201-43a5-a150-c8f75092e094",
    :exec-duration 430,
    :id "f494fcac-e21e-47a4-b2cc-e1e5421ac4b0",
    :kind "code",
    :output-log-lines {:stdout 2},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "f6160593-2f2f-45d3-b642-5465d0ef4d96"
   {:id "f6160593-2f2f-45d3-b642-5465d0ef4d96", :kind "file"},
   "fa0e1bb8-a80d-4bc3-b9db-4605d8f7c71b"
   {:id "fa0e1bb8-a80d-4bc3-b9db-4605d8f7c71b", :kind "file"},
   "faa1be1e-d764-4bf6-a4ba-9b882bd64f0b"
   {:compute-ref #uuid "ad9148db-8582-46ee-9b7c-0eb56d105244",
    :exec-duration 40,
    :id "faa1be1e-d764-4bf6-a4ba-9b882bd64f0b",
    :kind "code",
    :output-log-lines {},
    :runtime [:runtime "cdff0c2d-a0d2-4a2c-ba89-67461bff8432"]},
   "fb2c878f-c63f-4216-8a15-6338fe67f6e4"
   {:id "fb2c878f-c63f-4216-8a15-6338fe67f6e4", :kind "code-listing"},
   "fd0fd6b3-977d-4169-afab-b62d4b90588b"
   {:id "fd0fd6b3-977d-4169-afab-b62d4b90588b", :kind "file"},
   "ffcf0396-b3f9-40e6-a0c2-654401879781"
   {:id "ffcf0396-b3f9-40e6-a0c2-654401879781",
    :kind "code-listing",
    :name "deps.edn"}},
  :nextjournal/id #uuid "02f56a82-6ad3-4313-9953-38995a6ba96f",
  :article/change
  {:nextjournal/id #uuid "5fa8d55a-483a-4d07-8deb-30bcbe96c1bb"}}}

```
</details>
