package at.ac.tuwien.inso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.ac.tuwien.inso.model.Friend
import at.ac.tuwien.inso.repository.FriendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel for the screens in [at.ac.tuwien.inso.ui.navigation.FriendNavigation].
 *
 * Logic layer to provide data to the view and handle user input or actions.
 */
class FriendViewModel(
    private val friendRepository: FriendRepository
) : ViewModel() {

    // Flow of "friends" for the UI.
    // This will emit new data when the database table gets updated at any point.
    val friends = friendRepository
        .getFriends()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Initially load data from the API.
        refreshFriends()
    }

    /**
     * Uses the [FriendRepository] to update the friend list.
     * This will execute the API request and persist the result to the database.
     * When the database is updated, the view will automatically get notified
     * in the flow [friends].
     */
    fun refreshFriends() {
        // Launch I/O operation asynchronously and bind it to the Scope of the ViewModel.
        viewModelScope.launch(Dispatchers.IO) {
            friendRepository.refreshFriends()
        }
    }

    /**
     * Creates a new [Friend] via the repository.
     *
     * @param friend The [Friend] which will be created.
     */
    fun insertFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) { friendRepository.insert(friend) }
    }

    /**
     * Deletes the given friend through the repository.
     *
     * @param friend The [Friend] to delete.
     */
    fun deleteFriend(friend: Friend) {
        viewModelScope.launch(Dispatchers.IO) { friendRepository.delete(friend) }
    }

    /**
     * Loads a single [Friend] by its ID.
     *
     * @param id The ID of the friend.
     * @return A [Friend] if one exists for the given ID. Otherwise null.
     */
    fun getFriendById(id: Int): Friend? {
        return friends.value.firstOrNull { it.id == id }
    }
}
