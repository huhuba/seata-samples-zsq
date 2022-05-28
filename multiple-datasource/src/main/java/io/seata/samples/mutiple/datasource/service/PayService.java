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
package io.seata.samples.mutiple.datasource.service;

/**
 * @author HelloWoodes
 */
public interface PayService {
    /**
     * @param userId 用户 ID
     * @param price  扣减金额
     * @return 返回操作结果
     * @throws Exception 失败时抛出异常
     */
    boolean reduceBalance(Long userId, Integer price) throws Exception;

}
