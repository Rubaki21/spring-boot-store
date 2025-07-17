# It's a shop

✅ Реализация интернет-магазина на Spring Boot 3

<img src="src/main/resources/static/index.png">
<hr>

## Технологии

- Java 21
- Spring framework (Boot, MVC, Rest, Data JPA, Security)
- JUnit (Jupiter) + Mockito
- H2 — БД *(in-memory mode)*
- Thymeleaf + HTML/CSS
- Kafka

## Установка JDK

Для сборки проекта, необходимо установить **JDK 21**

Для установки **JDK**, необходимо выбрать дистрибутив, относительно вашей **операционной системы** https://www.oracle.com/java/technologies/downloads/#jdk21-linux

После установки **JDK**, необходимо прописать `enviroment variable`:

`For Unix`: в bash прописываем `export JAVA_HOME=путь до корня установленной jdk`. Для проверки команда `printenv`

`For Windows`: в cmd прописываем `setx JAVA_HOME "путь до корня установленной jdk"`, для проверки команда `echo %JAVA_HOME%`

При проверке результата, необходимо перезапустить оболочку, т.к. она знает только о тех `enviroment variable`, которые имелись на момент 
её запуска

Пример: 

`setx JAVA_HOME C:\p_env\Java\jdk-21`

## Инфрастуктура

В корне проекта находится `docker-compose.yaml`, который разворачивает всю необходимую инфраструктуру. Для этого необходимо, чтобы был 
установлен `Docker`

**Команда для разворачивания инфраструктуры:** 

Необходимо убедиться, что стоите в корне проекта 

```
$ docker compose up -d
```

После выполнения, необходимо подождать около 30 сек, пока создадутся топики или увидеть их в `kafka-ui`

В инфраструктуре имеется `kafka-ui`, который находится по адресу http://localhost:7777. Использующиеся topic's можно увидеть во вкладке, 
**Topics**

При развёртывании **инфраструктуры**, автоматически создаются 2 топика
1) `out_store.shop_store.products_info`: для загрузки product's из вне
2) `shop_store.out_store.products_info`: результат обработки product's

![kafka-ui.png](src/main/resources/static/kafka-ui.png)

Важно поднимать инфраструктуру до запуска проекта

## Установка приложения

**Команда для клонирования репозитория:**

```
$ git clone git@github.com:Rubaki21/spring-boot-store.git
``` 

**Заходим в корневой каталог проекта и собираем проект. Команда для сборки проекта:**

```
$ ./mvnw clean install
```

**Убедитесь, что используете **JDK 21** !!!**

### **Переходи к следующему пункту, если хочешь запускать в Docker-контейнере, а не локально**

Если планируется запускать приложение локально, то необходимо изменить конфигурацию в `src/main/resources/application.yml` заменить 
`bootstrap-servers: kafka:9093` на `bootstrap-servers: localhost:9092` 

**Команда для локального запуска:**
```
$ ./mvnw spring-boot:run
```

## Docker

Если планируется запускать приложение в Docker-контейнер, для этого необходимо перейти в корневой каталог проекта и создать JAR-файл 
приложения. Команда для сборки проекта:

```
$ ./mvnw clean package
```

**Команда для создания Docker-образа:**

```
$ docker build -t store .
```

**Команда для запуска Docker-образа в контейнере:**
```
$ docker run -d --name store --network=kafka_cluster_local-kafka -e EXTERNAL_GET_PRODUCT_URL=http://localhost:8080/external/products/findAll -p8080:8080 store:latest
```
`EXTERNAL_GET_PRODUCT_URL` должен ссылаться на **rest endpoint** во внешний источник и из которого могут быть получены 1 или несколько 
Products. Если **rest endpoint** отсутствует, то этот параметр можно опустить

Важно для подключения к другому docker container использовать параметр `--network=kafka_cluster_local-kafka`

**Готово!** Приложение будет запущено на порту 8080 и имеет доступ к kafka:9093

**Команда для остановки Docker контейнера:**
```
$ docker stop store
```

## Логин

Из коробки приложение имеет двух пользователей:

- `admin@mail.com` : `admin`, права `admin`
- `user@mail.com` : `user`, права `user`

