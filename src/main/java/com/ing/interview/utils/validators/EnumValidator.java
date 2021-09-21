package com.ing.interview.utils.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;

public class EnumValidator implements ConstraintValidator<EnumSetValue, Enum<?>> {

  private Set<? extends Enum<?>> valueList;

  @Override
  public void initialize(EnumSetValue constraintAnnotation) {
    @SuppressWarnings("unchecked")
    Class<Enum<?>> enumClass = (Class<Enum<?>>) constraintAnnotation.enumClass();
    Map<String, Enum<?>> enumMap = Arrays.stream(enumClass.getEnumConstants()).collect(Collectors.toMap(Enum::name, Function.identity()));
    if (constraintAnnotation.include().length > 0) {
      valueList = Arrays.stream(constraintAnnotation.include()).map(enumMap::get).collect(Collectors.toSet());
    } else {
      Arrays.stream(constraintAnnotation.exclude()).forEach(enumMap::remove);
      valueList = new HashSet<>(enumMap.values());
    }
  }

  @Override
  public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
    if (isEmpty(value)) {
      return true;
    }
    return valueList.contains(value);
  }
}
