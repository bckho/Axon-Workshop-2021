package nl.rso.axon.happergame.commandside.model

interface Movable

interface Playable : Movable
interface Pushable : Movable

abstract class GameElement(open val asciRepresentation: Char)

data class Happer(override val asciRepresentation: Char = 'E'): GameElement(asciRepresentation = asciRepresentation), Playable
data class Human(override val asciRepresentation: Char = 'H'): GameElement(asciRepresentation = asciRepresentation), Playable
data class Box(override val asciRepresentation: Char = 'B'): GameElement(asciRepresentation = asciRepresentation), Pushable
data class Block(override val asciRepresentation: Char = 'R'): GameElement(asciRepresentation = asciRepresentation)




