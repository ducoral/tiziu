package com.github.ducoral.tiziu;

import java.util.Map;

@FunctionalInterface
public interface Function {

    Object call(Map<String, Object> params);
}
