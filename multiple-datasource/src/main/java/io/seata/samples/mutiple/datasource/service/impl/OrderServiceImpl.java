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
import io.seata.samples.mutiple.datasource.common.OperationResponse;
import io.seata.samples.mutiple.datasource.common.order.Order;
import io.seata.samples.mutiple.datasource.common.order.OrderStatus;
import io.seata.samples.mutiple.datasource.common.order.PlaceOrderRequestVO;
import io.seata.samples.mutiple.datasource.config.DataSourceKey;
import io.seata.samples.mutiple.datasource.config.DynamicDataSourceContextHolder;
import io.seata.samples.mutiple.datasource.dao.OrderDao;
import io.seata.samples.mutiple.datasource.service.OrderService;
import io.seata.samples.mutiple.datasource.service.PayService;
import io.seata.samples.mutiple.datasource.service.StockService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HelloWoodes
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PayService payService;

    @Autowired
    private StockService stockService;

    @GlobalTransactional
    @Override
    public OperationResponse placeOrder(PlaceOrderRequestVO placeOrderRequestVO) throws Exception {
        log.info("=============ORDER=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);
        log.info("?????? XID: {}", RootContext.getXID());

        Integer amount = 1;
        Integer price = placeOrderRequestVO.getPrice();

        Order order = Order.builder().userId(placeOrderRequestVO.getUserId()).productId(
            placeOrderRequestVO.getProductId()).status(OrderStatus.INIT).payAmount(price).build();

        Integer saveOrderRecord = orderDao.saveOrder(order);

        log.info("????????????{}", saveOrderRecord > 0 ? "??????" : "??????");

        // ????????????
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.STOCK);
        boolean operationStockResult = stockService.reduceStock(placeOrderRequestVO.getProductId(), amount);

        // ????????????
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.PAY);
        boolean operationBalanceResult = payService.reduceBalance(placeOrderRequestVO.getUserId(), price);

        log.info("=============ORDER=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.ORDER);

        Integer updateOrderRecord = orderDao.updateOrder(order.getId(), OrderStatus.SUCCESS);
        log.info("????????????:{} {}", order.getId(), updateOrderRecord > 0 ? "??????" : "??????");

        return OperationResponse.builder().success(operationStockResult && operationBalanceResult).build();
    }
}
