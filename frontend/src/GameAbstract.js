import React from 'react'
import {Link} from 'react-router-dom'
import GameStats from "./GameStats";


function GameAbstract(props) {

    const showLiveWatchLink = props.gameStats.gameStatus === 'GAME_ON'

    let liveLink = null
    if (showLiveWatchLink) {

        const url = `livewatcher/${props.gameId}`
        liveLink = <Link to={url}>Watch live</Link>

    }
    return <li>
            <span class="abstract-label">Game ID: </span> &nbsp; &nbsp; <span id="gameId">{props.gameId}</span>
            <GameStats stats={props.gameStats} orientation="vertical"/>
            <span class="abstract-label">Start date </span><span id="start-date">{props.gameStarted}</span> <br/>
            <span class="abstract-label">End date: </span><span id="end-date">{props.gameEnded}</span>
            {liveLink}
            </li>
}

export default GameAbstract