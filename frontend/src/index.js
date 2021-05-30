import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Link, Route} from 'react-router-dom'
import App from './App';
import GameOverview from './GameOverview'
import LiveWatcher from './LiveWatcher'
import WallOfFame from './WallOfFame'

const routing = (
    <Router>
        <div>
            <nav>
                <ul>
                    <li>
                        <Link to="/"> Start game </Link>
                    </li>
                    <li>
                        <Link to="/gameoverview"> Game overview </Link>
                     </li>
                    <li>
                        <Link to= "/walloffame"> Wall of fame </Link>
                    </li>
                </ul>
            </nav>
            <br/>
            <Route exact path="/" component={App} />
            <Route path="/gameoverview" component={GameOverview} />
            <Route path="/walloffame" component={WallOfFame} />
            <Route path="/livewatcher/:gameId" component={LiveWatcher} />
        </div>
    </Router>
)

ReactDOM.render(routing, document.getElementById('root'));

