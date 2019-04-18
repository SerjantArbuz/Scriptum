package sgtmelon.scriptum

import sgtmelon.scriptum.test.IntroTest
import sgtmelon.scriptum.test.RotationTest
import sgtmelon.scriptum.test.SplashTest
import sgtmelon.scriptum.test.main.BinTest
import sgtmelon.scriptum.test.main.MainTest
import sgtmelon.scriptum.test.main.NotesTest
import sgtmelon.scriptum.test.main.RankTest
import sgtmelon.scriptum.ui.screen.main.BinScreen
import sgtmelon.scriptum.ui.screen.main.NotesScreen
import sgtmelon.scriptum.ui.screen.main.RankScreen

/**
 * Описание сценариев для UI тестов
 *
 * Значения:
 * UI - Проверяется UI тестом
 * CONTROL - Проверяется UI тестом с контролем тестировщика
 * MANUAL - Проверяется вручную тестировщиком
 *
 * Для поворота экрана использовать ctrl + left/rightArrow
 *
 * @author SerjantArbuz
 */
@Suppress("unused")
private class ScenarioUi {

    /**
     * TODO:
     * Тест сортировки заметок
     * Тест перетаскивания категорий
     * Тест поворота rename диалога
     * Тест поворото rank контента
     */

    /**
     * Сценарии для [SplashTest]
     */
    class Splash {

        /**
         * UI - Открыть приветствие :: [SplashTest.introScreenOpen]
         */

        /**
         * UI - Открыть главный экран :: [SplashTest.mainScreenOpen]
         */

        /**
         * UI - Открыть заметку через уведомление
         * # Текст :: [SplashTest.statusTextNoteOpen]
         * # Список :: [SplashTest.statusRollNoteOpen]
         */

    }

    /**
     * Сценарии для [IntroTest]
     */
    class Intro {

        /**
         * UI - Расположение контента на страницах :: [IntroTest.contentPlacement]
         */

        /**
         * UI - Доступ к кнопке end :: [IntroTest.endButtonEnable]
         */

        /**
         * UI - Работа кнопки end :: [IntroTest.endButtonWork]
         */

    }

    /**
     * Сценарии для [MainTest]
     */
    class Main {

        /**
         * UI - Стартовый экран :: [MainTest.startScreen]
         */

        /**
         * UI - Отображение правильного экрана при нажатии на пункт меню :: [MainTest.menuClickCorrectScreen]
         */

        /**
         * UI - Отображения кнопки добавить :: [MainTest.addFabVisible]
         */

        /**
         * UI - Работа диалога добавления заметки
         * # Открыть :: [MainTest.addDialogOpen]
         *
         * # Закрыть:
         * # Кнопкой назад :: [MainTest.addDialogCloseSoft]
         * # Свайпом вниз :: [MainTest.addDialogCloseSwipe]
         *
         * # Создать текст :: [MainTest.addDialogCreateTextNote]
         * # Создать список :: [MainTest.addDialogCreateRollNote]
         */

        /**
         * CONTROL - Скроллинга страниц до верха
         * # Категории :: [MainTest.rankScreenScrollTop]
         * # Заметки :: [MainTest.notesScreenScrollTop]
         * # Корзина :: [MainTest.binScreenScrollTop]
         */

    }

    /**
     * Сценарии для [RankTest]
     */
    class Rank {

        /**
         * UI - Контент
         * # Пусто :: [RankTest.contentEmpty]
         * # Список :: [RankTest.contentList]
         **/

        /**
         * CONTROL - Скроллинг списка :: [RankTest.listScroll]
         */

        /**
         * UI - Работа toolbar'а добавления категории
         * # Доступ к кнопке добавить
         * # Пустой текст :: [RankTest.toolbarEnterAddEmpty]
         * # Текст из списка :: [RankTest.toolbarEnterAddFromList]
         * # Доступна :: [RankTest.toolbarEnterAddEnable]
         *
         * # Работа кнопки отчистки :: [RankTest.toolbarEnterClear]
         * # Добавление категории в начало :: [RankTest.toolbarEnterAddStart]
         * # Добавление категории в конец :: [RankTest.toolbarEnterAddEnd]
         */

        /**
         * UI/MANUAL - Управление категорией
         * # Видимость:
         * # Обычное нажатие :: [RankTest.rankVisibleAnimationClick]
         * # Долгое нажатие :: [RankTest.rankVisibleAnimationLongClick]
         * # Заметка :: [RankTest.rankVisibleForNotes]
         * # Заметка в корзине :: [RankTest.rankVisibleForBin]
         *
         * # Удаление:
         * # Удаление из списка :: [RankTest.rankClearFromList]
         * # Заметка :: [RankTest.rankClearForNote]
         *
         * # Перетаскивание
         */

        /**
         * UI - Работа диалога переименовывания
         * # Открыть :: [RankTest.renameDialogOpen]
         *
         * # Закрыть:
         * # Кнопкой назад :: [RankTest.renameDialogCloseSoft]
         * # Кнопкой отмена :: [RankTest.renameDialogCloseCancel]
         *
         * # Блокировка кнопки:
         * # Идентичное название :: [RankTest.renameDialogBlockApplySameName]
         * # Название из списка :: [RankTest.renameDialogBlockApplyFromList]
         *
         * # Результат :: [RankTest.renameDialogResult]
         */

    }

    /**
     * Сценарии для [NotesTest]
     */
    class Notes {

