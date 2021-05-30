package nl.rso.axon.happergame.commandside.model

import java.util.*


data class Playground(
        val width: Int,
        val Height: Int,
        val squares: List<Square>){

    fun moveHumanToResult(direction: Direction) : MovePlayableResult<Human, Pushable> {
        val (moved, squaresInvolved) = moveHumanToInfo(direction)

        when {
            moved -> {
                val movement = toMovement(squaresInvolved[0], direction) as Movement<Human>
                val pushes = squaresInvolved.drop(1)

                when {
                    !pushes.isEmpty() -> {
                        val resultPushes = pushes.map { squareK -> toMovement(squareK, direction) } as List<Movement<Pushable>>
                        return MovePlayableResult(movement = movement, movedBoxes = resultPushes)
                    }
                    else -> return MovePlayableResult(movement = movement, movedBoxes = listOf())
                }

            }
            else -> return MovePlayableResult(null, movedBoxes = listOf())
        }
    }

    fun humanOnSameSquareAsHapper(): Boolean {
        return getSquareWithHapper() == getSquareWithHuman()
    }

    fun happerIsAbleToMove(): Boolean {
        return !getSquaresWhereHapperCanMoveTo().isEmpty()
    }


    private fun getHapperRandomMovementInfo() : Movement<Happer> {
        val randomSquareNextToHapper = getSquaresWhereHapperCanMoveTo().getRandomElement()
        val happerSquare = getSquareWithHapper()
        val direction = happerSquare.getDirection(randomSquareNextToHapper)

        return Movement(happerSquare.peekGameElement() as Happer, direction!!, happerSquare.coordinate, randomSquareNextToHapper.coordinate)
    }

    fun getHapperMovementInfo() : Movement<Happer>? {
        val isPathPossible = AstarAlgorithm().getShortestPath(this).size > 0
        if(isPathPossible) {
            val bestMoveToHapper = AstarAlgorithm().getShortestPath(this).reversed().get(1)
            return getHapperMovementInfo(getSquareWithHapper(), bestMoveToHapper)
        } else {
            return getHapperRandomMovementInfo()
        }
    }

    private fun getHapperMovementInfo(happerSquare: Square, squareToMove: Square) : Movement<Happer> {
        val direction = happerSquare.getDirection(squareToMove)
        return Movement(happerSquare.peekGameElement() as Happer, direction!!, happerSquare.coordinate, squareToMove.coordinate)
    }

     fun findSquare(coordinate: Coordinate?) : Square? {
       return this.squares.find { squareK -> squareK.coordinate == coordinate }
    }

    private fun findNeighbour(square: Square?, direction: Direction): Square? {
        val neighbourCoordinate = square?.getNeighbourCoordinate(direction)
        return findSquare(neighbourCoordinate)
    }

    private fun getSquaresNextTo(square: Square?) : List<Square> {
        return Direction.values()
                .mapNotNull({directionsK -> findNeighbour(square, directionsK)})
    }

    private fun getSquaresInDirectionFromGivenSquare(given: Square, direction: Direction) : List<Square> {

        fun getSquaresInDirectionHelper(current: Square, result: ArrayList<Square>): List<Square>  {

            val neighbour = findNeighbour(current, direction)
            when (neighbour) {
                null -> return result
                else -> {
                    result += neighbour
                    return getSquaresInDirectionHelper(neighbour, result)
                }
            }
        }

        return getSquaresInDirectionHelper(given, arrayListOf(given))
    }

    @Throws(IllegalStateException::class)
    private fun moveHumanToInfo(direction: Direction) : MoveHumanInfo {

        val humanSquare = getSquareWithHuman()
        val squaresInvolved = getSquaresInDirectionFromGivenSquare(humanSquare, direction)

        when (squaresInvolved.size) {
            0    -> throw IllegalStateException("Human must be  on the playground")
            1    -> return Pair(false, listOf())
            else ->  if (squaresInvolved[1].isEmpty()) {
                        return Pair(true, listOf(humanSquare))
                     } else {
                        when (squaresInvolved[1].peekGameElement()) {
                            Happer() -> return Pair(true, listOf(humanSquare))
                            Block()  -> return Pair(false, listOf())
                            else     -> return isPushable(squaresInvolved)
                        }
            }
        }
    }

    private fun toMovement(square: Square, direction: Direction) : Movement<Movable> {
        val movedGameElementK = square.peekGameElement()!!

        when (movedGameElementK) {
            is Movable -> return Movement(gameElementK = movedGameElementK, direction = direction, from = square.coordinate, to = findNeighbour(square, direction)!!.coordinate)
            else       -> throw IllegalArgumentException("Game element can not be moved")
        }
    }

    private fun isPushable(involvedSquares: List<Square>): MoveHumanInfo {
        val takeWhileUntil = involvedSquares.takeWhile { squareK -> !squareK.isEmpty() }
        when {
            takeWhileUntil == involvedSquares -> return Pair(false, listOf())
            else -> {
                val isMovableByPushing = takeWhileUntil.drop(1).all { squareK -> squareK.peekGameElement()!! is Pushable }
                return Pair(isMovableByPushing, takeWhileUntil)
            }
        }
    }

    fun moveGameElement(gameElement: GameElement, from: Coordinate, to: Coordinate): Playground {

        val fromSquare = findSquare(coordinate = from)!!
        val toSquare = findSquare(coordinate = to)!!

        fun convert(iter: Square): Square {
            when (iter) {
                fromSquare -> return iter.removeGameElement(gameElement)
                toSquare   -> return iter.pushGameElement(gameElement)
                else -> return iter
            }
        }
        return this.copy(squares = this.squares.map { iter -> convert(iter) })
    }

    fun getEmptySquaresNextTo(square: Square?) : List<Square> {
        return getSquaresNextTo(square)
                .filter { squareK -> squareK.isEmpty() || squareK.humanOnTop() }
    }

    fun getSquaresWhereHapperCanMoveTo() : List<Square> {
        return getEmptySquaresNextTo(getSquareWithHapper())

    }

    fun getSquareWithHuman(): Square {
        return this.squares.find { squareK -> squareK.gameElements.any({gameElementK -> gameElementK is Human })}
                ?: throw IllegalStateException("There must be a human in the gameViewModel")
    }

    fun getSquareWithHapper(): Square {
        return this.squares.find { squareK -> squareK.gameElements.any({gameElementK -> gameElementK is Happer })}
                ?: throw IllegalStateException("There must be a happer in the gameViewModel")}
}

fun <E> List<E>.getRandomElement() = this[Random().nextInt(this.size)]



