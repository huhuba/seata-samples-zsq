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
package io.seata.samples.tcc.sofarpc.action.impl;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.samples.tcc.sofarpc.action.ResultHolder;
import io.seata.samples.tcc.sofarpc.action.TccActionOne;

/**
 * The type Tcc action one.
 *
 * @author zhangsen
 */
public class TccActionOneImpl implements TccActionOne {

    @Override
    public boolean prepare(BusinessActionContext actionContext, int a) {
        String txId = actionContext.getXid();
        System.out.println("TccActionOne prepare, txId:" + txId);
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext actionContext) {
        String txId = actionContext.getXid();
        System.out.println("TccActionOne commit, txId:" + txId);
        ResultHolder.setActionOneResult(txId, "T");
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext actionContext) {
        String txId = actionContext.getXid();
        System.out.println("TccActionOne rollback, txId:" + txId);
        ResultHolder.setActionOneResult(txId, "R");
        return true;
    }
}
