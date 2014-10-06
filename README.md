phystech-java-2014
==================

Parallel database task for Java2 course

Отчет: 

1.  Нарисована схема модулей и классов
2. Выпилен весь мусор (корни старых проектов) + переработаны интерфейсы Table и других.
3. База данных разделена на модули:
   shell-api
   shell-impl
   table-api
   db-table-api
   db-table-impl
   db-shell-impl
   db-main
4. Используется ApplicationContext конфигурируемый через аннотации. 
5. Команды для Shell теперь добавляются сами через @Autowired. ShellState тоже заполняется автоматически через @Autowired + 
   путь к базе данных выставляется через внешний .properties файл (см. корень проекта)
5. Прикручен логгинг через slf4j + log4j. Логгирует в файл (указывается через -Dlog.path=...) вызовы команд shell. 
6. Вся работа с файлами и join-ы строк берутся из Google Guava и Apache Commons. 
7. Тесты оставлены, работают через junit. 
