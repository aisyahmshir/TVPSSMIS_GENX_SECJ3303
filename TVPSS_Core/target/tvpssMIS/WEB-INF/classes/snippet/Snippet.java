package snippet;

public class Snippet {
	<?xml version="1.0" encoding="UTF-8"?>
	
	<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	  <modelVersion>4.0.0</modelVersion>
	
	  <groupId>com.tvpss</groupId>
	  <artifactId>TVPSS_Core</artifactId>
	  <version>0.0.1-SNAPSHOT</version>
	  <packaging>war</packaging>
	
	  <name>tvpssMIS Maven Webapp</name>
	  <url>http://www.example.com</url>
	
	  <properties>
	    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
	    <maven.compiler.source>8</maven.compiler.source>
	    <maven.compiler.target>8</maven.compiler.target>
	    <spring.version>5.3.21</spring.version>
	    <thymeleaf.version>3.0.15.RELEASE</thymeleaf.version>
	    <thymeleaf.spring5.version>3.0.15.RELEASE</thymeleaf.spring5.version>
	  </properties>
	
	  <dependencies>
	    <!-- JUnit for Testing -->
	    <dependency>
	      <groupId>junit</groupId>
	      <artifactId>junit</artifactId>
	      <version>4.13.1</version>
	      <scope>test</scope>
	    </dependency>
	    
	    <dependency>
	    <groupId>mysql</groupId>
	    <artifactId>mysql-connector-java</artifactId>
	    <version>8.0.33</version> <!-- Use the latest version -->
	</dependency>
	
	    
	    <!-- Spring Web MVC -->
	    <dependency>
	      <groupId>org.springframework</groupId>
	      <artifactId>spring-webmvc</artifactId>
	      <version>${spring.version}</version>
	    </dependency>
	
	    <!-- Thymeleaf Core -->
	    <dependency>
	      <groupId>org.thymeleaf</groupId>
	      <artifactId>thymeleaf</artifactId>
	      <version>${thymeleaf.version}</version>
	    </dependency>
	
	    <!-- Thymeleaf Spring Integration -->
	    <dependency>
	      <groupId>org.thymeleaf</groupId>
	      <artifactId>thymeleaf-spring5</artifactId>
	      <version>${thymeleaf.spring5.version}</version>
	    </dependency>
	
	    <!-- Servlet API Dependency -->
	    <dependency>
	      <groupId>javax.servlet</groupId>
	      <artifactId>javax.servlet-api</artifactId>
	      <version>3.0.1</version>
	      <scope>provided</scope>
	    </dependency>
	
	    <!-- JSTL (Optional, for JSP if needed) -->
	    <dependency>
	      <groupId>javax.servlet</groupId>
	      <artifactId>jstl</artifactId>
	      <version>1.2</version>
	    </dependency>
	    
	    <!-- Logging Dependencies (Optional) -->
	    <dependency>
	      <groupId>org.slf4j</groupId>
	      <artifactId>slf4j-api</artifactId>
	      <version>1.7.36</version>
	    </dependency>
	    <dependency>
	      <groupId>org.slf4j</groupId>
	      <artifactId>slf4j-log4j12</artifactId>
	      <version>1.7.36</version>
	    </dependency>
	  </dependencies>
	
	  <build>
	    <finalName>tvpssMIS</finalName>
	    <pluginManagement>
	      <plugins>
	        <plugin>
	          <artifactId>maven-clean-plugin</artifactId>
	          <version>3.4.0</version>
	        </plugin>
	        <plugin>
	          <artifactId>maven-resources-plugin</artifactId>
	          <version>3.3.1</version>
	        </plugin>
	        <plugin>
	          <artifactId>maven-compiler-plugin</artifactId>
	          <version>3.13.0</version>
	        </plugin>
	        <plugin>
	          <artifactId>maven-surefire-plugin</artifactId>
	          <version>3.3.0</version>
	        </plugin>
	        <plugin>
	          <artifactId>maven-war-plugin</artifactId>
	          <version>3.4.0</version>
	        </plugin>
	        <plugin>
	          <artifactId>maven-install-plugin</artifactId>
	          <version>3.1.2</version>
	        </plugin>
	        <plugin>
	          <artifactId>maven-deploy-plugin</artifactId>
	          <version>3.1.2</version>
	        </plugin>
	      </plugins>
	    </pluginManagement>
	  </build>
	</project>
}
