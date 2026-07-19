package com.onclass.capacidad.domain.exceptions;

import com.onclass.capacidad.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

  public BusinessException(TechnicalMessage technicalMessage) {
    super(technicalMessage.getDescription(), technicalMessage);
  }
}

