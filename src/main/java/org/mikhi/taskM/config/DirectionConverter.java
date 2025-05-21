package org.mikhi.taskM.config;

import org.mikhi.taskM.model.Direction;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;

@ComponentScan
public class DirectionConverter implements Converter<String, Direction> {

  @Override
  public Direction convert(String source) {
    return Direction.fromString(source);
  }
}