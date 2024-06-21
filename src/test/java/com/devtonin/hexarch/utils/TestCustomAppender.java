package com.devtonin.hexarch.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

public class TestCustomAppender extends ListAppender<ILoggingEvent> {

   public void clear() {
      this.list.clear();
   }
}
