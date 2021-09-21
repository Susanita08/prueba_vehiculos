package com.ing.interview.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
@ToString
public enum ApplicationMessage {

  SUCCESS(0, "Successfully", "00"),

  CREATED(1, "Created Successfully", "01"),

  UNAVAILABLE(2, "Unavailable", "02"),

  GENERIC_ERROR(3, "Generic Error", "03"),

  INVALID_OPERATION(4, "Invalidate Operation", "04"),

  TRANSACTION_NOT_FOUND(5, "Transaction Not found", "05"),

  BAD_FORMAT(6, "Bad Format", "06"),

  INVALID_DATE(7, "Invalidate date", "07"),

  TIMEOUT(99, "Timeout error", "99"),

  UNEXPECTED(99, "Unexpected error", "99");

  private static final long serialVersionUID = 1L;

  private Integer code;
  private String message;
  private String strCode;

  public static ApplicationMessage fromCode(Integer code) {
    return Arrays.stream(values()).filter(applicationMessage -> Objects.equals(code, applicationMessage.getCode())).findAny().orElse(UNEXPECTED);
  }

  public static ApplicationMessage fromCode(String strCode) {
    return Arrays.stream(values()).filter(applicationMessage -> Objects.equals(strCode, applicationMessage.getStrCode())).findAny()
        .orElse(UNEXPECTED);
  }
}
