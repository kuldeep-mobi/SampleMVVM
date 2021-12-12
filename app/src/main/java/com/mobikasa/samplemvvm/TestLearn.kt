package com.mobikasa.samplemvvm

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import kotlin.random.Random

fun main() {
    checkExecutors()
}

fun checkExecutors() {
    val e = Executors.newFixedThreadPool(8)
    val future = e.submit(TestThread())
    for (i in 1..5) {
        println(Thread.currentThread().name)
    }
    println(future)
    val value = future.get()
    println(value)

}

class TestThread : Callable<Int> {

    override fun call(): Int {
        Thread.sleep(2000)
        return Random(100).nextInt()
    }

}
