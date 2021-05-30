import React from 'react'
import Game from './Game'

function LiveWatcher(props) {

    return <div>
        <Game gameId = {props.match.params.gameId} watchType = 'update' />
    </div>
}

export default LiveWatcher