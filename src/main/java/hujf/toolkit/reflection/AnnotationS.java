package hujf.toolkit.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author zhaoyan.hjf
 * @since 2018-09-14
 */
public final class AnnotationS {

    private AnnotationS() {
    }

    /**
     * find annotation on method of this class
     */
    public static <A extends Annotation> A findDeclaredAnnotation(Method method, Class<A> annotationClass) {
        if (method == null || annotationClass == null) {
            return null;
        }
        return method.getAnnotation(annotationClass);
    }

    /**
     * find annotation on class of this class
     */
    public static <A extends Annotation> A findDeclaredAnnotation(Class<?> clazz, Class<A> annotationClass) {
        if (clazz == null || annotationClass == null) {
            return null;
        }
        return clazz.getAnnotation(annotationClass);
    }

    /**
     * find annotation on method of this class and super class
     */
    public static <A extends Annotation> A findAnnotation(Method method, Class<A> annotationClass) {
        if (method == null || annotationClass == null) {
            return null;
        }
        A annotation = method.getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }
        return searchAnnotationOnParent(method.getDeclaringClass(), method, annotationClass);
    }

    private static <A extends Annotation> A searchAnnotationOnParent(Class<?> methodClass, Method method, Class<A> annotationClass) {
        if (methodClass == null || method == null || annotationClass == null) {
            return null;
        }
        if (methodClass.equals(Object.class)) {
            return null;
        }

        A annotation = null;
        //先深度遍历所有接口
        if (methodClass.getInterfaces() != null) {
            for (Class<?> itf : methodClass.getInterfaces()) {
                Method equivalentMethod = equivalentMethod(itf, method);
                if (equivalentMethod != null) {
                    annotation = equivalentMethod.getAnnotation(annotationClass);
                    if (annotation != null) {
                        return annotation;
                    }
                }
                annotation = searchAnnotationOnParent(itf, method, annotationClass);
                if (annotation != null) {
                    return annotation;
                }
            }
        }

        //再深度遍历所有父类
        Class<?> superClass = methodClass.getSuperclass();
        Method equivalentMethod = equivalentMethod(superClass, method);
        if (equivalentMethod != null) {
            annotation = equivalentMethod.getAnnotation(annotationClass);
            if (annotation != null) {
                return annotation;
            }
        }
        annotation = searchAnnotationOnParent(superClass, method, annotationClass);
        if (annotation != null) {
            return annotation;
        }

        return annotation;
    }

    private static Method equivalentMethod(Class<?> targetClass, Method method) {
        if (targetClass == null || method == null) {
            return null;
        }

        Method equivalentMethod = null;
        try {
            equivalentMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException ex) {
            for (Method aMethod : targetClass.getMethods()) {
                if (aMethod.getName().equals(method.getName())) {//这里没有匹配参数类型
                    equivalentMethod = aMethod;
                    break;
                }
            }
        }
        return equivalentMethod;
    }


}