        /**
         * UI - Контент
         * # Пусто :: [NotesTest.contentEmpty]
         * # Список :: [NotesTest.contentList]
         **/

        /**
         * UI - Открыть настройки :: [NotesTest.openPreference]
         */

        /**
         * CONTROL - Скроллинг списка :: [NotesTest.listScroll]
         */

        /**
         * UI - Видимость кнопки добавить при скроллинге :: [NotesTest.addFabVisibleOnScroll]
         */

        /**
         * UI - Открыть заметку
         * # Текст :: [NotesTest.textNoteOpen]
         * # Список :: [NotesTest.rollNoteOpen]
         */

        /**
         * UI - Создание заметки и возврат назад без сохранения
         * # Текст :: [NotesTest.textNoteCreateAndReturn]
         * # Список :: [NotesTest.rollNoteCreateAndReturn]
         */

        /**
         * UI - Создание заметки и возврат назад с сохранением
         * # Текст :: [NotesTest.textNoteCreateAndReturnWithSave]
         * # Список :: [NotesTest.rollNoteCreateAndReturnWithSave]
         */

        /**
         * UI/CONTROL/MANUAL - Работа диалога управления заметкой
         * # Текст:
         * # Открыть :: [NotesTest.textNoteDialogOpen]
         * # Закрыть :: [NotesTest.textNoteDialogClose]
         * # Прикрепить к статус бару :: [NotesTest.textNoteDialogBind]
         * # Открепить от статус бара :: [NotesTest.textNoteDialogUnbind]
         * # Открепить от статус бара при удалении :: [NotesTest.textNoteDialogUnbindOnDelete]
         * # Конвертировать :: [NotesTest.textNoteDialogConvert]
         * # Копировать текст
         * # Удаление :: [NotesTest.textNoteDialogDelete]
         *
         * # Список:
         * # Открыть :: [NotesTest.rollNoteDialogOpen]
         * # Закрыть :: [NotesTest.rollNoteDialogClose]
         * # Выполнить всё :: [NotesTest.rollNoteDialogCheckAllFromEmpty], [NotesTest.rollNoteDialogCheckAll]
         * # Снять выделения :: [NotesTest.rollNoteDialogUncheckAll]
         * # Прикрепить к статус бару :: [NotesTest.rollNoteDialogBind]
         * # Открепить от статус бара :: [NotesTest.rollNoteDialogUnbind]
         * # Открепить от статус бара при удалении :: [NotesTest.rollNoteDialogUnbind]
         * # Конвертировать :: [NotesTest.rollNoteDialogConvert]
         * # Копировать текст
         * # Удаление :: [NotesTest.rollNoteDialogDelete]
         */

    }

    /**
     * Сценарии для [BinTest]
     */
    class Bin {

        /**
         * UI - Контент
         * # Пусто :: [BinTest.contentEmpty]
         * # Список :: [BinTest.contentList]
         **/

        /**
         * CONTROL - Скроллинг списка :: [BinTest.listScroll]
         */

        /**
         * UI - Открыть заметку
         * # Текст :: [BinTest.textNoteOpen]
         * # Список :: [BinTest.rollNoteOpen]
         */

        /**
         * UI - Работы диалога отчистки корзины :: [BinTest.clearDialogWork]
         * # Открыть :: [BinTest.clearDialogOpen]
         *
         * # Закрыть:
         * # Кнопкой назад :: [BinTest.clearDialogCloseSoft]
         * # Кнопкой нет :: [BinTest.clearDialogCloseCancel]
         *
         * # Результат работы :: [BinTest.clearDialogWork]
         */

        /**
         * UI/MANUAL - Работа диалога управления заметкой
         * # Текст:
         * # Открыть :: [BinTest.textNoteDialogOpen]
         * # Закрыть :: [BinTest.textNoteDialogClose]
         * # Восстановить :: [BinTest.textNoteDialogRestore]
         * # Копировать текст
         * # Отчистить :: [BinTest.textNoteDialogClear]
         *
         * # Список:
         * # Открыть :: [BinTest.rollNoteDialogOpen]
         * # Закрыть :: [BinTest.rollNoteDialogClose]
         * # Восстановить :: [BinTest.rollNoteDialogRestore]
         * # Копировать текст
         * # Отчистить :: [BinTest.rollNoteDialogClear]
         */

    }

    /**
     * Сценарии для [RotationTest]
     */
    class Rotation {

        /**
         * CONTROL - Диалог добавления заметки :: [RotationTest.addDialog]
         */

        /**
         * CONTROL - Страница [RankScreen]
         * # Контент
         * # Диалог переименовывания с данными
         */

        /**
         * CONTROL - Страница [NotesScreen]
         * # Контент:
         * # Пусто :: [RotationTest.notesScreenContentEmpty]
         * # Список :: [RotationTest.notesScreenContentList]
         *
         * # Диалог управления заметкой:
         * # Текст :: [RotationTest.notesScreenTextNoteDialog]
         * # Список :: [RotationTest.notesScreenRollNoteDialog]
         */

        /**
         * CONTROL - Страница [BinScreen]
         * # Контент:
         * # Пусто :: [RotationTest.binScreenContentEmpty]
         * # Список :: [RotationTest.binScreenContentList]
         *
         * # Диалог отчистки корзины :: [RotationTest.binScreenClearDialog]
         *
         * # Диалог управления заметкой:
         * # Текст :: [RotationTest.binScreenTextNoteDialog]
         * # Список :: [RotationTest.binScreenRollNoteDialog]
         */

    }

}