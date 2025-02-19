// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.multitenant.context;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.talend.daikon.multitenant.provider.DefaultTenant;

public class ThreadLocalTenancyContextHolderStrategyTest extends TenancyContextHolderTest {

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        TenancyContextHolder.setStrategyName(TenancyContextHolder.MODE_THREADLOCAL);
    }

    @AfterEach
    @Override
    public void tearDown() {
        TenancyContextHolder.setStrategyName(TenancyContextHolder.MODE_THREADLOCAL);
    }

    @Test
    public void testSpawnThread() throws InterruptedException {
        TenancyContext tc = new DefaultTenancyContext();
        tc.setTenant(new DefaultTenant("id", "myTenant"));
        TenancyContextHolder.setContext(tc);
        StatusHolder statusHolder = new StatusHolder();
        Runnable runnable = () -> {
            statusHolder.assertNotEquals(tc, TenancyContextHolder.getContext());
            statusHolder.assertNull(tc, TenancyContextHolder.getContext());
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join(60000L);
        statusHolder.assertSuccess();
    }
}
