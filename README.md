<h5>重组ARSC文件</h5>
<br><br>
主要功能：将一个arsc文件分为若干模块，修改后重组为一个arsc文件。
<br><br>
1.把resources.arsc文件放在resources文件夹下，ARSCWrite类中的devideService方法会将arsc文件分成一个header，一个StringPool，一个StringStylePool，一个StringKeyPool，一个package，若干Type模块，分解好的模块放在data目录下。
<br><br>
2.ARSCWrite类中的component(int type_count)方法用来将data下的所有零碎字节模块重组为resources.arsc.重组好的arsc文件，是data目录下的resources.arsc文件。type_count表示要重组的arsc文件type类型文件的个数，
<br><br>
3.在resources文件夹下附加的arsc文件格式可直接分析
<br><br>
4. src中com.ly.disk目录下的类用于修改data目录下某块文件，REsTableHeader有关header的操作，ResPackage有关package模块，StringPool有关StringPool,StringStylePool,StringKeyPool三个模块的，Type模块有关type的处理。每个type包含一个type_spec结构和n个type结构。
<br><br>
5.changeContent的内容用于修改type模块的内容，before.arsc放准备修改的byte[]，after.arsc放准备修改成的byte[]内容。add.arsc内放置向type中添加byte[]的信息。