## Вызов **rest endpoint** во внешний источник

Кнопка вызова находится в админ панели и называется `Загрузить продукты из внешнего источника`

![rest-external-get-api.png](src/main/resources/static/rest-external-get-api.png)

## Формат объектов вызываемых в **rest endpoint**

Схеме объекта находится по пути `src/main/resources/avro/product.avsc` по которому формируется объект с полями:

    `title`: наименование объекта (обязательное поле)
    `imageLocation`: ссылка на фотографию (не обязательное поле) 
    `categoryName`: наименование категории (обязательное поле). Необходимо заполнять наименование из уже имеющихся категорий
    `price`: цена (обязательный параметр). Стоимость товара с дробной частью
    `quantity`: количество (не обязательный параметр)

Пример:

    [
        { "title": "товар 1", "price": "21", "categoryName": "CPU", "quantity":"7" },
        { "title": "товар 2", "price": "22.3", "categoryName": "Camera" }
    ]

## Формат сообщений kafka

1) `out_store.shop_store.products_info`: потребляет Json коллекцию (от 1 до n продуктов). Схема объекта находится по пути 
   `src/main/resources/avro/product.avsc` по которому формируется объект

    - `title`: наименование объекта (обязательное поле)
    - `imageLocation`: ссылка на фотографию (не обязательное поле) 
    - `categoryName`: наименование категории (обязательное поле). Необходимо заполнять наименование из уже имеющихся категорий
    - `price`: цена (обязательный параметр). Стоимость товара с дробной частью
    - `quantity`: количество (не обязательный параметр) 

Пример:

    [
        { "title": "товар 1", "price": "21", "categoryName": "CPU", "quantity":"7" },
        { "title": "товар 2", "price": "22.3", "categoryName": "Camera" }
    ]

2) `shop_store.out_store.products_info`: сюда кладутся результаты обработки product's. Схема объекта находится по пути
   `src/main/resources/avro/result.avsc` по которому формируется объект

   - `result`: результат обработки product
   - `processingTime`: время сохранения product в БД
   - `productInfo`: объект product, который обработали
   - `exceptionMessage`: ошибка обработки, если она была

Пример:

1) успешная обработка

```
{\"result\": true, \"processingTime\": \"2025-07-06 21:43:54\", \"productInfo\": \"{\\\"title\\\": \\\"мой 2 новый товар\\\", 
\\\"imageLocation\\\": null, \\\"categoryName\\\": \\\"CPU\\\", \\\"price\\\": \\\"22\\\", \\\"quantity\\\": 0}\", \"exceptionMessage\": \"\"}
```

2) не успешная обработка

```
 {\"result\": false, \"processingTime\": \"2025-07-06 21:44:05\", \"productInfo\": \"{\\\"title\\\": \\\"мой 1 новый товар\\\", 
   \\\"imageLocation\\\": null, \\\"categoryName\\\": \\\"CP\\\", \\\"price\\\": \\\"21\\\", \\\"quantity\\\": 0}\", \"exceptionMessage\": \"в базе данных отсутствует категория с именем: CP\"}
```

### И ещё кое-что

- Запуск происходит локально на порту `8080`
- Доступ к БД можно получить по URL `/h2-console`. Доступ имеют только авторизованные пользователи с правами `admin`
- Данные для входа в БД:
    - URL: `jdbc:h2:mem:store`
    - Логин: `sa`
    - Password:` `  (пароль пустой)  
      <img src="src/main/resources/static/h2.png" height=40% width=40%>
- Картинки в БД хранятся в виде ссылок из интернета. Просто скопируйте ссылку на картинку и вставьте её при создании
  товара  
  <img src="src/main/resources/static/urlcopy.png" height=30% width=30%>

### Скриншоты

<p align="center">
<img src="src/main/resources/static/cart.png" height=60% width=60%>
<hr>
<p align="center">
<img src="src/main/resources/static/product.png" height=60% width=60%>
<hr>
<p align="center">
<img src="src/main/resources/static/admin.png" height=60% width=60%>
<hr>
<p align="center">
<img src="src/main/resources/static/error.png" height=60% width=60%>
<hr>
