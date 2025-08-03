# Kommodity

✨ *Reusable Kotlin goodies for every project* ✨

## 🚀 What is Kommodity?

Kommodity is a multi-module collection of Kotlin utility libraries and build scripts aimed at boosting productivity across projects. It uses Gradle 7.4+ and comes with the Gradle Wrapper for hassle-free builds.

## 🧩 Module Highlights

- **base-commons** – foundational helpers and API annotations like `@LibraryApi` 💠
- **app-commons** – application-level tools such as colorful Spring Boot banners 🎨
- **api-commons** – shared API infrastructure with exception handling 🌐
- **data-commons** – thread-safe JSON/XML mappers and XML builders 🔒
- **blob-commons** – coroutine-based file system blob service for uploads/downloads 📦
- **hibernate-commons** – JPA/Hibernate type converters, e.g. `BooleanConverter` 🔄
- **signal-commons** – lightweight event signaling framework 📣
- **test-commons** – Kotest matchers and testing utilities 🧪

## 🏗️ Build & Test

```bash
./gradlew build
```
This compiles all modules and runs their tests. Check `doc/BUILD.md` for more options.

## 🤝 Contributing

We welcome issues and pull requests! See `doc/CONTRIBUTING.md` for guidelines.

## 📚 Learn More

- Explore the `callisto-gradle` directory to see custom Gradle plugins ⚙️
- Dive into individual modules to find reusable utilities 💡

## 📄 License

Kommodity will be released under the MIT License ✅
