package org.example.section5

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.example.log
import java.io.File

data class CustomerTransaction(
    val id: String,
    val name: String,
    val numItems: Int,
    val amount: Double,
)

fun main(): Unit = runBlocking {
    try {
        // Get URI to CSV file in the classpath
        val data = ClassLoader.getSystemResource("CustomerTransactionData.csv")?.toURI()
            ?: throw IllegalArgumentException("File not found")

        // Launch async on the IO dispatcher to read and parse file
        val files = async(Dispatchers.IO) {
            File(data).readCsv().map { it.toString() }
        }

        // Wait for result of the file processing and log transaction lines
        files.await().forEach {
            log(it)
        }

        log("Processed.")
    } catch (e: Exception) {
        log("Exception caught: ${e.message}")
    }
}

/**
 * Extension function to read and parse a CSV file into a list of CustomerTransaction objects.
 *
 * @receiver File - the CSV file to parse
 * @return a list of CustomerTransaction records or empty list
 */
private fun File.readCsv(): List<CustomerTransaction> {
    return try {
        log("Reading...")
        readLines().map { line ->
            line.split(",")
                .let {
                    CustomerTransaction(
                        id = it[0],
                        name = it[1],
                        numItems = it[2].toInt(),
                        amount = it[3].toDouble(),
                    )
                }
        }
    } catch (e: Exception) {
        log("Exception caught: ${e.message}")
        emptyList<CustomerTransaction>()
    }
}