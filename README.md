# Сервис для рассылки email(серверная часть).

## Приложение умеет рассылать сообщения(в т.ч. в виде html) с файлами-вложениями(и без) по электронной почте

### Установка для локального тестирования: 

- установите Inteliij Idea от компании JetBrains. Ссылка: <https://www.jetbrains.com/idea/download/>

- установите докер. Ссылка на туториал: <https://learn.microsoft.com/ru-ru/virtualization/windowscontainers/manage-docker/configure-docker-daemon>
            ещё одна ссылка: <https://www.youtube.com/watch?v=PHYRSPCD69U&t=450s>

- установите postgres. Ссылка: <https://winitpro.ru/index.php/2019/10/25/ustanovka-nastrojka-postgresql-v-windows/>
            ещё одна ссылка: <https://winitpro.ru/index.php/2019/10/25/ustanovka-nastrojka-postgresql-v-windows/>

- создайте в postgres БД с именем email-sender (в схеме public);

- склонируйте данный репозиторий на свою локальную машину

- создайте на сервисе яндекса свое приложение с правами доступа: к email пользователя, сохранению и чтению в любом месте диска.
        ссылка на api Яндекса: <https://yandex.ru/dev/id/doc/ru/>
        создание приложения Яндекс Диск <https://oauth.yandex.ru/client/new>

- переименуйте файл example.txt в .env

- заполните поля в файле .env вашими данными. JWT-SECRET - любая строка, чем длиннее и бессмысленней, тем надежней токен

- переименуйте файл src\main\resources\db\changelog\changes\01-insert-u.xml в 01-insert-users.xml
- заполните параметр value на 10 строке почтой пользователя, который будет админом. Строка с паролем - это зашифрованное 
значение 100, если хотите вы можете поменять его, воспользовавшись сервисом ByCrypt. Ссылка: <https://bcrypt-generator.com/>

- откройте приложение в Inteliij Idea и запустите его из этой среды разработки. Приложение работает на порту 8008
