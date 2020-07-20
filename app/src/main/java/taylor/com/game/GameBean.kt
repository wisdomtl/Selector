package cn.neoclub.uki.home.game

import java.io.Closeable

data class GameType(
    var name: String?,
    var img: String?
): Closeable {
    override fun close() {
    }
}

data class Games(
    var title: String?,
    var gameTypes: List<GameType>?
)

data class GameAttrs(
    var title: String?,
    var attrs: List<String>?
)

data class GameBean(
    var games: Games?,
    var gameAttrs: List<GameAttrs>?
)
