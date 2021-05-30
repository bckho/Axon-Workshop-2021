import React, {Component} from 'react';
import uuid from "uuid";
import axios from 'axios';
import HButton from "./HButton";
import PauseButton from "./PauseButton"
import Game from "./Game"

class App extends Component {


    constructor(props) {
        super(props)
        this.state = {currentGameId:  null,  isPaused: false, playground: []}
        document.addEventListener("keydown", this.handleKeyDown.bind(this))
    }

    startGame(level) {
        let gameId = uuid.v4()
        axios.post(`/api/games/create/${gameId}/${level}`)
            .catch(reason => console.error(reason))

        this.setState({currentGameId: gameId})
    }


    startRandomGame(width, height, difficulty) {
        let gameId = uuid.v4()
        axios.post(`/api/games/create/random/${gameId}/${width}/${height}/${difficulty}`)
            .catch(reason => console.error(reason))

        this.setState({currentGameId: gameId})
    }

    pauseGame() {
        let gameId = this.state.currentGameId

        if ( this.state.isPaused ) {
            this.setState({isPaused: false})
            axios.post(`/api/games/restart/${gameId}`)
        } else {
            this.setState({isPaused: true})
            axios.post(`/api/games/pause/${gameId}`)
        }

    }

    stopGame() {
        let gameId = this.state.currentGameId
        axios.post(`/api/games/stop/${gameId}`)
    }

    handleKeyDown(event) {
        let chosenDirection;

        switch (event.keyCode) {
            case 37: chosenDirection = "WEST"; break;
            case 38: chosenDirection = "NORTH"; break;
            case 39: chosenDirection = "EAST"; break;
            case 40: chosenDirection = "SOUTH"; break;

            default: chosenDirection = null
        }
        if (chosenDirection != null) {
            axios.post(`/api/games/movehuman/${this.state.currentGameId}/${chosenDirection}`)
        }
    }

    render() {

        let game = <Game gameId = {this.state.currentGameId} watchType = 'update' size='normal'/>

        return (<div >
            <section className="rabo-blocks">
                <HButton title="Easy game" handleFunction = { () => this.startGame('1')} />
                <HButton title="Hard game" handleFunction = { () => this.startGame('2')} />
                <HButton title="Random game" handleFunction = { () => this.startRandomGame(24, 10, 'EASY')} />
                <PauseButton isPaused={this.state.isPaused} handleFunction = { () => this.pauseGame()} />
                <HButton title="Stop game" handleFunction = { () => this.stopGame()} />
            </section>
            <br/>

            {game}

        </div>)
    }
}

export default App;
