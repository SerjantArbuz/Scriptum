package sgtmelon.scriptum

import sgtmelon.scriptum.test.IntroTest
import sgtmelon.scriptum.test.SplashTest
import sgtmelon.scriptum.test.main.BinTest
import sgtmelon.scriptum.test.main.MainTest
import sgtmelon.scriptum.test.main.NotesTest
import sgtmelon.scriptum.test.main.RankTest

/**
 * Описание сценариев для тестов
 *
 * @author SerjantArbuz
 */
@Suppress("unused")
class Scenario {

    // TODO сценарии из книги (поворот экрана и прочее)

    /**
     * Сценарии для [SplashTest]
     */
    class Splash {

        /**
         * 1. UI - Открытие приветствия
         */

        /**
         * 2. UI - Открытие главного экрана
         */

        /**
         * 3. UI - Открытие заметок через уведомление
         *      #Текст
         *      #Список
         */

    }

    /**
     * Сценарии для [IntroTest]
     */
    class Intro {

        /**
         * 1. UI - Расположение контента на страницах
         */

        /**
         * 2. UI - Доступ к кнопке end
         */

        /**
         * 3. UI - Работа кнопки end
         */

    }

    /**
     * Сценарии для [MainTest]
     */
    class Main {

        /**
         * 1. UI - Отображение правильного экрана при нажатии на пункт меню
         */

        /**
         * 2. UI - Стартовый экран
         */

        /**
         * 3. UI - Отображения кнопки добавить
         */

        /**
         * 4. UI - Открытие диалога добавления заметки
         */

        /**
         * 5. HANDLE - Поворот диалога добавления заметки
         */

        /**
         * 6. UI - Работа диалога добавления заметки
         *      #Создать текст
         *      #Создать список
         */

        /**
         * 7. UI/CONTROL - Скроллинга страниц до верха
         *      #Категории
         *      #Заметки
         *      #Корзина
         */

    }

    /**
     * Сценарии для [RankTest]
     */
    class Rank

    /**
     * Сценарии для [NotesTest]
     */
    class Notes {

        /**
         * 1. UI - Контент
         *      #Пусто
         *      #Список
         **/

        /**
         * 2. HANDLE - Контент после поворота
         *      #Пусто
         *      #Список
         */

        /**
         * 3. UI - Открытие настроек
         */

        /**
         * 4. UI - Видимость кнопки добавить при скроллинге
         */

        /**
         * 5. UI - Открытие заметки
         *      #Текст
         *      #Список
         */

        /**
         * 6. UI - Создание заметки и возврат назад без сохранения
         *      #Текст
         *      #Список
         */

        /**
         * 7. UI - Создание заметки и возврат назад с сохранением
         *      #Текст
         *      #Список
         */

        /**
         * 8. UI - Открытие диалога управления заметкой
         *      #Текст
         *      #Список
         */

        /**
         * 9. HANDLE - Поворот диалога управления заметкой
         *      #Текст
         *      #Список
         */

        /**
         * 10. UI - Отметка пунктов списка
         *      #Выполнить всё
         *      #Снять выделения
         */

        /**
         * 11. UI/CONTROL - Прикрепить/открепить заметку к статус бару
         *      #Прикрепить текст
         *      #Открепить текст
         *      #Прикрепить список
         *      #Открепить список
         */

        /**
         * 12. UI - Конвертирование заметки
         *      #Текст
         *      #Список
         */

        /**
         * 13. UI - Копирование текста заметки
         *      #Текст
         *      #Список
         */

        /**
         * 14. UI - Удаление заметки
         *      #Текст
         *      #Список
         */

    }

    /**
     * Сценарии для [BinTest]
     */
    class Bin {

        /**
         * 1. Проверка контента
         */

        /**
         * 2. Скроллинг списка до конца и обратно
         */

        /**
         * 3. Проверка открытия заметки - текст / список
         */

        /**
         * 4. Проверка работы диалога отчистки корзины
         */

        /**
         * 5. Проверка открытия диалога управления заметкой - текст / список
         */

        /**
         * 6. Проверка восстановления заметки - текст / список
         */

        /**
         * 7. Проверка копирования текста - текст / список
         */

        /**
         * 8. Проверка удаления заметки - текст / список
         */

    }

}