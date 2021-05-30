import React, {Component} from 'react'
import GameAbstract from "./GameAbstract";

class GameOverview extends Component {

    constructor(props) {
        super(props);
        this.state = ({currentGames : []})
        this.eventSource = new EventSource('/api/overview')

        this.eventSource.onmessage = e => {
            let gameOverviewModel = JSON.parse(e.data)
            this.updateGameOverview(gameOverviewModel)
        }
    }

    componentWillUnmount() {
        this.eventSource.close()
    }

    updateGameOverview(updatedGameOverviewModel) {

        let existingModel = this.state.currentGames.find(m => m.gameId === updatedGameOverviewModel.gameId)

        if ( existingModel ) {
            let newCurrentGames = this.state.currentGames.map (gm => {
                if (gm.gameId === updatedGameOverviewModel.gameId ) {
                    return updatedGameOverviewModel
                } else {
                    return gm
                }
            })
            this.setState({currentGames : newCurrentGames})
        } else {
            let newCurrentGames = [updatedGameOverviewModel].concat(this.state.currentGames)
            this.setState({currentGames : newCurrentGames})
        }
    }

    render() {
        return <section className="history">
            <ul id="gameList">
                {
                    this.state.currentGames.map( gameOverViewModel =>
                        <GameAbstract gameId = {gameOverViewModel.gameId}
                                      gameStats = {gameOverViewModel.gameStats}
                                      gameStarted = {gameOverViewModel.gameStarted}
                                      gameEnded = {gameOverViewModel.gameEnded} />
                    )
                }

            </ul>
        </section>
    }
}

export default GameOverview