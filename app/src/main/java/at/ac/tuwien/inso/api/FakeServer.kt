package at.ac.tuwien.inso.api

import android.util.Log
import at.ac.tuwien.inso.model.Friend
import kotlinx.coroutines.delay
import kotlin.random.Random

private var serverFriendList: ArrayList<Friend> = arrayListOf()

/**
 * A suspend function to fake an API server request.
 *
 * This function will delay the thread and return a random list of [Friend].
 *
 * Note: in most cases different data classes for the API and database layer should be used.
 * Using the same class for both might increase the complexity for defining the database
 * and serialization structure unnecessary. And in some cases, we might need different
 * variables and data for both cases - eg. an internal ID.
 *
 * @return A list of [Friend]
 */
suspend fun getFriendsFromApi(): List<Friend> {

    Log.d("Server", "Starting Fake Update")

    // Make an API call to receive and parse API responses.
    delay(2000)

    if (serverFriendList.isEmpty()) {
        serverFriendList.addAll(generateRandomFriendList())
    }

    return serverFriendList
}

suspend fun uploadFriend(friend: Friend) {
    delay(2000)
    serverFriendList.add(friend)
}

suspend fun removeFriend(friend: Friend) {
    delay(2000)
    serverFriendList.remove(friend)
}

private fun generateRandomNumber() = Random.nextInt(0, 10)

private fun getRandomName() = listOf(
    "Christian",
    "Richard",
    "Karin",
    "Stefan",
    "Gabriel",
    "Brigitte",
    "Gabriela"
).shuffled().first()

fun generateRandomFriendList() = listOf(
    Friend(
        id = 0,
        name = "Monika",
        score = generateRandomNumber()
    ),
    Friend(
        id = 1,
        name = "Clemens",
        score = generateRandomNumber()
    ),
    Friend(
        id = 2,
        name = "Xenia",
        score = generateRandomNumber()
    ),
    Friend(
        id = generateRandomNumber(),
        name = getRandomName(),
        score = generateRandomNumber()
    )
)
