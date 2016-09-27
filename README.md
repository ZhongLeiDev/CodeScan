# CodeScan
一个仓库扫描二维码出货并记录信息的程序。

此程序适用于Android（4.2及以上）系统的无线条码扫描枪设备（如：ZKC3501），代码编码格式为GBK（Windows）,在不同平台下可能显示为乱码。

#界面说明：

整体界面分布：

![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/a.PNG)

（1）主界面

![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/b.PNG)
![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/c.PNG)

主界面是进行扫描操作的界面，可以在此界面定义扫描后需要进行的操作到底是“出库”操作还是“入库”操作，正常来讲，对于出库扫描这一个大的情景来说，进行的扫描操作当然是“出库”扫描，而“入库”操作，是针对在实际操作时可能扫错批次，即将A批次的条码误扫入B批次这种误操作来进行的一个保险措施，当扫错批次时，只要将扫错的条码进行“入库”操作，即可恢复到正确的状态，这里的“入库”状态是由点击“撤销”按钮来实现的。

(2)本地数据库界面

![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/d.PNG)

(3)全局设置界面

![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/e.PNG)

(4)批次管理界面

![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/f.PNG)
![image](https://github.com/ZhongLeiDev/ZhongLeiDev.github.io/blob/master/scanimg/g.PNG)

#版本说明：

v1.1为仅支持内网访问的版本

v1.2为支持外网访问的版本
