import React from "react"

function GameStats(props) {

    let sep = <span>&nbsp;&nbsp;</span>
    if (props.orientation === 'vertical') {
        sep = <br/>
    }


    return <section className="game-info">

                <span className="happer-blue">Game status: </span><span>{props.stats.gameStatus}</span>   {sep}
                <span className="happer-blue">Moves human: </span><span>{props.stats.movesHuman}</span>   {sep}
                <span className="happer-blue">Moves happer: </span><span>{props.stats.movesHapper}</span> {sep}
                <span className="happer-blue">Boxes moved: </span><span>{props.stats.boxesMoved}</span>   {sep}
                <span className="happer-blue">Inactivity: </span><span>{props.stats.inActivity}</span>    {sep}
            </section>
}

export default GameStats