# logistics(）

### 数据库配置
/logistics/src/main/resources
>切换数据库，更换配置文件即可，系统会自动建表
/logistics/sql 有项目登录的最基础的超级管理员的用户信息，导入数据库即可


###客户BOM物料匹配规则
D:\MYSRM\logistics-back-master\src\main\java\com\web\cost\service\internal\CustomerBomImpl.java
>根据类别，品牌料号和规格关键字筛选物料数据
>>1.类别——

>>>（1）获取类别关键字，找出类别；

>>>（2）找到类别名称之后，筛选出相关物料。（对物料名称进行模糊匹配）

>>2.品牌料号——

>>>（1）获取品牌料号，如果存在，则对品牌料号进行精准匹配。（如果品牌料号存在，筛选完之后则返回数据，不再进行规格关键字的筛选）

>>3.规格关键字——

>>>（1）获取分隔符数组；

>>>（2）根据类别名称获取规格关键字，如果获取不到规格关键字，则不进行关键字匹配；

>>>（3）如果获取到了关键字，则分成100%匹配和非100%匹配；

>>>（4）特别地，对于误差关键字，如±20%、±5%这样的±，进行非100%匹配，误差值小于即可筛选出来。



