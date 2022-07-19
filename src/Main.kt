import java.io.File

fun main(args: Array<String>) {
    val sortingType = try {
        args[args.indexOf("-sortingType") + 1]
    } catch (_: Exception) {
        println("No sorting type defined!")
        ""
    }
    val dataType = try {
        args[args.indexOf("-dataType") + 1]
    } catch (_: Exception) {
        println("No data type defined!")
        ""
    }
    val inputFile =
        if (args.contains("-inputFile")) {
            try {
                args[args.indexOf("-inputFile") + 1]
            } catch (_: Exception) {
                ""
            }
        } else {
            ""
        }
    val outputFile =
        if (args.contains("-outputFile")) {
            try {
                args[args.indexOf("-outputFile") + 1]
            } catch (_: Exception) {
                ""
            }
        } else {
            ""
        }
    val resultInfo =
        if (sortingType.isNotBlank() && dataType.isNotBlank()) {
            checkOtherParameters(args, sortingType, dataType, inputFile, outputFile)
            val lines = readLines(inputFile)
            when (dataType) {
                "long" -> getNumbersInfo(lines, sortingType)
                "line" -> getLinesInfo(lines, sortingType)
                else -> getWordsInfo(lines, sortingType)
            }
        } else {
            ""
        }
    if (outputFile.isBlank()) {
        print(resultInfo)
    } else {
        val myFile = File(outputFile)
        myFile.writeText(resultInfo)
    }
}

fun checkOtherParameters(
    args: Array<String>, sortingType: String, dataType: String, inputFile: String, outputFile: String
) {
    for (arg in args) {
        if (
            arg != "-sortingType" && arg != sortingType && arg != "-dataType" && arg != dataType &&
            arg != "-inputFile" && arg != inputFile && arg != "-outputFile" && arg != outputFile
        ) {
            println("$arg is not a valid parameter. It will be skipped.")
        }
    }
}

fun readLines(inputFile: String): List<String> {
    val lines = mutableListOf<String>()
    try {
        if (inputFile.isBlank()) {
            while (true) {
                lines.add(readln())
            }
        } else {
            lines.addAll(File(inputFile).readLines())
        }
    } catch (_: Exception) {
    }
    return lines
}

fun getNumbersInfo(lines: List<String>, sortingType: String): String {
    val numbers = getNumbers(lines)
    val sortedNumbers = numbers.sorted()
    var resultInfo = "Total numbers: ${sortedNumbers.size}.\n"
    return if (sortingType == "byCount") {
        val sortedStringNumbers = mutableListOf<String>()
        for (number in sortedNumbers) {
            sortedStringNumbers.add(number.toString())
        }
        getSortedMapInfo(sortedStringNumbers, resultInfo)
    } else {
        resultInfo += "Sorted data: ${sortedNumbers.joinToString(" ")}"
        resultInfo
    }
}

fun getNumbers(lines: List<String>): List<Int> {
    val numbers = mutableListOf<Int>()
    for (line in lines) {
        for (potentialNumber in line.split(" ")) {
            if (potentialNumber.isNotBlank()) {
                try {
                    numbers.add(potentialNumber.toInt())
                } catch (_: NumberFormatException) {
                    println("$potentialNumber is not a long. It will be skipped.")
                }
            }
        }
    }
    return numbers
}

fun getLinesInfo(lines: List<String>, sortingType: String): String {
    val sortedLines = lines.sorted()
    var resultInfo = "Total lines: ${sortedLines.size}.\n"
    return if (sortingType == "byCount") {
        getSortedMapInfo(sortedLines, resultInfo)
    } else {
        resultInfo += "Sorted data:\n"
        for (line in sortedLines) {
            resultInfo += "$line\n"
        }
        resultInfo
    }
}

fun getWordsInfo(lines: List<String>, sortingType: String): String {
    val words = mutableListOf<String>()
    for (line in lines) {
        for (word in line.split(" ")) {
            if (word.isNotBlank()) {
                words.add(word)
            }
        }
    }
    val sortedWords = words.sorted()
    var resultInfo = "Total words: ${sortedWords.size}.\n"
    return if (sortingType == "byCount") {
        getSortedMapInfo(sortedWords, resultInfo)
    } else {
        resultInfo += "Sorted data: "
        for (word in sortedWords) {
            resultInfo += "$word "
        }
        resultInfo
    }
}

fun getSortedMapInfo(strings: List<String>, info: String): String {
    val map = mutableMapOf<String, Int>()
    var resultInfo = info
    for (currentString in strings) {
        if (map.containsKey(currentString)) {
            val value = map[currentString]?.plus(1) ?: -1
            map[currentString] = value
        } else {
            map[currentString] = 1
        }
    }
    val mapSize = strings.size
    val maxValue = map.values.sorted()[map.size - 1]
    for (i in 1..maxValue) {
        val keysToRemove = mutableListOf<String>()
        for (pair in map) {
            if (pair.value == i) {
                resultInfo += "${pair.key}: ${pair.value} time(s), ${100 * pair.value / mapSize}%\n"
                keysToRemove.add(pair.key)
            }
        }
        for (removeKey in keysToRemove) {
            map.remove(removeKey)
        }
    }
    return resultInfo
}