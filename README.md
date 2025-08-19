# 🚀 Test Automation Framework

Автоматизированное тестирование веб-приложения SauceDemo с использованием:
- **Java 17**
- **Playwright** (для взаимодействия с браузерами)
- **TestNG** (управление тестами)
- **Allure Report** (визуализация результатов)
- **GitHub Actions** (CI/CD)

## 📌 Быстрый старт

### Предварительные требования
- JDK 17+
- Maven 3.9+
- Node.js 18+ (для Playwright)
- Git

### Установка
```bash
git clone https://github.com/AlexandrRezchikov/java-playwright-testng.git
cd java-playwright-testng
mvn clean install
```

## 🧪 Запуск тестов

### Локальный запуск
```bash
# Стандартный запуск (headless=true)
mvn test

# С GUI браузером
mvn test -Dheadless=false

# Для конкретного браузера (по дефолту используется chrome)
mvn test -Pfirefox  # или -Pwebkit
```

### Параметры запуска
| Параметр         | Значение по умолчанию | Описание                            |
|------------------|-----------------------|-------------------------------------|
| `-Dheadless`     | `true`                | Режим без GUI                       |
| `-Dbrowser`      | `chromium`            | Браузер (chromium/firefox/webkit)   |
| `-DslowMo`       | `0`                   | Задержка между действиями (мс)      |

## 📊 Генерация отчётов
После выполнения тестов:
```bash
mvn allure:serve  # Для просмотра в браузере
mvn allure:report  # Генерация HTML-отчёта в target/allure-report
```

## 🛠️ CI/CD
Тесты автоматически запускаются:
- При каждом PR в `master`
- Вручную через GitHub Actions UI

## 🏗️ Структура проекта
```
src/
├── main/java/
|   ├── components   # компоненты страницы
│   ├── pages/       # Page Object Model
│   ├── utils/       # Вспомогательные классы
│   └── config/      # Конфигурация
└── test/java/
    ├── tests/       # Тестовые классы
    └── resources/   # testng.xml, конфиги
```

## 🐛 Как запустить в дебаг-режиме
1. В IntelliJ IDEA:
    - Создайте конфигурацию "TestNG"
    - Укажите VM Options: `-Dheadless=false -DslowMo=500`
2. Или через командную строку:
   ```bash
   mvn test -Dtest=LoginTests -Dheadless=false -DslowMo=500
   ```

## 🤝 Как внести вклад
1. Создайте ветку от `master`
2. Напишите тесты
3. Сделайте PR с описанием изменений
