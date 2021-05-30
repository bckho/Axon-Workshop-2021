package nl.rso.axon.happergame.commandside.model

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.sign

class AstarAlgorithm {

    private val shortestDistanceToStartSquare: HashMap<Square, Int> = HashMap()
    private val predecessors: HashMap<Square, Square> = HashMap()
    private val closed: HashSet<Square> = HashSet()

    private var open: PriorityQueue<Square> = PriorityQueue();

    fun getShortestPath(playGround: Playground) : List<Square> {

        val start = playGround.getSquareWithHapper()
        val goal = playGround.getSquareWithHuman()

        initOpenDataStructure(playGround.getSquareWithHuman())

        open.add(start)
        shortestDistanceToStartSquare.put(start, 0)

        while (!open.isEmpty() && !closed.contains(goal)) {
            val currentSquare = open.poll()
            closed.add(currentSquare)
            relaxeNeighbours(currentSquare, playGround.getEmptySquaresNextTo(currentSquare))
        }

        return createPath(start, goal)
    }


    private fun relaxeNeighbours(square: Square, neighs: List<Square>) {

        for (neigh in neighs) {
            if (! closed.contains(neigh)) {
                val shortestToGoal =  shortestDistanceToStartSquare.getOrElse(square, {0})
                var shortestToNeigh = shortestDistanceToStartSquare.getOrElse(neigh, {Int.MAX_VALUE})

                if (shortestToNeigh > shortestToGoal + 1) {
                    shortestDistanceToStartSquare.put(neigh, shortestToGoal + 1)
                    predecessors.put(neigh, square)
                    open.add(neigh)
                }
            }
        }
    }

    private fun createPath(start:Square, goal:Square) : List<Square> {
        var path = listOf<Square>(goal)
        var ready = false;
        var searchSquare: Square = goal

        while (!ready) {
            val iter = predecessors.get(searchSquare)
            if (iter == null) {
                return emptyList();
            }
            path = path.plus(iter!!)
            searchSquare = iter
            if (iter == start) {
                ready = true
            }
        }
        return path
    }

    private fun initOpenDataStructure(goal: Square) {
        open = PriorityQueue(1) { t1: Square, t2: Square ->

            val distance1 = shortestDistanceToStartSquare.get(t1)!! + manhattanDistance(t1, goal)
            val distance2 = shortestDistanceToStartSquare.get(t2)!! + manhattanDistance(t2, goal)

            sign(distance1.toDouble() - distance2.toDouble()).toInt() }
    }

    private fun manhattanDistance(from: Square, to: Square) : Int {
        val xDelta = Math.abs(from.coordinate.x - to.coordinate.x)
        val yDelta = Math.abs(from.coordinate.y - to.coordinate.y)
        return xDelta + yDelta
    }

}
