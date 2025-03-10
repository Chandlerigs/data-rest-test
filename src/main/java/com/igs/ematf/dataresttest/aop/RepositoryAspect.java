package com.igs.ematf.dataresttest.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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


}