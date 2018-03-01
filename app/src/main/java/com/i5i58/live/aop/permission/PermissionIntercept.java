package com.i5i58.live.aop.permission;

import android.content.Context;

import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.manager.APPManager;
import com.i5i58.live.common.manager.PermissionManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import java.lang.reflect.Method;

/**
 * Created by Lee on 2017/4/11.
 */

@Aspect
public class PermissionIntercept {

    private static final String TAG = "BooleIntercept";

    @Pointcut("execution(@com.i5i58.live.aop.permission.PermissionCut  * *(..))")
    public void method() {}

    @Around("method()")
    public Object aroundAspectJ(ProceedingJoinPoint joinPoint) throws Throwable {
        if(before(joinPoint)){
            return joinPoint.proceed();
        }else{
            return null;
        }
    }

    private boolean before(ProceedingJoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType();
        Method[] method = cls.getDeclaredMethods();
        for (Method mth: method){
            PermissionCut aspectJAnnotation = mth.getAnnotation(PermissionCut.class);
            if(null == aspectJAnnotation){
                continue;
            }
            String[] permissions = aspectJAnnotation.permission();
            Context context = (Context) joinPoint.getThis();
            String[] needPermission = PermissionManager.getInnerInstance().checkPermission(context,permissions);
            if (null != needPermission && needPermission.length > 0) {
                BaseActivity TopActivity = (BaseActivity) APPManager.getInstance().currentActivity();
                PermissionManager.getInnerInstance().askPermission(TopActivity,needPermission);
                return false;
            }
        }
        return true;
    }
}