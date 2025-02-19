// ============================================================================
//
// Copyright (C) 2006-2023 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.daikon.spring.audit.logs.api;

import org.talend.daikon.spring.audit.common.api.AuditLogScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate audit log annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GenerateAuditLog {

    /**
     * Application from which the log is generated
     *
     * @return the name of the application
     */
    String application();

    /**
     * Type of the generated event
     *
     * @return event type
     */
    String eventType();

    /**
     * Category of the generated event
     *
     * @return Event category
     */
    String eventCategory();

    /**
     * Operation of the generated event
     *
     * @return Event operation
     */
    String eventOperation();

    /**
     * Should include or not the HTTP response body in the generated audit log
     *
     * @return boolean indicating if the HTTP response body is included or not
     */
    boolean includeBodyResponse() default true;

    /**
     * Should include or not the HTTP location header in the generated audit log
     *
     * @return boolean indicating if the HTTP location header is included or not
     */
    boolean includeLocationHeader() default false;

    /**
     * Filter whose purpose is to filter HTTP request/response before generation
     *
     * @return Filter class
     */
    Class<? extends AuditContextFilter> filter() default NoOpAuditContextFilter.class;

    /**
     * Indicate if audit logs must be generated for :
     * - ALL cases
     * - SUCCESS cases only
     * - ERROR cases only
     *
     * @return Audit log scope
     */
    AuditLogScope scope() default AuditLogScope.ALL;
}
