package com.i5i58.live.aop.http;

import android.util.Log;

import com.i5i58.live.common.base.BaseActivity;
import com.i5i58.live.common.manager.APPManager;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;


/**
 * Created by Lee on 2017/4/11.
 */

@Aspect
public class HttpRequestIntercept {

    private String TAG = "HttpRequestIntercept";

    //带有DebugLog注解的所有类
    @Pointcut("within(@com.i5i58.live.aop.http.HttpResultCut *)")
    public void withinAnnotatedClass() {}

    //在带有DebugLog注解的所有类，除去synthetic修饰的方法
    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {}

    //在带有DebugLog注解的所有类，除去synthetic修饰的构造方法
    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {}

    //在带有DebugLog注解的方法
    @Pointcut("execution(@com.i5i58.live.aop.http.HttpResultCut * *(..)) || methodInsideAnnotatedType()")
    public void method() {}

    //在带有DebugLog注解的构造方法
    @Pointcut("execution(@com.i5i58.live.aop.http.HttpResultCut *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {}

    @Around("method()|| constructor()")
    public Object httpAndExecute(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        if(beforeMethod(joinPoint)){
            return joinPoint.proceed();
        }
        return null;
    }

    private boolean beforeMethod(ProceedingJoinPoint joinPoint){
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        Class<?> cls = codeSignature.getDeclaringType();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] parameterValues = joinPoint.getArgs();
        Method[] method = cls.getDeclaredMethods();
        for (Method mth: method){
            HttpResultCut httpResult = mth.getAnnotation(HttpResultCut.class);
            if(null == httpResult){
                continue;
            }
            String value = httpResult.value();
            for (int i = 0;i < parameterNames.length;i++){
                if(parameterNames[i].equals(value)){
                    boolean success = (boolean) parameterValues[i];
                    JSONObject jsonObjects = (JSONObject) parameterValues[0];
                    if(!success){
                        BaseActivity activity = (BaseActivity) APPManager.getInstance().currentActivity();
                        try {
                            String msg = jsonObjects.getString("msg");
                            activity.TSBError(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG,e.toString());
                        }
                        return true;
                    }
                    break;
                }
            }
        }
        return true;
    }

}
