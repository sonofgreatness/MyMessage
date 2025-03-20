package project.mymessage.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.mymessage.database.Daos.ConversationDao
import project.mymessage.database.Daos.MessageDao
import project.mymessage.database.Daos.SearchDao
import project.mymessage.database.MainDatabase
import project.mymessage.domain.repository.implementations.ConversationRepositoryImpl
import project.mymessage.domain.repository.implementations.MessageRepositoryImpl
import project.mymessage.domain.repository.implementations.SearchRepositoryImpl
import project.mymessage.domain.repository.interfaces.ConversationRepository
import project.mymessage.domain.repository.interfaces.MessageRepository
import project.mymessage.domain.repository.interfaces.SearchRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    /************************************************
     * LOCAL DATABASE
     ************************************************/
    @Provides
    @Singleton //to ensure single instance
    fun providesConversationDao(app : Application): ConversationDao {
        return MainDatabase.getDatabase(app).getConversationDao()
    }

    @Provides
    @Singleton
    fun provideMessageDao(app: Application): MessageDao{
        return MainDatabase.getDatabase(app).getMessageDao()
    }
    @Provides
    @Singleton
    fun providesSearchDao(app: Application): SearchDao{
        return MainDatabase.getDatabase(app).getSearchDao()
    }



    @Provides
    @Singleton
    fun provideMessageRepository(app: Application,messageDao: MessageDao): MessageRepository{
        return MessageRepositoryImpl(messageDao)
    }

    @Provides
    @Singleton
    fun provideConversationRepository(app: Application, conversationDao: ConversationDao): ConversationRepository{
        return ConversationRepositoryImpl(conversationDao)
    }

   @Provides
   @Singleton
   fun provideSearchRepository (searchDao: SearchDao) : SearchRepository{
    return SearchRepositoryImpl(searchDao)

   }

     

}