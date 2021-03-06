/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.nutz.demo.dubbo.rpc;

import io.nutz.demo.bean.Account;
import io.nutz.demo.bean.Stock;
import io.nutz.demo.dubbo.rpc.service.BusinessService;
import org.nutz.boot.NbApp;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

@IocBean
public class DemoFescarWebLauncher {

    private static final Log log = Logs.get();

    @Inject
    protected BusinessService businessService;

    // 订购信息
    @Ok("json:full")
    @At("/api/purchase")
    public NutMap purchase(String userId, String commodityCode, int orderCount, boolean dofail) {
        try {
            businessService.purchase(userId, commodityCode, orderCount, dofail);
            return new NutMap("ok", true);
        } catch (Throwable e) {
            log.debug("purchase fail", e);
            return new NutMap("ok", false);
        }
    }

    // --------------------------------------------
    @Inject
    protected Dao dao;

    // 用于页面显示数据
    @Ok("json:full")
    @At("/api/info")
    public NutMap info() {
        NutMap re = new NutMap();
        NutMap data = new NutMap();
        data.put("account", dao.fetch(Account.class, Cnd.where("userId", "=", "U100001")));
        data.put("stock", dao.fetch(Stock.class, Cnd.where("commodityCode", "=", "C00321")));
        return re.setv("data", data).setv("ok", true);
    }

    // 要启动zk做dubbo注册服务, fescar-server也需要下载启动
    // 数据库名称 fescar_demo, 用户名密码均为root,可以在application.properties里面修改 
    //启动顺序: account,stock,order,web, 服务启动完成后再启动下一个
    // 页面操作: http://127.0.0.1:8080/
    //正常操作:
    // http://127.0.0.1:8080/api/purchase?userId=U100001&commodityCode=C00321&orderCount=1 
    // 故意抛出异常:
    // http://127.0.0.1:8080/api/purchase?userId=U100001&commodityCode=C00321&orderCount=1&dofail=true
    public static void main(String[] args) throws Exception {
        new NbApp().run();
    }

}
