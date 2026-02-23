![License](https://img.shields.io/badge/license-MIT-blue) ![Splatgames Nexus](https://img.shields.io/badge/repository-Splatgames.de-orange) ![Version](https://img.shields.io/badge/version-1.0.0-blue)
# Aether Events 🚀

Aether Events is a powerful asynchronous event system for the JVM.
It enables modular and flexible event processing with support for synchronous and asynchronous events, event pipelines,
and cancellable events.

> [!WARNING]
> ## ⚠️ Project Discontinued
>
> This repository is no longer maintained due to structural changes within **Splatgames.de Software**.
>
> The project in its current form is considered deprecated.
>
> It may either:
> - be re-released in a redesigned and structurally aligned version, or  
> - remain permanently abandoned.
>
> No updates, fixes, or support will be provided.
>
> Use at your own risk.

---

## ✨ Features

✅ **EventBus** for centralized event and listener management  
✅ **Event Pipelines** for filtering, prioritization, and processing  
✅ **Asynchronous Events** with multi-threading support  
✅ **Cancellable Events** to stop specific events  
✅ **Thread Safety** for parallel processing  
✅ **Easy Integration**, no complex dependencies required

---

## 📦 Installation

Aether Events is available via **Maven** and **Gradle**.

#### **Maven**

There are two repositories available for Aether Events:

- **Libraries Repository (Pointer to Nexus)**: `https://libraries.splatgames.de/`
- **Nexus Repository**: `https://nexus.splatgames.de/repository/maven-public/`

```xml

<repository>
    <id>splatgames.de</id>
    <url>https://libraries.splatgames.de/</url>
</repository>

<dependency>
    <groupId>de.splatgames.aether</groupId>
    <artifactId>aether-events</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### **Gradle**

```groovy
repositories {
    maven {
        url 'https://libraries.splatgames.de/'
    }
}

dependencies {
    implementation 'de.splatgames.aether:aether-events:1.0.0'
}
```

---

## 🚀 Quick Start

### 1️⃣ Creating an EventBus

```java
EventBus eventBus = EventBus.builder().build();
```

### 2️⃣ Registering a Listener

```java
eventBus.subscribe(new TestListener());
```

### 3️⃣ Firing an Event

```java
MyEvent event = new MyEvent(42);
eventBus.createPipeline("test:pipeline").fire(event);
```

---

## 🎯 Event Pipelines

Event Pipelines allow flexible event processing with filtering and prioritization:

```java
EventPipeline<MyEvent> pipeline = eventBus.createPipeline("customPipeline");

pipeline
    .filter(event -> event.getExampleValue() > 0)
    .sorted(Comparator.comparing(MyEvent::getExampleValue))
    .registerConsumer(event -> System.out.println("Event received: " + event.getExampleValue()));

pipeline.fire(new MyEvent(10));
```

---

## ⚡ Asynchronous Events

Events can be processed asynchronously:

```java
Thread thread = new Thread(() -> {
    EventPipeline<AsyncMyEvent> asyncPipeline = eventBus.createPipeline("test:asyncPipeline");
    asyncPipeline.fire(new AsyncMyEvent(99));
});

thread.start();
```

---

## ❌ Cancellable Events

An event can be canceled before it is processed:

```java
MyCancellableEvent cancellableEvent = new MyCancellableEvent();

EventPipeline<MyCancellableEvent> cancellablePipeline = eventBus.createPipeline("test:cancellablePipeline");
cancellablePipeline.fire(cancellableEvent);

if (cancellableEvent.isCancelled()) {
    System.out.println("The event was cancelled!");
}
```

---

## 🛠 Listener Example

A simple listener that receives events:

```java
public class TestListener implements Listener {
    
    @Subscribe
    public void onMyEvent(final MyEvent event) {
        System.out.println("MyEvent: " + event.getExampleValue());
    }
    
    @Subscribe
    public void onAsyncMyEvent(final AsyncMyEvent event) {
        System.out.println("AsyncMyEvent: " + event.getExampleValue());
    }
    
    @Subscribe
    public void onMyCancellableEvent(final MyCancellableEvent event) {
        event.setCancelled(true);
        System.out.println("MyCancellableEvent was cancelled");
    }
}
```

---

## 📢 Latest Release

- 🚀 **Version:** `1.0.0`
- 📅 **Release Date:** `March 5, 2025`
- 📦 **Available on**:
[![Splatgames Nexus](https://img.shields.io/badge/repository-Splatgames.de-orange)](https://nexus.splatgames.de/repository/maven-public/)

---

## 🤝 Contributing

We welcome contributions! 🎉  
Please check out our [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute.

---

## 📜 License

Aether Events is released under the **MIT License**.

```text
MIT License

Copyright (c) 2025 Splatgames.de Software

Permission is hereby granted, free of charge, to any person obtaining a copy of this software...
```

---

## 🌟 Conclusion

Aether Events is a powerful solution for event-driven architectures on the JVM. It offers high flexibility, asynchronous
processing, and a modern API for easy integration.

🔥 **Get started with Aether Events now!** 🚀
