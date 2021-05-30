package nl.rso.axon.happergame.commandside.model

data class Square(val coordinate: Coordinate, val gameElements: List<GameElement>) {

    fun pushGameElement(gameElementK: GameElement): Square {
        return this.copy(gameElements = this.gameElements.plus(gameElementK))
    }

    fun removeGameElement(gameElement: GameElement): Square {
        return this.copy(gameElements = this.gameElements.reversed().minus(gameElement).reversed())
    }

    fun getNeighbourCoordinate(direction: Direction): Coordinate {

        val neighbour = when (direction) {
            Direction.EAST -> Coordinate(coordinate.x + 1, coordinate.y)
            Direction.WEST -> Coordinate(coordinate.x - 1, coordinate.y)
            Direction.SOUTH -> Coordinate(coordinate.x, coordinate.y + 1)
            Direction.NORTH -> Coordinate(coordinate.x, coordinate.y - 1)
        }

        return neighbour
    }

    fun getDirection(other: Square): Direction? {
        val xDelta = other!!.coordinate.x - this.coordinate.x
        val yDelta = other!!.coordinate.y - this.coordinate.y
        when (xDelta) {
            1 -> return Direction.EAST
            -1 -> return Direction.WEST
        }

        when (yDelta) {
            1 -> return Direction.SOUTH
            -1 -> return Direction.NORTH
        }

        return null
    }

    fun peekGameElement(): GameElement? {
        return when (this.gameElements.size) {
            0 -> null
            else -> this.gameElements.last()
        }
    }

    fun isEmpty(): Boolean {
        return this.gameElements.isEmpty()
    }

    fun humanOnTop(): Boolean {
        return peekGameElement() == Human()

    }

    fun happerOnTop(): Boolean {
        return peekGameElement() == Happer()
    }
}