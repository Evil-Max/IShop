<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="
   http://www.springframework.org/schema/beans
   http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">


    <bean id = "fakeData" class = "com.test.ishop.config.FakeData">
        <property name="map">
            <map key-type="java.lang.String" value-type="java.lang.String">
                <#list map as entry>
                <entry key="${entry.key}" value="${entry.value}"/>
                </#list>
            </map>
        </property>
        <property name="message" value="${message}"/>

    </bean>
    <bean id="fakeClass" class="com.test.ishop.config.FakeClass">
        <property name="fakeData" ref="fakeData"/>
    </bean>
</beans>