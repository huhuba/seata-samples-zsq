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
package io.seata.order.service;

import io.seata.order.client.ProductClient;
import io.seata.order.entity.Order;
import io.seata.order.mapper.OrderMapper;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderMapper orderMapper;

    //这里切记不要加@GlobalTransactional
    @Transactional
    @ShardingTransactionType(TransactionType.BASE)
    public void seataDemo(Boolean hasError) {
        //下单操作
        Order order = new Order();
        order.setOrderName("测试数据");
        order.setBuyNum(2);
        orderMapper.insert(order);

        //减库存（这里参数什么的就自己脑补了）
        productClient.minusStock();

        //异常模拟
        if (hasError) {
            int i = 1 / 0;
        }
    }
}
