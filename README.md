# CityPicker
## 中国城市选择器

### 一个简单的城市选择器
  数据来源: <a href="http://www.mca.gov.cn/article/sj/xzqh/2018/201804-12/20181201301111.html">2018年12月中华人民共和国县以上行政区划代码</a>

### Screenshot
<img src="captures/1.png" width=35%></img>

### Demo
<a href="https://github.com/actor20170211030627/CityPicker/raw/master/captures/app-debug.apk">download apk</a>

## How to
To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
<pre>
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
</pre>


**Step 2.** Add the dependency, the last version:
[![](https://jitpack.io/v/actor20170211030627/CityPicker.svg)](https://jitpack.io/#actor20170211030627/CityPicker)

	dependencies {
	        implementation 'com.github.actor20170211030627:CityPicker:version'
	}

### TODO
<ol>
    <li>自定义item</li>
    <li>自定义item条数</li>
    <li>更新地址数据</li>
</ol>

### Thanks:
  <a href="https://github.com/crazyandcoder/citypicker">citypicker</a>,
  数据来源参考了这个项目, 感谢作者

### License [![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)