import React from 'react';

function Square(props) {

    let gameElementClass = 'game-element'

    if (props.size === 'small') {
        gameElementClass = 'game-element-small'
    }

    const classes = `${gameElementClass} ${props.type}`
    return <div className = {classes} > </div>
}

export default Square