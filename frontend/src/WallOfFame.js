import React, {Component} from 'react'
import axios from 'axios'
import Game from './Game'

class WallOfFame extends Component {


    constructor(props) {
        super(props)
        this.state = {wallOfFame : []}
        axios.get('/api/walloffame')
            .catch(err=> console.error(`Could not get wall of fame games ${err}`))
            .then(res => {
                this.setState({wallOfFame: res.data})
            })
    }

    render() {
       return <div id = 'wall-of-fame-container'>
                {this.state.wallOfFame.map(game => <Game gameId = {game.gameId} watchType = 'replay' size='small'/> ) }
            </div>
    }
}

export default WallOfFame