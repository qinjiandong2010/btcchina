1.安装redis：
  --BTC API使用redis作为数据存储，请确保安装redis key-value内存数据库。
  --下载地址：http://redis.io/download
    
2.修改配置config.properties

3.目前BTC API已实现如下功能：
    1.api/data/ticker 获取实时行情。(三个网站已实现)
    2.api/data/depth 获取深度行情。(三个网站已实现)
    3.api/trade/buy 购买比特币。(三个网站已实现)
    4.api/trade/sell 出售比特币。(三个网站已实现)
    5.api/trade/cancel/id/{id} 取消订单。(BTCChina已实现)
    6.api/trade/orders 获取订单列表。(BTCChina已实现)