package com.github.ducoral.tiziu;

import java.util.List;

@FunctionalInterface
public interface Function {

    Object call(List<Object> params);
}
