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
package io.nutz.demo.dubbo.rpc.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fescar.core.context.RootContext;

import io.nutz.demo.bean.Account;
import io.nutz.demo.dubbo.rpc.service.AccountService;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

@IocBean
@Service(interfaceClass = AccountService.class)
public class AccountServiceImpl implements AccountService {

    private static final Log log = Logs.get();

    @Inject
    private Dao dao;

    @Override
    public void debit(String userId, int money) {
        log.info("Account Service ... xid: " + RootContext.getXID());
        log.infof("Deducting balance SQL: update account_tbl set money = money - %s where user_id = %s", money, userId);

        dao.update(Account.class, Chain.makeSpecial("money", "-" + money), Cnd.where("userId", "=", userId));
        log.info("Account Service End ... ");
    }
}