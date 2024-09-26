package com.corgrimm.gitrdone

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.corgrimm.gitrdone.db.TodoDao
import com.corgrimm.gitrdone.db.TodoDatabase
import com.corgrimm.gitrdone.model.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@RunWith(AndroidJUnit4::class)
class TodoDatabaseTest {
    private lateinit var todoDao: TodoDao
    private lateinit var db: TodoDatabase

    private val testDispatcher = TestCoroutineDispatcher()

    private val testTodoList = listOf(
        Todo(name = "Todo 1", isDone = false),
        Todo(name = "Todo 2", isDone = false),
        Todo(name = "Todo 3", isDone = false),
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java)
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
        todoDao = db.todoDao()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()

        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndGetAllTodos() = runBlocking {
        testTodoList.forEach {
            todoDao.insert(it)
        }
        val latch = CountDownLatch(1)
        val job = launch(Dispatchers.IO) {
            todoDao.getAllTodos().collect {
                assertThat(it.size, equalTo(testTodoList.size))
                latch.countDown()
            }
        }

        latch.await()
        job.cancel()
    }
}