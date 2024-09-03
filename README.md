# Music Player with iTunes search API
Музыкальный плеер на основе Jetpack Media3. Возможности:
- Поиск и прослушивание превью треков в приложении
- Прослушивание в фоновом режиме
- Создание и редактирование плейлистов
- Добавление/удаление треков в плейлистах

# Технологии
- TDD
- JUnit4
- Espresso
- Jetpack Media3
- MVVM
- Room (many to many)
- Retrofit2
- Picasso
- Custom: observers, DI and Navigation

Приложение разработано по TDD-методологии. 

[UI-тесты](https://github.com/AlexeyQQQ/ItunesSearchMusicApp/blob/master/app/src/androidTest/java/ru/easycode/intensive2itunessearch/ui/ScenarioTest.kt) покрывают все основные кейсы использования приложения: поиск треков, воспроизведение, остановка музыки. Манипуляции с треками и плейлистами - добавление, удаление, редактирование и т.д. Подробное описание каждого UI тест-кейса доступно в файле. 
Также тест-кейсами прокрыты room, вьюмодели и репозитории.

Работа с медиа ведется с помощью библиотеки Jetpack Media3 для обеспечения фонового воспроизведения. Используемые компоненты: ExoPlayer, MediaController, MediaSessionService.

Приложение использует [iTunes Search API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html#//apple_ref/doc/uid/TP40017632-CH3-SW1)
________
Репозиторий является форком от приватного командного репозиторий для дальнейшей самостоятельной разработки.
<img src="https://github.com/user-attachments/assets/531a9f7d-6c9a-4e43-a0cd-3398d65de764" width="180"/>
<img src="https://github.com/user-attachments/assets/afd0097a-5036-4552-b6ce-0c4384dc4e48" width="180"/>
