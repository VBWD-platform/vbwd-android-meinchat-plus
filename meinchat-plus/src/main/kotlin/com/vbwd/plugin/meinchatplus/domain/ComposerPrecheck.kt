package com.vbwd.plugin.meinchatplus.domain

import com.vbwd.plugin.meinchat.domain.MeinChatSecureMessaging
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive

/**
 * The composer's peer-selection decision (S28.7 §4.6). Port of the iOS
 * `ComposerPrecheckResult`/`ComposerPrecheck`.
 */
sealed interface ComposerPrecheckResult {
    data object Ready : ComposerPrecheckResult

    data object LocalNotPaired : ComposerPrecheckResult

    data object PeerCannotReceive : ComposerPrecheckResult

    data class ProbeFailedOptimistic(val error: String) : ComposerPrecheckResult

    val canCompose: Boolean
        get() = this is Ready || this is ProbeFailedOptimistic
}

class ComposerPrecheck(private val secure: MeinChatSecureMessaging) {
    // Broad catch is intentional: a transient probe failure enables optimistically
    // (the send path's fail-closed guard still catches a real downgrade).
    @Suppress("TooGenericExceptionCaught")
    suspend fun check(peerUserId: String): ComposerPrecheckResult {
        if (!secure.isReady()) return ComposerPrecheckResult.LocalNotPaired
        return try {
            if (secure.peerCanReceiveE2E(peerUserId)) {
                ComposerPrecheckResult.Ready
            } else {
                ComposerPrecheckResult.PeerCannotReceive
            }
        } catch (error: Exception) {
            currentCoroutineContext().ensureActive() // re-throw if cancelled
            ComposerPrecheckResult.ProbeFailedOptimistic(error.toString())
        }
    }
}
