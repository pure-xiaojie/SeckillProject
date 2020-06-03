# SeckillProject
### 项目简介：

​		SeckillProject基于Springboot开发的秒杀系统，实现的功能主要是登录、商品列表、商品详情、秒杀商品，订单详情等功能。在系统业务处理中，使用到分布式session维持会话、Redis预减库存降低数据库访问压力，消息队列异步下单（削峰）、客户端轮询结果、接口限流防刷等



### 开发技术：

后端处理：SpringBoot 、MyBatis 、 MySQL、JSR303、RabbitMQ、Redis、Druid

前端处理：Html、jQuery 、Thymeleaf



### 实现细节记录：



#### 1、用户密码两次MD5加密

第一次MD5加密：防止用户明文密码在网络进行传输

第二次MD5加密：防止数据库被盗，避免通过MD5反推出密码，双重保险





#### 2、分布式session维持会话

后端通过验证用户账号密码都正确情况下，通过UUID生成唯一id作为token，再将token作为key、用户信息对象作为value存储到redis，同时将token存储到cookie，维持会话状态。当用户访问接口时，只需从cookie取出对应的token信息，根据token键值从redis获取用户对象。





#### 3、异常统一处理

通过自定义拦截器的方式，对所有所有异常进行拦截，并进行相应的处理，然后把结果信息返回给客户端处理。





#### 4、页面缓存 + 对象缓存

**页面缓存：**

​		通过在手动渲染得到的html页面缓存到redis，下次访问相同页面时直接从redis中获取进行返回，减少服务端处理的压力

**对象缓存：**

​		把相应的热点对象进行缓存到Redis，比如：用户对象、商品对象、订单对象等，利用缓存来减少对数据库的访问，提高系统的响应速度。





#### 5、页面静态化

​		使用前后端分离技术，用ajax实现异步请求数据，得到数据后绑定到当前页面。第一次访问后，页面和数据都会缓存到客户端的浏览器，当再次请求相同的页面时会直接从浏览器缓存加载。





#### 6、内存标记 + redis预减库存 + RabbitMQ异步处理 

​		通过**内存标记** + **redis预减库存** + **RabbitMQ异步处理下单**，最后才会访问数据库，减少对数据库的访问，是系统整体负载达到最高。

**具体实现：**

```java
//系统启动时会对其初始化，将所有秒杀商品id存入map，库存为0是为true
private Map<Long,Boolean> localOverMap = new HashMap<Long,Boolean>();

//内存标记，减少redis访问
boolean over = localOverMap.get(goodsId);
if(over) {
    return Result.error(CodeMsg.MIAO_SHA_OVER);
}
//redis预减库存
long stock = redisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId);//10
if (stock < 0) {
    localOverMap.put(goodsId,true);
    return Result.error(CodeMsg.MIAO_SHA_OVER);
}

//判断是否已经秒杀到了
SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
if(order != null) {
    return Result.error(CodeMsg.REPEATE_MIAOSHA);
}

//压入消息队列
//入队
SeckillMessage sm = new SeckillMessage();
sm.setUser(user);
sm.setGoodsId(goodsId);
sender.sendSeckillMessage(sm);
```

1. 在用户发起秒杀访问时，先访问本地已经初始化好的map，看当前秒杀商品id的库存是否已售罄，若已售罄，直接返回秒杀结束异常，若库存还有，在执行下面的操作。通过内存标记可以减少对后面步骤中的redis访问操作，降低redis的压力，不然每个请求都将访问一次redis

   

2. 系统启动时，即将商品和库存数据初始化到redis中，所有的抢购操作都在redis中进行处理，通过Redis预减少库存来减少数据库访问

   

3. 通过使用RabbitMQ用异步队列处理下单，实现系统高响应。此处响应客户端后，一般都是抢购成功了，当然不排除例外，此时客户端通过ajax请求轮询访问下单结果接口，直到响应状态成功或者失败





#### 7、解决超卖

​		一开始没有优化时，通过Jmeter压测发现，数据库商品出现了负数的情况，并且产生的订单数量也是超过了当时库存数量的。简而言之，就是库存为10，一万个线程压测后，库存变成负值（-5，-13，-35都可能出现）。

**解决方法：**

1. 更sql语句，只有当库存大于0才能更新库存

   ```sql
   update seckill_goods set stock_count = stock_count-1 where goods_id = #{goodsId} and stock_count > 0
   ```

2. 对用户id和商品id建立一个唯一索引，通过这种约束避免同一用户发同时两个请求秒杀到两件相同商品

![image-20200604000433842](upload%5Cimage-20200604000433842.png)

3. 实现乐观锁，给商品信息表增加一个version字段，为每一条数据加上版本。每次更新的时候version+1，并且更新时候带上版本号，当提交前版本号等于更新前版本号，说明此时没有被其他线程影响到，正常更新，如果冲突了则不会进行提交更新。当库存是足够的情况下发生乐观锁冲突就进行一定次数的重试。





#### 8、接口限流

​		通过记录用户在某一时间内访问的次数进行拒绝。



### 效果展示

**1、项目结构**

![image-20200604001323172](upload%5Cimage-20200604001323172.png)



**2、登录页**

![image-20200604001403938](upload%5Cimage-20200604001403938.png)



**3、商品列表**

![image-20200604001511474](upload%5Cimage-20200604001511474.png)



**4、商品详情**

![image-20200604001859333](upload%5Cimage-20200604001859333.png)



**5、订单详情**

![image-20200604001921870](upload%5Cimage-20200604001921870.png)