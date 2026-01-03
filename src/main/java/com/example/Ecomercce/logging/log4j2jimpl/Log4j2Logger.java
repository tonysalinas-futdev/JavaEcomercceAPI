package com.example.Ecomercce.logging.log4j2jimpl;

import com.example.Ecomercce.logging.interface_.LoggerInterface;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectMessage;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class Log4j2Logger implements LoggerInterface {
  private Logger logger = LogManager.getLogger(Log4j2Logger.class);

  @Override
  public void info(Object payload) {

    logger.info(new ObjectMessage(payload));
  }

  @Override
  public void error(Object payload) {
    logger.error(payload);
  }

  @Override
  public void warn(Object payload) {
    logger.warn(payload);
  }
}
