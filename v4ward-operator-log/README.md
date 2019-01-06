# v4ward-operate-log的使用说明

### 简介
   当对业务内容进行编辑时，记录何人何时何ip进行何种改动（包含了原值和修改后的值），保存到数据库中


### 环境
- maven
- jdk 1.8
- spring boot 2.x release
- mysql 5.0+
- mybatis-plus (已经引入到本包，后续使用不必再引用依赖)
- fastjson (已经引入到本包，后续使用不必再引用依赖)
- aop (已经引入到本包，后续使用不必再引用依赖)
- lombok (已经引入到本包，后续使用不必再引用依赖)
### 使用
1. 添加依赖
```
    <dependency>
        <groupId>com.v4ward</groupId>
        <artifactId>v4ward-operator-log</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

2. 在启动类上面加注解 @EnableV4wardLog

3. 在需要记录的方法上使用注解 @V4wardLog

+ 简单例子：
```
    @V4wardLog(name = ModifyName.SAVE,serviceClass = DemoService.class,businessName = "新增用户信息",
       dataIndex = 0,parseClass = DefaultContentParse.class,fieldName = "id")
       public Object addDemo(@RequestBody Demo demo){
           demoService.insert(demo);
           return RestResponseEntity.ok("新增成功！");
       }
```

+ 参数说明：
```
1、name:         必填参数！！！     ModifyName枚举类型的参数操作名称（新增，修改，删除），根据操作的业
                                    务选对应的类型，对于log表中的operate_type字段。
2、serviceClass：必填参数！！！     指定操作数据的service接口类，根据操作的业务填对应的service 
3、businessName：选填参数！         业务名称，对应log表中的business_name字段，不填则记录操作接口的URI
4、dataIndex：   根据参数来决定是否必填！！ 此参数用于指定业务操作数据在参数列表中的顺序，从0开始，默认为0，
                                    即参数列表中的第一个参数.
                                    如：方法签名为 public Object addDemo(@RequestBody Demo demo) 则业务数
                                    据参数为demo，是第一个参数，下标记为0，注解中可以写成 dataIndex = 0，
                                    而默认为0，故此处可以省略dataIndex参数。
                                    如果方法签名为public Object addDemo(HttpRequest req,@RequestBody Demo demo)
                                    而涉及到数据业务的参数为demo，为参数列表中的第2个，下标记为1，所以 dataIndex=1,不能省略
5、parseClass:   与fieldName 配合使用，不填则默认使用DefaultContentParse.class,与此同时，fieldName则为"id"
                                    (默认值，可不填)如果要使用自定义类，则自定义类必须实现ContentParse接口，                             
```

+ 编写解析类，默认的解析类为使用id查询，自定义的解析类请继承ContentParser接口，并在注解中赋值
```
 
/**
 * 基础解析类
 * 单表编辑时可以直接使用id来查询
 * 如果为多表复杂逻辑，请自行编写具体实现类
 * @author zk
 * @date 2018-03-02
 */
public class DefaultContentParse implements ContentParser {
    @Override
    public Object getResult(Map<String,Object> feildValues, EnableOperateLog enableGameleyLog) {
        Assert.isTrue(feildValues.containsKey("id"),"未解析到id值，请检查前台传递参数是否正确");
        Object result= feildValues.get("id");
        Integer id=0;
        if(result instanceof String){
            id= Integer.parseInt((String) result);

        }else if(result instanceof Integer){
            id= (Integer) result;
        }
        IService service= null;
        Class cls=enableGameleyLog.serviceclass();
        service = (IService) SpringUtil.getBean(cls);

        return  service.selectById(id);
    }


}
 
```

4 . 注解@IgnoreCompare

```$xslt
    当用户对某数据什么都不修改，直接保存，那么数据库最终可能仅仅修改了一个更新时间，如果你不想让这个修改操作被操作日志记录，
    1，可以对该数据的任何操作都不记录，即不使用@V4wardLog
    2,你想记录其它修改操作，但不想记录更新时间的修改，那么可以在实体的属性上面添加这个注解，如 在updateTime上加@IgnoreCompare注解。
```

+ 默认的操作方式有：
 ```
public class ModifyName {
    public final static String SAVE="新建";
    public final static String UPDATE="编辑";
    public final static String DELETE="删除";
}
```

+ 注意事项
```$xslt
1、不能将多个@V4wardLog注解使用在嵌套方法上 ，如下错误示例：
    @V4wardLog()
    public void funcA(Demo demo){
        funcB(Demo demo);
    }
    @V4wardLog()
    public void funcB(Demo demo){
    ...
    }
    
2、可以用在并列方法上，如
    public void funcA(Demo demo){
        funcB(Demo demo);
        funcC(Demo demo);
    }
    @V4wardLog()
    public void funcB(Demo demo){
    ...
    }
    @V4wardLog()
    public void funcC(Demo demo){
    ...
    }

```


### 展示图
![操作记录表截图](file:///C:/Users/Thinkpad/Desktop/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9/pic/log.png "操作记录表截图")


### 建表语句
```
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` bigint(20) NOT NULL COMMENT ' 主键ID',
  `data_id` bigint(20) DEFAULT NULL COMMENT '操作数据的主键ID，可用于追踪轨迹',
  `operate_type` varchar(64) NOT NULL COMMENT '操作类型：新增，修改，删除',
  `business_name` varchar(255) DEFAULT NULL COMMENT '业务名称或接口URI',
  `operate_data` json DEFAULT NULL COMMENT '操作数据',
  `operate_ip` varchar(32) DEFAULT NULL COMMENT '操作用户的IP',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `create_emp` varchar(32) DEFAULT NULL COMMENT '操作用户名',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```