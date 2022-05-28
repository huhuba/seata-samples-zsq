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
package io.seata.samples.mutiple.datasource.dao;

import io.seata.samples.mutiple.datasource.common.order.Order;
import io.seata.samples.mutiple.datasource.common.order.OrderStatus;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author HelloWoodes
 */
@Mapper
public interface OrderDao {

    /**
     * 保存订单
     *
     * @param order 订单
     * @return 影响行数
     */
    @Insert(
        "INSERT INTO orders (user_id, product_id, pay_amount, status) VALUES (#{userId}, #{productId}, #{payAmount}, "
            + "#{status})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    Integer saveOrder(Order order);

    /**
     * 更新订单状态
     *
     * @param id     订单 ID
     * @param status 状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status} WHERE id = #{id}")
    Integer updateOrder(@Param("id") Integer id, @Param("status") OrderStatus status);

}
