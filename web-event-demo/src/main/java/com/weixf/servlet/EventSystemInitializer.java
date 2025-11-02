package com.weixf.servlet;// EventSystemInitializer.java

import com.weixf.event.EventPublisher;
import com.weixf.event.impl.LoginEventListener;
import com.weixf.event.impl.UserLoginEvent;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class EventSystemInitializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        // 初始化事件系统
        EventPublisher publisher = EventPublisher.getInstance();

        // 注册监听器
        publisher.addListener(UserLoginEvent.class, new LoginEventListener());

        // 将事件发布器存储在ServletContext中供其他组件使用
        sce.getServletContext().setAttribute("eventPublisher", publisher);
    }



    public void contextDestroyed(ServletContextEvent sce) {
        // 清理资源
        EventPublisher publisher = (EventPublisher) sce.getServletContext().getAttribute("eventPublisher");
        if (publisher != null) {
            publisher.shutdown();
        }
    }


}
