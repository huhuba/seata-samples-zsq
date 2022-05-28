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
package io.seata.samples.mutiple.datasource.service.impl;

import io.seata.core.context.RootContext;
import io.seata.samples.mutiple.datasource.dao.AccountDao;
import io.seata.samples.mutiple.datasource.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author HelloWoodes
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private AccountDao accountDao;

    /**
     * 事务传播特性设置为 REQUIRES_NEW 开启新的事务
     *
     * @param userId 用户 ID
     * @param price  扣减金额
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public boolean reduceBalance(Long userId, Integer price) throws Exception {
        log.info("=============PAY=================");
        log.info("当前 XID: {}", RootContext.getXID());

        checkBalance(userId, price);

        log.info("开始扣减用户 {} 余额", userId);
        Integer record = accountDao.reduceBalance(price);
        log.info("扣减用户 {} 余额结果:{}", userId, record > 0 ? "操作成功" : "扣减余额失败");

        return record > 0;

    }

    private void checkBalance(Long userId, Integer price) throws Exception {
        log.info("检查用户 {} 余额", userId);
        Integer balance = accountDao.getBalance(userId);

        if (balance < price) {
            log.warn("用户 {} 余额不足，当前余额:{}", userId, balance);
            throw new Exception("余额不足");
        }

    }

}
