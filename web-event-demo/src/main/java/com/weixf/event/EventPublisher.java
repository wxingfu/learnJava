package com.weixf.event;// EventPublisher.java

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class EventPublisher {


    private static EventPublisher instance;

    private final Map<Class<? extends Event>, List<EventListener>> listeners;

    private final ExecutorService executorService;


    private EventPublisher() {
        this.listeners = new ConcurrentHashMap<Class<? extends Event>, List<EventListener>>();
        this.executorService = Executors.newFixedThreadPool(10);
    }


    public static synchronized EventPublisher getInstance() {
        if (instance == null) {
            instance = new EventPublisher();
        }
        return instance;
    }


    public <T extends Event> void addListener(Class<T> eventType, EventListener<T> listener) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners == null) {
            eventListeners = new CopyOnWriteArrayList<EventListener>();
            listeners.put(eventType, eventListeners);
        }
        eventListeners.add(listener);
    }


    public <T extends Event> void removeListener(Class<T> eventType, EventListener<T> listener) {
        List<EventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }


    public void publish(Event event) {
        List<EventListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener listener : eventListeners) {
                // listener.onEvent(event);
                // 异步执行监听器以避免阻塞
                executorService.submit(new EventTask(listener, event));
            }
        }
    }


    private static class EventTask implements Runnable {
        private final EventListener listener;
        private final Event event;

        public EventTask(EventListener listener, Event event) {
            this.listener = listener;
            this.event = event;
        }

        public void run() {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void shutdown() {
        executorService.shutdown();
    }


}
