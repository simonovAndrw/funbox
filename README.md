### Инструкция по запуску приложения

1. Должны быть установлены Java11, Maven, Redis, Git, Postman
2. Запустить сервер Redis командой  
   ```redis-server```
3. Собрать и запустить проект с помощью команды в директории проекта  
```mvn spring-boot:run```
4. Чтобы выполнить Unit тесты выполнить команду  
```mvn test```
5. Посылать HTTP запросы с помощью Postman

### Конфигурация сервиса
```
server.port=8080
server.address=127.0.0.1

spring.redis.database=0
spring.redis.host=localhost
spring.redis.port=6379
```
 
### Работа сервиса
1. Добавлять посещенные ссылки можно только в правильном формате  
 POST http://localhost:8080/visited_links
```
{
  "links": [
  "https://ya.ru",
  "https://ya.ru?q=123",
  "funbox.ru",
  "https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor"
  ]
}
```  
2. В запросе данных время окончания должно быть не меньше чем время начала  
GET http://localhost:8080/visited_domains?from=1545221231&to=1545217638