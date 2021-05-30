import React, {Component} from 'react'
import Playground from "./Playground";
import GameStats from "./GameStats"
import ReplayButtons from "./ReplayButtons";


class Game extends Component {

    constructor(props) {

        super(props)
        this.state = {playground: [], stats: {}, gameId: props.gameId}

        if (props.watchType === 'update') {
           this.setUpUpdateEventSource(props.gameId, props.watchType)
        } else {

            this.eventSource = new EventSource(`/api/games/replaynewstyle/${props.gameId}/NORMAL/FORWARD`)
            this.eventSource.onmessage = e => {
                let model = JSON.parse(e.data)
                this.setState({playground: model.gameViewModel.simplePlayground, stats: model.gameViewModel.gameStats})
            }

            this.eventSource.onerror = err => {
                this.eventSource.close()
            }
        }
    }

    setUpUpdateEventSource(gameId) {
        if (this.eventSource) {
            this.eventSource.close()
        }

        this.eventSource = new EventSource(`/api/game/update/${gameId}`)
        this.eventSource.onmessage = e => {
            let model = JSON.parse(e.data)
            this.setState({playground: model.gameViewModel.simplePlayground, stats: model.gameViewModel.gameStats})

            if ( this.isGameEnded(model.gameViewModel.gameStats.gameStatus)) {
                this.eventSource.close()
            }
        }

        this.eventSource.onerror = err => {
            this.eventSource.close()
        }
    }

    componentWillUnmount() {
        this.eventSource.close()

        if (this.replayEventSource) {
            this.replayEventSource.close()
        }
    }

    replay(speed, direction) {

        this.eventSource.close()
        this.replayEventSource = new EventSource(`/api/games/replaynewstyle/${this.props.gameId}/${speed}/${direction}`)

        this.replayEventSource.onmessage = e => {


            let model = JSON.parse(e.data)
            console.log('replayData: ' + model)
            this.setState({playground: model.gameViewModel.simplePlayground, stats: model.gameViewModel.gameStats})

            if (direction === 'FORWARD' && this.isGameEnded(model.game.gameStats.gameStatus)) {
                this.replayEventSource.close()
            }
        }
    }

    componentDidUpdate(prevProps) {

        if (prevProps.gameId != this.props.gameId) {
            this.setUpUpdateEventSource(this.props.gameId)
        }
    }

    isGameEnded(gameStatus) {
        let replayableStatuses = ['WON', 'CANCELED', 'LOST', 'LOST_BY_INACTIVITY']
        return replayableStatuses.indexOf(gameStatus) >= 0
    }

    render() {

        let replayButtons = null
        if (this.isGameEnded(this.state.stats.gameStatus)) {
           replayButtons = <ReplayButtons title="Replay button" handle = { (speed, direction) => this.replay(speed, direction)} />
        }

       return <div>
            <GameStats stats = {this.state.stats} />
            <Playground playground = {this.state.playground} size={this.props.size} />
            {replayButtons}
            <br/>
        </div>
    }

}
export default Game