package homework

enum class Genre(val label: String) {
    FANTASY("Фэнтези"),
    ADVENTURE("Приключения"),
    SCIENCE("Наука")
}


object LibraryRules {
    fun loanDays(genre: Genre): Int {
        return when (genre) {
            Genre.FANTASY -> 21
            Genre.ADVENTURE -> 14
            Genre.SCIENCE -> 7
        }
    }
}

class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: Genre = Genre.FANTASY,
) {
    data class BookInfo(
        val title: String,
        val author: String,
        val genre: Genre
    )
    var isAvailable: Boolean = true
        private set

    init {
        require(id > 0)
        require(title.isNotBlank())
        require(author.isNotBlank())
    }

    fun borrow(): Boolean {
        if (!isAvailable) return false
        isAvailable = false
        return true
    }

    fun returnToLibrary(): Boolean {
        if (isAvailable) return false
        isAvailable = true
        return true
    }

    fun info(): BookInfo {
        return BookInfo(title, author, genre)
    }
}



interface Notifier {
    fun send(message: String)
}

class ConsoleNotifier: Notifier {
    override fun send(message: String) = println("${message}\n")
}
class CountingNotifier: Notifier {
    var sentCount: Int = 0
    var lastMessage: String = ""
    override fun send(message: String) {
        lastMessage = message
        sentCount++
    }
}

class Library(private val books: Array<Book>, private val notifier: Notifier) {
    fun hasBook(id: Int): Boolean {
        for (book in books) {
            if (book.id == id) return true
        }
        return false
    }

    fun borrowBook(id: Int): Boolean {
        if (books[id].borrow()) {
            notifier.send("Выдана книга: ${books[id].title}")
            return true
        }
        return false
    }

    fun returnBook(id: Int): Boolean {
        if (books[id].returnToLibrary()) {
            notifier.send("Возвращена книга: ${books[id].title}")
            return true
        }
        return false
    }

    fun availableCount(): Int {
        var count = 0
        for (book in books) {
            if (book.isAvailable) count++
        }
        return count
    }

    fun countByGenre(genre: Genre): Triple<Int, Int, Int> {
        var count1 = 0
        var count2 = 0
        var count3 = 0
        for (book in books) {
            when (genre) {
                Genre.FANTASY -> count1++
                Genre.ADVENTURE -> count2++
                Genre.SCIENCE -> count3++
            }
        }
        return Triple(count1, count2, count3)
    }

    fun report(): String {
        var s: String = ""
        for (book in books) {
            var n: String = ""
            if (book.borrow()) {
                n="выдана"
            } else {
                n="доступна"
            }

            s += "${book.id}. ${book.title} - ${book.author}: ${n}\n"
        }
        if (s.isNotBlank()) return s
        return "Библиотека пуста"
    }
}
