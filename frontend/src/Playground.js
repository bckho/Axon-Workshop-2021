import React from 'react';
import Square from './Square';


function convert(c) {

    var result = '';
    switch(c) {
        case "H":
            result = 'human'
            break;
        case "E":
            result = 'happer'
            break;
        case "B":
            result =  "box"
            break;
        case "R":
            result =  "block"
            break;
        case "X":
            result =  "empty"
            break;
        default: result =  "unknown"
    }

    return result
}

function Playground(props) {

    let squares = props.playground.map(function(gameElement){
        return <Square type = {convert(gameElement.charAt(0))} size = {props.size}/>;
    })


    let happerContainerId = "happer-container"

    if (props.size === 'small') {
        happerContainerId = "happer-container-small"
    }
    return <section className="game"><div id={happerContainerId}> {squares} </div></section>
}

export default Playground