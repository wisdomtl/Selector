package cn.neoclub.uki.home.game

import java.io.Closeable

data class GameType(
    var name: String?,
    var img: String?
) : Closeable {
    override fun close() {
        name = null
        img = null
    }
}

data class Games(
    var title: String?,
    var gameTypes: List<GameType>?
)

data class GameAttrs(
    var title: String?,
    var attrs: List<GameAttrName>?
)

data class GameAttrName(
    var name: String?
) : Closeable {
    override fun close() {
        name = null
    }
}

data class GameBean(
    var games: Games?,
    var gameAttrs: List<GameAttrs>?
)
