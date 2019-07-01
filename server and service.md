# 软件创新作业笔记

by Jiahui Huang

ip: 202.120.40.81

# Part1: 配置环境

---

### 创建实例

 在实例一栏选择创建实例，需要填的几项：

- 详情-实例名称
- 源-选择镜像
- 实例类型-推荐选择large
- 密钥对- 选择当前电脑的公钥

**实例创建完成后，为其绑定浮动ip即可**

实例创建成功后：

![](_20190625121423-22fc89ae-c403-4602-b96d-7175502910ec.png)

端口映射规则：

![](_20190625121256-d65b6c33-fcdd-4a09-a5b5-63602e2b84b0.png)

**远程登录命令**

    ssh -p 30550 ubuntu[@202.120.40.8](mailto:centos@202.120.40.8) 
    //用户名即镜像名，如选择centos镜像即 centos@202.120.40.8
    //登录后由创建者在.ssh的authorized_keys中添加小组成员的public key，即完成授权可在各自的pc登录。

### 安全组规则添加

配置一次即可，在安全组default中选择管理规则-添加规则,具体如下图：

![](_20190625122536-ac6cf005-e1eb-46ac-991f-81c8b5dabcf4.png)

### Commands

    # 查看所有pod状态
    sudo kubectl get pods --all-namespaces -o wide 
    #查看节点状态
    sudo kubectl get nodes
    # 上传本地文件，只可写入tmp文件夹
    scp -P 30550 -r serviceMaster ubuntu@202.120.40.8:/tmp
    # 创建docker镜像
    sudo mvn clean package docker:build
    # 将镜像运行在一个容器中
    sudo docker run -p 8080:8080 -t mighty/servicemaster:latest
    sudo docker build -t liuzhiwang/saythx-be .
    
    #创建deployment
    kubectl create -f k8s-service.yaml
    kubectl get deployments -查看
    
    kubectl expose deployment k8s-service --type=LoadBalancer
    
    # 修改nginx文件后需要重启
    nginx -s reload
    
    # 连接mysql服务
    kubectl exec -it mysql-5b5668c448-t44ml -- mysql -uroot -pChangeme