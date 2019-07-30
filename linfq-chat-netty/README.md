# 模拟微信

## 启动fastDFS

* 1、启动tracker
```jshelllanguage
/usr/bin/fdfs_trackerd /etc/fdfs/tracker.conf
```

* 2、启动storage服务
```jshelllanguage
/usr/bin/fdfs_storaged /etc/fdfs/storage.conf
```

* 3、启动nginx
```jshelllanguage
/usr/local/nginx/sbin/nginx
```