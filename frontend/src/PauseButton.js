import React from 'react';

function PauseButton(props) {

    function getButtonTitle(isPaused) {
        if (isPaused) {
            return 'Unpause'
        } else {
            return 'Pause'
        }
    }
    return <button className="happer-block" type="submit" onClick={props.handleFunction}>{getButtonTitle(props.isPaused)}</button>
}

export default PauseButton