package com.yxj.ssoclient.spi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CustomAuthenticationInterface {

    Boolean authentication(HttpServletRequest servletRequest, HttpServletResponse servletResponse);
}
