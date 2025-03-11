package com.igs.ematf.dataresttest.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryAspect {
    @Pointcut("execution(* org.springframework.data.jpa.repository.support.SimpleJpaRepository.findAll(..))")
    public void findAllMethods() {
    }

    // 定义切点，匹配SimpleJpaRepository的findAll方法
    @Pointcut("execution(* org.springframework.data.jpa.repository.support.SimpleJpaRepository.findAll(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable))")
    public void findAllWithSpecification() {
    }

    // 在方法执行前执行的通知
    @Before("findAllWithSpecification()")
    public void beforeFindAllWithSpecification() {
        System.out.println("Before executing SimpleJpaRepository.findAll(Specification, Pageable)");
    }

    @Around("findAllWithSpecification()")
    public Object beforeFindAllWithSpecification222(ProceedingJoinPoint pjp) {
        System.out.println("Before executing SimpleJpaRepository.findAll(Specification, Pageable)");
        return null;
    }

    @Before("findAllMethods()")
    public void beforeFindAllWithSpecification2() {
        System.out.println("Before executing SimpleJpaRepository.findAll(Specification, Pageable)");
    }

    @Around("findAllMethods()")
    public Object beforeFindAllWithSpecification2333(ProceedingJoinPoint pjp) {
        System.out.println("Before executing SimpleJpaRepository.findAll(Specification, Pageable)");
        return null;
    }

    @Around(value = "findAllMethods() && args(specification, pageable)",
            argNames = "pjp,specification,pageable")
    public Object beforeFindAll(ProceedingJoinPoint pjp, Specification<?> specification, Pageable pageable) {
        // 在这里修改 Specification 实现行权限控制
        System.out.println("Around findAll method");
        // 示例：修改 specification
        // specification = ...;
        return null;
    }
}