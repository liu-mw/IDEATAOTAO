<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		 xmlns="http://java.sun.com/xml/ns/javaee"
		 xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
		 id="WebApp_ID" version="2.5">
	<display-name>taotao-manage</display-name>

	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:spring/applicationContext*.xml</param-value>
	</context-param>

	<!--Spring的ApplicationContext 载入 -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

	<!-- 编码过滤器，以UTF8编码 -->
	<filter>
		<filter-name>encodingFilter</filter-name>
		<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF8</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>encodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<!-- 解决PUT请求无法提交表单数据的问题 -->
	<filter>
		<filter-name>HttpMethodFilter</filter-name>
		<filter-class>org.springframework.web.filter.HttpPutFormContentFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>HttpMethodFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>

	<!-- 将POST请求转化为DELETE或者是PUT 要用_method指定真正的请求参数 -->
	<filter>
		<filter-name>HiddenHttpMethodFilter</filter-name>
		<filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
	</filter>
	<filter-mapping>
		<filter-name>HiddenHttpMethodFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>


	<!-- 配置SpringMVC框架入口 -->
	<servlet>
		<servlet-name>taotao-manage</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring/taotao-manager-servlet.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>taotao-manage</servlet-name>
		<url-pattern>/rest/*</url-pattern>
	</servlet-mapping>

	<welcome-file-list>
		<welcome-file>index.jsp</welcome-file>
	</welcome-file-list>

</web-app>