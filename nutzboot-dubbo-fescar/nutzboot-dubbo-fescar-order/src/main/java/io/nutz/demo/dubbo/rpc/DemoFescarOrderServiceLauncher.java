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

import io.nutz.demo.bean.Order;
import org.nutz.boot.NbApp;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean(create = "init")
public class DemoFescarOrderServiceLauncher {

    @Inject
    Dao dao;

    public void init() {
        dao.create(Order.class, false);
    }

    public static void main(String[] args) throws Exception {
        new NbApp().run();
    }

}
