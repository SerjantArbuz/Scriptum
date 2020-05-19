package sgtmelon.scriptum.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.room.converter.model.*
import javax.inject.Singleton

/**
 * Module for provide converters
 */
@Module
class ConverterModule {

    @Provides
    @Singleton
    fun provideAlarmConverter() = AlarmConverter()

    @Provides
    @Singleton
    fun provideNoteConverter() = NoteConverter()

    @Provides
    @Singleton
    fun provideRankConverter() = RankConverter()

    @Provides
    @Singleton
    fun provideRollConverter() = RollConverter()

    @Provides
    @Singleton
    fun provideStringConverter() = StringConverter()

}