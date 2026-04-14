package com.deliverytech.delivery_api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.deliverytech.delivery_api.validation.validator.CategoriaValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy= CategoriaValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CategoriaValida {
    String message() default "Categoria inválida";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default{};
}
